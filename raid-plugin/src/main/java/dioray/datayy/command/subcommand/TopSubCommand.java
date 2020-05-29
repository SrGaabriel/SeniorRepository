package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.inventory.TopInventoryHolder;
import dioray.datayy.model.Team;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.TeamService;
import dioray.datayy.util.ItemBuilder;
import dioray.datayy.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class TopSubCommand extends SubCommand {

    private final TeamService teamService;

    public TopSubCommand(RaidPlugin main) {
        super(main, "top", "Show top teams");

        this.teamService = main.getService(TeamService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        this.main.getServer().getScheduler().runTaskAsynchronously(this.main, () -> {
            List<Team> tops = teamService.getTop();
            if (tops == null || tops.size() == 0) {
                messageService.sendMessage(player, "top.error");

                return;
            }

            Inventory inventory = Bukkit.createInventory(TopInventoryHolder.INSTANCE, 9 * 3, messageService.get("top.title"));

            int place = 1;
            for (Team team : tops) {
                List<String> memberNameList = this.main.getTeamDao().getMemberNames(team);
                if (memberNameList == null) {
                    memberNameList = new ArrayList<>();
                }

                ItemStack itemStack = ItemBuilder
                        .create(Material.SKULL_ITEM)
                        .durability(SkullType.PLAYER.ordinal())
                        .name(messageService.get("top.item.name", "team", team.getTag()))
                        .build();
                List<String> lore = new ArrayList<>();

                lore.add("");
                lore.add(messageService.get("top.item.lore.1", "place", place));
                lore.add(messageService.get("top.item.lore.2", "value", team.getValue()));
                lore.add(messageService.get("top.item.lore.3", "cores", team.getCorecount()));
                lore.add(messageService.get("top.item.lore.4"));
                for (String memberName : memberNameList) {
                    lore.add(messageService.get("top.item.lore.member", "name", memberName));
                }

                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();

                meta.setLore(lore);
                meta.setOwningPlayer(Bukkit.getOfflinePlayer(team.getOwnerUUID()));

                itemStack.setItemMeta(meta);
                inventory.setItem(9 + (place - 1), itemStack);

                place++;
            }

            player.openInventory(inventory);
        });
    }
}
