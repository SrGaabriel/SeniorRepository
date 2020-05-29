package dioray.datayy.provider.world;

import dioray.datayy.provider.message.MessageProvider;
import org.bukkit.Location;

public class WorldProvider {

    private static WorldProvider worldProvider;

    public static WorldProvider getInstance() {
        return worldProvider == null ? (worldProvider = new WorldProvider()) : worldProvider;
    }

    private final MessageProvider messageProvider = MessageProvider.getInstance();

    public boolean isWorld(Location location) {
        return location.getWorld().getName().equalsIgnoreCase(messageProvider.get("plot-world"));
    }
}
