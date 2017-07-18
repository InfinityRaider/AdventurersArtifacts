package com.infinityraider.adventurersartifacts.artifacts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import com.infinityraider.infinitylib.utility.TranslationHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class ItemArtifactMeleeWeapon extends ItemWithModelBase implements IDualWieldedWeapon {
    public static final Set<Block> EFFECTIVE_ON = getItemAxeEffectiveBlocks();

    public enum TYPE {
        SWORD,
        AXE,
        HAMMER,
        SPEAR
    }

    public ItemArtifactMeleeWeapon(String name) {
        super(name);
        this.setMaxStackSize(1);
        this.setCreativeTab(AdventurersArtifacts.MOD_TAB);
    }

    public abstract IArtifactModuleWeaponWithAbility getModule();

    public abstract TYPE getType();

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
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
        return this.getModule().getCooldown() + 1;
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
        if (newStack.getItem() == this) {
            if (newStack.getItemDamage() - oldStack.getItemDamage() <= 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        switch(this.getType()) {
            case SWORD:
                if (block == Blocks.WEB) {
                    return 15.0F;
                } else {
                    if (material == Material.PLANTS || material == Material.VINE ||material == Material.CORAL || material == Material.LEAVES || material == Material.GLASS) {
                        return 1.5F;
                    } else {
                        return 1.0F;
                    }
                }
            case AXE:
                if (EFFECTIVE_ON.contains(block) || material == Material.WOOD || material == Material.PLANTS || material == Material.VINE) {
                    return 8.0F;
                } else if (material == Material.CORAL || material == Material.LEAVES || material == Material.GLASS) {
                    return 1.5F;
                } else {
                    return 1.0F;
                }
            case HAMMER:

            case SPEAR:

            default:
                return 1.0F;
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getModule().getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", this.getModule().getAttackSpeed(), 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack != null) {
            list.add(TranslationHelper.translateToLocal("adventurers_artifacts.tooltip." + this.getInternalName() + "_main"));
            if (AdventurersArtifacts.proxy.isShiftKeyPressed()) {
                int lines = this.getNumberOfTooltipLines();
                for(int i = 1; i <= lines; i++) {
                    list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip." + this.getInternalName() + "_help_l" + i));
                }
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.dual_wield"));
            } else {
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.more_info"));
            }
        }
    }

    public abstract int getNumberOfTooltipLines();

    @SuppressWarnings("unchecked")
    private static Set<Block> getItemAxeEffectiveBlocks() {
        Field[] fields = ItemAxe.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(Set.class) && Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                try {
                    return (Set<Block>) field.get(null);
                } catch (Exception e) {
                    break;
                }
            }
        }
        //failed to grab set from ItemAxe, oh well... just create a new one
        return Sets.newHashSet(
                Blocks.PLANKS,
                Blocks.BOOKSHELF,
                Blocks.LOG,
                Blocks.LOG2,
                Blocks.CHEST,
                Blocks.PUMPKIN,
                Blocks.LIT_PUMPKIN,
                Blocks.MELON_BLOCK,
                Blocks.LADDER,
                Blocks.WOODEN_BUTTON,
                Blocks.WOODEN_PRESSURE_PLATE);
    }

}
