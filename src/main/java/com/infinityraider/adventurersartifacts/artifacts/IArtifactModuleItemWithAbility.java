package com.infinityraider.adventurersartifacts.artifacts;

public interface IArtifactModuleItemWithAbility extends IArtifactModule {

    int getCooldown();

    IArtifactModuleWeaponWithAbility setCooldown(int cooldown);

}
