package com.github.marobim815.dimensionPlugin

import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DimensionPlugin : JavaPlugin(), Listener {

    override fun onEnable() {
        logger.info("플러그인 활성화!!")
        server.pluginManager.registerEvents(this, this)
    }

    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
