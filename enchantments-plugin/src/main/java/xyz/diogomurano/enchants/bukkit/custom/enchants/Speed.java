package xyz.diogomurano.enchants.bukkit.custom.enchants;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.diogomurano.enchants.bukkit.custom.AbstractCustomEnchant;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Speed extends AbstractCustomEnchant {

    public Speed() {
        super(UUID.randomUUID(), "Speed");
    }

    @Override
    public void run(Player player, Block block, int level) {}

    @Override
    public List<String> getLore() {
        return Collections.singletonList("§7Get the speed effect");
    }
}
