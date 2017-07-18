package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityTargetingHandler {
    private static final EntityTargetingHandler INSTANCE = new EntityTargetingHandler();

    public static EntityTargetingHandler getInstance() {
        return INSTANCE;
    }

    private EntityTargetingHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityTargetingEvent(LivingSetAttackTargetEvent event) {
        EntityLivingBase target = event.getTarget();
        EntityLivingBase attacker = event.getEntityLiving();
        if(target == null || attacker == null || !(target instanceof EntityPlayer)) {
            return;
        }
        if(target.isPotionActive(ModuleShadowBlade.getInstance().potionShadowBlade)) {
            ((EntityLiving) attacker).setAttackTarget(null);
        }
    }
}
