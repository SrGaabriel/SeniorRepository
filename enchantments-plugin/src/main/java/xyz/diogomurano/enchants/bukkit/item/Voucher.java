package xyz.diogomurano.enchants.bukkit.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.utils.ItemBuilder;

public class Voucher {

    private static final String NAME = ChatColor.translateAlternateColorCodes('&', "&b&lAutoSell Voucher &7(Click on Autosell in the GUI)");

    public static ItemStack create() {
        return new ItemBuilder(Material.PAPER)
                .amount(1)
                .name(NAME)
                .lore(
                        ChatColor.WHITE + "This voucher can be claimed to",
                        ChatColor.WHITE + "max out " + ChatColor.AQUA + "Autosell" + ChatColor.WHITE + ", can be redeemed",
                        ChatColor.WHITE + "from crates or the store",
                        ChatColor.AQUA  + "store.liquidprison.com"
                )
                .build();
    }

    public static boolean isVoucher(ItemStack itemStack) {
        return itemStack.getType() == Material.PAPER &&
               itemStack.hasItemMeta() &&
               itemStack.getItemMeta().hasDisplayName() &&
               itemStack.getItemMeta().getDisplayName().equals(NAME) &&
               itemStack.getItemMeta().hasLore();
    }

}