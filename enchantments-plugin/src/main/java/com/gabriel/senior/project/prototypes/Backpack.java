package com.gabriel.senior.project.prototypes;

import com.gabriel.senior.project.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
        inventory.setItem(13, new ItemBuilder(Material.SKULL_ITEM)
                .name("§b§lMINING BACKPACK")
                .skullOwner("MHF_Chest")
                .lore("", "§fUpgrade your backpack to gain more money while mining", "", "§fCurrent Level: §c" + this.level, "§fAmount: §b0.0", "§fCount: §c0/2000000")
                .build()
        );
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
