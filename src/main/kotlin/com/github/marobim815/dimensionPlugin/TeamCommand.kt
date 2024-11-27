package com.github.marobim815.dimensionPlugin

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TeamCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("이 명령어는 플래이어만 사용할 수 있습니다!!")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("§c사용법: /팀 [생성/추가/정보] <팀 이름> <플래이어 이름>")
            return true
        }

        when (args[0].lowercase()) {
            "생성" -> {
                if (args.size < 2) {
                    sender.sendMessage("§c사용법: /팀 [생성/추가/정보] <팀 이름> <플래이어 이름>")
                    return true
                }
                val teamName = args[1]
                if (TeamManager.createTeam(teamName, sender)) {
                    sender.sendMessage("§a팀 '$teamName'이 생성되었습니다!")
                } else {
                    sender.sendMessage("§c이미 존재하는 팀 이름입니다!")
                }
            }

            "추가" -> {
                if (args.size < 3) {
                    sender.sendMessage("§c사용법: /팀 [생성/추가/정보] <팀 이름> <플래이어 이름>")
                    return true
                }
                val teamName = args[1]
                val targetPlayer = Bukkit.getPlayer(args[2])
                if (targetPlayer == null) {
                    sender.sendMessage("§c플래이어를 찾을 수 없습니다!")
                    return true
                }
                if (TeamManager.addMember(teamName, targetPlayer)) {
                    sender.sendMessage("§a${targetPlayer.name}님이 팀'$teamName'에 추가되었습니다!")
                } else {
                    sender.sendMessage("§c팀 추가에 실패했습니다!")
                }
            }

            "정보" -> {
                val team = TeamManager.getTeam(sender)
                if (team == null) {
                    sender.sendMessage("§c팀에 속해있지 않습니다!")
                } else {
                    sender.sendMessage("§6팀 이름: ${team.teamName}")
                    sender.sendMessage("§b팀원: ${team.members.joinToString(", ") { it.name }}")
                }
            }

            else -> {
                sender.sendMessage("§c사용법: /팀 [생성/추가/정보] <팀 이름> <플래이어 이름>")
            }
        }
        return true
    }
}