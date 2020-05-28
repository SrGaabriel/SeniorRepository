package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ValueBoosterService extends Service {

    public ValueBoosterService(RaidPlugin main) {
        super(main);
    }

    public ItemStack setBoost(ItemStack itemStack, float multiplier) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        nbtTag.setFloat("valuebooster_multiplier", multiplier);

        nmsItemStack.setTag(nbtTag);

        itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemMeta meta = itemStack.getItemMeta();

        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        lore.add(String.format("%sValue Booster x%.1f", ChatColor.GRAY.toString(), multiplier));
        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public float getAverageBoostTeam(Team team) {
        float boost = 0;
        for (TeamPlayer teamPlayer : team.getOnlinePlayers()) {
            Player player = teamPlayer.getPlayer();
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack != null && itemStack.getType().name().contains("PICKAXE")) {
                    float itemBoost = getBoost(itemStack);

                    boost += itemBoost > 0 ? itemBoost : 0;
                }
            }
        }

        return boost / team.getOnlinePlayers().size();
    }

    public float getBoost(ItemStack itemStack) {
        if (itemStack == null) return -1;
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        if (nbtTag.hasKey("valuebooster_multiplier")) {
            return nbtTag.getFloat("valuebooster_multiplier");
        }

        return -1;
    }

}