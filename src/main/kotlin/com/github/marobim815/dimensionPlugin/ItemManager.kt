package com.github.marobim815.dimensionPlugin

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class ItemManager(private val plugin: JavaPlugin) {
    private val items = mutableMapOf<String, ItemStack>()

    init {
        registerItems()
    }

    private fun registerItems() {
        items["member_add_card"] = createCustomItem(
            Material.PAPER,
            "§4팀원 생성 카드",
            listOf("", ""),
            "member_add_card"
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

    fun getItem(id: String): ItemStack? {
        return items[id]?.clone()
    }
}