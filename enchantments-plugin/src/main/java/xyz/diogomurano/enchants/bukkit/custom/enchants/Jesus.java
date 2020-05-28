package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.model.Team;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Jesus extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;

    public Jesus() {
        super(UUID.randomUUID(), "Jesus");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            Location location = block.getLocation().clone();

            final Team team = getTeamPlayer(player).getTeam();

            Direction dir = getDirection(player);

            Mine originMine = Mines.getAPI().getByLocation(block.getLocation());

            final CustomEnchant fortune = this.enchantService.get("Fortune");

            for (Block crossBlock : getCrossBlocks(dir, location)) {
                handleBlockBreak(player, crossBlock, level, fortune, team, originMine);
            }

            if (team != null) {
                getTeamDao().update(team);
            }
        }
    }

    private List<Block> getCrossBlocks(Direction dir, Location location) {
        if (dir == Direction.WEST || dir == Direction.EAST) {
            return Arrays.asList(
                    location.clone().add( 1, 0, 0).getBlock(),
                    location.clone().add( 2, 0, 0).getBlock(),
                    location.clone().add(-1, 0, 0).getBlock(),
                    location.clone().add(-2, 0, 0).getBlock(),
                    location.clone().add( 0, 1, 0).getBlock(),
                    location.clone().add( 0,-1, 0).getBlock(),
                    location.clone().add( 0,-2, 0).getBlock(),
                    location.clone().add( 0,-3, 0).getBlock(),
                    location.clone().add( 0,-4, 0).getBlock()
            );
        } else if (dir == Direction.SOUTH || dir == Direction.NORTH) {
            return Arrays.asList(
                    location.clone().add(0, 0,  1).getBlock(),
                    location.clone().add(0, 0,  2).getBlock(),
                    location.clone().add(0, 0, -1).getBlock(),
                    location.clone().add(0, 0, -2).getBlock(),
                    location.clone().add( 0, 1, 0).getBlock(),
                    location.clone().add( 0,-1, 0).getBlock(),
                    location.clone().add( 0,-2, 0).getBlock(),
                    location.clone().add( 0,-3, 0).getBlock(),
                    location.clone().add( 0,-4, 0).getBlock()
            );
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Cross-shaped break");
    }

    private Direction getDirection(Player player) {
        if (player.getLocation().getPitch() == 90) {
            return Direction.HIGH;
        }

        double rotation = (player.getLocation().getYaw() - 90) % 360;

        if (rotation < 0) {
            rotation += 360.0;
        }

        if (0 <= rotation && rotation < 22.5) {
            return Direction.NORTH;
        } else if (22.5 <= rotation && rotation < 67.5) {
            return Direction.NORTH;
        } else if (67.5 <= rotation && rotation < 112.5) {
            return Direction.EAST;
        } else if (112.5 <= rotation && rotation < 157.5) {
            return Direction.SOUTH;
        } else if (157.5 <= rotation && rotation < 202.5) {
            return Direction.SOUTH;
        } else if (202.5 <= rotation && rotation < 247.5) {
            return Direction.SOUTH;
        } else if (247.5 <= rotation && rotation < 292.5) {
            return Direction.WEST;
        } else if (292.5 <= rotation && rotation < 337.5) {
            return Direction.NORTH;
        } else if (337.5 <= rotation && rotation < 360.0) {
            return Direction.NORTH;
        }

        return null;
    }

    private enum Direction {
        SOUTH, EAST, WEST, NORTH, HIGH;
    }
}
