package dioray.datayy.item;

import dioray.datayy.util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ValueVoucher {

    private static final String NAME = ChatColor.translateAlternateColorCodes('&', "&b&lValue Voucher &7(Right Click to use)");

    public static ItemStack create(int amount) {
        return ItemBuilder
                .create(Material.MAGMA_CREAM)
                .name(NAME)
                .lore(
                        ChatColor.GRAY + "Amount: " + amount
                )
                .build();
    }

    public static int getAmount(ItemStack itemStack) {
        return itemStack.getItemMeta().getLore().stream()
                .filter(line -> line.startsWith(ChatColor.GRAY + "Amount: "))
                .map(line -> Integer.parseInt(line.split(" ")[1]))
                .findFirst()
                .orElse(0);
    }

    public static boolean isVoucher(ItemStack itemStack) {
        return itemStack.getType() == Material.MAGMA_CREAM &&
               itemStack.hasItemMeta() &&
                itemStack.getItemMeta().hasDisplayName() &&
                itemStack.getItemMeta().getDisplayName().equals(NAME) &&
                itemStack.getItemMeta().hasLore();
    }

}