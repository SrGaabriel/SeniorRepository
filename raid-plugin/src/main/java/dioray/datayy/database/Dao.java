package dioray.datayy.database;

import dioray.datayy.RaidPlugin;

public abstract class Dao<T> {

    protected final RaidPlugin main;

    public Dao(RaidPlugin main) {
        this.main = main;
    }

    public abstract void insert(T object);

    public abstract void update(T object);

    public abstract void delete(T object);

}