package com.infinityraider.adventurersartifacts.artifacts.blinkdagger;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class ItemBlinkDagger extends ItemArtifactMeleeWeapon {
    protected ItemBlinkDagger() {
        super(Names.Artifacts.BLINK_DAGGER);
    }

    @Override
    public ModuleBlinkDagger getModule() {
        return ModuleBlinkDagger.getInstance();
    }

    @Override
    public Type getType() {
        return Type.DAGGER;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 1;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if(shift && !player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
            RayTraceResult result = RayTraceHelper.getTargetBlock(player, this.getModule().getRange());
            if(result != null && result.hitVec != null) {
                player.setPositionAndUpdate(result.hitVec.xCoord, result.hitVec.yCoord + 0.1, result.hitVec.zCoord);
                this.getModule().playSound(player);
                stack.setItemDamage(this.getModule().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }
}
