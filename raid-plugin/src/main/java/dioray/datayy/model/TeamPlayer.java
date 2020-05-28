package dioray.datayy.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPlayer {

    private final UUID uuid;
    private Team team;
    private Role role;
    private int valueCounter;

    public TeamPlayer(UUID uuid, Team team, Role role) {
        this.uuid = uuid;
        this.team = team;
        this.role = role;

        if (this.team != null) {
            this.team.addPlayer(this);
        }
    }

    public UUID getUUID() {
        return uuid;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof TeamPlayer)) return false;
        return this.uuid.equals(((TeamPlayer) other).uuid);
    }

    public boolean checkBlockBreak() {
        return true;

        /*this.valueCounter++;
        if (valueCounter >= 1000) {
            this.valueCounter = 0;

            return true;
        }

        return false;*/
    }

    public void resetCounter() {
        this.valueCounter = 0;
    }
}