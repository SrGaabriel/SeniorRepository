package dioray.datayy.listener;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

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

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockTeleport(BlockFromToEvent e) {
        if (e.getBlock().getType() == Material.DRAGON_EGG) {
            Location plotLocation = Util.toPlotLocation(e.getBlock().getLocation());
            Plot plot = plotLocation.getPlot();
            if (plot == null || !this.main.isRaidPlot(plot)) return;

            e.setCancelled(true);
        }
    }
}