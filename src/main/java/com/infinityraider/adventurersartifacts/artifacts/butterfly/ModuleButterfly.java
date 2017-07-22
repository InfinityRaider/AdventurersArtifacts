package com.infinityraider.adventurersartifacts.artifacts.butterfly;

import com.infinityraider.adventurersartifacts.artifacts.ArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.adventurersartifacts.registry.ModPotionRegistry;
import com.infinityraider.infinitylib.item.ItemBase;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

public class ModuleButterfly extends ArtifactModuleWeaponWithAbility {
    private static final ModuleButterfly INSTANCE = new ModuleButterfly();

    public static ModuleButterfly getInstance() {
        return INSTANCE;
    }

    public final ItemBase itemButterfly;
    public final Potion potionFlutter;

    private int duration;

    private ModuleButterfly() {
        super(Names.Artifacts.BUTTERFLY);
        this.itemButterfly = new ItemButterfly();
        this.potionFlutter = new PotionFlutter();
    }

    public int getDuration() {
        return duration;
    }

    public ModuleButterfly setDuration(int time) {
        this.duration = time <= 0 ? 0 : time;
        return this;
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
}
