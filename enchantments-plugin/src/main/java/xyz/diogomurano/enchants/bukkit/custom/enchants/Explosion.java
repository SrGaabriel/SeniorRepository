package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import dioray.datayy.prototype.Team;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class Explosion extends AbstractCustomEnchant {

    private final Map<Location, Player> explosionPlayer;
    private final CustomEnchantService enchantService;

    public Explosion() {
        super(UUID.randomUUID(), "Explosion");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
        explosionPlayer = new HashMap<>();
    }

    @Override
    public final void run(final Player player, final Block block, final int level) {
        final float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            final Location location = block.getLocation().clone();

            explosionPlayer.put(location, player);
            location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 8.0F, false, true);
        }
    }

    @EventHandler
    public final void onBlockExplodeEvent(final BlockExplodeEvent event) {
        final Player player = explosionPlayer.remove(event.getBlock().getLocation());
        if (player != null) {
            event.setCancelled(true);

            final Team team = getTeamPlayer(player).getTeam();

            for (Block block : event.blockList()) {
                final int level = Explosion.this.getEnchantmentLevel(player.getInventory().getItemInMainHand());

                handleBlockBreak(player, block, level, enchantService.get("Fortune"), team, Mines.getAPI().getByLocation(event.getBlock().getLocation()));
            }

        }
    }

    @EventHandler
    public final void onEntityDamageEvent(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
        }
    }

    @Override
    public final List<String> getLore() {
        return Collections.singletonList("§7A little bit that drill");
    }
}
