package com.infinityraider.adventurersartifacts.artifacts.hurricanepike;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import com.infinityraider.infinitylib.utility.RegisterHelper;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.config.Configuration;

public class ModuleHurricanePike implements IArtifactModuleWeaponWithAbility {
    private static final ModuleHurricanePike INSTANCE = new ModuleHurricanePike();

    public static ModuleHurricanePike getInstance() {
        return INSTANCE;
    }

    public final ItemHurricanePike itemHurricanePike;

    private SoundEvent sound;

    private int strength;
    private int range;
    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    private ModuleHurricanePike() {
        this.itemHurricanePike = new ItemHurricanePike();
    }

    @Override
    public SoundEvent getSound() {
        return this.sound;
    }

    public int getStrength() {
        return strength;
    }

    public ModuleHurricanePike setStrength(int str) {
        this.strength = str <= 0 ? 0 : str;
        return this;
    }

    public int getRange() {
        return range;
    }

    public ModuleHurricanePike setRange(int range) {
        this.range = range <= 0 ? 0 : range;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleHurricanePike setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleHurricanePike setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleHurricanePike setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "hurricane_pike";
    }

    @Override
    public ModuleHurricanePike loadConfiguration(Configuration modConfig) {
        this.setRange(modConfig.getInt("hurricane pike strength", this.getName(), 32, 0, 256,
                "The strength of hurricane pike's push"));
        this.setRange(modConfig.getInt("hurricane pike range", this.getName(), 32, 0, 256,
                "The range from which hurricane pike can affect a target"));
        this.setCooldown(modConfig.getInt("hurricane pike cooldown", this.getName(), 300, 0, 9000000,
                "The cooldown of using hurricane pike's push in ticks"));
        this.setAttackDamage(modConfig.getInt("hurricane pike attack damage", this.getName(), 3, 0, 20,
                "The attack damage of the hurricane pike (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("hurricane pike attack speed", this.getName(), 0.6F, 0, 10,
                "The attack cooldown of the hurricane pike, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleHurricanePike activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }

    @Override
    public ModuleHurricanePike registerSounds() {
        this.sound = RegisterHelper.registerSound(Reference.MOD_ID, this.getName());
        return this;
    }
}