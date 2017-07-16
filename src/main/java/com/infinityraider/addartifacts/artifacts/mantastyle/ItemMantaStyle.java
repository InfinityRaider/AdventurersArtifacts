package com.infinityraider.addartifacts.artifacts.mantastyle;

import com.google.common.collect.ImmutableList;
import com.infinityraider.addartifacts.AddArtifacts;
import com.infinityraider.addartifacts.reference.Reference;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemMantaStyle extends ItemWithModelBase implements IDualWieldedWeapon {

    public ItemMantaStyle() {
        super("manta_style");
        this.setMaxStackSize(1);
        this.setCreativeTab(AddArtifacts.MOD_TAB);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if(ctrl) {
            EntityReplicate.getReplicates(player).forEach(EntityReplicate::setDead);
            this.createReplicas(player, player.getHeldItem(EnumHand.MAIN_HAND), player.getHeldItem(EnumHand.OFF_HAND));
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        if(ctrl) {
            return true;
        }
        return false;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":manta_style", "inventory")));
    }

    public boolean createReplicas(EntityPlayer player, ItemStack main, ItemStack off) {
        if(!player.getEntityWorld().isRemote) {
            int amount = 0;
            boolean mainIsManta = main.getItem() instanceof ItemMantaStyle;
            boolean offIsManta = off.getItem() instanceof ItemMantaStyle;
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
                    ModuleMantaStyle.getInstance().setPlayerPosition(player, newX, y, newZ);
                } else {
                    replicas.add(new EntityReplicate(player, ModuleMantaStyle.getInstance().getReplicaLifeTime()));
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
        switch(index) {
            case 2: return 90;
            case 4: return 45;
            case 6: return 30;
            default: return 0;
        }
    }
}
