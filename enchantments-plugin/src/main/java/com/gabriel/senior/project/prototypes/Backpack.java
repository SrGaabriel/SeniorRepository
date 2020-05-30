package com.gabriel.senior.project.prototypes;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Data
public class Backpack implements Prototype {

    private final Account owner;
    private final UUID uniqueId;
    private ItemStack[] contents;

    public Backpack(Account owner) {
        this.owner = owner;
        this.uniqueId = owner.getUniqueId();
    }

}
