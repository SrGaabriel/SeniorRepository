package com.raidplugin.api.prototype.wall;

import com.raidplugin.api.prototype.Team;
import org.bukkit.block.Block;

public interface Wall {

    Integer[] getVector();

    Block getBlock();

    Team getTeam();

    int getLife();

    int getLevel();

    void setLife(int life);

    void setLevel(int level);

}
