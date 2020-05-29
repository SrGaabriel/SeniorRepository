package xyz.diogomurano.enchants;

import xyz.diogomurano.enchants.custom.CustomEnchantService;

public interface EnchantmentPlugin {

    CustomEnchantService getEnchantService();

    net.milkbowl.vault.economy.Economy getEconomy();

}