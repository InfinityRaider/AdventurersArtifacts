package com.infinityraider.adventurersartifacts.artifacts.scytheofvyse;

import com.google.common.collect.*;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.infinitylib.modules.entitylistener.IEntityLeaveOrJoinWorldListener;
import com.infinityraider.infinitylib.modules.entitylistener.ModuleEntityListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

public class DropScytheHandler implements IEntityLeaveOrJoinWorldListener {
    private static final DropScytheHandler INSTANCE = new DropScytheHandler();

    public static DropScytheHandler getInstance() {
        return INSTANCE;
    }

    private Map<Integer, List<Triple<Double, Double, Double>>> pigs;

    private DropScytheHandler() {
        this.pigs = Maps.newHashMap();
        ModuleEntityListener.getInstance().registerListener(this);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityStruckByLightning(EntityStruckByLightningEvent event) {
        if(event.getEntity() instanceof EntityPig && !event.getEntity().getEntityWorld().isRemote) {
            EntityPig pig = (EntityPig) event.getEntity();
            if(!pigs.containsKey(pig.dimension)) {
                pigs.put(pig.dimension, Lists.newArrayList());
            }
            pigs.get(pig.dimension).add(Triple.of(pig.posX, pig.posY, pig.posZ));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingDeathEvent(LivingDeathEvent event) {
        if(event.getEntity() instanceof EntityPigZombie && !event.getEntity().getEntityWorld().isRemote &&  event.getSource() instanceof EntityDamageSource) {
            EntityDamageSource source = (EntityDamageSource) event.getSource();
            NBTTagCompound tag = event.getEntity().getEntityData();
            if(source.getEntity() instanceof EntityPlayer && tag.hasKey(Names.NBT.LIGHTNING) && tag.getBoolean(Names.NBT.LIGHTNING)) {
                EntityPlayer player = (EntityPlayer) source.getEntity();
                if(player.isRiding() && player.getRidingEntity() instanceof EntityPig) {
                    ItemStack stack = new ItemStack(ModuleScytheOfVyse.getInstance().itemScytheOfVyse, 1);
                    EntityItem item = new EntityItem(event.getEntity().getEntityWorld(), event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, stack);
                    event.getEntity().getEntityWorld().spawnEntityInWorld(item);
                }
            }
        }
    }

    @Override
    public void onEntityJoinWorld(Entity entity) {
        if(entity instanceof EntityPigZombie && !entity.getEntityWorld().isRemote) {
            NBTTagCompound tag = entity.getEntityData();
            if(!tag.hasKey(Names.NBT.LIGHTNING)) {
                boolean flag = false;
                if(pigs.containsKey(entity.dimension)) {
                    for (Triple<Double, Double, Double> coords : pigs.get(entity.dimension)) {
                        if (coords.getLeft() == entity.posX && coords.getMiddle() == entity.posY && coords.getRight() == entity.posZ) {
                            flag = true;
                            break;
                        }
                    }
                }
                tag.setBoolean(Names.NBT.LIGHTNING, flag);
            }
        }
    }

    @Override
    public void onEntityLeaveWorld(Entity entity) {
        if(entity instanceof EntityPig && !entity.getEntityWorld().isRemote) {
            if(pigs.containsKey(entity.dimension)) {
                Iterator<Triple<Double, Double, Double>> iterator = pigs.get(entity.dimension).iterator();
                while(iterator.hasNext()) {
                    Triple<Double, Double, Double> coords = iterator.next();
                    if(coords.getLeft() == entity.posX && coords.getMiddle() == entity.posY && coords.getRight() == entity.posZ) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }
}
