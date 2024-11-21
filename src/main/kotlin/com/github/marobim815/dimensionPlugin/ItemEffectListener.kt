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
            }
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
