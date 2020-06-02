package com.raidplugin.sdk.thread;

import com.raidplugin.api.prototype.invite.Invite;
import com.raidplugin.api.prototype.raid.Raid;
import com.raidplugin.sdk.event.raid.RaidFinishedEvent;
import com.raidplugin.sdk.manager.invite.InviteManager;
import com.raidplugin.sdk.manager.raid.RaidManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RaidThread {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private final InviteManager inviteManager = InviteManager.getInstance();
    private final RaidManager raidManager = RaidManager.getInstance();

    public void apply() {
        List<Invite> invites = new ArrayList<>();

        executorService.scheduleAtFixedRate(() -> {
            for(Invite invite : inviteManager.getCollection()) {
                if(System.currentTimeMillis() >= invite.getTime()) invites.add(invite);
            }

            for(Raid raid : raidManager.getCollection()) {
                if(System.currentTimeMillis() >= raid.getTime()) new RaidFinishedEvent(raid);
            } inviteManager.removeAll(invites); invites.clear();
        }, 1, 1, TimeUnit.MINUTES);
    }

}
