package com.infinityraider.adventurersartifacts.artifacts;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public interface IArtifactModuleItemWithAbility extends IArtifactModule {

    int getCooldown();

    IArtifactModuleWeaponWithAbility setCooldown(int cooldown);

    SoundEvent getSound();

    default void playSound(Entity entity) {
        this.playSound(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ);
    }

    default void playSound(World world, double x, double y, double z) {
        world.playSound(null, x, y, z, this.getSound(), SoundCategory.PLAYERS, 0.5F, 1);
    }
}
