package dioray.datayy.model;

import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.RaidPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Team {

    private final String tag;
    private final Set<TeamPlayer> playerSet;
    private String selectedPlot;
    private long remainingTime;
    private boolean raiding;
    private final Plot plot;
    private int corecount;
    private long value;
    private int valueCounter;

    public Team(String tag, Plot plot, int corecount, long value) {
        this.tag = tag;
        this.playerSet = new HashSet<>();
        this.remainingTime = 0;
        this.selectedPlot = null;
        this.plot = plot;
        this.corecount = corecount;
        this.value = value;
    }

    public Set<TeamPlayer> getOnlinePlayers() {
        return Collections.unmodifiableSet(this.playerSet);
    }

    public void addPlayer(TeamPlayer teamPlayer) {
        this.playerSet.add(teamPlayer);
    }

    public void setSelectedPlot(String selectedPlot) {
        this.selectedPlot = selectedPlot;
    }

    public String getSelectedPlot() {
        return selectedPlot;
    }

    public boolean isRaiding(Plot plot) {
        return this.raiding && plot.getId().toCommaSeparatedString().equalsIgnoreCase(this.getSelectedPlot());
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRaiding(boolean raiding) {
        this.raiding = raiding;
    }

    public boolean isRaiding() {
        return raiding;
    }

    public boolean equals(Team team) {
        return team != null && team.tag.equals(this.tag);
    }

    public void removePlayer(TeamPlayer teamPlayer) {
        this.playerSet.remove(teamPlayer);
    }

    public String getTag() {
        return this.tag;
    }

    public Plot getPlot() {
        return plot;
    }

    public void addCorecount() {
        this.corecount++;
    }

    public int getCorecount() {
        return corecount;
    }

    public long getValue() {
        return value;
    }

    public void addValue(long value) {
        this.value += value;
    }

    public void removeValue(long value) {
        this.value -= value;
        if (this.value < 0) {
            this.value = 0;
        }
    }

    public UUID getOwnerUUID() {
        return this.plot.getOwners().iterator().next();
    }

    public void checkBlockBreak() {
        this.valueCounter++;

        if (this.valueCounter >= 1000) {
            this.addValue(RaidPlugin.getInstance().getConfig().getInt("value-reward"));

            this.valueCounter = 0;
        }
    }

}