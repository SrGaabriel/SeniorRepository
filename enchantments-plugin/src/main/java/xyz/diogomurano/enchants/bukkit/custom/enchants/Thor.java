package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.model.Team;
import dioray.datayy.util.BlockUtil;
import me.clip.ezblocks.EZBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Thor extends AbstractCustomEnchant {

    private final CustomEnchantService enchantService;
    private final Random random;

    private final int xRange;
    private final int yRange;
    private final int zRange;

    public Thor() {
        super(UUID.randomUUID(), "Thor");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
        random = new Random();

        xRange = getConfiguration().getInt("enchants.Thor.x-range");
        yRange = getConfiguration().getInt("enchants.Thor.y-range");
        zRange = getConfiguration().getInt("enchants.Thor.z-range");
    }

    @Override
    public final void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (random.nextFloat() <= chance) {
            final Location blockLocation = block.getLocation();

            final Mine originMine = Mines.getAPI().getByLocation(block.getLocation());

            player.playSound(blockLocation, Sound.ENTITY_LIGHTNING_THUNDER, 1, 1);
            destroyLighting(blockLocation, player, level, originMine);
        }
    }

    private void destroyLighting(final Location location, final Player author, final int level, final Mine originMine) {
        new BukkitRunnable() {
            int x = -xRange;

            @Override
            public void run() {
                final Location l = location.getBlock().getRelative(x, 0, 0).getLocation();
                destroy(l, author, level, originMine);

                x++;

                if (x >= xRange) {
                    cancel();
                }
            }
        }.runTaskTimer(BukkitEnchantmentPlugin.getInstance(), 0, 1);

        new BukkitRunnable() {
            int z = -zRange;

            @Override
            public void run() {
                final Location l = location.getBlock().getRelative(0, 0, z).getLocation();
                destroy(l, author, level, originMine);

                z++;

                if (z >= zRange) {
                    cancel();
                }
            }
        }.runTaskTimer(BukkitEnchantmentPlugin.getInstance(), 0, 1);
    }

    private void destroy(Location location, Player player, int level, Mine originMine) {
        ItemStack hand = player.getInventory().getItemInMainHand();
        if (hand == null || !hand.getType().name().contains("PICKAXE")) return;

        final Team team = getTeamPlayer(player).getTeam();

        final CustomEnchant fortune = this.enchantService.get("Fortune");

        int brokenBlocks = 0;

        for (int y = 0; y > -yRange; y--) {
            final Block block = location.clone().add(0, y, 0).getBlock();
            final Location blockLocation = block.getLocation();

            if (block.getType() == Material.AIR) continue;

            if (handleDestroy(player, blockLocation, level)) continue;

            if (!BlockUtil.canBeBroken(block)) continue;

            final Mine mine = Mines.getAPI().getByLocation(blockLocation);
            if (mine == null || !mine.getName().equals(originMine.getName())) continue;

            brokenBlocks++;

            ItemStack drop = BlockUtil.getItem(block);

            EZBlocks.getEZBlocks().getBreakHandler().handleBlockBreakEvent(player, block);
            block.setType(Material.AIR);

            if (fortune.hasEnchantment(hand)) {
                int fortuneLevel = fortune.getEnchantmentLevel(hand);
                drop.setAmount((random.nextInt(fortuneLevel) + 1) / 2);
            }

            User.addItem(player, drop);

            final ItemStack newHand = User.handleCounter(player, hand, false);
            if (newHand != null) {
                hand = newHand;
            }

            final int blocksRemaining = mine.getBlocksRemaining();
            if (blocksRemaining > 0 && mine.getBlocksTotal() != 0) {
                mine.setBlocksRemaining(blocksRemaining - 1);
            }

            if (team != null) {
                team.checkBlockBreak();
            }
        }

        if (brokenBlocks > 0) {
            player.getInventory().setItemInMainHand(hand);
            player.updateInventory();
        }

        if (team != null) {
            getTeamDao().update(team);
        }
    }

    @Override
    public final List<String> getLore() {
        return Arrays.asList("§7Lightning strikes and makes streaks", "§7on the floor like lightning\n");
    }
}
