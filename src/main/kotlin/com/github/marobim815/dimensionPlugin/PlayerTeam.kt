package com.github.marobim815.dimensionPlugin

import org.bukkit.entity.Player

data class Teams(val teamName: String, val members: MutableList<Player> = mutableListOf(), var leader: Player? = null)

object TeamManager {
    private val teams = mutableMapOf<String, Teams>()
    private var registeredPlayer = mutableListOf<Player>()
    
    fun createTeam(name: String, leader: Player): Boolean {
        if (teams.containsKey(name)) return false
        val team = Teams(name, mutableListOf(leader), leader)
        teams[name] = team
        return true
    }

    fun addMember(teamName: String, player: Player): Boolean {
        val team = teams[teamName] ?: return false
        if (team.members.size >= 5) return false
        team.members.add(player)
        return true
    }

    fun getTeam(player: Player): Teams? {
        return teams.values.firstOrNull { it.members.contains(player) }
    }
    
    fun registerPlayer(player: Player): Boolean {
        if (!registerPlayer.contains(player)) return false
        registeredPlayer.add(player)
        return true
    }
}