package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemShadowBlade extends ItemArtifactMeleeWeapon {
    protected ItemShadowBlade() {
        super("shadow_blade");
    }

    @Override
    public IArtifactModuleWeaponWithAbility getModule() {
        return ModuleShadowBlade.getInstance();
    }

    @Override
    public TYPE getType() {
        return TYPE.SWORD;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 1;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            if (!player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
                InvisibilityHandler.getInstance().setInvisible(player);
                stack.setItemDamage(ModuleMantaStyle.getInstance().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
