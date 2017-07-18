package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemEtherealBlade extends ItemArtifactMeleeWeapon {
    protected ItemEtherealBlade() {
        super("ethereal_blade");
    }

    @Override
    public IArtifactModuleWeaponWithAbility getModule() {
        return ModuleEtherealBlade.getInstance();
    }

    @Override
    public TYPE getType() {
        return TYPE.SWORD;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 3;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            if (!player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {

                stack.setItemDamage(this.getModule().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
