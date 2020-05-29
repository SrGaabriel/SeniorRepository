package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.service.MessageService;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected final RaidPlugin main;
    private final String name, permission, description;

    protected final MessageService messageService;
    protected final TeamPlayerService teamPlayerService;

    public SubCommand(RaidPlugin main, String name, String description) {
        this(main, name, null, description);
    }

    public SubCommand(RaidPlugin main, String name, String permission, String description) {
        this.main = main;
        this.name = name;
        this.permission = permission;
        this.description = description;

        this.messageService = main.getService(MessageService.class);
        this.teamPlayerService = main.getService(TeamPlayerService.class);
    }

    public abstract void run(Player player, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }
}