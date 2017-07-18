package com.infinityraider.adventurersartifacts.artifacts.etherealblade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
    public void doRender(EntityGhostlyRemnant replica, double x, double y, double z, float entityYaw, float partialTicks) {
        //TODO: render ghostly image of the player
        AbstractClientPlayer player = replica.getClientPlayer();
        Render<? extends AbstractClientPlayer> renderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(player);
        if(renderer instanceof RenderPlayer) {
            RenderPlayer renderPlayer = (RenderPlayer) renderer;
            renderPlayer.doRender(player, x, y, z, entityYaw, partialTicks);
        }
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
