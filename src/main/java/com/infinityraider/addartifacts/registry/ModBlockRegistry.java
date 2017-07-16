package com.infinityraider.addartifacts.registry;

public class ModBlockRegistry {
    private static final ModBlockRegistry INSTANCE = new ModBlockRegistry();

    public static ModBlockRegistry getInstance() {
        return INSTANCE;
    }

    private ModBlockRegistry() {}
}
