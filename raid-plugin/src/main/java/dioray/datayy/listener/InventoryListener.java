package dioray.datayy.listener;

import dioray.datayy.RaidPlugin;
import dioray.datayy.inventory.TopInventoryHolder;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.service.MessageService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    private final RaidPlugin main;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;
    private final TeamDao teamDao;
    private final PlotWallDao plotWallDao;

    public InventoryListener(RaidPlugin main) {
        this.main = main;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.teamDao = main.getTeamDao();
        this.plotWallDao = main.getPlotWallDao();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (e.getClickedInventory() == null) return;

        if (e.getClickedInventory().getHolder() instanceof UpgradeInventoryHolder) {
            e.setCancelled(true);

            PlotWall plotWall = ((UpgradeInventoryHolder) e.getClickedInventory().getHolder()).getSelectedPlotWall();

            if (e.getCurrentItem().getType() == Material.CONCRETE) {
                if (e.getCurrentItem().getDurability() == 13) {
                    TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer((Player) e.getWhoClicked());
                    Team team = teamPlayer.getTeam();
                    if (team == null) {
                        e.getWhoClicked().closeInventory();

                        return;
                    }

                    int price = RaidPlugin.getPlotWallPrice(plotWall.getLevel() + 1);
                    if (team.getValue() < price) {
                        messageService.sendMessage(e.getWhoClicked(), "plotwall.not-enough", "price", price);
                        e.getWhoClicked().closeInventory();

                        return;
                    }

                    team.removeValue(price);
                    teamDao.update(team);

                    plotWall.upgrade();
                    plotWallDao.update(plotWall);

                    messageService.sendMessage(e.getWhoClicked(), "plotwall.upgrade.upgraded", "level", plotWall.getLevel());
                }

                e.getWhoClicked().closeInventory();
            }
        } else if (e.getClickedInventory().getHolder() instanceof TopInventoryHolder) {
            e.setCancelled(true);
        }
    }

}
