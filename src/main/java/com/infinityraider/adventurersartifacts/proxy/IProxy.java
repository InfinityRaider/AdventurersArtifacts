package com.infinityraider.adventurersartifacts.proxy;

import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.artifacts.IArtifactModule;
import com.infinityraider.adventurersartifacts.config.ModConfig;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy extends IProxyBase {
    @Override
    default void postInitStart(FMLPostInitializationEvent event) {
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.registerPotions(ModPotionRegistry.getInstance(), ModConfig.getInstance().getConfiguration()));
    }

    @Override
    default void initConfiguration(FMLPreInitializationEvent event) {
        ModConfig.getInstance().init(event);
    }

    @Override
    default void registerEventHandlers() {
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.registerEventHandlers(this));
    }

    @Override
    default void activateRequiredModules() {
        AdventurersArtifacts.ARTIFACTS.forEach(IArtifactModule::activateRequiredInfinityLibModules);
    }

    @Override
    default void registerCapabilities() {

    }

    @Override
    default void registerSounds() {
        AdventurersArtifacts.ARTIFACTS.forEach(IArtifactModule::registerSounds);
    }

    default boolean isShiftKeyPressed() {
        return false;
    }
}
