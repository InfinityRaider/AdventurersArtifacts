package com.infinityraider.addartifacts.registry;

import com.infinityraider.addartifacts.artifacts.mantastyle.EntityReplicate;
import com.infinityraider.addartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;

public class ModEntityRegistry {
    private static final ModEntityRegistry INSTANCE = new ModEntityRegistry();

    public static ModEntityRegistry getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    private ModEntityRegistry() {
        this.entityReplicate = ModuleMantaStyle.getInstance().entityReplicate;
    }

    public final EntityRegistryEntry<EntityReplicate> entityReplicate;
}
