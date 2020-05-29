package dioray.datayy.provider;

import dioray.datayy.inventory.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VoucherProvider {

    private static VoucherProvider voucherProvider;

    public static VoucherProvider getInstance() {
        return voucherProvider == null ? (voucherProvider = new VoucherProvider()) : voucherProvider;
    }

    public ItemStack toItemStack(int amount) {
        return new ItemBuilder(Material.MAGMA_CREAM)
                .name("§b§lVoucher")
                .lore(
                        " ",
                        " §7Amount: " + amount,
                        " §cRight click to use this!",
                        " "
                ).build();
    }

    // Apply NBT.

}