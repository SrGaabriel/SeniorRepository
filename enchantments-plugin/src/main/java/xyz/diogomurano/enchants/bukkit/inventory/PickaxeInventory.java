package xyz.diogomurano.enchants.bukkit.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.bukkit.utils.ItemBuilder;
import xyz.diogomurano.enchants.bukkit.utils.ItemNBT;
import xyz.diogomurano.enchants.custom.CustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

public class PickaxeInventory implements InventoryHolder {

    private static PickaxeInventory instance;

    public static PickaxeInventory getInstance() {
        if (instance == null) {
            instance = new PickaxeInventory(BukkitEnchantmentPlugin.getInstance().getEnchantService());
        }

        return instance;
    }

    private final CustomEnchantService enchantService;

    private PickaxeInventory(CustomEnchantService enchantService) {
        this.enchantService = enchantService;
    }

    public void open(Player player, ItemStack hand) {
        Inventory inventory = Bukkit.createInventory(this, 9 * 6, "Pickaxe Upgrade Menu");

        inventory.setItem(13, hand.clone());

        int slot = 28;
        for (CustomEnchant enchant : this.enchantService.getAll()) {
            User.Counter counter = User.Counter.getByCustomEnchant(enchant);

            int level = enchant.getEnchantmentLevel(hand);

            int count = ItemNBT.getCount(hand, counter);

            String countLoreLine = "§fCount: §c" + count + "/" + enchant.getBlockSet();
            if (User.getSelected(player) == counter) {
                countLoreLine = "§fCount: §a" + count + "/" + enchant.getBlockSet();
            }

            final String fCountLoreLine = countLoreLine;

            inventory.setItem(slot, ItemBuilder
                    .create(Material.ENCHANTED_BOOK)
                    .name("§b" + enchant.getName())
                    .lore(lore -> {
                        lore.add("");
                        lore.addAll(enchant.getLore());
                        lore.add("");
                        lore.add("§fCurrent Level: §b" + (level == enchant.getMaxLevel() ? "§3§lMAXED" : level));
                        lore.add("§fMax Level: §b" + enchant.getMaxLevel());
                        lore.add("");
                        lore.add(fCountLoreLine);
                    })
                    .flags(ItemFlag.HIDE_ENCHANTS)
                    .build()
            );

            slot++;

            if (slot == 35 || slot == 44) {
                slot += 2;
            }
        }

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
