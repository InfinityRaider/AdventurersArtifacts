package com.infinityraider.adventurersartifacts.artifacts.blinkdagger;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;

public class ModuleBlinkDagger implements IArtifactModuleWeaponWithAbility {
    private static final ModuleBlinkDagger INSTANCE = new ModuleBlinkDagger();

    public static ModuleBlinkDagger getInstance() {
        return INSTANCE;
    }

    public final ItemBlinkDagger itemBlinkDagger;

    private SoundEvent sound;

    private int range;
    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    private ModuleBlinkDagger() {
        this.itemBlinkDagger = new ItemBlinkDagger();
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    public int getRange() {
        return range;
    }

    public ModuleBlinkDagger setRange(int range) {
        this.range = range <= 0 ? 0 : range;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleBlinkDagger setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleBlinkDagger setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleBlinkDagger setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "blink_dagger";
    }

    @Override
    public ModuleBlinkDagger loadConfiguration(Configuration modConfig) {
        this.setRange(modConfig.getInt("blink dagger range", this.getName(), 32, 0, 256,
                "The range of blink dagger's blink"));
        this.setCooldown(modConfig.getInt("blink dagger cooldown", this.getName(), 240, 0, 9000000,
                "The cooldown of using blink dagger's blink in ticks"));
        this.setAttackDamage(modConfig.getInt("blink dagger attack damage", this.getName(), 3, 0, 20,
                "The attack damage of the blink dagger (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("blink dagger attack speed", this.getName(), 0.6F, 0, 10,
                "The attack cooldown of the blink dagger, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleBlinkDagger activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ModuleBlinkDagger registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}
