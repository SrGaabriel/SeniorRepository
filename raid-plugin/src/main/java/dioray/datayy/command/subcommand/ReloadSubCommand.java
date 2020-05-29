package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import org.bukkit.entity.Player;

public final class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand(RaidPlugin main) {
        super(main, "reload", "raid.reload", "Reload the plugin messages");
    }

    @Override
    public void run(final Player player, final String[] args) {
        messageService.reload();
        messageService.sendMessage(player, "command.raid.reload.success");
    }
}
