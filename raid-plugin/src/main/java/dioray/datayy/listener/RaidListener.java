package dioray.datayy.listener;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.provider.message.MessageProvider;
import dioray.datayy.provider.wall.WallProvider;
import dioray.datayy.provider.world.WorldProvider;
import dioray.datayy.repository.raid.RaidRepository;
import dioray.datayy.repository.team.TeamRepository;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class RaidListener  implements Listener {

    private final TeamRepository teamRepository = TeamRepository.getInstance();
    private final RaidRepository raidRepository = RaidRepository.getInstance();
    private final WorldProvider worldProvider = WorldProvider.getInstance();
    private final WallProvider wallProvider = WallProvider.getInstance();
    private final MessageProvider messageProvider = MessageProvider.getInstance();

    private final PlotAPI plotAPI = new PlotAPI();

    @EventHandler(priority = EventPriority.LOW)
    private void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        ItemStack itemStack = event.getItemInHand();

        if(!worldProvider.isWorld(location)) return;

        Plot plot = plotAPI.getPlot(location); if(plot == null) return;
        TeamPlayer teamPlayer = teamRepository.get(player); if(teamPlayer == null) return;
        Team team = teamPlayer.getTeam();

        if(team.isRaiding()) { // On raid.
            Team target = raidRepository.get(team);

            if(target == null) return;

            event.setCancelled(true); return;
        } // On place wall.

        int level = wallProvider.getLevel(itemStack); if(level == -1) return;

        Block down = block.getRelative(BlockFace.DOWN);

        if(!wallProvider.isType(down)) {
            event.setCancelled(true); return;
        }

        int blocks = 0;

        for(int i = -1; i >= -3; i--) {
            if(wallProvider.isType(block.getRelative(0, i, 0))) blocks++;
        }

        if(blocks > 3) {
            event.setCancelled(true); return;
        }

        PlotWall plotWall = new PlotWall(level, getLife(level),
                location.getBlockX(), location.getBlockY(), location.getBlockZ(), plot);

        team.getWalls().add(plotWall);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();

        if(!worldProvider.isWorld(location)) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();



        }

    }

    public boolean isSameTeam(Team team, Team target) {
        return team.getPrefix().equals(target.getPrefix());
    }

    public int getLife(int level) {
        return level * messageProvider.get(Integer.class, "plot-wall-life-grow-amount");
    }

}
