package dioray.datayy;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import dioray.datayy.command.PlotWallCommand;
import dioray.datayy.command.RaidCommand;
import dioray.datayy.command.ValueCommand;
import dioray.datayy.database.PlotWallDao;
import dioray.datayy.database.TeamDao;
import dioray.datayy.database.TeamPlayerDao;
import dioray.datayy.listener.BlockListener;
import dioray.datayy.listener.InventoryListener;
import dioray.datayy.listener.PlayerListener;
import dioray.datayy.listener.PlotListener;
import dioray.datayy.placeholder.RaidExpansion;
import dioray.datayy.service.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class RaidPlugin extends JavaPlugin {

    private static RaidPlugin instance;

    private Set<Service> serviceSet;
    private TeamPlayerDao teamPlayerDao;
    private PlotWallDao plotWallDao;
    private TeamDao teamDao;

    private Material plotWallType;
    private int plotWallMaxLevel;
    private World plotWorld;

    private Economy economy;

    @SuppressWarnings("unchecked")
    @Override
    public void onEnable() {
        instance = this;

        if (!hookVault()) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Vault was not found");
            this.getServer().getPluginManager().disablePlugin(this);

            return;
        }

        saveDefaultConfig();

        this.plotWallType = Material.matchMaterial(this.getConfig().getString("plot-wall-type"));
        this.plotWallMaxLevel = this.getConfig().getInt("plot-wall-max-level");

        registerServices(
                MessageService.class,
                DatabaseService.class,
                TeamService.class,
                TeamPlayerService.class,
                PlotWallService.class,
                RaidService.class,
                InviteService.class,
                SearchCooldownService.class,
                ValueBoosterService.class,
                PlotWallUpgradeService.class
        );

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BlockListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlotListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);

        this.teamPlayerDao = new TeamPlayerDao(this);
        this.plotWallDao = new PlotWallDao(this);
        this.teamDao = new TeamDao(this);

        new RaidCommand(this).register();
        new ValueCommand(this).register();
        new PlotWallCommand(this).register();

        new RaidExpansion(this).register();

        this.getLogger().info("Raid Plugin initialized with success");
    }

    @Override
    public void onDisable() {
        this.serviceSet.forEach(Service::disable);

        this.getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }

    private boolean hookVault() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vault")) return false;

        RegisteredServiceProvider<Economy> rsp = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;

        return (this.economy = rsp.getProvider()) != null;
    }

    public TeamPlayerDao getTeamPlayerDao() {
        return teamPlayerDao;
    }

    public PlotWallDao getPlotWallDao() {
        return this.plotWallDao;
    }

    public TeamDao getTeamDao() {
        return this.teamDao;
    }

    public Economy getEconomy() {
        return economy;
    }

    @SuppressWarnings("unchecked")
    public <T extends Service> T getService(Class<T> clazz) {
        Service service = this.serviceSet.stream().filter(s -> s.getClass() == clazz).findFirst().orElse(null);
        if (service == null) {
            throw new IllegalStateException(clazz.getSimpleName() + " was not loaded yet");
        }

        return (T) service;
    }

    @SuppressWarnings("unchecked")
    private void registerServices(Class<? extends Service>... services) {
        this.serviceSet = new HashSet<>();

        try {
            for (Class<?> serviceClass : services) {
                Service service = (Service) serviceClass.getConstructor(RaidPlugin.class).newInstance(this);

                this.serviceSet.add(service);
            }

            for (Service service : this.serviceSet) {
                service.enable();

                this.getLogger().info(service.getClass().getSimpleName() + " was enabled");
            }
        } catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, "Something went wrong when registering the services", ex);
        }
    }

    public World getPlotWorld() {
        if (this.plotWorld == null) {
            this.plotWorld = Bukkit.getWorld(this.getConfig().getString("plot-world"));
        }

        return this.plotWorld;
    }

    public PlotArea getPlotArea() {
        return PS.get().getPlotAreas(this.getConfig().getString("plot-world")).iterator().next();
    }

    public boolean isRaidPlot(Plot plot) {
        return plot.getArea().worldname.equalsIgnoreCase(this.getConfig().getString("plot-world"));
    }

    public Material getPlotWallType() {
        return this.plotWallType;
    }

    public int getPlotWallMaxLevel() {
        return this.plotWallMaxLevel;
    }

    public static int getPlotWallLife(int level) {
        int perLevel = RaidPlugin.getPlugin(RaidPlugin.class).getConfig().getInt("plot-wall-life-grow-amount");

        return level * perLevel;
    }

    public static int getPlotWallPrice(int level) {
        int perLevel = RaidPlugin.getPlugin(RaidPlugin.class).getConfig().getInt("plot-wall-price-grow-amount");

        return level * perLevel;
    }

    public static RaidPlugin getInstance() {
        return instance;
    }
}