package dioray.datayy.prototype;

import com.google.common.collect.Lists;
import com.intellectualcrafters.plot.object.Plot;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Data
public class Team {

    @NonNull
    private String prefix;

    @NonNull
    private Plot plot;

    private List<TeamPlayer> players = Lists.newLinkedList();
    private List<PlotWall> walls = Lists.newLinkedList();

    private int[] base;

    private boolean raiding;

    private int health, counter;

    private long value, remainingTime = 0;


    /*
     * Integer: value-reward
     * String: plotwall.item-name
     * Integer: plot-wall-value-percentage
     * Integer: plot-wall-minium-reward
     * String: raid.plot-wall-percentage
     * Integer: plot-wall-max-level
     * String: plot-wall-type
     * String: plot-world
     * Integer: plot-wall-life-grow-amount
     * Integer: plot-wall-price-grow-amount
     * String: plotwall.upgrade.accept
     * String: plotwall.upgrade.gui-title
     * String: plotwall.upgrade.current
     * String: plotwall.upgrade.deny
     * String: raid.not-found
     * String: raid.select
     * String: end-raid-time-minutes
     * String: raid.start
     * String: raid.being-raided
     * String: raid.ended
     * String: raid.plot-wall-percentage
    */
}
