package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;
import java.util.UUID;

public class InvisibilityHandler {
    private static final InvisibilityHandler INSTANCE = new InvisibilityHandler();

    public static InvisibilityHandler getInstance() {
        return INSTANCE;
    }

    private final Set<UUID> invisiblePlayers;

    private InvisibilityHandler() {
        this.invisiblePlayers = Sets.newHashSet();
    }

    public boolean isInvisible(EntityPlayer player) {
        return this.invisiblePlayers.contains(player.getUniqueID());
    }

    public void setInvisible(EntityPlayer player) {
        invisiblePlayers.add(player.getUniqueID());
        player.addPotionEffect(new PotionEffect(ModuleShadowBlade.getInstance().potionShadowBlade, ModuleShadowBlade.getInstance().getDuration(), 0, false, true));
        new MessageInvisibility(player, true).sendToAll();
    }

    public void reveal(EntityPlayer player) {
        invisiblePlayers.remove(player.getUniqueID());
        player.removePotionEffect((ModuleShadowBlade.getInstance().potionShadowBlade));
        new MessageInvisibility(player, false).sendToAll();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if((!event.getEntity().getEntityWorld().isRemote) && (event.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if(this.isInvisible(player)) {
                if(!player.isPotionActive(ModuleShadowBlade.getInstance().potionShadowBlade)) {
                    this.reveal(player);
                }
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getEntityPlayer().getEntityWorld().isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if(this.isInvisible(player)) {
                this.reveal(player);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onAttack(AttackEntityEvent event) {
        if (!event.getEntity().getEntityWorld().isRemote) {
            EntityPlayer player = event.getEntityPlayer();
            if (this.isInvisible(player)) {
                this.reveal(player);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onDeath(LivingDeathEvent event) {
        if((!event.getEntity().getEntityWorld().isRemote) && (event.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if(this.isInvisible(player)) {
                this.reveal(player);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onUseItem(LivingEntityUseItemEvent event) {
        if((!event.getEntity().getEntityWorld().isRemote) && (event.getEntity() instanceof EntityPlayer)) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if(this.isInvisible(player)) {
                this.reveal(player);
            }
        }
    }
}
