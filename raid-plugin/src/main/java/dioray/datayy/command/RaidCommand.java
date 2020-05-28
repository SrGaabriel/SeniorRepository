package dioray.datayy.command;

import dioray.datayy.RaidPlugin;
import dioray.datayy.command.subcommand.*;
import dioray.datayy.service.MessageService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RaidCommand implements CommandExecutor, TabExecutor {

    private final RaidPlugin main;
    private final SubCommand[] subCommands;

    private final MessageService messageService;

    public RaidCommand(RaidPlugin main) {
        this.main = main;

        this.subCommands = new SubCommand[] {
                new ReloadSubCommand(main),
                new TeamSubCommand(main),
                new SearchSubCommand(main),
                new SeeSubCommand(main),
                new InviteSubCommand(main),
                new AcceptSubCommand(main),
                new DenySubCommand(main),
                new LeaveSubCommand(main),
                new PromoteSubCommand(main),
                new KickSubCommand(main),
                new HomeSubCommand(main),
                new TopSubCommand(main),
                new CooldownSubCommand(main)
        };

        this.messageService = main.getService(MessageService.class);
    }

    public void register() {
        this.main.getCommand("raid").setExecutor(this);
        this.main.getCommand("raid").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            messageService.sendMessage(sender, "command.only-player");

            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            handleHelp(player);

            return true;
        }

        for (SubCommand subCommand : this.subCommands) {
            if (args[0].equalsIgnoreCase(subCommand.getName())) {
                if (subCommand.getPermission() != null && !player.hasPermission(subCommand.getPermission())) {
                    messageService.sendMessage(player, "command.no-permission");
                    return true;
                }

                subCommand.run(player, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }

        handleHelp(player);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return null;

        if (args.length == 1) {
            return Arrays.stream(this.subCommands)
                    .filter(subCommand -> subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))
                    .map(SubCommand::getName)
                    .filter(subCommandName -> subCommandName.startsWith(args[0]))
                    .collect(Collectors.toList());
        }

        return null;
    }

    private void handleHelp(Player player) {
        messageService.sendMessage(player, "command.raid.usage");

        for (SubCommand subCommand : this.subCommands) {
            if (subCommand.getPermission() == null || player.hasPermission(subCommand.getPermission())) {
                messageService.sendMessage(player, "command.raid.help", "command", subCommand.getName(), "description", subCommand.getDescription());
            }
        }
    }

}