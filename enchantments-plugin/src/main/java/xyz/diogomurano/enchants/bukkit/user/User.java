package xyz.diogomurano.enchants.bukkit.user;

import lombok.Data;
import me.clip.autosell.AutoSell;
import me.clip.autosell.AutoSellAPI;
import me.clip.autosell.EcoUtil;
import me.clip.autosell.SellHandler;
import me.clip.autosell.multipliers.Multipliers;
import me.clip.autosell.objects.AreaMultiplier;
import me.clip.autosell.objects.Multiplier;
import me.clip.autosell.objects.PermissionMultiplier;
import me.clip.autosell.objects.Shop;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.Settings;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.item.BackpackInfo;
import xyz.diogomurano.enchants.bukkit.utils.ItemNBT;
import xyz.diogomurano.enchants.custom.CustomEnchant;

import java.util.*;

@Data
public class User {

    private static final Map<UUID, Counter> selectedCounterMap = new HashMap<>();
    private static final Set<UUID> disallowSellMessageSet = new HashSet<>();

    public static void setSelected(Player player, Counter selected) {
        User.selectedCounterMap.put(player.getUniqueId(), selected);
    }

    public static Counter getSelected(final Player player) {
        return User.selectedCounterMap.get(player.getUniqueId());
    }

    public static void removeSelected(final Player player) {
        User.selectedCounterMap.remove(player.getUniqueId());
    }

    public static void addItem(final Player player, final ItemStack itemStack) {
        Map<Integer, ItemStack> itemsBack = player.getInventory().addItem(itemStack);

        for (ItemStack item : itemsBack.values()) {
            addBackpackItem(player, item);
        }
    }

    public static ItemStack handleCounter(final Player player, ItemStack hand, final boolean update) {
        Counter selected = User.getSelected(player);
        if (selected == null) return hand;

        if (!ItemNBT.hasCounters(hand)) {
            hand = ItemNBT.initCounters(hand);
        }

        if (selected == Counter.BACKPACK) {
            User.handleBackpackCounter(player, 1);

            return null;
        } else {
            return User.handleEnchantCounter(player, selected, hand, update, 1);
        }
    }

    public static ItemStack handleEnchantCounter(final Player player, final Counter selected, ItemStack hand, final boolean update, final int amount) {
        ItemNBT.Result result = ItemNBT.addCount(hand, selected, amount);

        hand = result.itemStack;

        if (result.levelsUp > 0) {
            CustomEnchant customEnchant = BukkitEnchantmentPlugin.getInstance().getEnchantService().get(selected.name());

            int levelsUp = result.levelsUp;
            if (customEnchant.getEnchantmentLevel(hand) + levelsUp > customEnchant.getMaxLevel()) {
                levelsUp = customEnchant.getMaxLevel() - customEnchant.getEnchantmentLevel(hand);
            }

            if (levelsUp > 0) {
                int level = customEnchant.upgradeEnchantment(hand, levelsUp);

                player.sendMessage(ChatColor.AQUA + "Your pickaxe's " + customEnchant.getName() + " enchantment has been upgraded to level " + level);
            }
        }

        if (update) {
            player.getInventory().setItemInMainHand(hand);
            player.updateInventory();
        }

        return hand;
    }

    public static boolean handleBackpackCounter(final Player player, final int amount) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK") && itemStack.getItemMeta().hasLore()) {
                ItemNBT.Result result = ItemNBT.addCount(itemStack, Counter.BACKPACK, amount);
                itemStack = result.itemStack;

                if (result.levelsUp > 0) {
                    BackpackInfo info = ItemNBT.getBackpackInfo(itemStack);

                    int levelsUp = result.levelsUp;
                    if (info.getLevel() + levelsUp > Backpack.getMaxLevel()) {
                        levelsUp = Backpack.getMaxLevel() - info.getLevel();
                    }

                    if (levelsUp > 0) {
                        itemStack = ItemNBT.upgradeBackpack(itemStack, levelsUp);

                        player.sendMessage(ChatColor.AQUA + "Your backpack has been upgraded to level " + (info.getLevel() + levelsUp));
                    }
                }

                inventory.setItem(i, itemStack);
                player.updateInventory();

