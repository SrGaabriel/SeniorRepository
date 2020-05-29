package dioray.datayy.prototype.wall;

import com.intellectualcrafters.plot.object.Plot;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor @Data
public class PlotWall {

    private int level, life;
    private int x, y, z;

    private Plot plot;

}