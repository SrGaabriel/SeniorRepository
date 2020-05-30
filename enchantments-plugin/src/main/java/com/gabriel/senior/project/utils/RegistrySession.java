package com.gabriel.senior.project.utils;

import com.gabriel.senior.project.SeniorEnchantments;
import com.gabriel.senior.project.commands.BackpackCommand;
import com.gabriel.senior.project.enchantments.AbstractEnchantment;
import com.gabriel.senior.project.enchantments.impl.Speed;
import com.gabriel.senior.project.listeners.EnchantmentExecutorListener;
import com.gabriel.senior.project.repositories.impl.EnchantmentRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

@SuppressWarnings("deprecation")
public class RegistrySession {

    public RegistrySession() {
        registerCommands();
        registerListeners();
        registerEnchantments();
    }

    private void registerCommands() {
        final Reflections reflections = new Reflections(BackpackCommand.class.getPackage());

        for (Class<?> clazz : reflections.getSubTypesOf(CommandExecutor.class)) {
            try {
                CommandExecutor instance = (CommandExecutor) clazz.newInstance();
                SeniorEnchantments.getInstance().getCommand(instance.toString()).setExecutor(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerListeners() {
        final Reflections reflections = new Reflections(EnchantmentExecutorListener.class.getPackage());

        for (Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
            try {
                Bukkit.getPluginManager().registerEvents(clazz.newInstance(), SeniorEnchantments.getInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEnchantments() {
        final Reflections reflections = new Reflections(Speed.class.getPackage());

        for (Class<? extends AbstractEnchantment> clazz : reflections.getSubTypesOf(AbstractEnchantment.class)) {
            try {
                EnchantmentRepository.getInstance().register(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
