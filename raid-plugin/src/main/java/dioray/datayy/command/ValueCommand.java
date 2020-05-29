package dioray.datayy.command;

import dioray.datayy.RaidPlugin;
import dioray.datayy.item.ValueVoucher;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.MessageService;
import dioray.datayy.service.TeamPlayerService;
import dioray.datayy.service.ValueBoosterService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ValueCommand implements CommandExecutor {

    private final RaidPlugin main;

    private final MessageService messageService;
    private final TeamPlayerService teamPlayerService;
    private final ValueBoosterService valueBoosterService;

    public ValueCommand(RaidPlugin main) {
        this.main = main;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
        this.valueBoosterService = main.getService(ValueBoosterService.class);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                messageService.sendMessage(sender, "command.only-player");
                messageService.sendMessage(sender, "command.value.booster.usage");

                return true;
            }

            final Player player = (Player) sender;

            final TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);
            final Team team = teamPlayer.getTeam();

            if (team == null) {
                messageService.sendMessage(player, "command.value.no-team");

                return true;
            }

            messageService.sendMessage(player, "command.value.value", "value", team.getValue());

            return true;
        }

        if (args[0].equalsIgnoreCase("booster")) {
            if (!sender.hasPermission("raid.value.booster")) {
                messageService.sendMessage(sender, "command.no-permission");

                return true;
            }

            if (args.length < 3) {
                messageService.sendMessage(sender, "command.value.booster.usage");

                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                messageService.sendMessage(sender, "command.value.offline", "player", args[1]);

                return true;
            }

            try {
                float multipler = Float.parseFloat(args[2]);

                ItemStack itemStack = target.getInventory().getItemInMainHand();
                if (!itemStack.getType().name().contains("PICKAXE")) {
                    messageService.sendMessage(sender, "command.value.not-a-pickaxe", "player", target.getName());

                    return true;
                }

                itemStack = valueBoosterService.setBoost(itemStack, multipler);

                target.getInventory().setItemInMainHand(itemStack);

                messageService.sendMessage(sender, "command.value.boosted", "player", target.getName(), "multiplier", multipler);
            } catch (Exception ex) {
                messageService.sendMessage(sender, "command.value.not-a-number", "number", args[2]);
            }
        } else if (args[0].equalsIgnoreCase("voucher")) {
            if (!sender.hasPermission("raid.value.voucher")) {
                messageService.sendMessage(sender, "command.no-permission");

                return true;
            }

            if (args.length < 3) {
                messageService.sendMessage(sender, "command.value.voucher.usage");

                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                messageService.sendMessage(sender, "command.value.offline", "player", args[1]);

                return true;
            }

            try {
                int amount = Integer.parseInt(args[2]);

                if (amount <= 0) {
                    messageService.sendMessage(sender, "command.value.voucher.positive");

                    return true;
                }

                ItemStack voucher = ValueVoucher.create(amount);
                if (target.getInventory().addItem(voucher).size() == 0) {
                    messageService.sendMessage(sender, "command.value.voucher.gave", "player", target.getName(), "amount", amount);
                } else {
                    messageService.sendMessage(sender, "command.value.voucher.full", "player", target.getName());
                }
            } catch (Exception ex) {
                messageService.sendMessage(sender, "command.value.not-a-number", "number", args[2]);
            }
        }

        return true;
    }

    public void register() {
        this.main.getCommand("value").setExecutor(this);
    }
}
