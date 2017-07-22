package com.infinityraider.adventurersartifacts.artifacts.mjollnir;

import com.infinityraider.adventurersartifacts.artifacts.ItemArtifactMeleeWeapon;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;

public class ItemMjollnir extends ItemArtifactMeleeWeapon {
    protected ItemMjollnir() {
        super(Names.Artifacts.MJOLLNIR);
    }

    @Override
    public ModuleMjollnir getModule() {
        return ModuleMjollnir.getInstance();
    }

    @Override
    public Type getType() {
        return Type.HAMMER;
    }

    @Override
    public int getNumberOfTooltipLines() {
        return 1;
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift && !player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
            RayTraceResult result = RayTraceHelper.getTargetEntityOrBlock(player, this.getModule().getRange());
            if (result != null && result.hitVec != null) {
                player.getEntityWorld().addWeatherEffect(new EntityLightningBolt(player.getEntityWorld(), result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, false));
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
