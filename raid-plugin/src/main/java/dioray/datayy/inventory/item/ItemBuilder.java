package dioray.datayy.inventory.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder {

    private final ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, short data) {
        this(new ItemStack(material, amount, data));
    }

    private ItemBuilder applyStack(Consumer<ItemStack> itemStack) {
        itemStack.accept(this.itemStack); return this;
    }

    private ItemBuilder applyMeta(Consumer<ItemMeta> itemMeta) {
        ItemMeta meta = itemStack.getItemMeta();

        itemMeta.accept(meta);

        return applyStack(stack -> stack.setItemMeta(meta));
    }

    public ItemBuilder name(String name) {
        return applyMeta(meta -> meta.setDisplayName(name));
    }

    public ItemBuilder lore(String... lore) {
        return applyMeta(meta -> meta.setLore(Arrays.asList(lore)));
    }

    public ItemBuilder lore(List<String> lore) { return applyMeta(meta -> meta.setLore(lore)); }

    public ItemStack build() {
        return itemStack;
    }
}
