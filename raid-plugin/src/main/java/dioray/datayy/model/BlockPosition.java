package dioray.datayy.model;


import org.bukkit.Location;
import org.bukkit.World;

public class BlockPosition {

    private final int x, y, z;

    public BlockPosition(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public BlockPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public boolean equalsLocation(Location location) {
        return this.x == location.getBlockX()
                && this.y == location.getBlockY()
                && this.z == location.getBlockZ();
    }

    public Location toLocation(World world) {
        return new Location(world, this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "BlockPosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}