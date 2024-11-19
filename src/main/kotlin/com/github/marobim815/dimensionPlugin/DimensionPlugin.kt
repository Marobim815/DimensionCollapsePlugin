package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
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
        val team = TeamManager.getTeam(player)

        if (block.type == Material.BEDROCK) {
            val location = block.location
            val world = block.world
            TeamCore.placeCore(location, player)
        }
    }


    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
