package xyz.diogomurano.enchants.bukkit.timer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.diogomurano.enchants.EnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;

public class AutoSellTimer extends BukkitRunnable {

    private byte minuteCounter = 0;

    private final CustomEnchant autoSell;

    public AutoSellTimer(EnchantmentPlugin plugin) {
        this.autoSell = plugin.getEnchantService().get("AutoSell");
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack hand = player.getInventory().getItemInMainHand();
            if (!hand.getType().name().contains("PICKAXE")) continue;

            int level = this.autoSell.getEnchantmentLevel(hand);
            if (level >= this.autoSell.getMaxLevel()) {
                User.sellAllItems(player);
            }
        }

        this.minuteCounter++;
        if (this.minuteCounter >= 60) {
            this.minuteCounter = 0;

            for (Player player : Bukkit.getOnlinePlayers()) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (!hand.getType().name().contains("PICKAXE")) continue;

                int level = this.autoSell.getEnchantmentLevel(hand);
                if (level > 0 && level < this.autoSell.getMaxLevel()) {
                    User.sellAllItems(player);
                }
            }
        }
    }

}
