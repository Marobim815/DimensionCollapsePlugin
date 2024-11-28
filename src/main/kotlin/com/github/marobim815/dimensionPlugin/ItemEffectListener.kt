package com.github.marobim815.dimensionPlugin

import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class ItemEffectListener : Listener {

    @EventHandler
    fun onPlayerUseCard(event: PlayerInteractEvent) {
        val user = event.player

        val itemMeta: ItemMeta = user.inventory.itemInMainHand.itemMeta ?: return
        val plugin = JavaPlugin.getPlugin(DimensionPlugin::class.java)

        val key = NamespacedKey(plugin, "custom_item")
        val customItemId = itemMeta.persistentDataContainer.get(key, PersistentDataType.STRING)

        when {
            customItemId == "member_add_card" -> {
                val userTeam = TeamManager.getTeam(user)
                if (userTeam != null && user == userTeam.leader) {
                    useTeamMemberCard(user)
                } else {
                    event.isCancelled = true
                    user.sendMessage("§c팀원 생성 카드는 팀장만 사용할 수 있습니다!")
                }
            customItemId == "item_enhance_card" -> {
                useItemEnhanceCard(user)
            }
        }
    }

    private fun useItemEnhanceCard(player: Player): Boolean {
        val enhanceLevel: Int = Random.nextInt(1, 6)
        val canEnhance: (ItemStack) -> Boolean = { it ->
            when {
                item.type.toString().endsWith("BOOTS") -> true
                item.type.toString().endsWith("CHESTPLTE") -> true
                item.type.toString().endsWith("SWORD") -> true
                item.type.toString().endsWith("BOW") -> true
                item.type.toString().endsWith("CROSSBOW") -> true
                item.type.toString().endsWith("AXE") -> true
                item.type.toString().endsWith("LEGGINGS") -> true
                item.type.toString().endsWith("HELMET") -> true
                else -> false
            }
        }
        
        val itemToEnhance = player.inventory.firstOrNull { item ->
            item != null && canEnhance(item)
        }
        
        return if (itemToEnhance != null): Boolean {
            
            true
        } else {
            
            false
        }
    }

    private fun enhanceItem(player: Player, item: ItemStack, level: Int) {
        val meta = item.itemMeta
        val attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)
        val attackDamage = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)
        val maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        val armorUnbreak = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)
        val armorProtection = player.getAttribute(Attribute.GENERIC_ARMOR)
        
        if (meta != null) {
            when (item.type.toString) {
                Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD -> {
                    meta.lore = "§6강화된 검"
                    attackSpeed?.baseValue = (level.toDouble()) ^ 2
                    
                    // level ^ 2은 공격 속도 단축의 수치로, level * 2는 데미지 증가의 수치로 사용할 수 있습니다.
                }
                Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE -> {
                    meta.lore = "§6강화된 도끼"
                    // 데미지 증가
                    // level * 2
                }
                Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.LEATHER_BOOTS -> {
                    meta.lore = "§6강화된 신발"
                    // 신발은 이동 속도 증가
                }
                Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.LEATHER_CHESTPLATE -> {
                    meta.lore = "§6강화된 갑옷"
                    // 갑옷은 체력 증가
                    healthAttribute?.baseValue = (level.toDouble)^2
                    player.health = (level.toDouble)^2
                }
                Material.BOW, Material.CROSSBOW -> {
                    meta.lore = "§6강화된 활/석궁"
                    // 데미지 및 장전 시간 단축
                }
            }
            item.itemMeta = meta
        }
    }

    private fun useTeamMemberCard(player: Player): Boolean {
        val team = TeamManager.getTeam(player) ?: return false
        if (team.members.size >= 5) {
            player.sendMessage("§c이미 최대 팀원 수에 도달했습니다.")
            return false
        } else {
            val newMember = player.server.offlinePlayers.filter { it !in team.members }.random()
            team.members.add(newMember as Player)
            player.sendMessage("${newMember.name}님이 팀원으로 추가되었습니다!")
            return true
        }
    }
}
