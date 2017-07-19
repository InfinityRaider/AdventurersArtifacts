package com.infinityraider.adventurersartifacts.artifacts.mantastyle;

import com.infinityraider.adventurersartifacts.artifacts.IArtifactModuleWeaponWithAbility;
import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

import java.util.*;

public class ItemMantaStyle extends ItemArtifactMeleeWeapon {
    protected ItemMantaStyle() {
        super("manta_style");
    }

    @Override
    public IArtifactModuleWeaponWithAbility getModule() {
        return ModuleMantaStyle.getInstance();
    }

    @Override
    public TYPE getType() {
        return TYPE.AXE;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 3;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            //create replicas
            ItemStack other = player.getItemStackFromSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND);
            boolean otherOk = other == null || (other.getItem() instanceof ItemMantaStyle && other.getItemDamage() == 0);
            if (stack.getItemDamage() == 0 && otherOk) {
                EntityReplicate.getReplicates(player).forEach(EntityReplicate::setDead);
                this.createReplicas(player, player.getHeldItem(EnumHand.MAIN_HAND), player.getHeldItem(EnumHand.OFF_HAND));
                this.getModule().playSound(player);
                stack.setItemDamage(this.getModule().getCooldown());
                if (other != null && other.getItem() instanceof ItemMantaStyle) {
                    other.setItemDamage(this.getModule().getCooldown());
                }
            }
        } else if (ctrl) {
            //swap with replica
            this.swapWithReplica(player);
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return ctrl || shift;
    }

    @Override
    public boolean isEffectiveAgainstShield() {
        return true;
    }

    public boolean createReplicas(EntityPlayer player, ItemStack main, ItemStack off) {
        if (!player.getEntityWorld().isRemote) {
            int amount = 0;
            boolean mainIsManta = main.getItem() instanceof ItemMantaStyle;
            boolean offIsManta = off != null && off.getItem() instanceof ItemMantaStyle;
            amount += mainIsManta ? 1 : 0;
            amount += offIsManta ? 1 : 0;
            if (amount <= 0) {
                return false;
            }
            double x = player.posX;
            double y = player.posY;
            double z = player.posZ;
            float yaw = player.rotationYaw;
            double radius = 2.5F;
            List<EntityReplicate> replicas = new ArrayList<>();
            for (int i = 0; i < (amount + 1); i++) {
                double angle = (yaw + (i * 360 / (amount + 1)) + offsetForIndex(i)) % 360;
                double newX = x + radius * Math.cos(Math.toRadians(angle));
                double newZ = z + radius * Math.sin(Math.toRadians(angle));
                EntityLivingBase entity;
                if (i == 0) {
                    entity = player;
                    player.setPositionAndUpdate(newX, y, newZ);
                } else {
                    replicas.add(new EntityReplicate(player, ModuleMantaStyle.getInstance().getDuration()));
                    entity = replicas.get(i - 1);
                    player.getEntityWorld().spawnEntityInWorld(entity);
                }
                entity.setPosition(newX, y, newZ);
            }
            int swap = player.getRNG().nextInt(replicas.size() + 1);
            if (swap < replicas.size()) {
                replicas.get(swap).swapWithPlayer();
            }
        }
        return false;
    }

    protected int offsetForIndex(int index) {
        switch (index) {
            case 2:
                return 90;
            case 4:
                return 45;
            case 6:
                return 30;
            default:
                return 0;
        }
    }

    public void swapWithReplica(EntityPlayer player) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(player, 32, EntityReplicate.class);
        if (target != null && target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityReplicate) {
            EntityReplicate replica = (EntityReplicate) target.entityHit;
            if (replica.getPlayer().getUniqueID().equals(player.getUniqueID())) {
                replica.swapWithPlayer();
            }
        }
    }
}
