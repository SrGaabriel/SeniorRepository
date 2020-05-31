package com.raidplugin.sdk.prototype;

import com.google.common.collect.Lists;
import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.wall.Wall;
import org.bukkit.block.Block;

import java.util.List;

public class WTeam implements Team {

    private final String name;

    private final List<Member> memberList = Lists.newArrayListWithCapacity(5);
    private final List<Wall> wallList = Lists.newLinkedList();

    private int health, power, level, breaks = 0;
    private Plot plot;
    private Block block;

    public WTeam(String name, int health, int power, int level, Plot plot, Block block) {
        this.block = block; this.name = name; this.health = health; this.plot = plot; this.level = level; this.power = power;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPrefix() {
        return name.substring(0, 3).toUpperCase();
    }

    @Override
    public List<Member> getMembers() {
        return memberList;
    }

    @Override
    public List<Wall> getWalls() {
        return wallList;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public Plot getPlot() {
        return plot;
    }

    @Override
    public Block getEgg() {
        return block;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getBlocks() {
        return breaks;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public void setBlocks(int blocks) {
        this.breaks = blocks;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setPlot(Plot plot) {
        this.plot = plot;
    }

    @Override
    public void setEgg(Block block) {
        this.block = block;
    }

    @Override
    public void setPower(int power) {
        this.power = power;
    }
}
