package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InviteService extends Service {

    private final Map<UUID, Team> inviteMap;

    public InviteService(RaidPlugin main) {
        super(main);

        this.inviteMap = new HashMap<>();
    }

    public void createInvite(Player player, Team team) {
        this.inviteMap.put(player.getUniqueId(), team);
    }

    public Team getInvite(Player player) {
        return this.inviteMap.remove(player.getUniqueId());
    }

}