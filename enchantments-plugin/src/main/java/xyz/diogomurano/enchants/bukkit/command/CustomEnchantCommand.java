package xyz.diogomurano.enchants.bukkit.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.BukkitEnchantmentPlugin;
import xyz.diogomurano.enchants.bukkit.item.Backpack;
import xyz.diogomurano.enchants.bukkit.item.Voucher;
import xyz.diogomurano.enchants.custom.CustomEnchant;

import java.util.List;
import java.util.stream.Collectors;

public final class CustomEnchantCommand implements CommandExecutor, TabExecutor {

    private final BukkitEnchantmentPlugin plugin;

    public CustomEnchantCommand(final BukkitEnchantmentPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public final boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("command.customenchant")) {
            sender.sendMessage(ChatColor.RED + "You dont have permission to do that!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /customenchant <reload/enchant/voucher> [level] [player]");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            this.plugin.getEnchantService().getAll().forEach(CustomEnchant::reload);
            Backpack.init();
            sender.sendMessage(ChatColor.RED + "You have reloaded the config");
            return true;
        }

        if (args[0].equalsIgnoreCase("voucher")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /customenchant voucher <player>");
                return true;
            }

            final Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + args[1] + " is offline");
                return true;
            }

            final ItemStack voucher = Voucher.create();

            if (target.getInventory().addItem(voucher).size() == 0) {
                sender.sendMessage(ChatColor.GREEN + "You have gave a autosell voucher item to " + target.getName());
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + "'s inventory is full");
            }

            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can do that");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /customenchant <enchant> <level>");
            return true;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();
        if (!hand.getType().name().contains("PICKAXE")) {
            player.sendMessage(ChatColor.RED + "You can only enchant pickaxes");

            return true;
        }

        CustomEnchant customEnchant = plugin.getEnchantService().get(args[0]);
        if (customEnchant == null) {
            player.sendMessage(ChatColor.RED + "Enchantment " + args[0] + " not found");
            return true;
        }

        try {
            int level = Integer.parseInt(args[1]);
            if (level <= 0) {
                sender.sendMessage(ChatColor.RED + "Only positive numbers are allowed");
                return true;
            }

            if (level > customEnchant.getMaxLevel()) {
                sender.sendMessage(ChatColor.RED + customEnchant.getName() + " max level is " + customEnchant.getMaxLevel());
                return true;
            }

            customEnchant.addEnchantment(hand, level);

            player.sendMessage(ChatColor.GREEN + "You have enchanted your pickaxe with " + customEnchant.getName() + ' ' + level);
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + "The level must be a number");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> tab = this.plugin.getEnchantService().getAll().stream()
                    .map(enchant -> enchant.getName().toLowerCase()).collect(Collectors.toList());
            tab.add("voucher");

            String lower = args[0].toLowerCase();

            return tab.stream()
                    .filter(element -> element.startsWith(lower))
                    .collect(Collectors.toList());

        }

        return null;
    }

    public void register() {
        PluginCommand command = this.plugin.getCommand("customenchant");

        command.setExecutor(this);
        command.setTabCompleter(this);
    }
}
