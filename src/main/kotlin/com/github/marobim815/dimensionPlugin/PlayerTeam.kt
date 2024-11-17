package com.github.marobim815.dimensionPlugin

import org.bukkit.entity.Player

class PlayerTeam {
    private val playerTeam = mutableMapOf<String, MutableList<Player>>()
    private val registeredPlayer = mutableListOf<Player>()

    fun registerTeam(player: Player): Boolean {
        if (player !in registeredPlayer) {
            registeredPlayer.add(player)
            return true
        }
        return false
    }

    fun createTeam(teamName: String): Boolean {
        if (teamName !in playerTeam.keys) {
            playerTeam.keys.add(teamName)
            return true
        }
        return false
    }

}