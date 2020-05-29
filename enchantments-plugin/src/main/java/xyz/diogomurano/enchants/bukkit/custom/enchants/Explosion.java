package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.model.Team;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchant;
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
    public final void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            Location location = block.getLocation().clone();

            explosionPlayer.put(location, player);
            location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), 8.0F, false, true);
        }
    }

    @EventHandler
    public final void onBlockExplodeEvent(BlockExplodeEvent event) {
        final Player player = explosionPlayer.remove(event.getBlock().getLocation());
        if (player != null) {
            event.setCancelled(true);

            final Team team = getTeamPlayer(player).getTeam();

            Mine originMine = Mines.getAPI().getByLocation(event.getBlock().getLocation());

            final CustomEnchant fortune = this.enchantService.get("Fortune");

            for (Block block : event.blockList()) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                int level = Explosion.this.getEnchantmentLevel(hand);

                handleBlockBreak(player, block, level, fortune, team, originMine);
            }

            if (team != null) {
                getTeamDao().update(team);
            }
        }
    }

    @EventHandler
    public final void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            event.setCancelled(true);
        }
    }

    @Override
    public final List<String> getLore() {
        return Arrays.asList("§7A little bit that drill");
    }
}
