package com.gabriel.senior.project.prototypes;

import lombok.Data;

import java.util.UUID;

@Data
public class Account implements Prototype {

    private final UUID uniqueId;
    private Backpack backpack;

    public Account(UUID uniqueId) {
        this.uniqueId = uniqueId;
        this.backpack = new Backpack(this);
    }

}
