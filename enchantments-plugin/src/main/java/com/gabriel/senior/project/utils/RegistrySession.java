package com.gabriel.senior.project.utils;

import com.gabriel.senior.project.SeniorEnchantments;
import com.gabriel.senior.project.commands.BackpackCommand;
import com.gabriel.senior.project.enchantments.impl.Speed;
import com.gabriel.senior.project.enchantments.impl.Thor;
import com.gabriel.senior.project.listeners.BackpackListener;
import com.gabriel.senior.project.listeners.EnchantmentExecutorListener;
import com.gabriel.senior.project.listeners.LiquidListener;
import com.gabriel.senior.project.repositories.impl.EnchantmentRepository;
import org.bukkit.Bukkit;

public class RegistrySession {

    public RegistrySession() {
        final SeniorEnchantments instance = SeniorEnchantments.getInstance();

        instance.getCommand("backpack").setExecutor(new BackpackCommand());

        Bukkit.getPluginManager().registerEvents(new BackpackListener(), instance);
        Bukkit.getPluginManager().registerEvents(new EnchantmentExecutorListener(), instance);
        Bukkit.getPluginManager().registerEvents(new LiquidListener(), instance);

        EnchantmentRepository.getInstance().register(new Speed());
        EnchantmentRepository.getInstance().register(new Thor());
    }

}
