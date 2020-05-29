package xyz.diogomurano.enchants.bukkit;

import dioray.datayy.util.BlockUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.diogomurano.enchants.EnchantmentPlugin;
import xyz.diogomurano.enchants.EnchantmentSettings;
import xyz.diogomurano.enchants.bukkit.command.BackpackGiveCommand;
import xyz.diogomurano.enchants.bukkit.command.CustomEnchantCommand;
import xyz.diogomurano.enchants.bukkit.command.SellToggleCommand;
import xyz.diogomurano.enchants.bukkit.custom.CustomEnchantServiceImpl;
import xyz.diogomurano.enchants.bukkit.custom.enchants.*;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.listener.GeneralListener;
import xyz.diogomurano.enchants.bukkit.listener.InventoryListener;
import xyz.diogomurano.enchants.bukkit.timer.AutoSellTimer;
import xyz.diogomurano.enchants.bukkit.utils.menu.MenuListener;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

@EqualsAndHashCode(callSuper = true)
@Data
public final class BukkitEnchantmentPlugin extends JavaPlugin implements EnchantmentPlugin {

    private static BukkitEnchantmentPlugin instance;
    @Getter
    private static EnchantmentSettings settings;
    private CustomEnchantService enchantService;
    private net.milkbowl.vault.economy.Economy economy;

    @Override
    public final void onLoad() {
        instance = this;

        Settings.load(this);
    }

    @Override
    public final void onEnable() {
        enchantService = new CustomEnchantServiceImpl();

        if (!hookEconomy()) {
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "No economy plugin found");

            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        settings = new BukkitEnchantmentSettings(this).with(enchantmentSettings -> {
            enchantmentSettings.createFiles();
            enchantmentSettings.loadFiles();
        });

        new CustomEnchantCommand(this).register();
        getCommand("backpack").setExecutor(new BackpackGiveCommand());
        getCommand("selltoggle").setExecutor(new SellToggleCommand());

        getServer().getPluginManager().registerEvents(new GeneralListener(this), this);

        registerEnchants();

        BlockUtil.init();
        Backpack.init();

        new AutoSellTimer(this).runTaskTimerAsynchronously(this, 0, 20);

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private boolean hookEconomy() {
        final RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        return (this.economy = rsp.getProvider()) != null;
    }

    private void registerEnchants() {
        this.enchantService.add(new AutoSell());
        this.enchantService.add(new Demolisher());
        this.enchantService.add(new Digger());
        this.enchantService.add(new Efficiency());
        this.enchantService.add(new Explosion());
        this.enchantService.add(new Fortune());
        this.enchantService.add(new Haste());
        this.enchantService.add(new Jesus());
        this.enchantService.add(new KeyGen());
        this.enchantService.add(new Meteor());
        this.enchantService.add(new Speed());
        this.enchantService.add(new Thor());
    }

    @Override
    public final CustomEnchantService getEnchantService() {
        return enchantService;
    }

    public static BukkitEnchantmentPlugin getInstance() {
        return instance;
    }
}