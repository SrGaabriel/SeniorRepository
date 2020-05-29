package dioray.datayy.provider.wall;

import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.provider.booster.BoostProvider;
import dioray.datayy.provider.message.MessageProvider;
import dioray.datayy.repository.team.TeamRepository;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class WallProvider {
    
    private static WallProvider wallProvider;

    public static WallProvider getInstance() {
        return wallProvider == null ? (wallProvider = new WallProvider()) : wallProvider;
    }

    private final MessageProvider messageProvider = MessageProvider.getInstance();
    private final TeamRepository teamRepository = TeamRepository.getInstance();
    private final BoostProvider boostProvider = BoostProvider.getInstance();

    public int getLevel(ItemStack itemStack) {
        NBTTagCompound nbt = getNBT(itemStack);

        if(!nbt.hasKey("wall-level")) return -1;

        return nbt.getInt("wall-level");
    }

    public boolean isType(Block block) {
        return block.getType() == Material.getMaterial(messageProvider.get(String.class,"wall-type"));
    }

    public void hasDamaged(Player breaker, Block block, int damage) {
        PlotWall wall = teamRepository.get(block.getLocation()); if(wall == null) return;

        wall.setLife(wall.getLife() - damage);

        if(wall.getLife() == 0) {
            TeamPlayer attacker = teamRepository.get(breaker); if(attacker == null) return;
            Team team = attacker.getTeam();

            int damaged = (int) Math.ceil(messageProvider.get(Integer.class, "plot-wall-value-percentage") / 100f);

            if(damaged <= 0) damaged = messageProvider.get(Integer.class, "plot-wall-minumim-reward");

            float boost = boostProvider.getBoost(breaker.getInventory().getItemInMainHand());

            if(boost > 0) damaged *= boost;

            team.setValue(team.getValue() + damaged);
        }
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
