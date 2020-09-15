/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.mojang.authlib.GameProfile
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.PlayerProfileCache
 *  net.minecraft.world.World
 */
package lotr.common.network;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.fellowship.LOTRFellowship;
import lotr.common.fellowship.LOTRFellowshipProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class LOTRPacketBannerRequestInvalidName
implements IMessage {
    private int bannerID;
    private int slot;
    private String username;

    public LOTRPacketBannerRequestInvalidName() {
    }

    public LOTRPacketBannerRequestInvalidName(LOTREntityBanner banner, int i, String s) {
        this.bannerID = banner.getEntityId();
        this.slot = i;
        this.username = s;
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.bannerID);
        data.writeShort(this.slot);
        byte[] nameBytes = this.username.getBytes(Charsets.UTF_8);
        data.writeByte(nameBytes.length);
        data.writeBytes(nameBytes);
    }

    public void fromBytes(ByteBuf data) {
        this.bannerID = data.readInt();
        this.slot = data.readShort();
        byte length = data.readByte();
        this.username = data.readBytes((int)length).toString(Charsets.UTF_8);
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketBannerRequestInvalidName, IMessage> {
        public IMessage onMessage(LOTRPacketBannerRequestInvalidName packet, MessageContext context) {
            EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
            World world = entityplayer.worldObj;
            Entity bEntity = world.getEntityByID(packet.bannerID);
            if (bEntity instanceof LOTREntityBanner) {
                LOTREntityBanner banner = (LOTREntityBanner)bEntity;
                String username = packet.username;
                boolean valid = false;
                if (LOTRFellowshipProfile.hasFellowshipCode(username)) {
                    String fsName = LOTRFellowshipProfile.stripFellowshipCode(username);
                    LOTRFellowship fellowship = banner.getPlacersFellowshipByName(fsName);
                    if (fellowship != null) {
                        valid = true;
                    }
                } else {
                    GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(packet.username);
                    if (profile != null) {
                        valid = true;
                    }
                }
                LOTRPacketBannerValidate packetResponse = new LOTRPacketBannerValidate(banner.getEntityId(), packet.slot, packet.username, valid);
                LOTRPacketHandler.networkWrapper.sendTo((IMessage)packetResponse, entityplayer);
            }
            return null;
        }
    }

}