                return true;
            }
        }

        return false;
    }

    private static void addBackpackItem(final Player player, final ItemStack itemStack) {
        final PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack content = inventory.getItem(i);

            if (content != null && content.hasItemMeta() && content.getItemMeta().hasDisplayName() && content.getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK") && content.getItemMeta().hasLore()) {
                BackpackInfo info = ItemNBT.getBackpackInfo(content);
                int limit = info.getLevel() * 5000;

                if (info.getItems() >= limit) break;

                Shop shop = SellHandler.getShop(player);

                float itemsPrice = info.getItemsPrice();
                itemsPrice += itemStack.getAmount() * shop.getBaseWorth(itemStack);

                int itemsCount = info.getItems();

                itemsCount += itemStack.getAmount();

                if (itemsCount + itemStack.getAmount() > limit) {
                    itemsCount = limit;
                }

                info.setItemsPrice(itemsPrice);
                info.setItems(itemsCount);

                ItemMeta itemMeta = content.getItemMeta();
                itemMeta.setLore(Arrays.asList(
                        "",
                        "§7Upgrade your backpack to gain more",
                        "§7money while mining",
                        "",
                        "§fCurrent Level: §b" + info.getLevel(),
                        "§f" + info.getItems() + '/' + limit,
                        "§fAmount: §b" + info.getItemsPrice()
                ));

                content.setItemMeta(itemMeta);

                content = ItemNBT.updateBackpack(content, info);

                inventory.setItem(i, content);
                player.updateInventory();

                break;
            }
        }
    }

    public static void sellAllItems(Player player) {
        PlayerInventory inventory = player.getInventory();

        Shop shop = SellHandler.getShop(player);

        float totalCoins = 0;
        int totalItems = 0;

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (itemStack != null) {
                if (AutoSell.getInstance().getOptions().disallowCustomItemMetaItems() && itemStack.hasItemMeta()) {
                    if (itemStack.getItemMeta().hasDisplayName()) continue;

                    if (itemStack.getItemMeta().hasLore()) continue;
                }

                float price = (float) shop.getBaseWorth(itemStack);
                if (price > 0) {
                    totalCoins += itemStack.getAmount() * price;
                    totalItems += itemStack.getAmount();

                    inventory.setItem(i, null);
                }
            }
        }

        float multiplier = 1.0f;

        Multiplier playerMultiplier = AutoSellAPI.getMultiplier(player);
        if (playerMultiplier != null) {
            multiplier += playerMultiplier.getMultiplier();
        }

        PermissionMultiplier permissionMultiplier = Multipliers.getPermissionMultiplier(player);
        if (permissionMultiplier != null) {
            multiplier += permissionMultiplier.getMultiplier();
        }

        AreaMultiplier areaMultiplier = Multipliers.getAreaMultiplier(player, shop);
        if (areaMultiplier != null) {
            multiplier += areaMultiplier.getMultiplier();
        }

        Multiplier globalMultiplier = Multipliers.getGlobalMultiplier();
        if (globalMultiplier != null) {
            multiplier += globalMultiplier.getMultiplier();
        }

        final float[] backpackData = User.sellBackpack(player);
        final float backpackTotalCoins = backpackData[0];
        final int backpackTotalItems = (int) backpackData[1];

        totalCoins += backpackTotalCoins;
        totalItems += backpackTotalItems;

        if (multiplier > 1) {
            totalCoins *= multiplier;
        }

        String formattedTotalCoins = String.format("%.2f", totalCoins);
        if (AutoSell.getInstance().getOptions().fixMoney()) {
            formattedTotalCoins = EcoUtil.fixMoney(totalCoins);
        }

        BukkitEnchantmentPlugin.getInstance().getEconomy().depositPlayer(player, totalCoins);

        if (!User.disallowSellMessageSet.contains(player.getUniqueId())) {
            for (String line : Settings.AUTO_SELL_MESSAGE) {
                String message = line
                        .replace("%items%", String.valueOf(totalItems))
                        .replace("%earnt%", String.valueOf(formattedTotalCoins))
                        .replace("%multiplier%", String.format("%.2f", multiplier));

                player.sendMessage(message);
            }
        }
    }

    public static float[] sellBackpack(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack content = inventory.getItem(i);

            if (content != null && content.hasItemMeta() && content.getItemMeta().hasDisplayName() && content.getItemMeta().getDisplayName().equals("§b§lMINING BACKPACK") && content.getItemMeta().hasLore()) {
                BackpackInfo info = ItemNBT.getBackpackInfo(content);
                int limit = info.getLevel() * 5000;

                if (info.getItemsPrice() <= 0 || info.getItems() <= 0) break;

                float itemsPrice = info.getItemsPrice();
                int count = info.getItems();

                BukkitEnchantmentPlugin.getInstance().getEconomy().depositPlayer(player, itemsPrice);

                info.setItems(0);
                info.setItemsPrice(0);

                ItemMeta itemMeta = content.getItemMeta();
                itemMeta.setLore(Arrays.asList(
                        "",
                        "§7Upgrade your backpack to gain more",
                        "§7money while mining",
                        "",
                        "§fCurrent Level: §b" + info.getLevel(),
                        "§f" + info.getItems() + '/' + limit,
                        "§fAmount: §b" + info.getItemsPrice()
                ));

                content.setItemMeta(itemMeta);

                content = ItemNBT.updateBackpack(content, info);

                inventory.setItem(i, content);
                player.updateInventory();

                return new float[] { itemsPrice, count };
            }
        }

        return new float[] { 0, 0 };
    }

    public static void toggleSellMessage(Player player) {
        if (User.disallowSellMessageSet.contains(player.getUniqueId())) {
            User.disallowSellMessageSet.remove(player.getUniqueId());

            player.sendMessage(ChatColor.GREEN + "You have activated the AutoSell message");
        } else {
            User.disallowSellMessageSet.add(player.getUniqueId());

            player.sendMessage(ChatColor.RED + "You have deactivated the AutoSell message");
        }
    }

    public enum Counter {
        BACKPACK,
        AUTOSELL,
        DEMOLISHER,
        DIGGER,
        EFFICIENCY,
        EXPLOSION,
        FORTUNE,
        HASTE,
        JESUS,
        KEYGEN,
        METEOR,
        SPEED,
        THOR;

        public static Counter getByCustomEnchant(CustomEnchant customEnchant) {
            return Counter.valueOf(customEnchant.getName().toUpperCase());
        }
    }

}