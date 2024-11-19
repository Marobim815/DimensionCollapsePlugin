package com.github.marobim815.dimensionPlugin

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Slime
import org.bukkit.potion.PotionEffect

object TeamCore {
    fun spawnCore(location: Location, world: World, customName: String? = null): Slime {
        val size: Int = 5

        val effects: List<PotionEffect> = emptyList()
        val core = world.spawn(location, Slime::class.java)

        if (customName != null) {
            core.customName = customName
            core.isCustomNameVisible = true
        }

        effects.forEach { core.addPotionEffect(it) }
        core.setAI(false)
        core.setGravity(false)
        core.health = 2000.0
        core.size = size
        return core
    }
}