package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.adventurersartifacts.reference.Names;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemShadowBlade extends ItemArtifactMeleeWeapon {
    protected ItemShadowBlade() {
        super(Names.Artifacts.SHADOW_BLADE);
    }

    @Override
    public ModuleShadowBlade getModule() {
        return ModuleShadowBlade.getInstance();
    }

    @Override
    public Type getType() {
        return Type.SWORD;
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
                this.getModule().playSound(player);
                stack.setItemDamage(this.getModule().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
