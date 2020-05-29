package xyz.diogomurano.enchants.bukkit.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.EnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.inventory.BackpackUpgradeInventory;
import xyz.diogomurano.enchants.bukkit.inventory.PickaxeInventory;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.item.BackpackInfo;
import xyz.diogomurano.enchants.bukkit.item.Voucher;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.bukkit.utils.ItemNBT;
import xyz.diogomurano.enchants.custom.CustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

public final class InventoryListener implements Listener {

    private final CustomEnchantService enchantService;

    public InventoryListener(final EnchantmentPlugin plugin) {
        this.enchantService = plugin.getEnchantService();
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getCurrentItem() == null) return;

        final Player player = (Player) event.getWhoClicked();

        Inventory topInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        if (topInventory.getHolder() instanceof PickaxeInventory) {
            final ItemStack cursor = event.getCursor();
            final ItemStack itemStack = event.getCurrentItem();

            event.setCancelled(true);

            if ((event.getAction().name().startsWith("PICKUP")  && Voucher.isVoucher(itemStack)) || (event.getAction().name().startsWith("PLACE") && Voucher.isVoucher(cursor))) {
                event.setCancelled(false);

                return;
            }

            if (itemStack.getType() == Material.ENCHANTED_BOOK) {
                final ItemStack hand = player.getInventory().getItemInMainHand();

                if (!hand.getType().name().contains("PICKAXE")) return;

                CustomEnchant enchant = this.enchantService.get(itemStack.getItemMeta().getDisplayName().substring(2));
                if (enchant == null) return;

                User.Counter counter = User.Counter.getByCustomEnchant(enchant);

                if (event.getAction().name().startsWith("PICKUP")) {
                    if (enchant.getEnchantmentLevel(hand) >= enchant.getMaxLevel()) {
                        player.sendMessage(ChatColor.RED + "This enchantment is already maxed");
                    } else {
                        User.setSelected(player, counter);
                        player.sendMessage(ChatColor.AQUA + "You have selected " + enchant.getName() + " to upgrade");
                    }

                    player.closeInventory();

                    return;
                }

                if (counter == User.Counter.AUTOSELL && event.getAction() == InventoryAction.SWAP_WITH_CURSOR && Voucher.isVoucher(cursor)) {
                    event.setCursor(null);

                    final int level = enchant.getEnchantmentLevel(hand);

                    if (level >= enchant.getMaxLevel()) {
                        player.sendMessage(ChatColor.RED + "The AutoSell enchant is already maxed");
                        player.closeInventory();

                        if (player.getInventory().addItem(cursor).size() > 0) {
                            player.getWorld().dropItemNaturally(player.getLocation(), event.getCursor());

                            player.sendMessage(ChatColor.RED + "Your voucher item have been dropped because your inventory is full");
                        }

                        return;
                    }

                    if (!ItemNBT.hasCounters(hand)) {
                        ItemNBT.initCounters(hand);
                    }

                    enchant.maxEnchantment(hand);

                    player.sendMessage(ChatColor.AQUA + "You have used your autosell voucher");
                    player.closeInventory();
                }
            }
        } else if (topInventory.getHolder() instanceof BackpackUpgradeInventory) {
            ItemStack itemStack = event.getCurrentItem();

            event.setCancelled(true);

            if (event.getAction().name().startsWith("PICKUP") && itemStack.getType() == Material.SKULL_ITEM) {
                ItemStack hand = player.getInventory().getItemInMainHand();

                if (hand.getType() != Material.SKULL_ITEM) return;

                BackpackInfo info = ItemNBT.getBackpackInfo(hand);

                if (info.getLevel() >= Backpack.getMaxLevel()) {
                    player.sendMessage(ChatColor.RED + "Your backpack is already maxed");
                } else {
                    User.setSelected(player, User.Counter.BACKPACK);
                    player.sendMessage(ChatColor.AQUA + "You have selected your backpack to upgrade");
                }

                player.closeInventory();
            }
        }
    }

}
