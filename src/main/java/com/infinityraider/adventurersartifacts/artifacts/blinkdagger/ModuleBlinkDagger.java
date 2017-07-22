package com.infinityraider.adventurersartifacts.artifacts.blinkdagger;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import net.minecraftforge.common.config.Configuration;

public class ModuleBlinkDagger extends ArtifactModuleWeaponWithAbility {
    private static final ModuleBlinkDagger INSTANCE = new ModuleBlinkDagger();

    public static ModuleBlinkDagger getInstance() {
        return INSTANCE;
    }

    public final ItemBlinkDagger itemBlinkDagger;

    private int range;

    private ModuleBlinkDagger() {
        this.itemBlinkDagger = new ItemBlinkDagger();
    }

    public int getRange() {
        return range;
    }

    public ModuleBlinkDagger setRange(int range) {
        this.range = range <= 0 ? 0 : range;
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
}
