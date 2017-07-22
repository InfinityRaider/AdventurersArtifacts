package com.infinityraider.adventurersartifacts.artifacts.mjollnir;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Names;
import net.minecraftforge.common.config.Configuration;

public class ModuleMjollnir extends ArtifactModuleWeaponWithAbility {
    private static final ModuleMjollnir INSTANCE = new ModuleMjollnir();

    public static ModuleMjollnir getInstance() {
        return INSTANCE;
    }

    public final ItemMjollnir itemMjollnir;

    private int range;

    private ModuleMjollnir() {
        super(Names.Artifacts.MJOLLNIR);
        this.itemMjollnir = new ItemMjollnir();
    }

    public int getRange() {
        return range;
    }

    public ModuleMjollnir setRange(int range) {
        this.range = range <= 0 ? 0 : range;
        return this;
    }

    @Override
    public ModuleMjollnir loadConfiguration(Configuration modConfig) {
        this.setRange(modConfig.getInt("mjollnir range", this.getName(), 32, 0, 256,
                "The range of mjollnir's invoke lightning"));
        this.setCooldown(modConfig.getInt("mjollnir cooldown", this.getName(), 400, 0, 9000000,
                "The cooldown of using mjollnir's invoke lightning in ticks"));
        this.setAttackDamage(modConfig.getInt("mjollnir attack damage", this.getName(), 15, 0, 20,
                "The attack damage of the mjollnir (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("mjollnir attack speed", this.getName(), 4.0F, 0, 10,
                "The attack cooldown of the mjollnir, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }
}