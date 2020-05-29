package dioray.datayy.repository.cooldown;

import com.google.common.collect.Maps;
import dioray.datayy.repository.Repository;

import java.util.Map;

public class CooldownRepository implements Repository<String, Long> {

    private final Map<String, Long> cooldownMap = Maps.newLinkedHashMap();

    @Override
    public Map<String, Long> getMap() {
        return cooldownMap;
    }

    @Override
    public void put(String key, Long value) {
        cooldownMap.put(key, value);
    }

    @Override
    public void remove(String key, Long value) {
        cooldownMap.remove(key, value);
    }

    @Override
    public Long get(String key) {
        Long time = cooldownMap.get(key);

        if(System.currentTimeMillis() >= time) return null;

        return cooldownMap.get(key);
    }
}
