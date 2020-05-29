package xyz.diogomurano.enchants.bukkit.custom;

import com.asylumdevs.mines.Mines;
import com.asylumdevs.mines.mine.Mine;
import dioray.datayy.RaidPlugin;
import dioray.datayy.database.TeamDao;
import dioray.datayy.model.PlotWall;
import dioray.datayy.model.Team;
import dioray.datayy.model.TeamPlayer;
import dioray.datayy.service.PlotWallService;
import dioray.datayy.service.TeamPlayerService;
import dioray.datayy.service.TeamService;
import lombok.Data;
import lombok.NonNull;
import me.clip.ezblocks.EZBlocks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.user.User;
import dioray.datayy.util.BlockUtil;
import xyz.diogomurano.enchants.custom.CustomEnchant;

import java.util.ArrayList;
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

    private final PlotWallService plotWallService;
    private final TeamService teamService;
    private final TeamPlayerService teamPlayerService;

    public AbstractCustomEnchant(@NonNull final UUID uniqueId, final String name) {
        this.uniqueId = uniqueId;
        this.name = name;

        final BukkitEnchantmentPlugin instance = BukkitEnchantmentPlugin.getInstance();
        instance.getServer().getPluginManager().registerEvents(this, instance);

        this.blockSet = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".block-set");
        this.maxLevel = BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getInt("enchants." + name + ".max-level");
        this.chance = ((float) BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration().getDouble("enchants." + name + ".chance") / 100);

        this.plotWallService = RaidPlugin.getInstance().getService(PlotWallService.class);
        this.teamService = RaidPlugin.getInstance().getService(TeamService.class);
        this.teamPlayerService = RaidPlugin.getInstance().getService(TeamPlayerService.class);
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
        return teamPlayerService.getTeamPlayerByPlayer(player);
    }

    public boolean handleDestroy(final Player player, final Location location, final int level) {
        if (location.getBlock().getType() != Material.OBSIDIAN) return false;

        PlotWall plotWall = plotWallService.getPlotWall(location);
        if (plotWall == null) return false;

        Team plotTeam = teamService.getByPlot(plotWall.getPlot());
        if (plotTeam == null) return false;

        return plotWallService.handlePlotWallBreak(plotTeam, player, plotWall, 2);
    }

    public FileConfiguration getConfiguration() {
        return BukkitEnchantmentPlugin.getSettings().getEnchantsConfiguration();
    }

    public TeamDao getTeamDao() {
        return RaidPlugin.getInstance().getTeamDao();
    }

    public float calculateChance(float level) {
        return this.chance * (level / this.maxLevel);
    }

    public void handleBlockBreak(Player player, Block block, int level, CustomEnchant fortune, Team team, Mine originMine) {
        if (block.getType() == Material.AIR) return;

        if (handleDestroy(player, block.getLocation(), level)) return;

        if (!BlockUtil.canBeBroken(block)) return;

        Mine mine = Mines.getAPI().getByLocation(block.getLocation());
        if (mine == null || !mine.getName().equals(originMine.getName())) return;

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand == null || !hand.getType().name().contains("PICKAXE")) return;

        EZBlocks.getEZBlocks().getBreakHandler().handleBlockBreakEvent(player, block);
        block.setType(Material.AIR);

        ItemStack drop = BlockUtil.getItem(block);

        if (fortune.hasEnchantment(hand)) {
            int fortuneLevel = fortune.getEnchantmentLevel(hand);

            drop.setAmount((ThreadLocalRandom.current().nextInt(fortuneLevel) + 1) / 2);
        }

        User.addItem(player, drop);

        User.handleCounter(player, hand, true);

        int blocksRemaining = mine.getBlocksRemaining();
        if (blocksRemaining > 0 && mine.getBlocksTotal() != 0) {
            mine.setBlocksRemaining(blocksRemaining - 1);
        }

        if (team != null) {
            team.checkBlockBreak();
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
}
