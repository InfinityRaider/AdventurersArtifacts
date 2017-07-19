package com.infinityraider.adventurersartifacts.artifacts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public interface IArtifactModuleItemWithAbility extends IArtifactModule {

    int getCooldown();

    IArtifactModuleWeaponWithAbility setCooldown(int cooldown);

    SoundEvent getSound();

    default void playSound(EntityPlayer player) {
        player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, this.getSound(), SoundCategory.PLAYERS, 0.5F, 1);
    }
}
