package com.raidplugin.sdk.provider.nbt;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class NBTProvider {

    public NBTTagCompound getNBT(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);

        return stack.getTag() == null ? new NBTTagCompound() : stack.getTag();
    }

    public ItemStack applyNBT(ItemStack itemStack, Consumer<NBTTagCompound> nbt) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound tag = getNBT(itemStack);

        nbt.accept(tag);

        stack.save(tag); return CraftItemStack.asBukkitCopy(stack);
    }

}
