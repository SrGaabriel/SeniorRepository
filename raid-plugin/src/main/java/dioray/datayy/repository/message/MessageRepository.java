package dioray.datayy.repository.message;

import com.google.common.collect.Maps;
import dioray.datayy.repository.Repository;

import java.util.Map;

public class MessageRepository implements Repository<String, String> {

    private final Map<String, String> messageMap = Maps.newLinkedHashMap();

    @Override
    public Map<String, String> getMap() {
        return messageMap;
    }

    @Override
    public void put(String key, String value) {
        messageMap.put(key, value);
    }

    @Override
    public void remove(String key, String value) {
        messageMap.remove(key, value);
    }

    @Override
    public String get(String key) {
        return messageMap.get(key);
    }
}
