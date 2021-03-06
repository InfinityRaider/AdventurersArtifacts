package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.adventurersartifacts.reference.Names;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.Optional;

public class ItemEtherealBlade extends ItemArtifactMeleeWeapon {
    protected ItemEtherealBlade() {
        super(Names.Artifacts.ETHEREAL_BLADE);
    }

    @Override
    public ModuleEtherealBlade getModule() {
        return ModuleEtherealBlade.getInstance();
    }

    @Override
    public Type getType() {
        return Type.SWORD;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 3;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            if (!player.getEntityWorld().isRemote) {
                Optional<EntityGhostlyRemnant> ghost = EntityGhostlyRemnant.getGhostlyRemnant(player);
                if(ghost.isPresent()) {
                    ghost.get().setDead();
                } else if(stack.getItemDamage() == 0) {
                    EntityGhostlyRemnant entity = new EntityGhostlyRemnant(player, ModuleEtherealBlade.getInstance().getDuration());
                    player.getEntityWorld().spawnEntityInWorld(entity);
                    this.getModule().playSound(player);
                    stack.setItemDamage(this.getModule().getCooldown());
                }
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
