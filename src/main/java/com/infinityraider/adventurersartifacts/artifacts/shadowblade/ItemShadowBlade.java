package com.infinityraider.adventurersartifacts.artifacts.shadowblade;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ItemMantaStyle;
import com.infinityraider.adventurersartifacts.artifacts.mantastyle.ModuleMantaStyle;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;

public class ItemShadowBlade extends ItemWithModelBase implements IDualWieldedWeapon {
    protected ItemShadowBlade() {
        super("shadow_blade");
        this.setMaxStackSize(1);
        this.setCreativeTab(AdventurersArtifacts.MOD_TAB);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":shadow_blade", "inventory")));
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(stack.getItemDamage() - 1);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getMaxDamage() {
        return ModuleShadowBlade.getInstance().getCooldown() + 1;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (oldStack == null || newStack == null) {
            return true;
        }
        if (oldStack.getItem() != oldStack.getItem()) {
            return true;
        }
        if (newStack.getItem() instanceof ItemMantaStyle) {
            if (newStack.getItemDamage() - oldStack.getItemDamage() <= 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.WEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            if (material == Material.PLANTS || material == Material.VINE ||material == Material.CORAL || material == Material.LEAVES || material == Material.GLASS) {
                return 1.5F;
            } else {
                return 1.0F;
            }
        }
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            if (!player.getEntityWorld().isRemote && stack.getItemDamage() == 0) {
                InvisibilityHandler.getInstance().setInvisible(player);
                stack.setItemDamage(ModuleMantaStyle.getInstance().getCooldown());
            }
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return shift;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return false;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ModuleShadowBlade.getInstance().getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ModuleShadowBlade.getInstance().getAttackSpeed(), 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack != null) {
            list.add(TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.shadow_blade_l1"));
            if (AdventurersArtifacts.proxy.isShiftKeyPressed()) {
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.shadow_blade_l2"));
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.dual_wield"));
            } else {
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.more_info"));
            }
        }
    }
}
