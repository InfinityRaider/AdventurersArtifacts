package com.infinityraider.addartifacts;

import com.infinityraider.addartifacts.artifacts.IArtifactModule;
import com.infinityraider.addartifacts.proxy.IProxy;
import com.infinityraider.addartifacts.reference.Reference;
import com.infinityraider.addartifacts.registry.ModBlockRegistry;
import com.infinityraider.addartifacts.registry.ModEntityRegistry;
import com.infinityraider.addartifacts.registry.ModItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

import java.util.List;

@Mod(
        modid = Reference.MOD_ID,
        name = Reference.MOD_NAME,
        version = Reference.MOD_VERSION,
        dependencies = "required-after:infinitylib"
)
public class AddArtifacts extends InfinityMod {
    @Mod.Instance(Reference.MOD_ID)
    @SuppressWarnings("unused")
    public static AddArtifacts instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static final CreativeTabs MOD_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return ModItemRegistry.getInstance().itemDebugger;
        }
    };

    public static final List<IArtifactModule> ARTIFACTS = IArtifactModule.modules();

    @Override
    public IProxyBase proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Reference.MOD_ID;
    }

    @Override
    public Object getModBlockRegistry() {
        return ModBlockRegistry.getInstance();
    }

    @Override
    public Object getModItemRegistry() {
        return ModItemRegistry.getInstance();
    }

    @Override
    public Object getModEntityRegistry() {
        return ModEntityRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        AddArtifacts.ARTIFACTS.forEach(a -> a.registerMessages(wrapper));
    }
}
