package com.raidplugin.sdk.command;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.member.type.Role;
import com.raidplugin.sdk.event.member.MemberPromoteEvent;
import com.raidplugin.sdk.event.member.MemberQuitTeamEvent;
import com.raidplugin.sdk.event.member.MemberTeleportHomeEvent;
import com.raidplugin.sdk.event.team.TeamDeletedEvent;
import com.raidplugin.sdk.event.team.TeamKickMemberEvent;
import com.raidplugin.sdk.event.team.TeamOptimizeLevelEvent;
import com.raidplugin.sdk.prototype.factory.PrototypeFactory;
import com.raidplugin.sdk.provider.TeamProvider;
import com.raidplugin.sdk.repository.TeamRepository;
import com.raidplugin.sdk.repository.member.MemberRepository;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class RaidCommand extends Command {

    public RaidCommand() {
        super("raid", "Your raid command.", "/raid", Collections.emptyList());
    }

    private final TeamProvider teamProvider = new TeamProvider();
    private final MemberRepository memberRepository = MemberRepository.getInstance();
    private final TeamRepository teamRepository = TeamRepository.getInstance();
    private final PrototypeFactory prototypeFactory = PrototypeFactory.getInstance();
    private final PlotAPI plotAPI = new PlotAPI();

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(new String[] {
                    " ",
                    " §e§lRaid - 2.1v",
                    "  §e/raid info - See info about how work raids.",
                    "  §e/raid admin - See info about commands to administrator.",
                    "  §e/raid commands - See info about commands of raid.",
                    " "
            }); return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(new String[] {
                        " ",
                        " §eRaids are a system that you can create own team and attack others.",
                        " §eIf your raid was success, your raid get more power!",
                        " §But, if you lose, your team will have less power.",
                        " ",
                        " §eHow to create a raid team? §fSimple! Use: /raid create <name to your raid>!",
                        " "
                }); return true;
            }

            /**
             * Below have commands to administrators.
             */

            if(args[0].equalsIgnoreCase("admin")) {
                if(!sender.hasPermission("raid.admin")) {
                    sender.sendMessage("§cHey! You not have permission to see info about commands admin."); return false;
                }

                sender.sendMessage(new String[] {
                        " ",
                        " §e/raid setpower <power> - Change the team power!",
                        " §e/raid teleport <team> - Teleport to home of team.",
                        " §e/raid delete <team> - Delete the team of server!",
                        " §e/raid setlevel <level> - Change the team level!",
                        " "
                }); return true;
            }

            /**
             * Below have commands to player.
             */

            if(args[0].equalsIgnoreCase("commands")) {
                sender.sendMessage(new String[] {
                        " ",
                        " §e/raid promote <target> - Promote the target!",
                        " §e/raid leave - Leave your team.",
                        " §e/raid kick <target> - Kick a target from your team",
                        " §e/raid home -  Teleport to your team's home",
                        " §e/raid search <target> - See info about of the target.",
                        " §e/raid accept <target> - Accept the invite from target.",
                        " §e/raid deny <target> - Deny the invite from target.",
                        " §e/raid top - See info about the top teams of server.",
                        " §e/raid balance - See the balance of your team.",
                        " §e/raid increase - Increase your raid level!",
                        " §e/raid invite <target> - Create an invite to target.",
                        " "
                }); return true;
            } if(!(sender instanceof Player)) return false;

            Member author = toAuthor(sender);
            Team team = author.getTeam();

            if(args[0].equalsIgnoreCase("leave")) {
                new MemberQuitTeamEvent(author, team); return true;
            }

            if(args[0].equalsIgnoreCase("home")) {
                new MemberTeleportHomeEvent(author, team); return true;
            }

            if(args[0].equalsIgnoreCase("top")) {
                // Open the inventory top.
            }

            if(args[0].equalsIgnoreCase("balance")) {
                sender.sendMessage(" §eYour team balance is: §f$" + team.getPower() + "§e!"); return true;
            }

            if(args[0].equalsIgnoreCase("increase")) {
                if(!teamProvider.isPossible(author.getTeam())) {
                    sender.sendMessage("§cYour team not have the need power to deploy a new level!"); return false;
                } new TeamOptimizeLevelEvent(team, teamProvider.getNeed(team), team.getLevel() + 1); return true;
            }
        }

        if(args.length == 2) {

            /**
             * Below have commands to players.
             * Any player can execute.
             * But, some commands require teams to execute.
             */

            if(args[0].equalsIgnoreCase("create")) {
                if(!(sender instanceof Player)) return false;

                Player player = (Player) sender;
                Team target = teamRepository.get(args[1]);

                if(target != null) {
                    sender.sendMessage("§cHey! That team exist!"); return false;
                } Plot plot = plotAPI.getPlot(player.getLocation());

                if(plot == null) {
                    sender.sendMessage("§cYou need are on a plot!"); return false;
                } prototypeFactory.createTeam(args[1], plot, player);

                sender.sendMessage("§aSuccess! You are on a team, invite others through of /raid invite <target>!"); return true;
            }

            if(args[0].equalsIgnoreCase("kick")) {
                Member author = toAuthor(sender); if(author == null) return false;

                if(author.getRole() == Role.MEMBER) {
                    sender.sendMessage("§cYou don't have permission to this!"); return false;
                } Member target = memberRepository.get(args[1], author.getTeam());

                if(target == null) {
                    sender.sendMessage("§cThat member don't exist in your team!"); return false;
                } new TeamKickMemberEvent(author.getTeam(), target);

                sender.sendMessage("§aSuccess! That member was kicked!"); return true;
            }

            if(args[0].equalsIgnoreCase("promote")) {
                Member author = toAuthor(sender); if(author == null) return false;

                Member target = memberRepository.get(args[1], author.getTeam());

                if(target == null) {
                    sender.sendMessage("§cThat member don't exists in your team."); return false;
                }

                if(target.getRole() == Role.MOD || target.getRole() == Role.OWNER) {
                    sender.sendMessage("§cThat member are Administrator!"); return false;
                } new MemberPromoteEvent(target, Role.MOD, sender);

                sender.sendMessage("§aSuccess! The member was promoted!"); return true;
            }

            Team team = teamRepository.get(args[1]);

            if(team == null) {
                sender.sendMessage("§cThat's a not team!"); return false;
            }

            if(args[0].equalsIgnoreCase("search")) {
                sender.sendMessage(new String[] {
                        " ",
                        "  §b" + team.getName(),
                        "  §eMembers: §f" + team.getMembers().size(),
                        "  §eLevel: §f" + team.getLevel(),
                        "  §ePower: §f" + team.getPower(),
                        " "
                }); return true;
            }

            /*
             * Below have commands to administrators.
             * Only senders that have permission: raid-admin can execute.
             */

            if(!sender.hasPermission("raid.admin")) {
                sender.sendMessage("§cKeep calm, you don't have permission to use this."); return false;
            }

            if(args[0].equalsIgnoreCase("setpower") || args[0].equalsIgnoreCase("setlevel")) {
                if(!(sender instanceof Player)) return false;

                Player player = (Player) sender;
                Team target = toTeam(player);

                if(target == null) {
                    sender.sendMessage("§cYou location not have a plot some to a team!"); return false;
                }

                if(!NumberUtils.isNumber(args[1])) {
                    sender.sendMessage("§cThe power is a not number!"); return false;
                } int amount = Integer.parseInt(args[1]);

                if(args[0].equalsIgnoreCase("setlevel")) team.setLevel(amount); else team.setPower(amount);
            }

            if(args[0].equalsIgnoreCase("delete")) {
                new TeamDeletedEvent(team, sender);
            } else if(args[0].equalsIgnoreCase("teleport")) {
                if(!(sender instanceof Player)) return false;

                ((Player) sender).teleport(toLocation(team));
            } sender.sendMessage("§cSuccess! The command was completed!"); return true;
        } sender.sendMessage("§cHey! Your command are wronged usage!"); return false;
    }

    public Team toTeam(Player player) {
        Location location = player.getLocation();

        Plot plot = plotAPI.getPlot(location); if(plot == null) return null;

        return teamRepository.get(plot);
    }

    public Member toAuthor(CommandSender commandSender) {
        if(!(commandSender instanceof Player)) return null;

        Player player = (Player) commandSender;
        Member member = memberRepository.get(player);

        if(member == null) {
            commandSender.sendMessage("§cYou need of a team to use this!"); return null;
        } return member;
    }

    public Location toLocation(Team team) {
        Plot plot = team.getPlot();

        com.intellectualcrafters.plot.object.Location location = plot.getHome();

        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

}
