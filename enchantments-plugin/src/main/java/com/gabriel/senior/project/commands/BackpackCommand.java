package com.gabriel.senior.project.commands;

import me.saiintbrisson.commands.Execution;
import me.saiintbrisson.commands.annotations.Command;
import me.saiintbrisson.commands.argument.Argument;

public class BackpackCommand implements CIndentifier {

    @Command(name = "backpack", permission = "enchantments.backpack", async = true)
    public void onCommand(Execution executor, @Argument String[] args) {
        if (executor.getPlayer() == null) {
            return;
        }
        executor.sendMessage("fodase?");
    }

}
