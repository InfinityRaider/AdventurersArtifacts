package com.infinityraider.adventurersartifacts.artifacts.butterfly;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;

public class ModuleButterfly implements IArtifactModuleWeaponWithAbility {
    private static final ModuleButterfly INSTANCE = new ModuleButterfly();

    public static ModuleButterfly getInstance() {
        return INSTANCE;
    }

    public final ItemBase itemButterfly;
    public final Potion potionFlutter;

    private SoundEvent sound;

    private int duration;
    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    @SuppressWarnings("unchecked")
    private ModuleButterfly() {
        this.itemButterfly = new ItemButterfly();
        this.potionFlutter = new PotionFlutter();
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    public int getDuration() {
        return duration;
    }

    public ModuleButterfly setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleButterfly setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleButterfly setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleButterfly setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "butterfly";
    }

    @Override
    public ModuleButterfly loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("butterfly flutter duration", this.getName(), 100, 0, 9000000,
                "The duration of butterfly's flutter in ticks"));
        this.setCooldown(modConfig.getInt("butterfly cooldown", this.getName(), 200, 0, 9000000,
                "The cooldown of using butterfly's flutter in ticks"));
        this.setAttackDamage(modConfig.getInt("butterfly attack damage", this.getName(), 9, 0, 20,
                "The attack damage of the butterfly (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("butterfly attack speed", this.getName(), 1.8F, 0, 10,
                "The attack cooldown of the butterfly, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleButterfly registerPotions(ModPotionRegistry registry, Configuration modConfig) {
        registry.registerPotion(this.potionFlutter, this.getName(), modConfig, this.getName());
        return this;
    }

    @Override
    public ModuleButterfly activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ModuleButterfly registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}
