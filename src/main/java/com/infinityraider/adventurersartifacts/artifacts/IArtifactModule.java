package com.infinityraider.adventurersartifacts.artifacts;

import com.google.common.collect.ImmutableList;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IArtifactModule {
    static List<IArtifactModule> modules() {
        return ImmutableList.of(
                ModuleMantaStyle.getInstance()
        );
    }

    String getName();

    IArtifactModule loadConfiguration(Configuration modConfig);

    @SideOnly(Side.CLIENT)
    IArtifactModule loadClientConfiguration(Configuration modConfig);

    IArtifactModule registerMessages(INetworkWrapper networkWrapper);

    IArtifactModule registerEventHandlers(IProxyBase proxy);

    @SideOnly(Side.CLIENT)
    IArtifactModule registerEventHandlersClient(IClientProxyBase proxy);

    IArtifactModule activateRequiredInfinityLibModules();

    @SideOnly(Side.CLIENT)
    IArtifactModule activateRequiredInfinityLibModulesClient();
}
