package dioray.datayy.provider.booster;

import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class BoostProvider {

    private static BoostProvider boostProvider;

    public static BoostProvider getInstance() {
        return boostProvider == null ? (boostProvider = new BoostProvider()) : boostProvider;
    }

    public ItemStack toItemStack(ItemStack target, float multiplier) {
        return applyNBT(target, nbt -> nbt.setFloat("valuebooster_multiplier", multiplier));
    }

    public float fromTeam(Team team) {
        float boost = 0;

        for(TeamPlayer teamPlayer : team.getPlayers()) {
            Player player = teamPlayer.getPlayer();

            for(ItemStack itemStack : player.getInventory().getContents()) {
                if(itemStack == null || !itemStack.getType().name().contains("PICKAXE")) continue;

                float target = getBoost(itemStack); if(target == -1) continue;

                boost += target;
            }
        } return boost;
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

    public ItemStack applyNBT(ItemStack itemStack, Consumer<NBTTagCompound> nbt) {
        net.minecraft.server.v1_12_R1.ItemStack stack = CraftItemStack.asNMSCopy(itemStack);

        NBTTagCompound tag = getNBT(itemStack);

        nbt.accept(tag);

        stack.save(tag); return CraftItemStack.asBukkitCopy(stack);
    }

}
