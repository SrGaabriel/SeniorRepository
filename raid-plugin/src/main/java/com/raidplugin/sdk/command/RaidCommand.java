package com.raidplugin.sdk.command;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.sdk.event.MemberLeaveTeamEvent;
import com.raidplugin.sdk.event.MemberTeleportHomeEvent;
import com.raidplugin.sdk.event.TeamIncreaseLevelEvent;
import com.raidplugin.sdk.event.TeamWasDeletedEvent;
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

    protected RaidCommand() {
        super("raid", "Your raid command.", "/raid", Collections.emptyList());
    }

    private final TeamProvider teamProvider = new TeamProvider();
    private final MemberRepository memberRepository = MemberRepository.getInstance();
    private final TeamRepository teamRepository = TeamRepository.getInstance();
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

            if(args[0].equalsIgnoreCase("commands")) {
                sender.sendMessage(new String[] {
                        " ",
                        " §e/raid promote <target> - Promote the target!",
                        " §e/raid leave - Leave of your team.",
                        " §e/raid kick <target> - Kick the target of your team.",
                        " §e/raid home -  Teleport to home of your team.",
                        " §e/raid search <target> - See info about of the target.",
                        " §e/raid accept <target> - Accept the invite from target.",
                        " §e/raid deny <target> - Deny the invite from target.",
                        " §e/raid top - See info about the top teams of server.",
                        " §e/raid balance - See the balance of your team.",
                        " §e/raid increase - Increase your raid level!",
                        " §e/raid invite <target> - Create a invite to target.",
                        " "
                }); return true;
            } if(!(sender instanceof Player)) return false;

            Player player = (Player) sender;

            Member member = toMember(player); if(member == null) return false;

            if(args[0].equalsIgnoreCase("leave")) {
                new MemberLeaveTeamEvent(member, member.getTeam()); return true;
            }

            if(args[0].equalsIgnoreCase("home")) {
                new MemberTeleportHomeEvent(member, member.getTeam()); return true;
            }

            if(args[0].equalsIgnoreCase("top")) {
                // Open the inventory top.
            }

            if(args[0].equalsIgnoreCase("balance")) {
                sender.sendMessage(" §eYour team balance is: §f$" + member.getTeam().getPower() + "§e!"); return true;
            }

            if(args[0].equalsIgnoreCase("increase")) {
                if(!teamProvider.isPossible(member.getTeam())) {
                    sender.sendMessage("§cYour team not have the need power to deploy a new level!"); return false;
                } new TeamIncreaseLevelEvent(member.getTeam(), teamProvider.getNeed(member.getTeam()), member.getTeam().getLevel() + 1); return true;
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("setpower") || args[0].equalsIgnoreCase("setlevel")) {
                if(!(sender instanceof Player)) return false;

                Player player = (Player) sender;
                Team team = toTeam(player);

                if(team == null) {
                    sender.sendMessage("§cYou location not have a plot some to a team!"); return false;
                }

                if(!NumberUtils.isNumber(args[1])) {
                    sender.sendMessage("§cThe power is a not number!"); return false;
                } int amount = Integer.parseInt(args[1]);

                if(args[0].equalsIgnoreCase("setlevel")) team.setLevel(amount); else team.setPower(amount);

                sender.sendMessage("§cSuccess! The team power was updated!"); return true;
            }

            Team team = teamRepository.get(args[1]);

            if(team == null) {
                sender.sendMessage("§cThat's is a not team!"); return false;
            }

            if(args[0].equalsIgnoreCase("delete")) {
                new TeamWasDeletedEvent(team, sender);
            } else if(args[0].equalsIgnoreCase("teleport")) {
                if(!(sender instanceof Player)) return false;

                ((Player) sender).teleport(toLocation(team));
            } sender.sendMessage("§cSuccess! The command was completed!"); return true;
        } sender.sendMessage("§cHey! Your command are wronged usage!"); return false;
    }

    public Member toMember(Player player) {
        Member member = memberRepository.get(player);

        if(member == null) {
            player.sendMessage("§cYou not have permission to use the command."); return null;
        } return member;
    }

    public Team toTeam(Player player) {
        Location location = player.getLocation();

        Plot plot = plotAPI.getPlot(location); if(plot == null) return null;

        return teamRepository.get(plot);
    }

    public Location toLocation(Team team) {
        Plot plot = team.getPlot();

        com.intellectualcrafters.plot.object.Location location = plot.getHome();

        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

}
