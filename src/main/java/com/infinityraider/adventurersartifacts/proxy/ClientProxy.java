package com.infinityraider.adventurersartifacts.proxy;

import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.artifacts.IArtifactModule;
import com.infinityraider.adventurersartifacts.config.ModConfig;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class ClientProxy implements IProxy, IClientProxyBase {
    @Override
    public void initConfiguration(FMLPreInitializationEvent event) {
        IProxy.super.initConfiguration(event);
        ModConfig.getInstance().initClientConfigs(event);
    }

    @Override
    public void registerEventHandlers() {
        IProxy.super.registerEventHandlers();
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.registerEventHandlersClient(this));
    }

    @Override
    public void activateRequiredModules() {
        IProxy.super.activateRequiredModules();
        AdventurersArtifacts.ARTIFACTS.forEach(IArtifactModule::activateRequiredInfinityLibModulesClient);
    }
}
