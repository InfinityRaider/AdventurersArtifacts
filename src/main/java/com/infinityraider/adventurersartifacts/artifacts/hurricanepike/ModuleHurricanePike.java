package com.infinityraider.adventurersartifacts.artifacts.hurricanepike;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Names;
import net.minecraftforge.common.config.Configuration;

public class ModuleHurricanePike extends ArtifactModuleWeaponWithAbility {
    private static final ModuleHurricanePike INSTANCE = new ModuleHurricanePike();

    public static ModuleHurricanePike getInstance() {
        return INSTANCE;
    }

    public final ItemHurricanePike itemHurricanePike;

    private int strength;
    private int range;

    private ModuleHurricanePike() {
        super(Names.Artifacts.HURRICANE_PIKE);
        this.itemHurricanePike = new ItemHurricanePike();
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
    public ModuleHurricanePike loadConfiguration(Configuration modConfig) {
        this.setRange(modConfig.getInt("hurricane pike strength", this.getName(), 32, 0, 256,
                "The strength of hurricane pike's push"));
        this.setRange(modConfig.getInt("hurricane pike range", this.getName(), 32, 0, 256,
                "The range from which hurricane pike can affect a target"));
        this.setCooldown(modConfig.getInt("hurricane pike cooldown", this.getName(), 300, 0, 9000000,
                "The cooldown of using hurricane pike's push in ticks"));
        this.setAttackDamage(modConfig.getInt("hurricane pike attack damage", this.getName(), 9, 0, 20,
                "The attack damage of the hurricane pike (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("hurricane pike attack speed", this.getName(), 3.0F, 0, 10,
                "The attack cooldown of the hurricane pike, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }
}