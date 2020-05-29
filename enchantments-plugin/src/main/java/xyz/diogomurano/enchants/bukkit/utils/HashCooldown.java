package xyz.diogomurano.enchants.bukkit.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public final class HashCooldown<T> {

    private final Map<Integer, Long> map;
    private final DecimalFormat decimalFormat;

    public HashCooldown() {
        map = new HashMap<>();

        decimalFormat = new DecimalFormat("#.#");
    }

    public final void insert(T o, long time) {
        if (o != null && time > 0)
            map.put(o.hashCode(), System.currentTimeMillis() + time);
    }

    public final String getReamingSeconds(T o) {
        if (o != null) {
            Long l = map.get(o.hashCode());
            if (l != null) {
                double time = (double) (l - System.currentTimeMillis()) / 1000;
                return decimalFormat.format(time);
            }
        }
        return "";
    }

    public final long getTime(T o) {
        if (o != null) {
            Long l = map.get(o.hashCode());
            if (l != null) {
                return l - System.currentTimeMillis();
            }
        }
        return 0;
    }

    public final boolean isWaiting(T o) {
        int i = o.hashCode();
        return map.containsKey(i) && map.get(i) > System.currentTimeMillis();
    }
}
