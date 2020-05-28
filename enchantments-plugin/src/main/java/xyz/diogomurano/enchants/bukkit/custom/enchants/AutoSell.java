package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AutoSell extends AbstractCustomEnchant {

    public AutoSell() {
        super(UUID.randomUUID(), "AutoSell");
    }

    @Override
    public void run(Player player, Block block, int level) {}

    @Override
    public List<String> getLore() {
        return Arrays.asList("§7Automatically sell everything you mine");
    }
}
