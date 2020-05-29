package xyz.diogomurano.enchants.bukkit.utils.menu;

import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

@Data
public class Holder implements InventoryHolder {

    private Menu menu;

    public Holder(Menu menu) {
        this.menu = menu;
    }

    @Override
    public Inventory getInventory() {
        return menu.getInventory();
    }
}
