// Main Class: UHCPlugin

import ... 

class UHCPlugin : JavaPlugin() {
    companion object {
        lateinit var plugin: UHCPlugin
    }

    override fun onEnable() {
        plugin = this
        KommandBukkit.initialize(this)
        CommandManager.register(this)
        server.PluginManager.registerEvents(EventListener(), this)
    }
}

// GameManager Object
import ... 

object GameManager {
    private val plugin = UHCPlugin.plugin
    var gameRunning = false
    var isGracePeriod = false
    var isPvp = false
    var isDeathMatch = false

    fun start() {
        gameRunning = true
        timer(3)
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            gracePeriod()
            pvp()
            deathMatch()
            endGame()
        }, 20 * 3L)
    }
    
    fun status(): String {
        return when {
            isGracePeriod -> "파밍 시간"
            isPvp -> "PvP 시간"
            isDeathMatch -> "데스매치"
        }
    }
    
    private fun gracePeriod() {
        isGracePeriod = true
        timer("gracePeriod")
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            isGracePeriod = false
        } 20 * 60 * 20L)
    }
    
    private fun pvp() {
        isPvp = true
        timer("pvp")
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            isPvp = false
        } 20 * 60 * 30L)
    }
    
    private fun deathMatch() {
        isDeathMatch = true
        timer("deathMatch")
        Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            isDeathMatch = false
        } 20 * 60 * 10L)
    }
    
    private fun endGame() {
        gameRunning = false
        announceWinner()
    }
    
    private fun announceWinner() {
        // todo
    }
    
    private fun timer(sec: Int) {
        
    }
} 

// CommandManager
import ... 

object CommandManager : CommandExecutor {
    fun register(plugin: Plugin) {
        KommandBukkit.register(plugin) {
            register("start") {
                requires { player != null && sender.name == "MaRobim" }
                executes {
                    GameManager.start()
                    sender.sendMessage("게임이 시작됩니다")
                }
            }

            register("status") {
                requires { player != null }
                executes {
                    val status = GameManager.status()
                    sender.sendMessage("게임 상태: $status")
                }
            }
        }
    }
}

// EventListener
object EventListener : Listener {
    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager as Player
        if (GameManager.isGracePeriod) {
            event.isCanceled = true
        }
    }
    
    // todo
}

// Utils
object Utils {
    fun timer(player: ServerPlayerEntity, seconds: Int, status: String) {
        var timeLeft = seconds

    // 타이머 시작: 매 초마다 남은 시간 UI에 표시
        ServerTickEvents.START_SERVER_TICK.register(object : ServerTickEvents.StartTick {
            override fun onStartTick(server: MinecraftServer) {
                if (timeLeft <= 0) return

                for (world in server.worlds) {
                    if (world.players.contains(player)) {
                        val actionBarText = Text.literal("§a$status §7| §e$timeLeft초 남음")
                        player.networkHandler.sendPacket(
                            TitleS2CPacket(
                                TitleS2CPacket.Action.ACTIONBAR,
                                actionBarText
                            )
                        )
                    }
                }

                timeLeft--

                if (timeLeft < 0) {
                    ServerTickEvents.START_SERVER_TICK.unregister(this)
                }
            }
        })
    }
}