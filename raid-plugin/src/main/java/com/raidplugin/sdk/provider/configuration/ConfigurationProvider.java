package com.raidplugin.sdk.provider.configuration;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ConfigurationProvider {

    public static ConfigurationProvider configurationProvider;

    public static ConfigurationProvider getInstance() {
        return configurationProvider == null ? (configurationProvider = new ConfigurationProvider()) : configurationProvider;
    }

    private final Map<String, Object> objectMap = Maps.newLinkedHashMap();

    public void put(String key, Object value) {
        objectMap.put(key, value);
    }

    public <T> T get(Class<T> clazz, String key, Object... replace) {
        Object value = objectMap.get(key);

        if(value instanceof String) setGeneric((String) value, replace);

        if(value instanceof List) setGeneric((List) value, replace);

        return (T) value;
    }

    private void setGeneric(String key, Object... replace) {
        for(int i = 0; i!=replace.length; i+=2) {
            key.replace(String.valueOf(replace[i]), String.valueOf(replace[i + 1]));
        }
    }

    private void setGeneric(List<String> list, Object... replace) {
        list.forEach(message -> setGeneric(message, replace));
    }
}
