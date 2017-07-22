package com.infinityraider.adventurersartifacts.artifacts.hurricanepike;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ItemHurricanePike extends ItemArtifactMeleeWeapon {
    protected ItemHurricanePike() {
        super("hurricane_pike");
    }

    @Override
    public ModuleHurricanePike getModule() {
        return ModuleHurricanePike.getInstance();
    }

    @Override
    public TYPE getType() {
        return TYPE.SPEAR;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 2;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if(!player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
            EntityLivingBase pushEntity = null;
            if (shift) {
                RayTraceResult result = RayTraceHelper.getTargetEntityOrBlock(player, this.getModule().getRange());
                if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityLivingBase) {
                    pushEntity = (EntityLivingBase) result.entityHit;
                }
            } else if (ctrl) {
                pushEntity = player;
            }
            if (pushEntity != null) {
                double vX = pushEntity.motionX;
                double vY = pushEntity.motionY;
                double vZ = pushEntity.motionZ;
                Vec3d dir = pushEntity.getLookVec();
                dir.scale(this.getModule().getStrength());
                pushEntity.addVelocity(dir.xCoord, dir.yCoord, dir.zCoord);
                if (pushEntity instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)pushEntity).connection.sendPacket(new SPacketEntityVelocity(pushEntity));
                    pushEntity.velocityChanged = false;
                    pushEntity.motionX = vX;
                    pushEntity.motionY = vY;
                    pushEntity.motionZ = vZ;
                }
                this.getModule().playSound(pushEntity);
                stack.setItemDamage(this.getModule().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}