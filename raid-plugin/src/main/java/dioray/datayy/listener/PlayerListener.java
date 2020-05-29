package dioray.datayy.listener;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import dioray.datayy.RaidPlugin;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.player.type.Role;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.prototype.wall.position.BlockPosition;
import dioray.datayy.provider.VoucherProvider;
import dioray.datayy.service.*;
import dioray.datayy.util.Util;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;

public class PlayerListener implements Listener {

    private final RaidPlugin main;
    private final TeamPlayerService teamPlayerService;
    private final TeamService teamService;
    private final PlotWallService plotWallService;
    private final PlotWallUpgradeService plotWallUpgradeService;
    private final MessageService messageService;
    private final TeamDao teamDao;

    public PlayerListener(RaidPlugin main) {
        this.main = main;

        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.teamService = main.getService(TeamService.class);
        this.plotWallService = main.getService(PlotWallService.class);
        this.plotWallUpgradeService = main.getService(PlotWallUpgradeService.class);
        this.messageService = main.getService(MessageService.class);
        this.teamDao = main.getTeamDao();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            TeamPlayerDao teamPlayerDao = this.main.getTeamPlayerDao();
            TeamPlayer teamPlayer = teamPlayerDao.select(e.getPlayer().getUniqueId());
            if (teamPlayer == null) {
                teamPlayer = new TeamPlayer(e.getPlayer().getUniqueId(), null, Role.MEMBER);

                teamPlayerDao.insert(teamPlayer);
            }

            TeamPlayerService teamPlayerService = this.main.getService(TeamPlayerService.class);
            teamPlayerService.addTeamPlayer(teamPlayer);
        });
    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
        Team team = teamPlayer.getTeam();

        teamPlayerService.removeTeamPlayer(teamPlayer);

        if (team != null) {
            team.removePlayer(teamPlayer);
        }

        PlotArea plotArea = this.main.getPlotArea();
        Set<Plot> plots = plotArea.getPlots(teamPlayer.getUuid());
        if (plots.size() == 0) return;

        Plot plot = plots.iterator().next();
        if (plot != null && teamService.getRaidingTeam(plot) == null) {
            plotWallService.clear(plot);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && e.getDamager() instanceof Player) {
            Player hited = (Player) e.getEntity();

            Team hitedTeam = teamPlayerService.getTeamByPlayer(hited);
            if (hitedTeam != null && hitedTeam.isRaiding()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem() && VoucherProvider.isVoucher(e.getItem())) {
            int amount = VoucherProvider.getAmount(e.getItem());
            if (amount > 0) {
                TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
                Team playerTeam = teamPlayer.getTeam();

                if (playerTeam == null) {
                    messageService.sendMessage(e.getPlayer(), "value-voucher.not-in-team");

                    return;
                }

                playerTeam.addValue(amount);
                teamDao.update(playerTeam);

                messageService.sendMessage(e.getPlayer(), "value-voucher.used", "amount", amount);
            }

            return;
        }

        if (!e.hasBlock()) return;
        if (!e.getPlayer().isSneaking()) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock().getType() != this.main.getPlotWallType()) return;

        Location plotLocation = Util.toPlotLocation(e.getClickedBlock().getLocation());
        Plot plot = plotLocation.getPlot();
        if (plot == null) return;

        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
        Team playerTeam = teamPlayer.getTeam();

        if (playerTeam == null) return;

        Team plotTeam = teamService.getByPlot(plot);

        if (!plotTeam.equals(playerTeam)) return;

        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            BlockPosition position = new BlockPosition(e.getClickedBlock().getLocation());
            PlotWall plotWall = this.main.getPlotWallDao().select(plot, position);
            if (plotWall == null) return;

            if (plotWall.getLevel() >= this.main.getPlotWallMaxLevel()) {
                MessageService messageService = this.main.getService(MessageService.class);
                messageService.sendMessage(e.getPlayer(), "plotwall.upgarde.max-level");

                return;
            }

            this.main.getServer().getScheduler().runTask(this.main, () -> plotWallUpgradeService.openUpgradeGUI(e.getPlayer(), plotWall));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(e.getPlayer());
        Team team = teamPlayer.getTeam();
        if (team != null && team.isRaiding()) {
            messageService.sendMessage(e.getPlayer(), "raid.cannot-use-commands");

            e.setCancelled(true);

            return;
        }

        if (!e.getMessage().startsWith("/plotwall") && e.getMessage().startsWith("/plot") && !e.getPlayer().isOp()) {
            messageService.sendMessage(e.getPlayer(), "plot.use");

            e.setCancelled(true);
        }
    }

}