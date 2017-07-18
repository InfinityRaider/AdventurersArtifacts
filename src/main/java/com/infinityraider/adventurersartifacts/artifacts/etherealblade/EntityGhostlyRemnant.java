package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.adventurersartifacts.reference.Names;
import com.infinityraider.infinitylib.network.MessageSetEntityDead;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class EntityGhostlyRemnant extends Entity implements IEntityAdditionalSpawnData {
    private static Map<UUID, EntityGhostlyRemnant> ghostMap = new HashMap<>();

    private EntityPlayer player;
    private int lifeTime;

    private double oPosX;
    private double oPosY;
    private double oPosZ;
    private float oYaw;
    private float oPitch;

    public EntityGhostlyRemnant(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {}

    public EntityGhostlyRemnant(EntityPlayer player, int lifeTime) {
        this(player.getEntityWorld());
        this.lifeTime = lifeTime;
        this.player = player;
        this.oPosX = player.posX;
        this.oPosY = player.posY;
        this.oPosZ = player.posZ;
        this.oYaw = player.rotationYaw;
        this.oPitch = player.rotationPitch;
        this.resetLocation();
        this.addToGhostMap();
    }

    public void resetLocation() {
        this.posX = this.oPosX;
        this.posY = this.oPosY;
        this.posZ = this.oPosZ;
        this.prevPosX = this.oPosX;
        this.prevPosY = this.oPosY;
        this.prevPosZ = this.oPosZ;
        this.rotationYaw = this.oYaw;
        this.rotationPitch = this.oPitch;
        this.prevRotationYaw = this.oYaw;
        this.prevRotationPitch = this.oPitch;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
    }

    public static Optional<EntityGhostlyRemnant> getGhostlyRemnant(EntityPlayer player) {
        if(!player.getEntityWorld().isRemote && player instanceof EntityPlayerMP) {
            if(ghostMap.containsKey(player.getUniqueID())) {
                return Optional.of(ghostMap.get(player.getUniqueID()));
            }
        }
        return Optional.empty();
    }

    protected void materializePlayer() {
        if(!this.getEntityWorld().isRemote && this.getPlayer() != null) {
            this.getPlayer().setPositionAndUpdate(this.posX, this.posY, this.posZ);
        }
    }

    protected void addToGhostMap() {
        if(!this.getEntityWorld().isRemote && this.getPlayer() instanceof EntityPlayerMP) {
            Optional<EntityGhostlyRemnant> ghost = getGhostlyRemnant(player);
            if(ghost.isPresent()) {
                ghost.get().setDead();
            }
            ghostMap.put(player.getUniqueID(), this);
        }
    }

    protected void removeFromGhostMap() {
        if(!this.getEntityWorld().isRemote && this.getPlayer() instanceof EntityPlayerMP) {
            if (ghostMap.containsKey(this.getPlayer().getUniqueID())) {
                //impossible, but hey, better safe than sorry
                ghostMap.remove(this.getPlayer().getUniqueID());
            }
        }
    }

    protected void initEntityAI() {}

    public EntityPlayer getPlayer() {
        return player;
    }

    @SideOnly(Side.CLIENT)
    public AbstractClientPlayer getClientPlayer() {
        if(this.getPlayer() instanceof AbstractClientPlayer) {
            return (AbstractClientPlayer) this.getPlayer();
        }
        return (AbstractClientPlayer) AdventurersArtifacts.proxy.getClientPlayer();
    }

    @Override
    public void onEntityUpdate() {
        this.resetLocation();
        if(!this.getEntityWorld().isRemote) {
            this.lifeTime--;
            if (this.getPlayer() == null || this.getPlayer().isDead || this.lifeTime < 0) {
                this.setDead();
            }
        }
    }

    @Override
    public void setDead() {
        if(!this.getEntityWorld().isRemote) {
            this.removeFromGhostMap();
            this.materializePlayer();
            new MessageSetEntityDead(this).sendToAll();
        }
        super.setDead();
    }

    @Override
    public void writeSpawnData(ByteBuf data) {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeEntityToNBT(tag);
        ByteBufUtils.writeTag(data, tag);
    }

    @Override
    public void readSpawnData(ByteBuf data) {
        NBTTagCompound tag = ByteBufUtils.readTag(data);
        this.readEntityFromNBT(tag);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        if(this.getPlayer() != null) {
            tag.setString(Names.NBT.PLAYER, this.getPlayer().getUniqueID().toString());
        }
        tag.setInteger(Names.NBT.COUNT, this.lifeTime);
        tag.setDouble(Names.NBT.X, this.oPosX);
        tag.setDouble(Names.NBT.Y, this.oPosY);
        tag.setDouble(Names.NBT.Z, this.oPosZ);
        tag.setFloat(Names.NBT.YAW, this.oYaw);
        tag.setFloat(Names.NBT.PITCH, this.oPitch);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        if(tag.hasKey(Names.NBT.PLAYER)) {
            this.player = this.getEntityWorld().getPlayerEntityByUUID(UUID.fromString(tag.getString(Names.NBT.PLAYER)));
        }
        this.lifeTime = tag.getInteger(Names.NBT.COUNT);
        this.oPosX = tag.getDouble(Names.NBT.X);
        this.oPosY = tag.getDouble(Names.NBT.Y);
        this.oPosZ = tag.getDouble(Names.NBT.Z);
        this.oYaw = tag.getFloat(Names.NBT.YAW);
        this.oPitch = tag.getFloat(Names.NBT.PITCH);
        this.resetLocation();
    }

    //Entity overrides to make sure this entity is invulnerable
    //---------------------------------------------------------
    @Override
    protected void setSize(float width, float height) {}

    @Override
    protected void setRotation(float yaw, float pitch) {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    @Override
    public void setPosition(double x, double y, double z) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void setAngles(float yaw, float pitch) {}

    @Override
    protected void setOnFireFromLava() {}

    @Override
    public void setFire(int seconds) {}

    @Override
    public void extinguish() {}

    @Override
    public void resetPositionToBB() {}

    @Override
    public boolean isSilent() {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {}

    @Override
    public void moveEntity(double x, double y, double z) {}

    @Override
    protected void dealFireDamage(int amount) {}

    @Override
    public void fall(float distance, float damageMultiplier) {}

    @Override
    public boolean isWet() {
        return false;
    }

    @Override
    public boolean handleWaterMovement() {
        return false;
    }

    @Override
    public void spawnRunningParticles() { }

    @Override
    protected void createRunningParticles() {}

    @Override
    public void moveRelative(float strafe, float forward, float friction) {}

    @Override
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void moveToBlockPosAndAngles(BlockPos pos, float rotationYawIn, float rotationPitchIn) {}

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {}

    @Override
    public void applyEntityCollision(Entity entityIn) {}

    @Override
    public void addVelocity(double x, double y, double z) {}

    @Override
    protected void setBeenAttacked() {}

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }

    @Override
    public boolean startRiding(Entity entityIn, boolean force) {
        return false;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public void setPortal(BlockPos pos) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z) { }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        return !player.isSpectator() && player != this.getPlayer();
    }

    @Override
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {}

    @Override
    protected boolean pushOutOfBlocks(double x, double y, double z) {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source) {
        return true;
    }

    @Override
    public void copyLocationAndAnglesFrom(Entity entityIn) {}

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public void setPositionAndUpdate(double x, double y, double z) {}

    @Override
    public boolean getAlwaysRenderNameTag() {
        return false;
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, @Nullable ItemStack stack, EnumHand hand) {
        return EnumActionResult.FAIL;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender() {
        return this.getAlwaysRenderNameTag();
    }

    @Override

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    public static class RenderFactory implements IRenderFactory<EntityGhostlyRemnant> {
        private static final RenderFactory INSTANCE = new RenderFactory();

        public static RenderFactory getInstance() {
            return INSTANCE;
        }

        private RenderFactory() {}

        @Override
        @SideOnly(Side.CLIENT)
        public Render<? super EntityGhostlyRemnant> createRenderFor(RenderManager manager) {
            return new RenderEntityGhostlyRemnant(manager);
        }
    }
}
