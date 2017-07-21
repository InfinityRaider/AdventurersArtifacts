package com.infinityraider.adventurersartifacts.artifacts.butterfly;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class ItemButterfly extends ItemArtifactMeleeWeapon {
    protected ItemButterfly() {
        super("butterfly");
    }

    @Override
    public ModuleButterfly getModule() {
        return ModuleButterfly.getInstance();
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
        if (ctrl && !player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
            player.addPotionEffect(new PotionEffect(this.getModule().potionFlutter, this.getModule().getDuration()));
            this.getModule().playSound(player);
            stack.setItemDamage(this.getModule().getCooldown());
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return ctrl;
    }
}
