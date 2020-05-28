package dioray.datayy.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class TopInventoryHolder implements InventoryHolder {

    public static final TopInventoryHolder INSTANCE = new TopInventoryHolder();

    @Override
    public Inventory getInventory() {
        return null;
    }
}