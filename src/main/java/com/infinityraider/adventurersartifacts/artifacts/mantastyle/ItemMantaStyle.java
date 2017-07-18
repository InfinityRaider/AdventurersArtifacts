package com.infinityraider.adventurersartifacts.artifacts.mantastyle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.reference.Reference;
import com.infinityraider.infinitylib.item.ItemWithModelBase;
import com.infinityraider.infinitylib.modules.dualwield.IDualWieldedWeapon;
import com.infinityraider.infinitylib.utility.RayTraceHelper;
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
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ItemMantaStyle extends ItemWithModelBase implements IDualWieldedWeapon {
    public static final Set<Block> EFFECTIVE_ON = getItemAxeEffectiveBlocks();

    public ItemMantaStyle() {
        super("manta_style");
        this.setMaxStackSize(1);
        this.setCreativeTab(AdventurersArtifacts.MOD_TAB);
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
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
        return ModuleMantaStyle.getInstance().getCooldown() + 1;
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
            if (EFFECTIVE_ON.contains(block) || material == Material.WOOD || material == Material.PLANTS || material == Material.VINE) {
                return 8.0F;
            } else if (material == Material.CORAL || material == Material.LEAVES || material == Material.GLASS) {
                return 1.5F;
            } else {
                return 1.0F;
            }
        }
    }

    @Override
    public void onItemUsed(ItemStack stack, EntityPlayer player, boolean shift, boolean ctrl, EnumHand hand) {
        if (shift) {
            //create replicas
            ItemStack other = player.getItemStackFromSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND);
            boolean otherOk = other == null || (other.getItem() instanceof ItemMantaStyle && other.getItemDamage() == 0);
            if (stack.getItemDamage() == 0 && otherOk) {
                EntityReplicate.getReplicates(player).forEach(EntityReplicate::setDead);
                this.createReplicas(player, player.getHeldItem(EnumHand.MAIN_HAND), player.getHeldItem(EnumHand.OFF_HAND));
                stack.setItemDamage(ModuleMantaStyle.getInstance().getCooldown());
                if (other != null && other.getItem() instanceof ItemMantaStyle) {
                    other.setItemDamage(ModuleMantaStyle.getInstance().getCooldown());
                }
            }
        } else if (ctrl) {
            //swap with replica
            this.swapWithReplica(player);
        }
    }

    @Override
    public boolean onItemAttack(ItemStack stack, EntityPlayer player, Entity e, boolean shift, boolean ctrl, EnumHand hand) {
        return ctrl || shift;
    }

    @Override
    public boolean isEffectiveAgainstShield() {
        return true;
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        return ImmutableList.of(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":manta_style", "inventory")));
    }

    public boolean createReplicas(EntityPlayer player, ItemStack main, ItemStack off) {
        if (!player.getEntityWorld().isRemote) {
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
                    player.setPositionAndUpdate(newX, y, newZ);
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
        switch (index) {
            case 2:
                return 90;
            case 4:
                return 45;
            case 6:
                return 30;
            default:
                return 0;
        }
    }

    public void swapWithReplica(EntityPlayer player) {
        RayTraceResult target = RayTraceHelper.getTargetEntityOrBlock(player, 32, EntityReplicate.class);
        if (target != null && target.typeOfHit == RayTraceResult.Type.ENTITY && target.entityHit instanceof EntityReplicate) {
            EntityReplicate replica = (EntityReplicate) target.entityHit;
            if (replica.getPlayer().getUniqueID().equals(player.getUniqueID())) {
                replica.swapWithPlayer();
            }
        }
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND || slot == EntityEquipmentSlot.OFFHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", ModuleMantaStyle.getInstance().getAttackDamage(), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", ModuleMantaStyle.getInstance().getAttackSpeed(), 0));
        }
        return multimap;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean flag) {
        if (stack != null) {
            list.add(TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.manta_style_l1"));
            if (AdventurersArtifacts.proxy.isShiftKeyPressed()) {
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.manta_style_l2"));
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.manta_style_l3"));
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.manta_style_l4"));
            } else {
                list.add("" + TextFormatting.ITALIC + TextFormatting.DARK_GRAY + TranslationHelper.translateToLocal("adventurers_artifacts.tooltip.more_info"));
            }
        }
    }

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
