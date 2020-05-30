package com.gabriel.senior.project.prototypes;

import java.util.UUID;

public class Account implements Prototype {

    private final UUID uniqueId;
    private Backpack backpack;
    private Long liquid;

    public Account(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.backpack = new Backpack(this);
    }

    public Account(UUID uniqueId, Backpack backpack, Long liquid) {
        this.uniqueId = uniqueId;
        this.backpack = backpack;
        this.liquid = liquid;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
    }

    public Long getLiquid() {
        return liquid;
    }

    public void setLiquid(Long liquid) {
        this.liquid = liquid;
    }
}
