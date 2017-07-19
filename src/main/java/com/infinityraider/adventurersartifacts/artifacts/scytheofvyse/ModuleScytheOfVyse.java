package com.infinityraider.adventurersartifacts.artifacts.scytheofvyse;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IClientProxyBase;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModuleScytheOfVyse implements IArtifactModuleWeaponWithAbility {
    private static final ModuleScytheOfVyse INSTANCE = new ModuleScytheOfVyse();

    public static ModuleScytheOfVyse getInstance() {
        return INSTANCE;
    }

    public final ItemScytheOfVyse itemScytheOfVyse;
    public final PotionHex potionHex;

    private SoundEvent sound;

    private int duration;
    private int cooldown;
    private int range;
    private int attackDamage;
    private double attackSpeed;

    private ModuleScytheOfVyse() {
        this.itemScytheOfVyse = new ItemScytheOfVyse();
        this.potionHex = new PotionHex();
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    public int getDuration() {
        return duration;
    }

    public ModuleScytheOfVyse setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleScytheOfVyse setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    public int getRange() {
        return this.range;
    }

    public ModuleScytheOfVyse setRange(int range) {
        this.range = range < 0 ? 0 : range;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleScytheOfVyse setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleScytheOfVyse setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "scythe_of_vyse";
    }

    @Override
    public ModuleScytheOfVyse loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("scythe of vyse hex duration", this.getName(), 80, 0, 9000000,
                "The duration of scythe of vyse's hex in ticks"));
        this.setCooldown(modConfig.getInt("scythe of vyse cooldown", this.getName(), 400, 0, 9000000,
                "The cooldown of using scythe of vyse's hex in ticks"));
        this.setRange(modConfig.getInt("scythe of vyse range", this.getName(), 16, 0, 128,
                "The maximum range from which the scythe of vyse can hex a target"));
        this.setAttackDamage(modConfig.getInt("scythe of vyse attack damage", this.getName(), 12, 0, 20,
                "The attack damage of the scythe of vyse (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("scythe of vyse attack speed", this.getName(), 3.4F, 0, 10,
                "The attack cooldown of the scythe of vyse, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerMessages(INetworkWrapper networkWrapper) {
        networkWrapper.registerMessage(MessageHexed.class);
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerEventHandlers(IProxyBase proxy) {
        proxy.registerEventHandler(HexedHandler.getInstance());
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModuleScytheOfVyse registerEventHandlersClient(IClientProxyBase proxy) {
        proxy.registerEventHandler(RenderHexedHandler.getInstance());
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerPotions(ModPotionRegistry registry, Configuration modConfig) {
        registry.registerPotion(this.potionHex, this.getName(), modConfig, this.getName());
        return this;
    }

    @Override
    public ModuleScytheOfVyse activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ModuleScytheOfVyse registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}
