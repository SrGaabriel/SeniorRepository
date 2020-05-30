package com.gabriel.senior.project.repositories;

import com.gabriel.senior.project.prototypes.Prototype;

import java.util.Map;
import java.util.UUID;

public interface Repository {

    void register(Prototype prototype);

    Prototype retrieve(UUID uniqueId);

    Map<UUID, ? extends Prototype> reveal();

    void exclude(Prototype prototype);

    void exclude(UUID uniqueId);

}
