package com.raidplugin.sdk.provider.wall;

import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.sdk.manager.WallManager;
import com.raidplugin.sdk.provider.nbt.NBTProvider;
import com.raidplugin.sdk.repository.member.MemberRepository;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WallProvider {

    private final NBTProvider nbtProvider = new NBTProvider();
    private final MemberRepository memberRepository = MemberRepository.getInstance();
    private final WallManager wallManager = WallManager.getInstance();

    public void hasDamaged(Block block, Player breaker, int damage) {
        // Wall event to damage.
    }

    public ItemStack toItemStack(Wall wall) {
        return null;
    }

}
