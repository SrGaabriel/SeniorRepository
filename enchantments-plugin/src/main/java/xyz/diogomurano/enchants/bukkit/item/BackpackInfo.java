package xyz.diogomurano.enchants.bukkit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BackpackInfo {

    private int level;
    private int items;
    private float itemsPrice;
    private int count;

}