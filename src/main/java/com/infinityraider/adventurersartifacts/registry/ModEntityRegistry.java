package com.infinityraider.adventurersartifacts.registry;

import com.infinityraider.adventurersartifacts.artifacts.mantastyle.EntityReplicate;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
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
