package dioray.datayy.command;

import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.PlotWallService;
import dioray.datayy.service.TeamPlayerService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlotWallCommand implements CommandExecutor {

    private final RaidPlugin main;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;
    private final PlotWallService plotWallService;
    private final TeamDao teamDao;

    public PlotWallCommand(RaidPlugin main) {
        this.main = main;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.plotWallService = main.getService(PlotWallService.class);
        this.teamDao = main.getTeamDao();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            if (sender.hasPermission("plotwall.give")) {
                messageService.sendMessage(sender, "command.plotwall.usage-admin");
            } else {
                messageService.sendMessage(sender, "command.plotwall.usage");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (!sender.hasPermission("plotwall.give")) {
                messageService.sendMessage(sender, "command.no-permission");

                return true;
            }

            if (args.length < 4) {
                messageService.sendMessage(sender, "command.plotwall.usage-admin");

                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                messageService.sendMessage(sender, "command.plotwall.not-a-number", "number", args[1]);
                return true;
            }

            int level;
            try {
                level = Integer.parseInt(args[2]);
                if (level <= 0 || level > this.main.getPlotWallMaxLevel()) {
                    messageService.sendMessage(sender, "command.plotwall.not-a-level", "level", args[2]);

                    return true;
                }
            } catch (Exception ex) {
                messageService.sendMessage(sender, "command.plotwall.not-a-level", "level", args[2]);
                return true;
            }

            Player target = Bukkit.getPlayer(args[3]);
            if (target == null) {
                messageService.sendMessage(sender, "command.plotwall.offline", "player", args[3]);

                return true;
            }

            messageService.sendMessage(sender, "command.plotwall.success", "amount", amount, "player", target.getName());

            ItemStack itemStack = plotWallService.createPlotWallItemStack(level, amount);
            if (target.getInventory().addItem(itemStack).size() != 0) {
                target.getWorld().dropItemNaturally(target.getLocation(), itemStack);

                messageService.sendMessage(sender, "command.plotwall.drop-close", "player", target.getName());
            }
        } else if (args[0].equalsIgnoreCase("buy")) {
            if (!(sender instanceof Player)) {
                messageService.sendMessage(sender, "command.only-player");

                return true;
            }

            Player player = (Player) sender;

            if (args.length < 3) {
                messageService.sendMessage(player, "command.plotwall.usage");

                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                messageService.sendMessage(player, "command.plotwall.not-a-number", "number", args[1]);

                return true;
            }

            try {
                int level = Integer.parseInt(args[2]);
                if (level <= 0 || level > this.main.getPlotWallMaxLevel()) {
                    messageService.sendMessage(player, "command.plotwall.not-a-level", "level", args[2]);

                    return true;
                }

                TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
                Team team = teamPlayer.getTeam();
                if (team == null) {
                    messageService.sendMessage(player, "command.plotwall.no-team");

                    return true;
                }

                int total = RaidPlugin.getPlotWallPrice(level) * amount;

                if (team.getValue() < total) {
                    messageService.sendMessage(player, "command.plotwall.not-enough", "amount", amount, "level", level, "total", total);

                    return true;
                }

                team.removeValue(total);
                teamDao.update(team);

                messageService.sendMessage(player, "command.plotwall.success-self", "amount", amount, "level", level);

                ItemStack itemStack = plotWallService.createPlotWallItemStack(level, amount);
                if (player.getInventory().addItem(itemStack).size() != 0) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);

                    messageService.sendMessage(player, "command.plotwall.drop-close-self");
                }
            } catch (Exception ex) {
                messageService.sendMessage(sender, "command.plotwall.not-a-level", "level", args[2]);
                return true;
            }
        }

        return false;
    }

    public void register() {
        this.main.getCommand("plotwall").setExecutor(this);
    }

}
