package com.raidplugin.sdk.manager.invite;

import com.raidplugin.api.manager.Manager;
import com.raidplugin.api.prototype.Team;
import com.raidplugin.api.prototype.invite.Invite;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class InviteManager implements Manager<UUID, Invite> {

    /**
     * That's a manager to change/add invites.
     * You can get invites with:
     *  Player.
     *  Player and Team.
     */

    private static InviteManager inviteManager;

    public static InviteManager getInstance() {
        return inviteManager == null ? (inviteManager = new InviteManager()) : inviteManager;
    }

    private final List<Invite> invites = new LinkedList<>();

    @Override
    public List<Invite> getCollection() {
        return invites;
    }

    @Override
    public void put(Invite value) {
        invites.add(value);
    }

    @Override
    public void putAll(List<Invite> collection) {
        invites.addAll(collection);
    }

    @Override
    public void remove(Invite value) {
        invites.remove(value);
    }

    @Override
    public void removeAll(List<Invite> collection) {
        invites.removeAll(collection);
    }

    @Override
    public Invite get(UUID key) {
        for(Invite invite : invites) {
            if(invite.getUUID().compareTo(key) == 0) return invite;
        } return null;
    }

    public Invite get(Player player) {
        return get(player.getUniqueId());
    }

    public Invite get(Player player, Team team) {
        Invite invite = get(player);

        if(invite == null) return null;

        if(!invite.getTarget().getName().equalsIgnoreCase(team.getName())) return null;

        return invite;
    }

}
