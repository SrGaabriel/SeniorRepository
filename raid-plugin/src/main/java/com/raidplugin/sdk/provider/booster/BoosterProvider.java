package com.raidplugin.sdk.provider.booster;

import com.raidplugin.sdk.provider.nbt.NBTProvider;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BoosterProvider {

    private final NBTProvider nbtProvider = new NBTProvider();

    public float getBoost(Player player) {
        NBTTagCompound compound = nbtProvider.getNBT(player.getInventory().getItemInMainHand());

        if(!compound.hasKey("booster")) return 1;

        return compound.getFloat("booster");
    }

    public ItemStack toItemStack(ItemStack itemStack, float booster) {
        return nbtProvider.applyNBT(itemStack, nbt -> nbt.setFloat("booster", booster));
    }

}
