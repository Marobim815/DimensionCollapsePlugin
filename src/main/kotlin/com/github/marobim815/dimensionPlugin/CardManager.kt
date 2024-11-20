package com.github.marobim815.dimensionPlugin

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.Material

object CardManager {
    fun useTeamMemberCard(player: Player): Boolean {
        val team = TeamManager.getTeam(player) ?: return false
        if (team.members.size >= 5) {
            player.sendMessage("이미 최대 팀원 수에 도달했습니다.")
            return false
        }

        val newMember = player.server.offlinePlayers.filter { it !in team.members }.random()
        team.members.add(newMember as Player)
        player.sendMessage("${newMember.name}님이 팀원으로 추가되었습니다!")
        return true
    }

    fun combineCardPieces(player: Player): ItemStack {
        return ItemStack(Material.DIAMOND_SWORD).apply {
            itemMeta = itemMeta?.apply {
                setDisplayName("강화 카드")
            }
        }
    }
}