package xyz.diogomurano.enchants.bukkit.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.diogomurano.enchants.EnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;

public class AutoSellTimer extends BukkitRunnable {

    private long mills = System.currentTimeMillis();

    private final CustomEnchant autoSell;

    public AutoSellTimer(EnchantmentPlugin plugin) {
        this.autoSell = plugin.getEnchantService().get("AutoSell");
    }

    @Override
    public final void run() {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final ItemStack hand = player.getInventory().getItemInMainHand();
            if (!hand.getType().name().contains("PICKAXE")) continue;

            int level = this.autoSell.getEnchantmentLevel(hand);
            if (level >= this.autoSell.getMaxLevel()) {
                User.sellAllItems(player);
            }
        }

        this.mills += 1000 * 60;
        if (System.currentTimeMillis() - mills >= 3600000) {
            this.mills = 0;

            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (!hand.getType().name().contains("PICKAXE")) continue;

                final int level = this.autoSell.getEnchantmentLevel(hand);
                if (level > 0 && level < this.autoSell.getMaxLevel()) {
                    User.sellAllItems(player);
                }
            }
        }
    }

}
