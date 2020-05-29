package xyz.diogomurano.enchants.bukkit.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.diogomurano.enchants.bukkit.user.User;

public final class SellToggleCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can toggle the sell message");

            return true;
        }
        User.toggleSellMessage((Player) sender);
        return true;
    }

}