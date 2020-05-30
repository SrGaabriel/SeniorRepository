package xyz.diogomurano.enchants.bukkit.listener;

import me.clip.autosell.events.SellAllEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.diogomurano.enchants.EnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.inventory.BackpackUpgradeInventory;
import xyz.diogomurano.enchants.bukkit.inventory.PickaxeInventory;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.custom.CustomEnchant;

import java.util.Collection;
import java.util.Random;

public class GeneralListener implements Listener {

    private final EnchantmentPlugin plugin;
    private final Random random = new Random();

    public GeneralListener(final EnchantmentPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent event) {
        final Player player = event.getPlayer();

        Block block = event.getBlock();

        if (block.getWorld().getName().equalsIgnoreCase("raids") && block.getType() != Material.OBSIDIAN) return;

        ItemStack hand = player.getInventory().getItemInMainHand();

        if (!hand.getType().name().contains("PICKAXE")) return;

        if (!hand.hasItemMeta()) {
            ItemMeta itemMeta = hand.getItemMeta();
            itemMeta.setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

            hand.setItemMeta(itemMeta);
        }

        for (CustomEnchant customEnchant : this.plugin.getEnchantService().getAll()) {
            int level = customEnchant.getEnchantmentLevel(hand);
            if (level > 0) {
                customEnchant.run(player, block, level);
            }

        }
        block.getDrops(hand).forEach(drop -> {
            final CustomEnchant fortune = plugin.getEnchantService().get("Fortune");
            if (fortune != null && fortune.hasEnchantment(hand)) {
                int level = fortune.getEnchantmentLevel(hand);
                drop.setAmount((random.nextInt(level) + 1) / 2);
            }

            if (block.getType() != Material.OBSIDIAN) {
                event.setDropItems(false);
                User.addItem(player, drop);
            }
        });

        User.handleCounter(player, hand, true);
    }

    @EventHandler
    public final void onBlockPlaceEvent(BlockPlaceEvent event) {
        final ItemStack itemInMainHand = event.getItemInHand();
        if (itemInMainHand.hasItemMeta() && itemInMainHand.getItemMeta().hasDisplayName() && itemInMainHand.getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK") && itemInMainHand.getType() == Material.CHEST) {
            event.setCancelled(true);
            event.setBuild(false);
        }
    }

    @EventHandler
    public final void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack hand = player.getInventory().getItemInMainHand();

        if (hand.getType() == Material.SKULL_ITEM && hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK")) {
            event.setCancelled(true);

            BackpackUpgradeInventory.getInstance().open(player, hand);
        } else if (hand.getType().name().contains("PICKAXE")) {
            PickaxeInventory.getInstance().open(player, hand);
        }
    }

    @EventHandler
    public final void onPlayerQuitEvent(final PlayerQuitEvent event) {
        User.removeSelected(event.getPlayer());
    }

    @EventHandler
    public final void onPlayerKickEvent(final PlayerKickEvent event) {
        User.removeSelected(event.getPlayer());
    }

    @EventHandler
    public final void onSellAll(final SellAllEvent event) {
        final float[] backpackData = User.sellBackpack(event.getPlayer());
        final float backpackTotalCoins = backpackData[0];
        final int backpackTotalItems = (int) backpackData[1];

        event.setTotalCost(event.getTotalCost() + backpackTotalCoins);
        event.setTotalItems(event.getTotalItems() + backpackTotalItems);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final ItemStack newItemStack = player.getInventory().getItem(event.getNewSlot());
        final ItemStack oldItemStack = player.getInventory().getItem(event.getPreviousSlot());

        final CustomEnchant haste = this.plugin.getEnchantService().get("haste");
        final CustomEnchant speed = this.plugin.getEnchantService().get("speed");

        if (newItemStack != null && newItemStack.getType().name().contains("PICKAXE")) {
            final int hasteLevel = haste.getEnchantmentLevel(newItemStack);
            final int speedLevel = speed.getEnchantmentLevel(newItemStack);

            if (hasteLevel > 0) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, hasteLevel - 1));
            }

            if (speedLevel > 0) {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel - 1));
            }
        } else if (oldItemStack != null && oldItemStack.getType().name().contains("PICKAXE")) {
            final int hasteLevel = haste.getEnchantmentLevel(oldItemStack);
            final int speedLevel = speed.getEnchantmentLevel(oldItemStack);

            if (hasteLevel > 0) {
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            }

            if (speedLevel > 0) {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

}
