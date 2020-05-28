package dioray.datayy.service;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.PlotWallDao;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.PlotWall;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.util.Util;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PlotWallService extends Service {

    private final Map<String, List<PlotWall>> plotWallMap;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;
    private final ValueBoosterService valueBoosterService;
    private final PlotWallDao plotWallDao;
    private final TeamDao teamDao;

    public PlotWallService(RaidPlugin main) {
        super(main);

        this.plotWallMap = new HashMap<>();

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.valueBoosterService = main.getService(ValueBoosterService.class);
        this.plotWallDao = main.getPlotWallDao();
        this.teamDao = main.getTeamDao();
    }

    public void fetchFromDatabase(Plot plot, Consumer<List<PlotWall>> consumer) {
        plotWallDao.fetchAll(plot, (plotWallList) -> {
            if (plotWallList.size() > 0) {
                this.plotWallMap.put(plot.getId().toCommaSeparatedString(), plotWallList);

                consumer.accept(plotWallList);
            }
        });
    }

    public float getPlotWallBreakPercentage(Plot plot) {
        float total = plotWallDao.getCount(plot);
        if (total == 0) return 1;

        List<PlotWall> plotWallList = this.plotWallMap.get(plot.getId().toCommaSeparatedString());
        if (plotWallList == null) return 1;

        return plotWallList.stream().filter(PlotWall::isBroken).count() / total;
    }

    public List<PlotWall> clear(Plot plot) {
        return this.plotWallMap.remove(plot.getId().toCommaSeparatedString());
    }

    @Override
    public void disable() {
        for (List<PlotWall> plotWallList : this.plotWallMap.values()) {
            plotWallList.forEach(PlotWall::removeHologram);
        }
    }

    public ItemStack createPlotWallItemStack(int level, int amount) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(new ItemStack(this.main.getPlotWallType(), amount));
        NBTTagCompound nbtTag = nmsItemStack.getTag() != null ? nmsItemStack.getTag() : new NBTTagCompound();

        nbtTag.setInt("plotwall_level", level);

        nmsItemStack.setTag(nbtTag);

        ItemStack itemStack = CraftItemStack.asCraftMirror(nmsItemStack);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(this.main.getService(MessageService.class).get("plotwall.item-name", "level", level));

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public int getLevelFromItemStack(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);

        if (nmsItemStack.getTag() != null && nmsItemStack.getTag().hasKey("plotwall_level")) {
            return nmsItemStack.getTag().getInt("plotwall_level");
        }

        return -1;
    }

    public PlotWall getPlotWall(Plot plot, Location location) {
        List<PlotWall> plotWallList = this.plotWallMap.get(plot.getId().toCommaSeparatedString());
        if (plotWallList != null) {
            for (PlotWall plotWall : plotWallList) {
                if (plotWall.getPosition().equalsLocation(location)) {
                    return plotWall;
                }
            }
        }

        return null;
    }

    public PlotWall getPlotWall(Location location) {
        Plot plot = Util.toPlotLocation(location).getPlot();
        if (plot == null) return null;

        return this.getPlotWall(plot, location);
    }

    public boolean handlePlotWallBreak(Team plotTeam, Player breaker, PlotWall plotWall, int damage) {
        plotWall.removeLife(damage);

        if (plotWall.getLife() == 0) {
            TeamPlayer breakerTeamPlayer = teamPlayerService.getTeamPlayerByPlayer(breaker);

            Team breakerTeam = breakerTeamPlayer.getTeam();

            if (breakerTeam == null) return false;

            plotWall.removeHologram();

            float plotWallBreakPercentage = this.main.getConfig().getInt("plot-wall-value-percentage") / 100f;
            int value = Math.round(plotTeam.getValue() * plotWallBreakPercentage);

            if (value <= 0) {
                value = this.main.getConfig().getInt("plot-wall-minumim-reward");
            }

            float boost = valueBoosterService.getBoost(breaker.getInventory().getItemInMainHand());
            if (boost > 0) {
                value *= boost;
            }

            breakerTeam.addValue(value);
            teamDao.update(breakerTeam);

            messageService.sendMessage(breaker, "raid.plot-wall-percentage", "value", value);

            return false;
        } else {
            plotWall.updateHologram();

            return true;
        }
    }

}