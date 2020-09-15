/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.world.World
 */
package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketClientsideGUI
implements IMessage {
    private int guiID;
    private int guiX;
    private int guiY;
    private int guiZ;

    public LOTRPacketClientsideGUI() {
    }

    public LOTRPacketClientsideGUI(int id, int x, int y, int z) {
        this.guiID = id;
        this.guiX = x;
        this.guiY = y;
        this.guiZ = z;
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.guiID);
        data.writeInt(this.guiX);
        data.writeInt(this.guiY);
        data.writeInt(this.guiZ);
    }

    public void fromBytes(ByteBuf data) {
        this.guiID = data.readInt();
        this.guiX = data.readInt();
        this.guiY = data.readInt();
        this.guiZ = data.readInt();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketClientsideGUI, IMessage> {
        public IMessage onMessage(LOTRPacketClientsideGUI packet, MessageContext context) {
            EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
            entityplayer.openGui((Object)LOTRMod.instance, packet.guiID, entityplayer.worldObj, packet.guiX, packet.guiY, packet.guiZ);
            return null;
        }
    }

}

