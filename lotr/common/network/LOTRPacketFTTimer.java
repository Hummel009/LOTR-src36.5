/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.player.EntityPlayer
 */
package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketFTTimer
implements IMessage {
    private int timer;

    public LOTRPacketFTTimer() {
    }

    public LOTRPacketFTTimer(int i) {
        this.timer = i;
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.timer);
    }

    public void fromBytes(ByteBuf data) {
        this.timer = data.readInt();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketFTTimer, IMessage> {
        public IMessage onMessage(LOTRPacketFTTimer packet, MessageContext context) {
            EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
            LOTRLevelData.getData(entityplayer).setTimeSinceFT(packet.timer);
            return null;
        }
    }

}

