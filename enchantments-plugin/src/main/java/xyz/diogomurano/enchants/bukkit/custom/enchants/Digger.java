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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class Digger extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;

    public Digger() {
        super(UUID.randomUUID(), "Digger");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public void run(Player player, Block block, int level) {
        final float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            final Location location = block.getLocation().clone();
            final Location location2 = block.getLocation().clone();

            location.add(-1, -1, -1);
            location2.add(1, 1, 1);

            final Team team = getTeamPlayer(player).getTeam();

            final Mine originMine = Mines.getAPI().getByLocation(block.getLocation());

            final CustomEnchant fortune = this.enchantService.get("Fortune");

            List<Block> blockList = new ArrayList<>();

            switch (getDirection(player)) {
                case SOUTH:
                    blockList = Arrays.asList(
                            block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() + 1),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() + 1)
                    );
                case NORTH:
                    blockList = Arrays.asList(
                            block.getWorld().getBlockAt(block.getX(), block.getY(), block.getZ() - 1),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ() - 1)
                    );
                case WEST:
                    blockList = Arrays.asList(
                            block.getWorld().getBlockAt(block.getX() - 1, block.getY(), block.getZ()),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()),
                            block.getWorld().getBlockAt(block.getX() - 1, block.getY() - 1, block.getZ())
                    );
                case EAST:
                    blockList = Arrays.asList(
                            block.getWorld().getBlockAt(block.getX() + 1, block.getY(), block.getZ()),
                            block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ()),
                            block.getWorld().getBlockAt(block.getX() + 1, block.getY() - 1, block.getZ())
                    );
                case HIGH:
                    blockList = new ArrayList<>();
            }

            for (final Block affectedBlock : blockList) {
                handleBlockBreak(player, affectedBlock, level, fortune, team, originMine);
            }

            if (team != null) {
                getTeamService().put(team.getPrefix(), team);
            }
        }
    }

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Creates a 4x4 hole in the ground");
    }
}
