package com.infinityraider.adventurersartifacts.artifacts;

public interface IArtifactModuleWeaponWithAbility extends IArtifactModuleItemWithAbility {

    int getAttackDamage();

    IArtifactModuleWeaponWithAbility setAttackDamage(int damage);

    double getAttackSpeed();

    IArtifactModuleWeaponWithAbility setAttackSpeed(double speed);
}
