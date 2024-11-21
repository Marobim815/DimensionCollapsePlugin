package com.github.marobim815.dimensionPlugin


import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.entity.Slime
import org.bukkit.event.Listener

@Suppress("DEPRECATION")
object TeamCore : Listener{
    private val cores = mutableMapOf<Location, Double>()
    private const val CORE_SIZE = 3
    private const val MAX_HP = 2000.0

    fun placeCore(location: Location, player: Player): Boolean {
        if (cores.containsKey(location)) {
            player.sendMessage("§c이미 이 위치에 코어가 설치되어 있습니다!")
            return false
        }

        val team = TeamManager.getTeam(player)
        val world = location.world ?: return false
        val slime = world.spawn(location, Slime::class.java) { entity ->
            entity.size = CORE_SIZE
            entity.isInvulnerable = true
            entity.customName = "${team?.teamName}팀의 코어"
            entity.isCustomNameVisible = true
        }

        cores[slime.location] = MAX_HP
        player.sendMessage("§a코어가 성공적으로 설치되었습니다!")
        return true
    }

    @Suppress("unused")
    fun damageCore(location: Location, damage: Double) {
        val currentHp = cores[location] ?: return
        val newHp = currentHp - damage
        if (newHp <= 0) {
            destroyCore(location)
        } else {
            cores[location] = newHp
        }
    }

    private fun destroyCore(location: Location) {
        cores.remove(location)
        location.world?.createExplosion(location, 0f)
        location.world?.players?.forEach {
            it.sendMessage("§c코어가 파괴되었습니다!")
            it.playSound(location, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f)
        }
    }
}