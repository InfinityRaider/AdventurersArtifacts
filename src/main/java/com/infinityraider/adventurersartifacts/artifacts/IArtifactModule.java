package com.infinityraider.adventurersartifacts.artifacts;

import com.google.common.collect.ImmutableList;
import com.infinityraider.adventurersartifacts.artifacts.etherealblade.ModuleEtherealBlade;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.adventurersartifacts.artifacts.shadowblade.ModuleShadowBlade;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IArtifactModule {
    static List<IArtifactModule> modules() {
        return ImmutableList.of(
                ModuleEtherealBlade.getInstance(),
                ModuleMantaStyle.getInstance(),
                ModuleShadowBlade.getInstance()
        );
    }

    String getName();

    default IArtifactModule loadConfiguration(Configuration modConfig) {
        return this;
    }

    @SideOnly(Side.CLIENT)
    default IArtifactModule loadClientConfiguration(Configuration modConfig) {
        return this;
    }

    default IArtifactModule registerMessages(INetworkWrapper networkWrapper) {
        return this;
    }

    default IArtifactModule registerEventHandlers(IProxyBase proxy) {
        return this;
    }

    @SideOnly(Side.CLIENT)
    default IArtifactModule registerEventHandlersClient(IClientProxyBase proxy) {
        return this;
    }

    default IArtifactModule registerPotions(ModPotionRegistry registry, Configuration modConfig) {
        return this;
    }

    default IArtifactModule activateRequiredInfinityLibModules() {
        return this;
    }

    @SideOnly(Side.CLIENT)
    default IArtifactModule activateRequiredInfinityLibModulesClient() {
        return this;
    }

    default IArtifactModule registerSounds() {
        return this;
    }
}
