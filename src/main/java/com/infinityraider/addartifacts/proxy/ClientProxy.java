package com.infinityraider.addartifacts.proxy;

import com.infinityraider.addartifacts.AddArtifacts;
import com.infinityraider.addartifacts.artifacts.IArtifactModule;
import com.infinityraider.addartifacts.config.ModConfig;
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
        AddArtifacts.ARTIFACTS.forEach(a -> a.registerEventHandlersClient(this));
    }

    @Override
    public void activateRequiredModules() {
        IProxy.super.activateRequiredModules();
        AddArtifacts.ARTIFACTS.forEach(IArtifactModule::activateRequiredInfinityLibModulesClient);
    }
}
