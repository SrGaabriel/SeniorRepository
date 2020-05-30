package com.raidplugin.sdk.manager;

import com.raidplugin.api.manager.Manager;
import com.raidplugin.api.prototype.wall.Wall;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.List;

public class WallManager implements Manager<Integer[], Wall> {

    private static WallManager wallManager;

    public static WallManager getInstance() {
        return wallManager == null ? (wallManager = new WallManager()) : wallManager;
    }

    private final List<Wall> wallList = new LinkedList<>();

    @Override
    public List<Wall> getCollection() {
        return wallList;
    }

    @Override
    public void put(Wall value) {
        wallList.add(value);
    }

    @Override
    public void remove(Wall value) {
        wallList.remove(value);
    }

    @Override
    public Wall get(Integer... key) {
        for(Wall wall : wallList) {
            if(isSameVector(wall, key)) return wall;
        } return null;
    }

    public Wall get(Block block) {
        return get(block.getX(), block.getY(), block.getZ());
    }

    public boolean isSameVector(Wall wall, Integer... vector) {
        int apply = 0;

        for(int i = 0; i!=vector.length; i++) {
            if(wall.getVector()[i].compareTo(vector[i]) == 0) apply++;
        } return apply == 3;
    }
}
