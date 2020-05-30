package com.raidplugin.sdk.inventory.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, short data) {
        this(new ItemStack(material, amount, data));
    }

    private ItemBuilder applyMeta(Consumer<ItemMeta> itemMeta) {
        ItemMeta meta = itemStack.getItemMeta();

        itemMeta.accept(meta);

        itemStack.setItemMeta(meta); return this;
    }

    public ItemBuilder name(String name) {
        return applyMeta(meta -> meta.setDisplayName(name));
    }

    public ItemBuilder lore(String... lore) {
        return applyMeta(meta -> meta.setLore(Arrays.asList(lore)));
    }

    public ItemStack build() {
        return itemStack;
    }
}
