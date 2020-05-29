package dioray.datayy.provider.message;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ConfigurationProvider {

    private static ConfigurationProvider configurationProvider;

    public static ConfigurationProvider getInstance() {
        return configurationProvider == null ? (configurationProvider = new ConfigurationProvider()) : configurationProvider;
    }

    private final Map<String, Object> keyMap = Maps.newLinkedHashMap();

    public void put(String key, Object message) {
        keyMap.put(key, message);
    }

    private void setGeneric(String target, Object... objects) {
        for(int i = 0; i!=objects.length; i+=2) {
            target.replace(String.valueOf(objects[i]), String.valueOf(objects[i + 1]));
        } target.replaceAll("&", "§");
    }

    private void setGeneric(List<String> stringList, Object... objects) {
        stringList.forEach(s -> setGeneric(s, objects));
    }

    public <T> T get(Class<T> clazz, String key, Object... replaces) {
        Object target = keyMap.get(key);

        if(target instanceof String) setGeneric((String) target, replaces);

        if(target instanceof List) setGeneric((List<String>) target, replaces);

        return clazz.cast(target);
    }

}
