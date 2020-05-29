package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Haste extends AbstractCustomEnchant {

    public Haste() {
        super(UUID.randomUUID(), "Haste");
    }

    @Override
    public final void run(Player player, Block block, int level) {}

    @Override
    public final List<String> getLore() {
        return Collections.singletonList("§7Get the haste effect");
    }
}
