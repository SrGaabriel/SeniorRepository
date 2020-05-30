package com.gabriel.senior.project.prototypes;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class Backpack implements Prototype {

    private final Account owner;
    private final UUID uniqueId;
    private int level;
    private Inventory inventory;

    public Backpack(Account owner) {
        this.owner = owner;
        this.uniqueId = owner.getUniqueId();
        this.level = 1;
        this.inventory = Bukkit.createInventory(null, 3*9, "Backpack");
    }

    public Backpack(Account owner, UUID uniqueId, int level, Inventory inventory) {
        this.owner = owner;
        this.uniqueId = uniqueId;
        this.level = level;
        this.inventory = inventory;
    }

    public Account getOwner() {
        return owner;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
