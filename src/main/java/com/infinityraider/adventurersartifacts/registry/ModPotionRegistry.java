package com.infinityraider.adventurersartifacts.registry;

import com.google.common.collect.ImmutableList;
import com.infinityraider.adventurersartifacts.reference.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ModPotionRegistry {
    private static final ModPotionRegistry INSTANCE = new ModPotionRegistry();

    public static ModPotionRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryNamespaced<ResourceLocation, Potion> potionRegistry;

    private List<Potion> potions;

    private int lastId = -1;

    private ModPotionRegistry() {
        this.potionRegistry = Potion.REGISTRY;
        this.potions = new ArrayList<>();
        getNextId();
    }

    public List<Potion> getModPotions() {
        return ImmutableList.copyOf(potions);
    }

    public Potion registerPotion(Potion potion, String name, Configuration config, String category) {
        int id = config.getInt(name + " potion id", category, getNextId(), 0, 255,
                "Id for the " + name + " potion effect, this potion effect is generated on the first run by detecting a free potion id.");
        if(config.hasChanged()) {
            config.save();
        }
        this.potionRegistry.register(id, new ResourceLocation(Reference.MOD_ID, potion.getName()), potion);
        potions.add(potion);
        return potion;
    }

    private int getNextId() {
        int id = lastId;
        boolean flag = false;
        while(!flag) {
            id = id + 1;
            Potion potion = potionRegistry.getObjectById(id);
            if(potion == null) {
                flag = true;
            }
        }
        lastId = id;
        return id;
    }
}
