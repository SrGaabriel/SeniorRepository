package dioray.datayy.service;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.PlotWallDao;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.Team;
import dioray.datayy.model.PlotWall;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.util.Util;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RaidService extends Service {

    private final Map<String, Hologram> hologramMap;

    private final MessageService messageService;
    private final SearchCooldownService searchCooldownService;
    private final PlotWallService plotWallService;
    private final TeamService teamService;
    private final TeamDao teamDao;
    private final PlotWallDao plotWallDao;

    public RaidService(RaidPlugin main) {
        super(main);

        this.hologramMap = new HashMap<>();

        this.messageService = main.getService(MessageService.class);
        this.searchCooldownService = main.getService(SearchCooldownService.class);
        this.plotWallService = main.getService(PlotWallService.class);
        this.teamService = main.getService(TeamService.class);
        this.teamDao = main.getTeamDao();
        this.plotWallDao = main.getPlotWallDao();
    }

    public void search(Player player, Team team) {
        Hologram currentHologram = this.hologramMap.remove(team.getTag());
        if (currentHologram != null) {
            currentHologram.delete();
        }

        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            Team raidableTeam = teamDao.getRandomRaidableTeam(team);
            if (raidableTeam == null) {
                messageService.sendMessage(player, "raid.not-found");

                return;
            }

            Plot plot = raidableTeam.getPlot();

            team.setSelectedPlot(plot.getId().toCommaSeparatedString());
            team.setRemainingTime(Util.currentTimeSeconds() + 30);

            this.main.getServer().getScheduler().runTask(this.main, () -> {
                Location plotSide = plot.getSide();
                org.bukkit.Location side = Util.fromPlotLocation(plotSide);
                String message = messageService.get("raid.select");

                for (TeamPlayer tTeamPlayer : team.getOnlinePlayers()) {
                    Player tPlayer = tTeamPlayer.getPlayer();

                    tPlayer.teleport(side);
                    tPlayer.sendMessage(message);
                }

                int plotwallCount = plotWallDao.getCount(plot);
                long teamValue = raidableTeam.getValue();

                Hologram hologram = HologramsAPI.createHologram(this.main, side.add(0, 2, 2));
                hologram.appendTextLine(ChatColor.AQUA + ChatColor.BOLD.toString() + "Raid Details:");
                hologram.appendTextLine(ChatColor.WHITE + "Value: " + ChatColor.AQUA + teamValue);
                hologram.appendTextLine(ChatColor.WHITE + "Plot Wall amount: " + ChatColor.AQUA + plotwallCount);
                hologram.appendTextLine(ChatColor.WHITE + "Average Reward: " + ChatColor.AQUA + Math.round(teamValue * 0.2f));

                this.hologramMap.put(team.getTag(), hologram);
            });
        });
    }

    public void startRaid(Team team) {
        TeamDao teamDao = this.main.getTeamDao();

        Hologram hologram = this.hologramMap.remove(team.getTag());
        if (hologram != null) {
            hologram.delete();
        }

        team.setRemainingTime(Util.currentTimeSeconds() + TimeUnit.MINUTES.toSeconds(this.main.getConfig().getInt("end-raid-time-minutes")));
        team.setRaiding(true);
        searchCooldownService.setCooldown(team);

        PlotArea plotArea = this.main.getPlotArea();
        Plot plot = plotArea.getPlot(PlotId.fromString(team.getSelectedPlot()));
        Team plotTeam = teamService.getByPlot(plot);

        teamDao.updateLastRaid(plotTeam);

        for (TeamPlayer teamPlayer : team.getOnlinePlayers()) {
            plot.addTrusted(teamPlayer.getUUID());
            messageService.sendMessage(teamPlayer.getPlayer(), "raid.start");
        }

        plotWallService.fetchFromDatabase(plot, (plotWallList) -> {
            this.main.getServer().getScheduler().runTask(this.main, () -> {
                World world = Bukkit.getWorld(plot.getWorldName());

                for (PlotWall plotWall : plotWallList) {
                    plotWall.spawnHologram(world);
                }
            });
        });

        TextComponent component = new TextComponent(messageService.get("raid.being-raided"));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/raid see"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                new TextComponent(ChatColor.GOLD + "Click here to see")
        }));

        for (TeamPlayer oTeamPlayer : plotTeam.getOnlinePlayers()) {
            oTeamPlayer.getPlayer().spigot().sendMessage(component);
        }
    }

    public void endRaid(Team team) {
        PlotArea plotArea = this.main.getPlotArea();
        Plot plot = plotArea.getPlot(PlotId.fromString(team.getSelectedPlot()));

        List<UUID> memberUUIDList = teamDao.getMemberUUIDs(team);
        if (memberUUIDList != null) {
            memberUUIDList.forEach(plot::removeTrusted);
        }

        Team attackedTeam = teamService.getByPlot(plot);
        for (TeamPlayer teamPlayer : team.getOnlinePlayers()) {
            Player teamPlayerPlayer = teamPlayer.getPlayer();
            messageService.sendMessage(teamPlayerPlayer, "raid.ended");

            for (TeamPlayer oTeamPlayer : attackedTeam.getOnlinePlayers()) {
                teamPlayerPlayer.showPlayer(this.main, oTeamPlayer.getPlayer());
            }
        }

        List<PlotWall> plotWallList = plotWallService.clear(plot);
        if (plotWallList != null) {
            for (PlotWall plotWall : plotWallList) {
                plotWall.getPosition().toLocation(this.main.getPlotWorld()).getBlock().setType(this.main.getPlotWallType());
                plotWall.removeHologram();
            }
        }

        team.setRaiding(false);
        team.setSelectedPlot(null);
        team.setRemainingTime(0);
    }
}