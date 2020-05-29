package dioray.datayy.provider.booster;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class BoostProvider {

    private static BoostProvider boostProvider;

    public static BoostProvider getInstance() {
        return boostProvider == null ? (boostProvider = new BoostProvider()) : boostProvider;
    }

    public float getBoost(ItemStack itemStack) {
        NBTTagCompound nbt = getNBT(itemStack);

        if(!nbt.hasKey("valuebooster_multiplier")) return -1;

        return nbt.getFloat("valuebooster_multiplier");
    }

    public NBTTagCompound getNBT(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);

        return stack.getTag() == null ? new NBTTagCompound() : stack.getTag();
    }

}
