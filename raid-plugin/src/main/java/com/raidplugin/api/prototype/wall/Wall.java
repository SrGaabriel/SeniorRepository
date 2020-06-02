package com.raidplugin.api.prototype.wall;

import com.raidplugin.api.prototype.Team;
import org.bukkit.block.Block;

/**
 * That's a wall. Wall is a block on team's plot.
 * Every a wall have level and life.
 * You can manage this.
 */

public interface Wall {

    Integer[] getVector();

    Block getBlock();

    Team getTeam();

    int getLife();

    int getLevel();

    void setLife(int life);

    void setLevel(int level);

}
