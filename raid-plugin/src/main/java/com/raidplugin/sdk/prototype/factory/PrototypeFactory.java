package com.raidplugin.sdk.prototype.factory;

import com.intellectualcrafters.plot.object.Plot;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.member.Member;
import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.api.prototype.member.type.Role;
import com.raidplugin.api.prototype.wall.Wall;
import com.raidplugin.sdk.manager.WallManager;
import com.raidplugin.sdk.manager.raid.RaidManager;
import com.raidplugin.sdk.prototype.WTeam;
import com.raidplugin.sdk.prototype.member.WMember;
import com.raidplugin.sdk.prototype.raid.WRaid;
import com.raidplugin.sdk.prototype.wall.CWall;
import com.raidplugin.sdk.provider.configuration.ConfigurationProvider;
import com.raidplugin.sdk.repository.TeamRepository;
import com.raidplugin.sdk.repository.member.MemberRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PrototypeFactory {

    private static PrototypeFactory prototypeFactory;

    public static PrototypeFactory getInstance() {
        return prototypeFactory == null ? (prototypeFactory = new PrototypeFactory()) : prototypeFactory;
    }

    private final ConfigurationProvider configuration = ConfigurationProvider.getInstance();
    private final MemberRepository memberRepository = MemberRepository.getInstance();
    private final TeamRepository teamRepository = TeamRepository.getInstance();
    private final RaidManager raidManager = RaidManager.getInstance();
    private final WallManager wallManager = WallManager.getInstance();

    public Team createTeam(String name, Plot plot, Player owner) {
        Location center = toLocation(plot); Block block = center.getBlock();

        block.setType(Material.DRAGON_EGG);

        Team team = new WTeam(name, configuration.get(Integer.class, "team-minium-health"), 0, 1, plot, block);

        createMember(owner, Role.OWNER, team);

        teamRepository.put(name, team);

        return team;
    }

    public Member createMember(Player target, Role role, Team team) {
        Member member = new WMember(target, team, role);

        team.getMembers().add(member);
        memberRepository.put(target.getUniqueId(), member);

        return member;
    }

    public Raid createRaid(Team attacker, Team victim) {
        long time = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(configuration.get(Integer.class, "team-raid-time"));

        Raid raid = new WRaid(victim, attacker, time);
        raidManager.put(raid);

        return raid;
    }

    public Wall createWall(Team team, Block block, int level, int life) {
        Location location = block.getLocation();
        Integer[] vector = {location.getBlockX(), location.getBlockY(), location.getBlockZ()};
        Wall wall = new CWall(team, block, level, life, vector);

        wallManager.put(wall);

        return wall;
    }

    private Location toLocation(Plot plot) {
        com.intellectualcrafters.plot.object.Location location = plot.getCenter();

        return new Location(Bukkit.getWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

}
