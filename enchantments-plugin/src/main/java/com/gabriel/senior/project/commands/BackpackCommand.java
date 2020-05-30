package com.gabriel.senior.project.commands;

import com.gabriel.senior.project.prototypes.Account;
import com.gabriel.senior.project.repositories.impl.AccountRepository;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackpackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player player = (Player)sender;

        final Account account = AccountRepository.getInstance().retrieve(player.getUniqueId());

        player.openInventory(account.getBackpack().getInventory());
        return false;
    }

}
