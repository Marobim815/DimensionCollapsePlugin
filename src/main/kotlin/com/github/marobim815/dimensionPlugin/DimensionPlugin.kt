package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

@Suppress("DEPRECATION")
class DimensionPlugin : JavaPlugin(), Listener {
    private lateinit var itemManager: ItemManager

    override fun onEnable() {
        server.pluginManager.registerEvents(ItemEffectListener(), this)
        itemManager = ItemManager(this)
        getCommand("팀")?.setExecutor(TeamCommand())
        logger.info("플러그인 활성화!!")
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (player.hasPlayedBefore()) return

        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
        player.sendTitle("§6§l환영합니다!!", "§a${player.name}", 10, 70, 20)

        val team = TeamManager.getTeam(player)
        if (team?.leader == player) {
            val coreBlock = itemManager.getItem("core_block")
            coreBlock?.let {
                player.inventory.addItem(it)
                player.sendMessage("§a팀 리더로서 코어 블록을 받았습니다!")
            }
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.player
        val inventory = player.inventory          
        val coreBlockKey = NamespacedKey(plugin, "custom_item")
        val itemsToKeep = inventory.contents.filter { item ->
            val meta = item?.itemMeta ?: return@filter false
            meta.persistentDataContainer[coreBlockKey, PersistentDataType.STRING] == "core_block"
        }

        itemsToKeep.forEach { item -> player.inventory.addItem(item) }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val location = block.location

        if (TeamCore.isCoreBlock(player.inventory.itemInMainHand)) {
            if (location.y !in 0.0..60.0) {
                player.sendMessage("§c코어는 y좌표 0~60 사이에만 설치할 수 있습니다!")
                player.playSound(location, Sound.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f)
                event.isCancelled = true
                return
            }

            if (TeamCore.placeCore(location, player)) {
                event.isCanceled = true
                player.playSound(location, Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.0f)
            }
        }
    }

    override fun onDisable() {
        logger.info("플러그인 꺼짐.")
    }
}
