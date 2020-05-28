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

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Digger extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;

    public Digger() {
        super(UUID.randomUUID(), "Digger");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            Location location = block.getLocation().clone();
            Location location2 = block.getLocation().clone();

            location.add(-1, -1, -1);
            location2.add(1, 1, 1);

            Team team = getTeamPlayer(player).getTeam();

            Mine originMine = Mines.getAPI().getByLocation(block.getLocation());

            CustomEnchant fortune = this.enchantService.get("Fortune");

            for (int x = location.getBlockX(); x < location2.getBlockX(); x++) {
                for (int y = location.getBlockY(); y < location2.getBlockY(); y++) {
                    for (int z = location.getBlockZ(); z < location2.getBlockZ(); z++) {
                        final Location blockLocation = new Location(location.getWorld(), x, y, z);

                        handleBlockBreak(player, blockLocation.getBlock(), level, fortune, team, originMine);
                    }
                }
            }

            if (team != null) {
                getTeamDao().update(team);
            }
        }
    }

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Creates a 4x4 hole in the ground");
    }
}
