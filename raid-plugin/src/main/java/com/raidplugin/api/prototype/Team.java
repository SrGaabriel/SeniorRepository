package com.raidplugin.api.prototype;

import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.wall.Wall;
import org.bukkit.block.Block;

import java.util.List;

/**
 * That's Team or the core of all.
 * You can manage all team's values!
 * But, ever verify the same in repositories/managers.
 */

public interface Team {

    String getName();

    String getPrefix();

    List<Member> getMembers();

    List<Wall> getWalls();

    int getHealth();

    Plot getPlot();

    Block getEgg();

    int getLevel();

    int getBlocks();

    int getPower();

    void setBlocks(int blocks);

    void setHealth(int health);

    void setLevel(int level);

    void setPlot(Plot plot);

    void setEgg(Block block);

    void setPower(int power);


}
