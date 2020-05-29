package dioray.datayy.prototype.player;

import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor @Data
public class TeamPlayer {

    private UUID uuid;
    private Team team;
    private Role role;

}