package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import org.bukkit.entity.Player;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(RaidPlugin main) {
        super(main, "reload", "raid.reload", "Reload the plugin messages");
    }

    @Override
    public void run(Player player, String[] args) {
        messageService.reload();
        messageService.sendMessage(player, "command.raid.reload.success");
    }
}
