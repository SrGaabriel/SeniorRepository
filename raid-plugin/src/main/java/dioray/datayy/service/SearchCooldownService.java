package dioray.datayy.service;

import dioray.datayy.RaidPlugin;
import dioray.datayy.model.Team;
import dioray.datayy.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchCooldownService extends Service {

    private final Map<String, Long> cooldownMap;

    public SearchCooldownService(RaidPlugin main) {
        super(main);

        this.cooldownMap = new HashMap<>();
    }

    @Override
    public void enable() {
        this.main.getServer().getScheduler().runTask(this.main, () -> {
            long current = Util.currentTimeSeconds();
            this.cooldownMap.entrySet().removeIf(entry -> entry.getValue() - current <= 0);
        });
    }

    public void setCooldown(Team team) {
        this.cooldownMap.put(team.getTag(), Util.currentTimeSeconds() + TimeUnit.MINUTES.toSeconds(30));
    }

    public boolean inCooldown(Team team) {
        Long cooldown = this.cooldownMap.get(team.getTag());
        if (cooldown != null) {
            long diff = cooldown - Util.currentTimeSeconds();
            if (diff <= 0) {
                this.cooldownMap.remove(team.getTag());
            }

            return diff > 0;
        }

        return false;
    }

    public String formatRemaining(Team team) {
        long diff = this.cooldownMap.get(team.getTag()) - Util.currentTimeSeconds();
        byte min = (byte) (diff / 60);
        byte sec = (byte) (diff % 60);

        return String.format("%02dm %02ds", min, sec);
    }

    public void removeCooldown(Team team) {
        this.cooldownMap.remove(team.getTag());
    }
}