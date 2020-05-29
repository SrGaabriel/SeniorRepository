package dioray.datayy.model;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Data
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
    }

    public void resetCounter() {
        this.valueCounter = 0;
    }
}