package com.gabriel.senior.project.repositories.impl;

import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import com.gabriel.senior.project.prototypes.Prototype;
import com.gabriel.senior.project.repositories.Repository;
import org.bukkit.enchantments.Enchantment;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnchantmentRepository implements Repository {

    private Map<UUID, AbstractEnchantment> repository = new HashMap<>();

    private static EnchantmentRepository instance;
    public static EnchantmentRepository getInstance() {
        if (instance == null) {
            instance = new EnchantmentRepository();
       }
        return instance;
    }

    @Override
    public void register(Prototype prototype) {
        if (!(prototype instanceof Enchantment)) {
            throw new InvalidParameterException("The prototype must be an Account!");
        }
        repository.put(prototype.getUniqueId(), (AbstractEnchantment) prototype);
    }

    @Override
    public Prototype retrieve(UUID uniqueId) {
        return repository.get(uniqueId);
    }

    @Override
    public Map<UUID, AbstractEnchantment> reveal() {
        return repository;
    }

    @Override
    public void exclude(Prototype prototype) {
        repository.remove(prototype);
    }

    @Override
    public void exclude(UUID uniqueId) {
        repository.remove(uniqueId);
    }
}
