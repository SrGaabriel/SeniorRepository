package xyz.diogomurano.enchants.custom;

import com.google.common.collect.ImmutableList;

import java.util.UUID;

public interface CustomEnchantService {

    ImmutableList<CustomEnchant> getAll();

    void add(CustomEnchant enchant);

    void remove(CustomEnchant enchant);

    CustomEnchant get(UUID uniqueId);

    CustomEnchant get(String name);

}
