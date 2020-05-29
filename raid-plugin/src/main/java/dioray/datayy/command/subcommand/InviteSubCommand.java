package dioray.datayy.command.subcommand;

import dioray.datayy.RaidPlugin;
import dioray.datayy.prototype.player.type.Role;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.service.InviteService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InviteSubCommand extends SubCommand {

    private final InviteService inviteService;

    public InviteSubCommand(RaidPlugin main) {
        super(main, "invite", "Invite a player to your team");

        this.inviteService = main.getService(InviteService.class);
    }

    @Override
    public void run(Player player, String[] args) {
        TeamPlayer teamPlayer = teamPlayerService.getTeamPlayerByPlayer(player);

        if (teamPlayer.getTeam() == null) {
            messageService.sendMessage(player, "command.raid.invite.no-team");

            return;
        }

        if (teamPlayer.getRole() == Role.MEMBER) {
            messageService.sendMessage(player, "command.raid.invite.no-permission");

            return;
        }

        if (args.length < 1) {
            messageService.sendMessage(player, "command.raid.invite.usage");

            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            messageService.sendMessage(player, "command.raid.invite.offline", "player", args[0]);

            return;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            messageService.sendMessage(player, "command.raid.invite.yourself");

            return;
        }

        if (teamPlayerService.getTeamByPlayer(target) != null) {
            messageService.sendMessage(player, "command.raid.invite.already-team", "player", target.getName());

            return;
        }

        if (teamPlayer.getTeam().getOnlinePlayers().size() >= 5) {
            messageService.sendMessage(player, "command.raid.invite.max-limit", "player", target.getName());

            return;
        }

        inviteService.createInvite(target, teamPlayer.getTeam());
        messageService.sendMessage(player, "command.raid.invite.success", "player", target.getName());

        messageService.sendMessage(target, "command.raid.invite.invited", "player", player.getName());

        TextComponent component = new TextComponent();
        TextComponent accept = new TextComponent(messageService.get("command.raid.invite.accept"));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/raid accept"));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                new TextComponent(messageService.get("command.raid.invite.accept"))
        }));

        TextComponent deny = new TextComponent(messageService.get("command.raid.invite.deny"));
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/raid deny"));
        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {
                new TextComponent(messageService.get("command.raid.invite.deny"))
        }));

        component.addExtra(accept);
        component.addExtra(" ");
        component.addExtra(deny);

        target.spigot().sendMessage(component);
    }
}