package com.infinityraider.addartifacts.artifacts.mantastyle;

import com.infinityraider.addartifacts.AddArtifacts;
import com.infinityraider.infinitylib.network.MessageBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSetPlayerPosition extends MessageBase<IMessage> {
    private double x;
    private double y;
    private double z;

    public MessageSetPlayerPosition() {
        super();
    }

    public MessageSetPlayerPosition(double x, double y, double z) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        EntityPlayer player = AddArtifacts.proxy.getClientPlayer();
        if (player != null) {
            ModuleMantaStyle.getInstance().setPlayerPosition(player, this.x, this.y, this.z);
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
