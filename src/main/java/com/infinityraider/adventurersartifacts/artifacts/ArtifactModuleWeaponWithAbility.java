package com.infinityraider.adventurersartifacts.artifacts;

import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.util.SoundEvent;

public abstract class ArtifactModuleWeaponWithAbility implements IArtifactModuleItemWithAbility {
    private final String name;

    private SoundEvent sound;

    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    protected  ArtifactModuleWeaponWithAbility(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ArtifactModuleWeaponWithAbility setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }
    public int getAttackDamage() {
        return attackDamage;
    }

    public ArtifactModuleWeaponWithAbility setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public ArtifactModuleWeaponWithAbility setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    public ArtifactModuleWeaponWithAbility activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ArtifactModuleWeaponWithAbility registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}
