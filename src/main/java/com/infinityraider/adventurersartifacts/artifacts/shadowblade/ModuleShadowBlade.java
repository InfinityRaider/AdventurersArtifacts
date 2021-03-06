package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.*;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleShadowBlade extends ArtifactModuleWeaponWithAbility {
    private static final ModuleShadowBlade INSTANCE = new ModuleShadowBlade();

    public static ModuleShadowBlade getInstance() {
        return INSTANCE;
    }

    public final ItemShadowBlade itemShadowBlade;
    public final Potion potionShadowBlade;

    private int duration;

    private ModuleShadowBlade() {
        super(Names.Artifacts.SHADOW_BLADE);
        this.itemShadowBlade = new ItemShadowBlade();
        this.potionShadowBlade = new PotionShadowBlade();
    }

    public int getDuration() {
        return duration;
    }

    public ModuleShadowBlade setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public ModuleShadowBlade loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("shadow blade invisibility duration", this.getName(), 400, 0, 9000000,
                "The duration of shadow blade's invisibility in ticks"));
        this.setCooldown(modConfig.getInt("shadow blade cooldown", this.getName(), 600, 0, 9000000,
                "The cooldown of using shadow blade's invisibility in ticks"));
        this.setAttackDamage(modConfig.getInt("shadow blade attack damage", this.getName(), 9, 0, 20,
                "The attack damage of the shadow blade (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("shadow blade attack speed", this.getName(), 2.0F, 0, 10,
                "The attack cooldown of the shadow blade, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleShadowBlade registerMessages(INetworkWrapper networkWrapper) {
        networkWrapper.registerMessage(MessageInvisibility.class);
        return this;
    }

    @Override
    public ModuleShadowBlade registerEventHandlers(IProxyBase proxy) {
        proxy.registerEventHandler(EntityTargetingHandler.getInstance());
        proxy.registerEventHandler(InvisibilityHandler.getInstance());
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleShadowBlade registerEventHandlersClient(IClientProxyBase proxy) {
        proxy.registerEventHandler(RenderPlayerHandler.getInstance());
        return this;
    }

    @Override
    public ModuleShadowBlade registerPotions(ModPotionRegistry registry, Configuration modConfig) {
        registry.registerPotion(this.potionShadowBlade, this.getName(), modConfig, this.getName());
        return this;
    }
}
