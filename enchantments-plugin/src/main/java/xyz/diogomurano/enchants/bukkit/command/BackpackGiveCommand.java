package xyz.diogomurano.enchants.bukkit.command;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.utils.ItemBuilder;
import xyz.diogomurano.enchants.bukkit.utils.ItemNBT;

public final class BackpackGiveCommand implements CommandExecutor {

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.backpackgive")) {
            sender.sendMessage(ChatColor.RED + "You dont have permission to do that!");
            return false;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /backpack give <level> <player>");
            return false;
        }

        final Integer level = NumberUtils.isNumber(args[1]) ? Integer.parseInt(args[1]) : null;
        if (level == null) {
            sender.sendMessage(ChatColor.RED + "The level must be a number");
            return false;
        }

        final Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + args[2] + " is offline");
            return false;
        }

        ItemStack backpackItemStack = new ItemBuilder(Material.SKULL_ITEM)
                .amount(1)
                .durability(SkullType.PLAYER.ordinal())
                .owner("Chest")
                .name("§b§lMINING BACKPACK")
                .lore(
                    "",
                    "§7Upgrade your backpack to gain more",
                    "§7money while mining",
                    "",
                    "§fCurrent Level: §b" + level,
                    "§f0/" + (level * 5000),
                    "§fAmount: §b0.0"
                )
                .build();

        backpackItemStack = ItemNBT.initBackpack(backpackItemStack, level);

        if (target.getInventory().addItem(backpackItemStack).size() == 0) {
            sender.sendMessage(ChatColor.GREEN + "The backpack item was give to " + target.getName());
        } else {
            sender.sendMessage(ChatColor.RED + target.getName() + "'s inventory is full");
        }

        return true;
    }
}
