package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.prototype.Team;
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

public final class Jesus extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;

    public Jesus() {
        super(UUID.randomUUID(), "Jesus");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public final void run(final Player player, final Block block, final int level) {
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
                getTeamService().put(team.getPrefix(), team);
            }
        }
    }

    private List<Block> getCrossBlocks(final Direction dir, final Location location) {
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
    public final List<String> getLore() {
        return Collections.singletonList("§7Cross-shaped break");
    }
}
