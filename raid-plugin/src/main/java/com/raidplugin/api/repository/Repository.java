package com.raidplugin.api.repository;

import java.util.List;
import java.util.Map;

/**
 * That's a repository to manage values.
 *
 * @param <K> - It's a key to get V.
 * @param <V> - It's a value from K.
 */

public interface Repository<K, V> {

    Map<K, V> getMap();

    void put(K key, V value);

    void putAll(List<V> collection);

    void remove(K key, V value);

    V get(K key);

}
