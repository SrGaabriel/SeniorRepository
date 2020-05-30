package com.raidplugin.api.manager;

import java.util.List;

public interface Manager<K, V> {

    List<V> getCollection();

    void put(V value);

    void remove(V value);

    V get(K key);

}
