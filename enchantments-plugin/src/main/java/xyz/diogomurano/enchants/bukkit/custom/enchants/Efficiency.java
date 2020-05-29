package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Efficiency extends AbstractCustomEnchant {

    public Efficiency() {
        super(UUID.randomUUID(), "Efficiency");
    }

    @Override
    public final void run(final Player player, final Block block, int level) {}

    @Override
    public final List<String> getLore() {
        return Collections.singletonList("§7Break the blocks faster");
    }

    @Override
    public final void addEnchantment(ItemStack stack, Integer level) {
        stack.addUnsafeEnchantment(Enchantment.DIG_SPEED, level);
        super.addEnchantment(stack, level);
    }
}
