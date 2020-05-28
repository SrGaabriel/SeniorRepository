package dioray.datayy.service;

import dioray.datayy.RaidPlugin;

public class Service {

    protected final RaidPlugin main;

    public Service(RaidPlugin main) {
        this.main = main;
    }

    public void enable() {}

    public void disable() {}

}