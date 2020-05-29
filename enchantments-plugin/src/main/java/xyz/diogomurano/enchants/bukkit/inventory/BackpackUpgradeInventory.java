package xyz.diogomurano.enchants.bukkit.inventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.item.BackpackInfo;
import xyz.diogomurano.enchants.bukkit.user.User;
import xyz.diogomurano.enchants.bukkit.utils.ItemBuilder;
import xyz.diogomurano.enchants.bukkit.utils.ItemNBT;

public class BackpackUpgradeInventory implements InventoryHolder {

    private static BackpackUpgradeInventory instance;

    public static BackpackUpgradeInventory getInstance() {
        if (instance == null) {
            instance = new BackpackUpgradeInventory();
        }

        return instance;
    }

    public void open(Player player, ItemStack hand) {
        BackpackInfo info = ItemNBT.getBackpackInfo(hand);

        int count = ItemNBT.getCount(hand, User.Counter.BACKPACK);

        String countLore = "§fCount: §c" + count + "/" + Backpack.getBlockSet();
        if (User.getSelected(player) == User.Counter.BACKPACK) {
            countLore = "§fCount: §a" + count + "/" + Backpack.getBlockSet();
        }

        Inventory inventory = Bukkit.createInventory(this, 9 * 3, "Backpack Upgrade Menu");

        inventory.setItem(13, new ItemBuilder(Material.SKULL_ITEM)
                .owner("Chest")
                .durability(SkullType.PLAYER.ordinal())
                .name("§b§lMINING BACKPACK")
                .lore(
                        "",
                        "§7Upgrade your backpack to gain more",
                        "§7money while mining",
                        "",
                        "§fCurrent Level: §b" + (info.getLevel() == Backpack.getMaxLevel() ? "§3§lMAXED" : info.getLevel()),
                        "§f" + info.getItems() + "/" + (info.getLevel() * 5000),
                        "§fAmount: §b" + info.getItemsPrice(),
                        countLore
                )
                .build()
        );

        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
