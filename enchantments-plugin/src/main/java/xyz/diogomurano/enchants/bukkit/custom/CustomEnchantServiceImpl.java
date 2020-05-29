package xyz.diogomurano.enchants.bukkit.custom;

import com.google.common.collect.ImmutableList;
import xyz.diogomurano.enchants.custom.CustomEnchant;
import xyz.diogomurano.enchants.custom.CustomEnchantService;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public class CustomEnchantServiceImpl implements CustomEnchantService {

    private final Set<CustomEnchant> enchants;

    public CustomEnchantServiceImpl() {
        enchants = new LinkedHashSet<>();
    }

    @Override
    public ImmutableList<CustomEnchant> getAll() {
        return ImmutableList.copyOf(enchants);
    }

    @Override
    public final void add(CustomEnchant enchant) {
        Objects.requireNonNull(enchant, "enchant can't be null.");
        this.enchants.add(enchant);
    }

    @Override
    public final void remove(CustomEnchant enchant) {
        Objects.requireNonNull(enchant, "enchant can't be null.");
        this.enchants.remove(enchant);
    }

    @Override
    public final CustomEnchant get(UUID uniqueId) {
        return find(enchant -> enchant.getUniqueId().equals(uniqueId));
    }

    @Override
    public final CustomEnchant get(String name) {
        return find(enchant -> enchant.getName().equalsIgnoreCase(name));
    }

    private CustomEnchant find(Predicate<CustomEnchant> predicate) {
        return enchants.stream().filter(predicate).findAny().orElse(null);
    }

}
