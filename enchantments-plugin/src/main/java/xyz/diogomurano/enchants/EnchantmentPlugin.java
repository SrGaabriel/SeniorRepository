package xyz.diogomurano.enchants;

import net.milkbowl.vault.economy.Economy;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

import java.util.function.Consumer;

public interface EnchantmentPlugin {

    CustomEnchantService getEnchantService();

    Economy getEconomy();

}