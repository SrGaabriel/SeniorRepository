package xyz.diogomurano.enchants.bukkit.utils.menu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Menu {

    private final String name;
    private final int rows;
    private final Map<Integer, ClickItem> slots;
    private final Inventory inventory;
    private final CloseHandler closeHandler;

    public Menu(String name, int rows) {
        this(name, rows, () -> {
        });
    }

    public Menu(String name, int rows, CloseHandler closeHandler) {
        this.name = name;
        this.rows = rows;
        this.slots = new HashMap<>();
        this.inventory = Bukkit.createInventory(new Holder(this), rows * 9, name);
        this.closeHandler = closeHandler;
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, new ClickItem(item));
    }

    public void setItem(int slot, ItemStack item, ClickHandler handler) {
        setItem(slot, new ClickItem(item, handler));
    }

    public void setItem(ClickItem item, int slot) {
        setItem(slot, item);
    }

    public void setItem(int slot, ClickItem item) {
        this.slots.put(slot, item);
        inventory.setItem(slot, item.getStack());
    }

    public int firstEmpty() {
        return inventory.firstEmpty();
    }

    public boolean hasItem(int slot) {
        return this.slots.containsKey(slot);
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public Map<Integer, ClickItem> getSlots() {
        return slots;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ClickItem getItem(int slot) {
        return slots.get(slot);
    }

    public void open(Player p) {
        p.getOpenInventory();
        if (p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST || p.getOpenInventory().getTopInventory().getSize() != rows * 9 || p.getOpenInventory().getTopInventory().getHolder() == null || !(p.getOpenInventory().getTopInventory().getHolder() instanceof Holder)) {
            createAndOpenInventory(p);
        } else {
            for (int i = 0; i < rows * 9; i++) {
                if (this.slots.containsKey(i))
                    p.getOpenInventory().getTopInventory().setItem(i, slots.get(i).getStack());
                else
                    p.getOpenInventory().getTopInventory().setItem(i, null);
            }
            p.updateInventory();
        }
        ((Holder) p.getOpenInventory().getTopInventory().getHolder()).setMenu(this);
        updateTitle(p);
    }

    public void updateTitle(Player p) {
        try {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW);
            packet.getChatComponents().write(0, WrappedChatComponent.fromText(name));
            Method getHandle = MinecraftReflection.getCraftPlayerClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(p);
            Field activeContainerField = entityPlayer.getClass().getField("activeContainer");
            Object activeContainer = activeContainerField.get(entityPlayer);
            Field windowIdField = activeContainer.getClass().getField("windowId");
            int id = windowIdField.getInt(activeContainer);
            packet.getStrings().write(0, "minecraft:chest");
            packet.getIntegers().write(0, id);
            packet.getIntegers().write(1, rows * 9);
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            int i = 0;
            for (ItemStack item : p.getInventory().getContents()) {
                p.getInventory().setItem(i, item);
                i += 1;
            }
            p.updateInventory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAndOpenInventory(Player p) {
        Inventory playerInventory = Bukkit.createInventory(new Holder(this), rows * 9, "");
        for (Map.Entry<Integer, ClickItem> entry : slots.entrySet()) {
            playerInventory.setItem(entry.getKey(), entry.getValue().getStack());
        }
        p.openInventory(playerInventory);
    }

    public CloseHandler getCloseHandler() {
        return closeHandler;
    }

    public void close(Player p) {
        p.closeInventory();
    }

    public interface ClickHandler {
        void onClick(InventoryClickEvent event);
    }

    public interface CloseHandler {
        void onClose();
    }

}
