package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class DimensionPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        server.pluginManager.registerEvents(this, this)

        getCommand("팀")?.setExecutor(TeamCommand())

        logger.info("플러그인 활성화!!")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
        player.sendTitle("§6§l환영합니다!!", "§a$player", 10, 70, 20)
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val location = block.location

        if (location.y in 0.0..60.0) {
            if (block.type == Material.BEDROCK) {
                TeamCore.placeCore(location, player)
                player.playSound(location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f)
            }
        } else {
            player.sendMessage("§c코어는 y좌표 0~60사이에 설치 가능합니다!!")
            player.playSound(location, Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = player.inventory.itemInMainHand

        //TODO: 카드 관련 아이템 수정
        if (item.type == Material.PAPER && item.itemMeta?.displayName == "§e팀원 생성 카드") {
            event.isCancelled = true
            val success = CardManager.useTeamMemberCard(player)
            if (success) {
                player.inventory.itemInMainHand.subtract()
            }
        } else if (item.type == Material.DIAMOND && item.itemMeta?.displayName == "§b의문의 카드 조각") {
            event.isCancelled = true
            val hasEnoughPieces = player.inventory.containsAtLeast(ItemStack(Material.DIAMOND), 9)
            if (hasEnoughPieces) {
                player.inventory.removeItem(ItemStack(Material.DIAMOND, 9))
                val combinedCard = CardManager.combineCardPieces(player)
                player.inventory.addItem(combinedCard)
                player.sendMessage("§a강화 카드를 얻었습니다!")

            } else {
                player.sendMessage("§c카드 조각이 부족합니다! (필요: 9개)")
            }
        }
    }

    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
