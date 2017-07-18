package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import com.infinityraider.adventurersartifacts.AdventurersArtifacts;
import com.infinityraider.infinitylib.reference.Constants;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityGhostlyRemnant extends Render<EntityGhostlyRemnant> {
    private final ModelPlayer model;


    public RenderEntityGhostlyRemnant(RenderManager renderManager) {
        super(renderManager);
        this.model = new ModelPlayer(0.0F, true);
    }

    @Override
    public boolean shouldRender(EntityGhostlyRemnant entity, ICamera camera, double camX, double camY, double camZ) {
        EntityPlayer player = entity.getPlayer();
        if(player != null) {
            EntityPlayer local = AdventurersArtifacts.proxy.getClientPlayer();
            return local == player || local.isSpectator();
        }
        return false;
    }

    @Override
    public void doRender(EntityGhostlyRemnant remnant, double x, double y, double z, float entityYaw, float partialTicks) {

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        //geometric operations
        GlStateManager.translate(x, y + 1.3, z);
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.rotate(remnant.rotationYaw, 0, 1, 0);
        GlStateManager.scale(Constants.UNIT, Constants.UNIT, Constants.UNIT);

        //apply transparency and color
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569F);

        //do the rendering
        this.getModel().isRiding = false;
        this.getModel().isChild = false;
        this.bindEntityTexture(remnant);
        this.getModel().render(remnant, 0, 0, 0, 0, 0, 1);

        //clean transparency and color
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.depthMask(true);

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public ModelPlayer getModel() {
        return this.model;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGhostlyRemnant entity) {
        AbstractClientPlayer player = entity.getClientPlayer();
        return player.getLocationSkin();
    }
}
