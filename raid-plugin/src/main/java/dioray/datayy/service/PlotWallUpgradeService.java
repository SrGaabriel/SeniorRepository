package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import dioray.datayy.inventory.UpgradeInventoryHolder;
import dioray.datayy.model.PlotWall;
import dioray.datayy.util.ItemBuilder;
import dioray.datayy.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlotWallUpgradeService extends Service {

    private final MessageService messageService;

    public PlotWallUpgradeService(RaidPlugin main) {
        super(main);

        this.messageService = main.getService(MessageService.class);
    }

    public void openUpgradeGUI(Player player, PlotWall plotWall) {
        Inventory inventory = Bukkit.createInventory(new UpgradeInventoryHolder(plotWall), 9 * 3, messageService.get("plotwall.upgrade.gui-title"));

        int price = RaidPlugin.getPlotWallPrice(plotWall.getLevel() + 1);
        inventory.setItem(11, ItemBuilder
                .create(Material.CONCRETE)
                .durability(13)
                .name(messageService.get("plotwall.upgrade.accept", "price", price, "level", plotWall.getLevel() + 1))
                .build()
        );

        inventory.setItem(13, ItemBuilder
                .create(this.main.getPlotWallType())
                .name(messageService.get("plotwall.upgrade.current", "level", plotWall.getLevel()))
                .build());

        inventory.setItem(15, ItemBuilder
                .create(Material.CONCRETE)
                .durability(14)
                .name(messageService.get("plotwall.upgrade.deny"))
                .build());

        player.openInventory(inventory);
    }

}
