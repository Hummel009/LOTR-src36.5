/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.world.World
 */
package lotr.common.network;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRBannerProtection;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRPlayerData;
import lotr.common.world.map.LOTRCustomWaypoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class LOTRPacketCreateCWP
implements IMessage {
    private String name;

    public LOTRPacketCreateCWP() {
    }

    public LOTRPacketCreateCWP(String s) {
        this.name = s;
    }

    public void toBytes(ByteBuf data) {
        byte[] nameBytes = this.name.getBytes(Charsets.UTF_8);
        data.writeShort(nameBytes.length);
        data.writeBytes(nameBytes);
    }

    public void fromBytes(ByteBuf data) {
        short length = data.readShort();
        this.name = data.readBytes((int)length).toString(Charsets.UTF_8);
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketCreateCWP, IMessage> {
        public IMessage onMessage(LOTRPacketCreateCWP packet, MessageContext context) {
            EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
            World world = entityplayer.worldObj;
            LOTRPlayerData pd = LOTRLevelData.getData((EntityPlayer)entityplayer);
            int numWaypoints = pd.getCustomWaypoints().size();
            if (numWaypoints <= pd.getMaxCustomWaypoints()) {
                IChatComponent[] protectionMessage = new IChatComponent[1];
                boolean protection = LOTRBannerProtection.isProtected(world, (Entity)entityplayer, LOTRBannerProtection.forPlayer_returnMessage((EntityPlayer)entityplayer, LOTRBannerProtection.Permission.FULL, protectionMessage), true);
                if (!protection) {
                    String wpName = LOTRCustomWaypoint.validateCustomName(packet.name);
                    if (wpName != null) {
                        LOTRCustomWaypoint.createForPlayer(wpName, (EntityPlayer)entityplayer);
                    }
                } else {
                    IChatComponent clientMessage = protectionMessage[0];
                    LOTRPacketCWPProtectionMessage packetMessage = new LOTRPacketCWPProtectionMessage(clientMessage);
                    LOTRPacketHandler.networkWrapper.sendTo((IMessage)packetMessage, entityplayer);
                }
            }
            return null;
        }
    }

}

