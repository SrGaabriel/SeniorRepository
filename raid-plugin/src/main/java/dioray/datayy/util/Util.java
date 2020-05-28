package dioray.datayy.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class Util {

    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    public static String toSQL(UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public static UUID fromSQL(String sql) {
        return UUID.fromString(sql.replaceAll("(.{8})(.{4})(.{4})(.{4})(.{12})", "$1-$2-$3-$4-$5"));
    }

    public static Location fromPlotLocation(com.intellectualcrafters.plot.object.Location location) {
        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public static com.intellectualcrafters.plot.object.Location toPlotLocation(Location location) {
        return new com.intellectualcrafters.plot.object.Location(location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}