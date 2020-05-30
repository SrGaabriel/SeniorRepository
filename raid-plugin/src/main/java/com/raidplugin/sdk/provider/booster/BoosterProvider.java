package com.raidplugin.sdk.provider.booster;

import com.raidplugin.sdk.provider.nbt.NBTProvider;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.entity.Player;

public class BoosterProvider {

    private final NBTProvider nbtProvider = new NBTProvider();

    public float getBoost(Player player) {
        NBTTagCompound compound = nbtProvider.getNBT(player.getInventory().getItemInMainHand());

        if(!compound.hasKey("booster")) return -1;

        return compound.getFloat("booster");
    }

}
