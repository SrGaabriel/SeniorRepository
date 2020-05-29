package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
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

public final class Meteor extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;

    public Meteor() {
        super(UUID.randomUUID(), "Meteor");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public final void run(Player player, Block block, int level) {
        final float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            createCylinder(block, player, level);
        }
    }

    private void createCylinder(Block center, Player player, int level) {
        Location location = center.getLocation();
        final Team team = getTeamPlayer(player).getTeam();

        com.asylumdevs.mines.mine.Mine originMine = Mines.getAPI().getByLocation(center.getLocation());

        final CustomEnchant fortune = this.enchantService.get("Fortune");

        for (Block block : sphere(location, 3)) {
            handleBlockBreak(player, block, level, fortune, team, originMine);
        }

        if (team != null) {
            getTeamService().put(team.getPrefix(), team);
        }
    }

    public final Set<Block> sphere(final Location center, final int radius) {
        Set<Block> sphere = new HashSet<>();

        for (int Y = -radius; Y < radius; Y++) {
            for (int X = -radius; X < radius; X++) {
                for (int Z = -radius; Z < radius; Z++) {
                    if (Math.sqrt((X * X) + (Y * Y) + (Z * Z)) <= radius) {
                        final Block block = center.getWorld().getBlockAt(X + center.getBlockX(), Y + center.getBlockY(), Z + center.getBlockZ());

                        sphere.add(block);
                    }
                }
            }
        }

        return sphere;
    }

    @Override
    public final List<String> getLore() {
        return Collections.singletonList("§7Make a big crater");
    }

}
