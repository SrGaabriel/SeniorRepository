package dioray.datayy.model;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.intellectualcrafters.plot.object.Plot;
import com.sk89q.worldedit.PlayerDirection;
import dioray.datayy.RaidPlugin;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;

import java.util.Objects;

public class PlotWall {

    private int level;
    private final BlockPosition position;
    private int life;
    private Hologram hologram;
    private final Plot plot;
    private final PlayerDirection dir;

    public PlotWall(int level, BlockPosition position, Plot plot, PlayerDirection dir) {
        this.level = level;
        this.position = position;
        this.life = RaidPlugin.getPlotWallLife(this.level);
        this.plot = plot;
        this.dir = dir;
    }

    public int getLevel() {
        return this.level;
    }

    public BlockPosition getPosition() {
        return this.position;
    }

    public int getLife() {
        return this.life;
    }

    public Plot getPlot() {
        return this.plot;
    }

    public void removeLife(int damage) {
        this.life -= damage;
        if (this.life < 0) {
            this.life = 0;
        }
    }

    public void upgrade() {
        this.level++;
    }

    public void spawnHologram(World world) {
        Location location = this.position.toLocation(world).add(0.5, 1.2, 0.5);
        if (this.dir == PlayerDirection.SOUTH) {
            location.add(0, 0, -1);
        } else if (this.dir == PlayerDirection.NORTH) {
            location.add(0, 0, 1);
        } else if (this.dir == PlayerDirection.EAST) {
            location.add(-1, 0, 0);
        } else if (this.dir == PlayerDirection.WEST) {
            location.add(1, 0, 0);
        }

        this.hologram = HologramsAPI.createHologram(RaidPlugin.getInstance(), location);
        this.hologram.appendTextLine(ChatColor.GREEN + "Level: " + this.level + "(" + this.life + ")");
    }

    public void updateHologram() {
        if (this.hologram != null) {
            ((TextLine) this.hologram.getLine(0)).setText(ChatColor.GREEN + "Level: " + this.level + "(" + this.life + ")");
        }
    }

    public void removeHologram() {
        if (this.hologram != null) {
            this.hologram.delete();
        }
    }

    public boolean isBroken() {
        return this.hologram == null || this.hologram.isDeleted();
    }

    public PlayerDirection getDirection() {
        return dir;
    }

    @Override
    public String toString() {
        return "PlotWall level = " + this.level + ", x = " + this.position.getX() + ", y = " + this.position.getY() + ", z = " + this.position.getZ();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlotWall that = (PlotWall) o;
        return level == that.level &&
                Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level, position, life, hologram, plot);
    }
}