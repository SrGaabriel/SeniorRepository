package dioray.datayy.provider.wall;

import dioray.datayy.provider.message.MessageProvider;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class WallProvider {
    
    private static WallProvider wallProvider;

    public static WallProvider getInstance() {
        return wallProvider == null ? (wallProvider = new WallProvider()) : wallProvider;
    }

    private final MessageProvider messageProvider = MessageProvider.getInstance();

    public int getLevel(ItemStack itemStack) {
        NBTTagCompound nbt = getNBT(itemStack);

        if(!nbt.hasKey("wall-level")) return -1;

        return nbt.getInt("wall-level");
    }

    public boolean isType(Block block) {
        return block.getType() == Material.getMaterial(messageProvider.get(String.class,"wall-type"));
    }

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
