package dioray.datayy.prototype.player;

import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.type.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPlayer {

    private final UUID uuid;
    private final Team team;
    private Role role;

    public TeamPlayer(UUID uuid, Team team, Role role) {
        this.role = role; this.uuid = uuid; this.team = team;
    }

    public Role getRole() {
        return role;
    }

    public Team getTeam() {
        return team;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

}