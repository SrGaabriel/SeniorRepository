package xyz.diogomurano.enchants.bukkit.item;

public class BackpackInfo {

    private int level;
    private int items;
    private float itemsPrice;
    private int count;

    public BackpackInfo(int level, int items, float itemsPrice, int count) {
        this.level = level;
        this.items = items;
        this.itemsPrice = itemsPrice;
        this.count = count;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public float getItemsPrice() {
        return itemsPrice;
    }

    public void setItemsPrice(float itemsPrice) {
        this.itemsPrice = itemsPrice;
    }

    public int getCount() {
        return count;
    }
}