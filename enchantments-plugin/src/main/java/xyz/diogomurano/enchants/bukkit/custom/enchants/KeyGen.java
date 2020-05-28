package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class KeyGen extends AbstractCustomEnchant {

    private final Map<String, Double> commands;

    public KeyGen() {
        super(UUID.randomUUID(), "KeyGen");

        commands = new HashMap<>();

        for (String key : getConfiguration().getConfigurationSection("enchants.KeyGen").getKeys(false)) {
            if(!key.equalsIgnoreCase("price")) {
                final String command = getConfiguration().getString("enchants.KeyGen." + key + ".message");
                commands.put(command, getConfiguration().getDouble("enchants.KeyGen." + key + ".chance"));
            }
        }
    }

    @Override
    public void run(Player player, Block block, int level) {
        float chance = this.calculateChance(level);

        if (ThreadLocalRandom.current().nextFloat() <= chance) {
            int r = ThreadLocalRandom.current().nextInt(100);

            commands.forEach((s, aDouble) -> {
                if(r < aDouble) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);
                }
            });
        }
    }

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Chance to give a key on drop");
    }

}
