package dioray.datayy.inventory;

import dioray.datayy.inventory.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class InventorySustainer {

    private final String nameInventory;
    private final MenuSize menuSize;
    private final InventoryWrapper inventoryWrapper;

    private final ClickService[] clickServices;
    private final ItemStack[] builders;

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new Listener() {

            private boolean isWrapper(Inventory inventory) {
                return inventory.getHolder() instanceof InventoryWrapper;
            }

            private InventoryWrapper getWrapper(Inventory inventory) {
                return (InventoryWrapper) inventory.getHolder();
            }

            @EventHandler(priority = EventPriority.HIGH)
            private void onClick(InventoryClickEvent e) {
                if (!isWrapper(e.getInventory())) return;

                InventoryWrapper inventoryWrapper = getWrapper(e.getInventory());
                int slot = e.getRawSlot();
                e.setCancelled(true);

                ClickService clickService = inventoryWrapper.getSustainer().clickServices[slot];

                if (clickService == null) return;

                clickService.applyEvent(e);
            }
        }, plugin);
    }

    public void open(HumanEntity entity) {
        Inventory inventory = Bukkit.createInventory(inventoryWrapper, menuSize.getSlot(), nameInventory);

        inventoryWrapper.setInventory(inventory);

        for (int i = 0; i != builders.length; i++) if (builders[i] != null) inventory.setItem(i, builders[i]);

        entity.openInventory(inventory);
    }

    public InventorySustainer(String nameInventory, MenuSize size) {
        this.nameInventory = nameInventory;
        this.menuSize = size;

        this.inventoryWrapper = new InventoryWrapper(this);
        this.clickServices = new ClickService[size.getSlot()];
        this.builders = new ItemStack[size.getSlot()];

    }

    public enum MenuSize {
        ONE_LINE(9),
        TWO_LINES(18),
        THREE_LINES(27),
        FOUR_LINES(36),
        FIVE_LINES(45),
        SIX_LINES(54);

        private final int slot;

        MenuSize(int size) {
            this.slot = size;
        }

        public int getSlot() {
            return slot;
        }
    }

    public static class InventoryWrapper implements InventoryHolder {

        private Inventory inventory;
        private final InventorySustainer inventorySustainer;

        public InventoryWrapper(InventorySustainer sustainer) {
            this.inventorySustainer = sustainer;
        }

        public InventorySustainer getSustainer() {
            return inventorySustainer;
        }

        public void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }

    protected interface ClickService {
        void applyEvent(InventoryClickEvent e);
    }

    public void setItem(int slot, ItemBuilder builder, ClickService service) {
        setItem(slot, builder.build(), service);
    }

    public void setItem(int slot, ItemBuilder itemBuilder) {
        setItem(slot, itemBuilder, null);
    }

    public void setItem(int slot, ItemStack itemStack, ClickService service) {
        clickServices[slot] = service; builders[slot] = itemStack;
    }
}
