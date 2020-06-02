package com.raidplugin.api.manager;

import java.util.List;

/**
 * This is a manager to manage collection from V.
 * You can change the manager values from methods.
 *
 * @param <K> - It's a key to get V.
 * @param <V> - It's a value from K.
 */

public interface Manager<K, V> {

    List<V> getCollection();

    void put(V value);

    void putAll(List<V> collection);

    void remove(V value);

    void removeAll(List<V> collection);

    V get(K key);

}
