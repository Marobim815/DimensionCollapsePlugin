package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class DimensionPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        logger.info("플러그인 활성화!!")
        server.pluginManager.registerEvents(this, this)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        player.sendTitle(
            "§6§l환영합니다!!",
            "§a$player",
            10, 70, 20
        )
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block

        if (block.type == Material.BEDROCK) {
            val location = block.location
            val world = block.world
        }
    }

    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
