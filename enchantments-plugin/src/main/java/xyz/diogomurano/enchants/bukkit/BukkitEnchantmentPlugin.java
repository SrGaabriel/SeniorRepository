package xyz.diogomurano.enchants.bukkit;

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
import xyz.diogomurano.enchants.bukkit.listener.InventoryListener;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.listener.GeneralListener;
import xyz.diogomurano.enchants.bukkit.timer.AutoSellTimer;
import xyz.diogomurano.enchants.bukkit.utils.BlockUtil;
import xyz.diogomurano.enchants.bukkit.utils.menu.MenuListener;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

public class BukkitEnchantmentPlugin extends JavaPlugin implements EnchantmentPlugin {

    private static BukkitEnchantmentPlugin instance;
    private static EnchantmentSettings settings;
    
    private CustomEnchantService enchantService;
    private Economy economy;

    @Override
    public void onLoad() {
        instance = this;

        Settings.load(this);
    }

    @Override
    public void onEnable() {
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

        new AutoSellTimer(this).runTaskTimer(this, 0, 20);

        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }
    
    private boolean hookEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
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
    public CustomEnchantService getEnchantService() {
        return enchantService;
    }

    @Override
    public Economy getEconomy() {
        return economy;
    }

    public static EnchantmentSettings getSettings() {
        return settings;
    }

    public static BukkitEnchantmentPlugin getInstance() {
        return instance;
    }
}