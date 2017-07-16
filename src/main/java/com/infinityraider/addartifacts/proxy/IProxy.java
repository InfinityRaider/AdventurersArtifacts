package com.infinityraider.addartifacts.proxy;

import com.infinityraider.addartifacts.AddArtifacts;
import com.infinityraider.addartifacts.artifacts.IArtifactModule;
import com.infinityraider.addartifacts.config.ModConfig;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IProxy extends IProxyBase {
    @Override
    default void initConfiguration(FMLPreInitializationEvent event) {
        ModConfig.getInstance().init(event);
    }

    @Override
    default void registerEventHandlers() {
        AddArtifacts.ARTIFACTS.forEach(a -> a.registerEventHandlers(this));
    }

    @Override
    default void activateRequiredModules() {
        AddArtifacts.ARTIFACTS.forEach(IArtifactModule::activateRequiredInfinityLibModules);
    }

    @Override
    default void registerCapabilities() {

    }
}
