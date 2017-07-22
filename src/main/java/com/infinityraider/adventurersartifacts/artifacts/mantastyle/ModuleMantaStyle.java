package com.infinityraider.adventurersartifacts.artifacts.mantastyle;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleMantaStyle extends ArtifactModuleWeaponWithAbility {
    private static final ModuleMantaStyle INSTANCE = new ModuleMantaStyle();

    public static ModuleMantaStyle getInstance() {
        return INSTANCE;
    }

    public final ItemMantaStyle itemMantaStyle;
    public final EntityRegistryEntry<EntityReplicate> entityReplicate;

    private int lifeTime;

    @SuppressWarnings("unchecked")
    private ModuleMantaStyle() {
        super("manta_style");
        this.itemMantaStyle = new ItemMantaStyle();
        this.entityReplicate = new EntityRegistryEntry<>(EntityReplicate.class, "entity.replicate")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityReplicate.RenderFactory.getInstance())
                .setEntityTargetedBy(EntityMob.class);
    }

    public int getDuration() {
        return lifeTime;
    }

    public ModuleMantaStyle setDuration(int time) {
        this.lifeTime = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public ModuleMantaStyle loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("manta style illusions lifetime", this.getName(), 600, 0, 9000000,
                "The lifetime of manta style illusions in ticks"));
        this.setCooldown(modConfig.getInt("manta style illusion creation cooldown", this.getName(), 600, 0, 9000000,
                "The cooldown of creating manta style illusions in ticks"));
        this.setAttackDamage(modConfig.getInt("manta style attack damage", this.getName(), 8, 0, 20,
                "The attack damage of the manta style (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("manta style attack speed", this.getName(), 1.6F, 0, 10,
                "The attack cooldown of the manta style, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleMantaStyle registerMessages(INetworkWrapper networkWrapper) {
        networkWrapper.registerMessage(MessageTrackPlayer.class);
        networkWrapper.registerMessage(MessageTrackPlayerUpdate.class);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleMantaStyle registerEventHandlersClient(IClientProxyBase proxy) {
        proxy.registerEventHandler(PlayerMovementTrackingHandler.getInstance());
        return this;
    }
}
