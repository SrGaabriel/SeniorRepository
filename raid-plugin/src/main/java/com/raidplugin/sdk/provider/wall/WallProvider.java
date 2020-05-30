package com.raidplugin.sdk.provider.wall;

import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.sdk.inventory.item.ItemBuilder;
import com.raidplugin.sdk.provider.nbt.NBTProvider;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WallProvider {

    private final NBTProvider nbtProvider = new NBTProvider();

    public ItemStack toItemStack(Wall wall) {
        ItemStack itemStack = new ItemBuilder(Material.OBSIDIAN)
                .name("§aWall Block")
                .lore(
                        " ",
                        " §7That's a block to protect your team.",
                        " §7Place block on your plot and see!",
                        " "
                ).build();

        return nbtProvider.applyNBT(itemStack, nbt -> {
            nbt.setInt("life", wall.getLife()); nbt.setInt("level", wall.getLevel());
        });
    }

}
