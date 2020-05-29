package xyz.diogomurano.enchants.bukkit.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public final class BlockUtil {

    private static StateFlag blockBreakFlag;

    public static void init() {
        BlockUtil.blockBreakFlag = (StateFlag) WorldGuardPlugin.inst().getFlagRegistry().get("block-break");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canBeBroken(Block block) {
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(block.getWorld());
        if (regionManager == null) return false;

        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(block.getLocation());
        if (regionSet.getRegions().size() == 0) return false;

        return regionSet.getRegions().stream().anyMatch(region -> region.getFlag(BlockUtil.blockBreakFlag) == StateFlag.State.ALLOW);
    }

    public static ItemStack getItem(Block block) {
        return new ItemStack(block.getType(), 1, block.getData());
    }

}