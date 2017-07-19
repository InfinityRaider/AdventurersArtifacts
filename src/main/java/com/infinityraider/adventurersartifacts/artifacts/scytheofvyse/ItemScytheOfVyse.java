package com.infinityraider.adventurersartifacts.artifacts.scytheofvyse;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class ItemScytheOfVyse extends ItemArtifactMeleeWeapon {
    public ItemScytheOfVyse() {
        super("scythe_of_vyse");
    }

    @Override
    public ModuleScytheOfVyse getModule() {
        return ModuleScytheOfVyse.getInstance();
    }

    @Override
    public TYPE getType() {
        return TYPE.SCYTHE;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 3;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if(shift && !player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
            RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(player, this.getModule().getRange());
            if(target != null && target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) target.entityHit;
                HexedHandler.getInstance().hexEntity(entity);
                stack.setItemDamage(this.getModule().getCooldown());
            }
        } else if(ctrl) {
            HexedHandler.getInstance().hexEntity(player);
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
