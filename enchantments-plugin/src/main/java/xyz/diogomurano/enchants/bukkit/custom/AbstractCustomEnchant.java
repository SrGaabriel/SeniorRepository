package xyz.diogomurano.enchants.bukkit.custom;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.prototype.Team;
import dioray.datayy.prototype.player.TeamPlayer;
import dioray.datayy.prototype.wall.PlotWall;
import dioray.datayy.provider.wall.WallProvider;
import dioray.datayy.repository.team.TeamRepository;
import lombok.Data;
import lombok.NonNull;
import me.clip.ezblocks.EZBlocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import util.BlockUtil;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.custom.enchants.Jesus;
import xyz.diogomurano.enchants.bukkit.events.TeamThousandBlocksBrokenEvent;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
public abstract class AbstractCustomEnchant implements CustomEnchant {

    private final UUID uniqueId;
    private final String name;
    private int maxLevel;
    private int blockSet;
    private float chance;

    private final TeamRepository teamService = TeamRepository.getInstance();
    private final WallProvider wallProvider = WallProvider.getInstance();

    public AbstractCustomEnchant(@NonNull final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        final BukkitEnchantmentPlugin instance = BukkitEnchantmentPlugin.getInstance();
        instance.getServer().getPluginManager().registerEvents(this, instance);

        this.blockSet = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".block-set");
        this.maxLevel = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".max-level");
        this.chance = ((float) BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getDouble("enchants." + name + ".chance") / 100);
    }

    @Override
    public final Integer getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getEnchantmentLevel(final ItemStack stack) {
        if (stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
            return stack.getItemMeta().getLore().stream()
                    .filter(line -> line.startsWith("§7" + this.name))
                    .map(line -> Integer.parseInt(line.split(" ")[1]))
                    .findFirst()
                    .orElse(0);
        }

        return 0;
    }

    @Override
    public boolean hasEnchantment(final ItemStack stack) {
        return stack.hasItemMeta() && stack.getItemMeta().hasLore() && stack.getItemMeta().getLore().stream()
                .anyMatch(line -> line.startsWith("§7" + this.name));
    }

    @Override
    public void addEnchantment(final ItemStack stack, final Integer level) {
        final List<String> lore = (stack.hasItemMeta() && stack.getItemMeta().hasLore() ? stack.getItemMeta().getLore() : new ArrayList<>());
        lore.add("§7" + name + " " + level);

        ItemMeta meta = stack.getItemMeta();

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stack.setItemMeta(meta);

        stack.addEnchantment(Enchantment.DURABILITY, 1);
    }

    @Override
    public void maxEnchantment(final ItemStack stack) {
        removeEnchantment(stack);
        addEnchantment(stack, this.maxLevel);
    }

    @Override
    public void removeEnchantment(final ItemStack stack) {
        if (stack.hasItemMeta() && stack.getItemMeta().hasLore()) {
            ItemMeta itemMeta = stack.getItemMeta();

            List<String> lore = itemMeta.getLore();

            lore.removeIf(s -> s.startsWith("§7" + this.name));

            itemMeta.setLore(lore);
            stack.setItemMeta(itemMeta);
        }
    }

    @Override
    public int upgradeEnchantment(final ItemStack stack, final int amount) {
        int level = getEnchantmentLevel(stack);

        removeEnchantment(stack);
        addEnchantment(stack, level + amount);

        return level + amount;
    }

    public TeamPlayer getTeamPlayer(final Player player) {
        return teamService.get(player);
    }

    public Jesus.Direction getDirection(final Player player) {
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

    public boolean handleDestroy(final Player player, final Location location, final int level) {
        if (location.getBlock().getType() != Material.OBSIDIAN) return false;

        PlotWall plotWall = teamService.get(location);
        if (plotWall == null) return false;

        Team plotTeam = teamService.get(plotWall.getPlot());
        if (plotTeam == null) return false;

        wallProvider.hasDamaged(player, location.getBlock(), 2);
        return true;
    }

    public FileConfiguration getConfiguration() {
        return BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration();
    }

    public float calculateChance(float level) {
        return this.chance * (level / this.maxLevel);
    }

    public void handleBlockBreak(final Player player, final Block block, final int level, final CustomEnchant fortune, final Team team, final Mine originMine) {
        if (block.getType() == Material.AIR) return;

        if (handleDestroy(player, block.getLocation(), level)) return;

        final Mine mine = Mines.getAPI().getByLocation(block.getLocation());
        if (mine == null || !mine.getName().equals(originMine.getName())) return;

        final ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand == null || !hand.getType().name().contains("PICKAXE")) return;

        EZBlocks.getEZBlocks().getBreakHandler().handleBlockBreakEvent(player, block);
        block.setType(Material.AIR);

        block.getDrops(hand).forEach(drop -> {
            if (fortune.hasEnchantment(hand)) {
                drop.setAmount((ThreadLocalRandom.current().nextInt(fortune.getEnchantmentLevel(hand)) + 1) / 2);

            }
            User.addItem(player, drop);
        });


        User.handleCounter(player, hand, true);

        int blocksRemaining = mine.getBlocksRemaining();
        if (blocksRemaining > 0 && mine.getBlocksTotal() != 0) {
            mine.setBlocksRemaining(blocksRemaining - 1);
        }

        if (team != null) {
            apply(team);
        }
    }

    @Override
    public int getCountToUpgrade() {
        return this.blockSet / this.maxLevel;
    }

    @Override
    public void reload() {
        this.blockSet = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".block-set");
        this.maxLevel = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".max-level");
        this.chance = ((float) BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getDouble("enchants." + name + ".chance") / 100);
    }

    public void apply(Team team) {
        team.setValue(team.getValue() + 1); if(team.getValue() >= 1000) Bukkit.getPluginManager().callEvent(new TeamThousandBlocksBrokenEvent(team));
    }

    public enum Direction {
        SOUTH, EAST, WEST, NORTH, HIGH
    }

}
