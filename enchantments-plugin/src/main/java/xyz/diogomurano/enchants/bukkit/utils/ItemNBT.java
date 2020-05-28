package xyz.diogomurano.enchants.bukkit.utils;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.item.BackpackInfo;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;

public class ItemNBT {

    // region Counter NBT
    public static boolean hasCounters(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        return customEnchants.hasKey("autosell_counter");
    }

    public static ItemStack initCounters(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        for (User.Counter counter : User.Counter.values()) {
            if (counter != User.Counter.BACKPACK) {
                customEnchants.setInt(counter.name().toLowerCase() + "_counter", 0);
            }
        }

        nbtTag.set("CustomEnchants", customEnchants);

        nmsItemStack.setTag(nbtTag);

        return CraftItemStack.asCraftMirror(nmsItemStack);
    }

    public static Result addCount(ItemStack itemStack, User.Counter counter, int amount) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        int blockSet, countToUpgrade;
        if (counter == User.Counter.BACKPACK) {
            countToUpgrade = Backpack.getCountToUpgrade();
            blockSet = Backpack.getBlockSet();
        } else {
            CustomEnchant customEnchant = BukkitEnchantmentPlugin.getInstance().getEnchantService().get(counter.name());

            countToUpgrade = customEnchant.getCountToUpgrade();
            blockSet = customEnchant.getBlockSet();
        }

        int counterValue = customEnchants.getInt(counter.name().toLowerCase() + "_counter");
        int newCounterValue = counterValue + amount;

        if (newCounterValue > blockSet) {
            newCounterValue = blockSet;
        }

        customEnchants.setInt(counter.name().toLowerCase() + "_counter", newCounterValue);

        nbtTag.set("CustomEnchants", customEnchants);

        nmsItemStack.setTag(nbtTag);

        int levelsUp = 0;
        if (counterValue > 0 && counterValue % countToUpgrade == 0) {
            levelsUp++;
        }

        if (amount > 1) {
            for (int i = counterValue + 1; i <= newCounterValue; i++) {
                if (i > 0 && i % countToUpgrade == 0) {
                    levelsUp++;
                }
            }
        }

        return new Result(CraftItemStack.asCraftMirror(nmsItemStack), levelsUp);
    }

    public static int getCount(ItemStack itemStack, User.Counter counter) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        return customEnchants.getInt(counter.name().toLowerCase() + "_counter");
    }
    // endregion

    // region Backpack NBT
    public static ItemStack initBackpack(ItemStack itemStack, int level) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        customEnchants.setInt("backpack_counter", 0);
        customEnchants.setInt("backpack_level", level);
        customEnchants.setInt("backpack_items", 0);
        customEnchants.setFloat("backpack_price", 0);

        nbtTag.set("CustomEnchants", customEnchants);

        nmsItemStack.setTag(nbtTag);

        return CraftItemStack.asCraftMirror(nmsItemStack);
    }

    public static BackpackInfo getBackpackInfo(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        int level = customEnchants.getInt("backpack_level");
        int items = customEnchants.getInt("backpack_items");
        float price = customEnchants.getFloat("backpack_price");
        int counter = customEnchants.getInt("backpack_counter");

        return new BackpackInfo(level, items, price, counter);
    }

    public static ItemStack upgradeBackpack(ItemStack itemStack, int amount) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        int level = customEnchants.getInt("backpack_level");
        customEnchants.setInt("backpack_level", level + amount);

        nbtTag.set("CustomEnchants", customEnchants);

        nmsItemStack.setTag(nbtTag);

        return CraftItemStack.asCraftMirror(nmsItemStack);
    }

    public static ItemStack updateBackpack(ItemStack itemStack, BackpackInfo info) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        NBTTagCompound customEnchants = nbtTag.hasKey("CustomEnchants") ? nbtTag.getCompound("CustomEnchants") : new NBTTagCompound();

        customEnchants.setInt("backpack_counter", info.getCount());
        customEnchants.setInt("backpack_level", info.getLevel());
        customEnchants.setInt("backpack_items", info.getItems());
        customEnchants.setFloat("backpack_price", info.getItemsPrice());

        nbtTag.set("CustomEnchants", customEnchants);

        nmsItemStack.setTag(nbtTag);

        return CraftItemStack.asCraftMirror(nmsItemStack);
    }

    // endregion

    public static class Result {
        public final ItemStack itemStack;
        public final int levelsUp;

        public Result(ItemStack itemStack, int levelsUp) {
            this.itemStack = itemStack;
            this.levelsUp = levelsUp;
        }
    }
}