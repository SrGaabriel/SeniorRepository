package com.gabriel.senior.project.repositories.impl;

import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.prototypes.Prototype;
import com.gabriel.senior.project.repositories.Repository;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AccountRepository implements Repository {

    private static AccountRepository instance;
    public static AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    private final Map<UUID, Account> repository = new HashMap<>();

    @Override
    public final void register(Prototype prototype) {
        if (!(prototype instanceof Account)) {
            throw new InvalidParameterException("The prototype must be an Account!");
        }
        repository.put(prototype.getUniqueId(), (Account)prototype);
    }

    @Override
    public Map<UUID, Account> reveal() {
        return repository;
    }

    @Override
    public final Prototype retrieve(UUID uniqueId) {
        return repository.get(uniqueId);
    }

    @Override
    public final void exclude(Prototype prototype) {
        repository.remove(prototype.getUniqueId());
    }

    @Override
    public final void exclude(UUID uniqueId) {
        repository.remove(uniqueId);
    }
}
