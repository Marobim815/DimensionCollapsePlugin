package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class DimensionPlugin : JavaPlugin(), Listener {
    private lateinit var itemManager: ItemManager

    override fun onEnable() {
        server.pluginManager.registerEvents(ItemEffectListener(), this)
        itemManager = ItemManager(this)
        getCommand("팀")?.setExecutor(TeamCommand())
        logger.info("플러그인 활성화!!")
    }

    @Suppress("DEPRECATION")
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

    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
