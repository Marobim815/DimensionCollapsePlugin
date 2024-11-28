package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

@Suppress("DEPRECATION")
class ItemEffectListener : Listener {
    @EventHandler
    fun onPlayerUseCard(event: PlayerInteractEvent) {
        val player = event.player
        val itemInHand = player.inventory.itemInMainHand

        val itemMeta: ItemMeta = itemInHand.itemMeta ?: return
        val plugin = JavaPlugin.getPlugin(DimensionPlugin::class.java)

        val key = NamespacedKey(plugin, "custom_item")
        val customItemId = itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING)

        when (customItemId) {
            "item_enhance_card" -> {
                if (useItemEnhanceCard(player)) {
                    consumeItem(itemInHand)
                } else {
                    player.sendMessage("§c강화에 실패했습니다!")
                }
            }
        }
    }

    private fun useItemEnhanceCard(player: Player): Boolean {
        val enhanceLevel = Random.nextInt(1, 6)
        val successChance = 0.5
        val isSuccess = Random.nextDouble() < successChance

        val canEnhance: (ItemStack) -> Boolean = { item ->
            item.type.name.endsWith("SWORD") ||
            item.type.name.endsWith("AXE") ||
            item.type.name.endsWith("BOW") ||
            item.type.name.endsWith("CROSSBOW") ||
            item.type.name.endsWith("HELMET") ||
            item.type.name.endsWith("CHESTPLATE") ||
            item.type.name.endsWith("LEGGINGS") ||
            item.type.name.endsWith("BOOTS")
        }

        val itemToEnhance = player.inventory.firstOrNull { it != null && canEnhance(it) }

        if (itemToEnhance != null) {
            if (isSuccess) {
                enhanceItem(player, itemToEnhance, enhanceLevel)
                player.sendMessage("§a§l아이템이 레벨 $enhanceLevel로 강화되었습니다!")
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                return true
            } else {
                player.sendMessage("§c강화에 실패하여 아이템이 손상되었습니다!")
                degradeItem(player, itemToEnhance)
                player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0f, 0.8f)
                return false
            }
        } else {
            player.sendMessage("§c강화할 수 있는 아이템이 없습니다!")
            return false
        }
    }

    private fun enhanceItem(player: Player, item: ItemStack, level: Int) {
        val meta = item.itemMeta ?: return

        when (item.type) {
            // 검
            Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD -> {
                meta.lore = listOf("§6강화된 검 (Lv. $level)")
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)?.baseValue = level * level.toDouble()
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = level * 2.0
                if (level == 5) {
                    item.durability = 0
                    player.noDamageTicks = 0
                }
            }
            // 도끼
            Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE -> {
                meta.lore = listOf("§6강화된 도끼 (Lv. $level)")
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = level * 2.0
                if (level == 5) {
                    item.durability = 0
                }
            }
            // 신발
            Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                meta.lore = listOf("§6강화된 신발 (Lv. $level)")
                player.walkSpeed = 0.2f + (level * 0.05f)
                if (level == 5) {
                    item.durability = 0
                }
            }
            // 갑옷 (투구, 흉갑, 바지)
            Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.LEATHER_HELMET,
            Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.LEATHER_LEGGINGS -> {
                meta.lore = listOf("§6강화된 갑옷 (Lv. $level)")
                val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
                if (maxHealth != null) {
                    maxHealth.baseValue += level * 20.0
                    player.health = maxHealth.baseValue
                }
                if (level == 5) {
                    item.durability = 0
                }
            }
            // 활 및 석궁
            Material.BOW, Material.CROSSBOW -> {
                meta.lore = listOf("§6강화된 원거리 무기 (Lv. $level)")
                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = level * 2.0
                if (level == 5) {
                    item.durability = 0
                }
            }
            else -> return
        }
        item.itemMeta = meta
    }

    private fun degradeItem(player: Player, item: ItemStack) {
        val failChance = 0.4 // 레벨 감소 확률 (40%)
        val meta = item.itemMeta ?: return

        if (Random.nextDouble() < failChance) {
            val currentLore = meta.lore
            if (currentLore != null && currentLore.isNotEmpty()) {
                val newLevel = currentLore.first().filter { it.isDigit() }.toInt() - 1
                if (newLevel > 0) {
                    meta.lore = listOf("§6강화된 아이템 (Lv. $newLevel)")
                    player.sendMessage("§e아이템의 레벨이 $newLevel로 감소했습니다.")
                }
            }
        } else {
            item.amount -= 1
            if (item.amount <= 0) {
                item.type = Material.AIR
                player.sendMessage("§c아이템이 파괴되었습니다!")
            }
        }
        item.itemMeta = meta
    }

    private fun consumeItem(item: ItemStack) {
        if (item.amount > 1) {
            item.amount -= 1
        } else {
            item.type = Material.AIR
        }
    }
}