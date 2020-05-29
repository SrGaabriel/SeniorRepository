package dioray.datayy.listener;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.OfflinePlotPlayer;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.OfflinePlayerUtil;
import com.plotsquared.bukkit.uuid.OfflineUUIDWrapper;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.PlotWallDao;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.BlockPosition;
import dioray.datayy.model.PlotWall;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.*;
import dioray.datayy.util.BlockUtil;
import dioray.datayy.util.Util;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    private final RaidPlugin main;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;
    private final TeamService teamService;
    private final PlotWallService plotWallService;
    private final ValueBoosterService valueBoosterService;
    private final RaidService raidService;
    private final TeamDao teamDao;
    private final PlotWallDao plotWallDao;

    public BlockListener(RaidPlugin main) {
        this.main = main;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.teamService = main.getService(TeamService.class);
        this.plotWallService = main.getService(PlotWallService.class);
        this.valueBoosterService = main.getService(ValueBoosterService.class);
        this.raidService = main.getService(RaidService.class);
        this.teamDao = main.getTeamDao();
        this.plotWallDao = main.getPlotWallDao();
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        TeamPlayer breakerTeamPlayer = teamPlayerService.getTeamPlayerByPlayer(event.getPlayer());
        Team breakerTeam = breakerTeamPlayer.getTeam();

        Location plotLocation = Util.toPlotLocation(event.getBlock().getLocation());
        Plot plot = plotLocation.getPlot();

        if (plot == null || !this.main.isRaidPlot(plot)) {
            ItemStack hand = event.getPlayer().getInventory().getItemInMainHand();

            if (hand == null || !hand.getType().name().contains("PICKAXE")) return;

            if (!BlockUtil.canBeBroken(event.getBlock())) return;

            if (breakerTeam != null) {
                breakerTeam.checkBlockBreak();

                teamDao.update(breakerTeam);
            }

            return;
        }

        if (breakerTeam == null) return;

        Team plotTeam = teamService.getByPlot(plot);
        BlockPosition blockPosition = new BlockPosition(event.getBlock().getLocation());

        event.setCancelled(true);

        if (breakerTeam.equals(plotTeam)) {
            Team raidingTeam = teamService.getRaidingTeam(plot);
            if (raidingTeam != null) return;

            PlotWall plotWall = plotWallDao.select(plot, blockPosition);
            if (plotWall == null) return;

            plotWallDao.delete(plotWall);

            event.setDropItems(false);
            event.setCancelled(false);

            this.main.getServer().getScheduler().runTaskLater(this.main, () -> {
                ItemStack itemStack = plotWallService.createPlotWallItemStack(plotWall.getLevel(), 1);
                org.bukkit.Location location = event.getBlock().getLocation().add(0.5, 0, 0.5);

                event.getBlock().getWorld().dropItemNaturally(location, itemStack);
            }, 5);

            return;
        }

        if (breakerTeam.isRaiding(plot)) {
            if (event.getBlock().getType() != this.main.getPlotWallType() && event.getBlock().getType() != Material.DRAGON_EGG) return;

            PlotWall plotWall = plotWallService.getPlotWall(plot, event.getBlock().getLocation());
            if (plotWall == null) {
                if (event.getBlock().getType() == Material.DRAGON_EGG) {
                    float breakPercentage = plotWallService.getPlotWallBreakPercentage(plot);
                    if (breakPercentage < 0.70) {
                        messageService.sendMessage(event.getPlayer(), "raid.cannot-break-core");

                        return;
                    }

                    int value = Math.round(breakerTeam.getValue() * 0.2f);
                    if (value <= 0) {
                        value = this.main.getConfig().getInt("plot-core-minumim-reward");
                    }

                    float averageTeamBoost = valueBoosterService.getAverageBoostTeam(breakerTeam);
                    if (averageTeamBoost > 0) {
                        value *= averageTeamBoost;
                    }

                    breakerTeam.addCorecount();
                    breakerTeam.addValue(value);
                    teamDao.update(breakerTeam);

                    plotTeam.removeValue(value);
                    teamDao.update(plotTeam);

                    messageService.sendMessage(event.getPlayer(), "raid.break-core", "value", value);

                    raidService.endRaid(breakerTeam);

                    event.setCancelled(false);

                    this.main.getServer().getScheduler().runTaskLater(this.main, () -> event.getBlock().setType(Material.DRAGON_EGG), 20 * 10);

                    return;
                }

                return;
            }

            boolean shouldCancel = plotWallService.handlePlotWallBreak(plotTeam, event.getPlayer(), plotWall, 1);
            if (!shouldCancel) {
                event.setDropItems(false);
                event.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Location plotLocation = Util.toPlotLocation(e.getBlock().getLocation());
        Plot plot = plotLocation.getPlot();
        if (plot == null || !this.main.isRaidPlot(plot)) return;

        TeamPlayer placerTeamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
        Team placerTeam = placerTeamPlayer.getTeam();
        if (placerTeam == null) return;

        Team plotTeam = teamService.getByPlot(plot);

        if (placerTeam.equals(plotTeam)) {
            Team raidingTeam = teamService.getRaidingTeam(plot);
            if (raidingTeam != null) {
                e.setCancelled(true);

                return;
            }

            int level = plotWallService.getLevelFromItemStack(e.getItemInHand());
            if (level == -1) {
                e.setCancelled(true);

                return;
            }

            Material belowType = e.getBlock().getRelative(BlockFace.DOWN).getType();
            if (belowType != Material.DIAMOND_BLOCK && belowType != this.main.getPlotWallType()) {
                messageService.sendMessage(e.getPlayer(), "plotwall.not-diamond");
                e.setCancelled(true);

                return;
            }

            if (belowType == this.main.getPlotWallType()) {
                int plotWallCount = 1;
                for (int i = -1; i >= -3; i--) {
                    if (e.getBlock().getRelative(0, i, 0).getType() == this.main.getPlotWallType()) {
                        plotWallCount++;
                    }
                }

                if (plotWallCount > 3) {
                    messageService.sendMessage(e.getPlayer(), "plotwall.limit");

                    e.setCancelled(true);

                    return;
                }
            }

            BukkitPlayer player = WorldEditPlugin.getPlugin(WorldEditPlugin.class).wrapPlayer(e.getPlayer());
            PlotWall plotWall = new PlotWall(level, new BlockPosition(e.getBlock().getLocation()), plot, player.getCardinalDirection());
            plotWallDao.insert(plotWall);

            return;
        }

        if (placerTeam.isRaiding(plot)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockTeleport(BlockFromToEvent e) {
        if (e.getBlock().getType() == Material.DRAGON_EGG) {
            Location plotLocation = Util.toPlotLocation(e.getBlock().getLocation());
            Plot plot = plotLocation.getPlot();
            if (plot == null || !this.main.isRaidPlot(plot)) return;

            e.setCancelled(true);
        }
    }
}