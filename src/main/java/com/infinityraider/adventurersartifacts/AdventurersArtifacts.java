package com.infinityraider.adventurersartifacts;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModule;
import com.infinityraider.adventurersartifacts.proxy.IProxy;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.adventurersartifacts.registry.ModBlockRegistry;
import com.infinityraider.adventurersartifacts.registry.ModEntityRegistry;
import com.infinityraider.adventurersartifacts.registry.ModItemRegistry;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
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
public class AdventurersArtifacts extends InfinityMod {
    @Mod.Instance(Reference.MOD_ID)
    @SuppressWarnings("unused")
    public static AdventurersArtifacts instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static final CreativeTabs MOD_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public Item getTabIconItem() {
            return Items.LEATHER_BOOTS;
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
        AdventurersArtifacts.ARTIFACTS.forEach(a -> a.registerMessages(wrapper));
    }
}
