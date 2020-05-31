package com.raidplugin.sdk.prototype.wall;

import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.wall.Wall;
import org.bukkit.block.Block;

public class CWall implements Wall {

    private final Integer[] vector;
    private final Block block;
    private final Team team;

    private int life, level;

    public CWall(Team team, Block block, int level, int life, Integer... vector) {
        this.team = team; this.block = block; this.level = level; this.life = life; this.vector = vector;
    }

    @Override
    public Integer[] getVector() {
        return vector;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public Team getTeam() {
        return team;
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLife(int life) {
        this.life = life;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
}
