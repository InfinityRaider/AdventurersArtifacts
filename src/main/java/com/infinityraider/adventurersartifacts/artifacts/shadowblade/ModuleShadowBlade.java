package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleShadowBlade implements IArtifactModuleWeaponWithAbility {
    private static final ModuleShadowBlade INSTANCE = new ModuleShadowBlade();

    public static ModuleShadowBlade getInstance() {
        return INSTANCE;
    }

    public final ItemShadowBlade itemShadowBlade;
    public final Potion potionShadowBlade;

    private SoundEvent sound;

    private int duration;
    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    private ModuleShadowBlade() {
        this.itemShadowBlade = new ItemShadowBlade();
        this.potionShadowBlade = new PotionShadowBlade();
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    public int getDuration() {
        return duration;
    }

    public ModuleShadowBlade setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleShadowBlade setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleShadowBlade setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleShadowBlade setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "shadow_blade";
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

    @Override
    public ModuleShadowBlade activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ModuleShadowBlade registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}
