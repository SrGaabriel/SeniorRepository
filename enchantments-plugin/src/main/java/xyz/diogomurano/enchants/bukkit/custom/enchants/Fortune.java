package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Fortune extends AbstractCustomEnchant {

    public Fortune() {
        super(UUID.randomUUID(), "Fortune");
    }

    @Override
    public void run(Player player, Block block, int level) {}

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Receive more ores with each block");
    }

    @Override
    public void addEnchantment(ItemStack stack, Integer level) {
        stack.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, level);

        super.addEnchantment(stack, level);
    }
}
