package com.infinityraider.adventurersartifacts.artifacts.scytheofvyse;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.Map;

public class HexedHandler {
    private static final HexedHandler INSTANCE = new HexedHandler();

    public static HexedHandler getInstance() {
        return INSTANCE;
    }

    private Map<Integer, Map<Integer, Boolean>> hexedLastTick;

    private HexedHandler() {
        this.hexedLastTick = Maps.newHashMap();
    }

    public void hexEntity(EntityLivingBase entity) {
        if(!entity.getEntityWorld().isRemote) {
            entity.addPotionEffect(new PotionEffect(ModuleScytheOfVyse.getInstance().potionHex, ModuleScytheOfVyse.getInstance().getDuration()));
            if(entity instanceof EntityLiving) {
                ((EntityLiving) entity).setAttackTarget(null);
            }
            ModuleScytheOfVyse.getInstance().playSound(entity);
            new MessageHexed(entity.dimension, entity.getEntityId(), true).sendToAll();
        }
    }

    public boolean isHexed(EntityLivingBase entity) {
        return hexedLastTick.containsKey(entity.dimension)
                && hexedLastTick.get(entity.dimension).containsKey(entity.getEntityId())
                && hexedLastTick.get(entity.dimension).get(entity.getEntityId());
    }

    @SideOnly(Side.CLIENT)
    protected void setHexedClient(int dimension, int id, boolean hexed) {
        if(!hexedLastTick.containsKey(dimension)) {
            hexedLastTick.put(dimension, Maps.newHashMap());
        }
        if(hexed) {
            hexedLastTick.get(dimension).put(dimension, true);
        } else {
            if(hexedLastTick.get(dimension).containsKey(id)) {
                hexedLastTick.get(dimension).remove(id);
            }
        }
    }

    protected void onHexedTick(EntityLivingBase entity) {
        if(!entity.getEntityWorld().isRemote) {
            if (!hexedLastTick.containsKey(entity.dimension)) {
                Map<Integer, Boolean> dimMap = Maps.newHashMap();
                dimMap.put(entity.getEntityId(), true);
                hexedLastTick.put(entity.dimension, dimMap);
                new MessageHexed(entity.dimension, entity.getEntityId(), true).sendToAll();
            } else {
                hexedLastTick.get(entity.dimension).put(entity.getEntityId(), true);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onTick(TickEvent.WorldTickEvent event) {
        if(!event.world.isRemote && event.phase == TickEvent.Phase.END) {
            int dimension = event.world.provider.getDimension();
            if(!hexedLastTick.containsKey(dimension)) {
                hexedLastTick.put(dimension, Maps.newHashMap());
            }
            Iterator<Map.Entry<Integer, Boolean>> iterator = hexedLastTick.get(dimension).entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<Integer, Boolean> entry = iterator.next();
                if(!entry.getValue()) {
                    iterator.remove();
                    new MessageHexed(dimension, entry.getKey(), false).sendToAll();
                } else {
                    entry.setValue(false);
                }
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(isHexed(event.getEntityPlayer()) && event.isCancelable()) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingAttack(LivingAttackEvent event) {
        if(event.getSource() instanceof EntityDamageSource && !(event.getSource() instanceof EntityDamageSourceIndirect)) {
            EntityDamageSource source = (EntityDamageSource) event.getSource();
            if(source.getSourceOfDamage() instanceof EntityLivingBase) {
                if(isHexed((EntityLivingBase) source.getSourceOfDamage())) {
                    event.setCanceled(true);
                    event.setResult(Event.Result.DENY);
                }
            }

        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingSetTarget(LivingSetAttackTargetEvent event) {
        if((event.getEntityLiving() instanceof EntityLiving) && isHexed(event.getEntityLiving())) {
            if(event.getTarget() != null) {
                ((EntityLiving) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerAttack(AttackEntityEvent event) {
        if (isHexed(event.getEntityPlayer())) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onUseItem(LivingEntityUseItemEvent.Start event) {
        if(isHexed(event.getEntityLiving())) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            event.setDuration(-1);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onUseItem(LivingEntityUseItemEvent.Tick event) {
        if(isHexed(event.getEntityLiving())) {
            event.setCanceled(true);
            event.setResult(Event.Result.DENY);
            event.setDuration(-1);
        }
    }
}
