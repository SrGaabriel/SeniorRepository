package xyz.diogomurano.enchants.bukkit.custom.enchants;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.model.Team;
import dioray.datayy.util.BlockUtil;
import me.clip.ezblocks.EZBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class Demolisher extends AbstractCustomEnchant {

    private final int MAX_RANGE;
    private final CustomEnchantService enchantService;

    public Demolisher() {
        super(UUID.randomUUID(), "Demolisher");

        MAX_RANGE = getConfiguration().getInt("enchants.Demolisher.range");

        enchantService = BukkitEnchantmentPlugin.getInstance().getEnchantService();
    }

    @Override
    public void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            final Location location = block.getLocation().clone();
            final Location location2 = block.getLocation().clone();

            location.add(-MAX_RANGE, 0, -MAX_RANGE);
            location2.add(MAX_RANGE, 0, MAX_RANGE);

            com.asylumdevs.mines.mine.BaseMine originMine = Mines.getAPI().getByLocation(block.getLocation());

            CustomEnchant fortune = this.enchantService.get("Fortune");

            new BukkitRunnable() {
                int x = location.getBlockX();

                @Override
                public void run() {
                    Team team = getTeamPlayer(player).getTeam();

                    ItemStack hand = player.getInventory().getItemInMainHand();

                    int brokenBlocks = 0;

                    if (hand != null && hand.getType().name().contains("PICKAXE")) {
                        for (int z = location.getBlockZ(); z < location2.getBlockZ(); z++) {
                            Location blockLocation = new Location(location.getWorld(), x, location.getY(), z);
                            Block block = blockLocation.getBlock();

                            if (block.getType() == Material.AIR) continue;

                            if (handleDestroy(player, blockLocation, level)) continue;

                            if (!BlockUtil.canBeBroken(block)) continue;

                            Mine mine = Mines.getAPI().getByLocation(blockLocation);
                            if (mine == null || !mine.getName().equals(originMine.getName())) continue;

                            brokenBlocks++;

                            ItemStack drop = BlockUtil.getItem(block);

                            EZBlocks.getEZBlocks().getBreakHandler().handleBlockBreakEvent(player, block);
                            block.setType(Material.AIR);

                            if (fortune.hasEnchantment(hand)) {
                                int fortuneLevel = fortune.getEnchantmentLevel(hand);

                                drop.setAmount((ThreadLocalRandom.current().nextInt(fortuneLevel) + 1) / 2);
                            }

                            User.addItem(player, drop);

                            final ItemStack newHand = User.handleCounter(player, hand, false);

                            if (newHand != null) {
                                hand = newHand;
                            }

                            int blocksRemaining = mine.getBlocksRemaining();
                            if (blocksRemaining > 0 && mine.getBlocksTotal() != 0) {
                                mine.setBlocksRemaining(blocksRemaining - 1);
                            }

                            if (team != null) {
                                team.checkBlockBreak();
                            }
                        }
                    }

                    if (brokenBlocks > 0) {
                        player.getInventory().setItemInMainHand(hand);
                        player.updateInventory();
                    }

                    x++;

                    if (x >= location2.getBlockX()) {
                        cancel();

                        if (team != null) {
                            getTeamDao().update(team);
                        }
                    }
                }
            }.runTaskTimer(BukkitEnchantmentPlugin.getInstance(), 0, 1);
        }
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList("§7Breaks an entire layer of", "§7the mine");
    }
}
