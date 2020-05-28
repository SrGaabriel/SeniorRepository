package dioray.datayy.inventory;

import dioray.datayy.model.PlotWall;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class UpgradeInventoryHolder implements InventoryHolder {

    private final PlotWall selectedPlotWall;

    public UpgradeInventoryHolder(PlotWall selectedPlotWall) {
        this.selectedPlotWall = selectedPlotWall;
    }

    public PlotWall getSelectedPlotWall() {
        return selectedPlotWall;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}