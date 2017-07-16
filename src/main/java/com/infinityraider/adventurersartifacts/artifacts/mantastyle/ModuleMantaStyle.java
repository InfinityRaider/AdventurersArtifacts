package com.infinityraider.adventurersartifacts.artifacts.mantastyle;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModule;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMantaStyle implements IArtifactModule {
    private static final ModuleMantaStyle INSTANCE = new ModuleMantaStyle();

    public static ModuleMantaStyle getInstance() {
        return INSTANCE;
    }

    private int lifeTime;

    public final ItemMantaStyle itemMantaStyle;
    public final EntityRegistryEntry<EntityReplicate> entityReplicate;

    @SuppressWarnings("unchecked")
    private ModuleMantaStyle() {
        itemMantaStyle = new ItemMantaStyle();

        entityReplicate = new EntityRegistryEntry<>(EntityReplicate.class, "entity.replicate")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityReplicate.RenderFactory.getInstance())
                .setEntityTargetedBy(EntityMob.class);
    }

    public int getReplicaLifeTime() {
        return lifeTime;
    }

    public ModuleMantaStyle setReplicaLifeTime(int time) {
        this.lifeTime = time <= 0 ? 0 : time;
        return this;
    }

    public void setPlayerPosition(EntityPlayer player, double x, double y, double z) {
        if(!player.getEntityWorld().isRemote && player instanceof EntityPlayerMP) {
            new MessageSetPlayerPosition(x, y, z).sendTo((EntityPlayerMP) player);
        } else {
            player.setPosition(x, y, z);
            player.prevPosX = x;
            player.prevPosY = y;
            player.prevPosZ = z;
        }
    }

    @Override
    public String getName() {
        return "manta_style";
    }

    @Override
    public ModuleMantaStyle loadConfiguration(Configuration modConfig) {
        this.setReplicaLifeTime(modConfig.getInt("manta style illusions lifetime", this.getName(), 6000, 0, 9000000,
                "The lifetime of manta style illusions in ticks"));

        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleMantaStyle loadClientConfiguration(Configuration modConfig) {
        return this;
    }

    @Override
    public ModuleMantaStyle registerMessages(INetworkWrapper networkWrapper) {
        networkWrapper.registerMessage(MessageSetPlayerPosition.class);
        networkWrapper.registerMessage(MessageSwapPlayerPosition.class);
        networkWrapper.registerMessage(MessageTrackPlayer.class);
        networkWrapper.registerMessage(MessageTrackPlayerUpdate.class);
        return this;
    }

    @Override
    public ModuleMantaStyle registerEventHandlers(IProxyBase proxy) {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleMantaStyle registerEventHandlersClient(IClientProxyBase proxy) {
        proxy.registerEventHandler(PlayerMovementTrackingHandler.getInstance());
        return this;
    }

    @Override
    public IArtifactModule activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IArtifactModule activateRequiredInfinityLibModulesClient() {
        return this;
    }
}
