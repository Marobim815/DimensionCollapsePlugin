package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

@Suppress("DEPRECATION")
class ItemManager(private val plugin: JavaPlugin) {
    private val items = mutableMapOf<String, ItemStack>()

    init {
        registerItems()
    }

    private fun registerItems() {
        items["member_add_card"] = createCustomItem(
            Material.PAPER,
            "§6팀원 생성 카드",
            listOf("§7팀원을 랜덤으로 생성합니다", ""),
            "member_add_card"
        )

		items["item_enhance_card"] = createCustomItem(
            Material.PAPER,
            "§6템 강화 카드",
            listOf("§7템빨을 책임지는 카드", ""),
            "item_enhance_card"
        )
    }

    private fun createCustomItem(
        material: Material,
        name: String,
        lore: List<String>,
        id: String
    ): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        meta.setDisplayName(name)
        meta.lore = lore

        val key = NamespacedKey(plugin, "custom_item")
        meta.persistentDataContainer.set(key, PersistentDataType.STRING, id)

        item.itemMeta = meta
        return item
    }

    @Suppress("unused")
    fun getItem(id: String): ItemStack? {
        return items[id]?.clone()
    }
}