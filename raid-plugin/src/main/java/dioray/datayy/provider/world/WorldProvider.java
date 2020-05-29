package dioray.datayy.provider.world;

public class WorldProvider {

    private static WorldProvider worldProvider;

    public static WorldProvider getInstance() {
        return worldProvider == null ? (worldProvider = new WorldProvider()) : worldProvider;
    }

}
