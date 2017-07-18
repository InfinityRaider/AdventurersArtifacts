package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.infinitylib.entity.EntityRegistryEntry;
import com.infinityraider.infinitylib.modules.dualwield.ModuleDualWield;
import net.minecraftforge.common.config.Configuration;

public class ModuleEtherealBlade implements IArtifactModuleWeaponWithAbility {
    private static final ModuleEtherealBlade INSTANCE = new ModuleEtherealBlade();

    public static ModuleEtherealBlade getInstance() {
        return INSTANCE;
    }

    public final ItemEtherealBlade itemEtherealBlade;
    public final EntityRegistryEntry<EntityGhostlyRemnant> entityGhostlyRemnant;

    private int duration;
    private int cooldown;
    private int attackDamage;
    private double attackSpeed;

    @SuppressWarnings("unchecked")
    private ModuleEtherealBlade() {
        this.itemEtherealBlade = new ItemEtherealBlade();
        entityGhostlyRemnant = new EntityRegistryEntry<>(EntityGhostlyRemnant.class, "entity.ghostly_remnant")
                .setTrackingDistance(64)
                .setUpdateFrequency(1)
                .setVelocityUpdates(true)
                .setRenderFactory(EntityGhostlyRemnant.RenderFactory.getInstance());
    }

    public int getDuration() {
        return duration;
    }

    public ModuleEtherealBlade setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public ModuleEtherealBlade setCooldown(int time) {
        this.cooldown = time <= 0 ? 0 : time;
        return this;
    }

    @Override
    public int getAttackDamage() {
        return attackDamage;
    }

    @Override
    public ModuleEtherealBlade setAttackDamage(int dmg) {
        this.attackDamage = dmg < 0 ? 0 : dmg;
        return this;
    }

    @Override
    public double getAttackSpeed() {
        return attackSpeed;
    }

    @Override
    public ModuleEtherealBlade setAttackSpeed(double speed) {
        this.attackSpeed = speed < 0 ? 0 : speed;
        return this;
    }

    @Override
    public String getName() {
        return "ethereal_blade";
    }

    @Override
    public ModuleEtherealBlade loadConfiguration(Configuration modConfig) {
        this.setDuration(modConfig.getInt("ethereal blade ghost walk duration", this.getName(), 200, 0, 9000000,
                "The duration of ethereal blade's ghost in ticks"));
        this.setCooldown(modConfig.getInt("ethereal blade cooldown", this.getName(), 400, 0, 9000000,
                "The cooldown of using ethereal blade's ghost in ticks"));
        this.setAttackDamage(modConfig.getInt("ethereal blade attack damage", this.getName(), 8, 0, 20,
                "The attack damage of the ethereal blade (sword damage as reference: wood = 3 | stone = 4 | iron = 5 | diamond = 6"));
        this.setAttackSpeed(modConfig.getFloat("ethereal blade attack speed", this.getName(), 1.8F, 0, 10,
                "The attack cooldown of the ethereal blade, smaller is faster (sword speed as reference: 2.4)"));
        return this;
    }

    @Override
    public ModuleEtherealBlade activateRequiredInfinityLibModules() {
        ModuleDualWield.getInstance().activate();
        return this;
    }
}
