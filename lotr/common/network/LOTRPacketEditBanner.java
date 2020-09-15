/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.mojang.authlib.GameProfile
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.management.PlayerProfileCache
 *  net.minecraft.util.StringUtils
 *  net.minecraft.world.World
 */
package lotr.common.network;

import java.util.List;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRBannerProtection;
import lotr.common.entity.item.LOTRBannerWhitelistEntry;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.fellowship.LOTRFellowship;
import lotr.common.fellowship.LOTRFellowshipProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class LOTRPacketEditBanner
implements IMessage {
    private int bannerID;
    public boolean playerSpecificProtection;
    public boolean selfProtection;
    public float alignmentProtection;
    public int whitelistLength;
    public String[] whitelistSlots;
    public int[] whitelistPerms;
    public int defaultPerms;

    public LOTRPacketEditBanner() {
    }

    public LOTRPacketEditBanner(LOTREntityBanner banner) {
        this.bannerID = banner.getEntityId();
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.bannerID);
        data.writeBoolean(this.playerSpecificProtection);
        data.writeBoolean(this.selfProtection);
        data.writeFloat(this.alignmentProtection);
        data.writeShort(this.whitelistLength);
        boolean sendWhitelist = this.whitelistSlots != null;
        data.writeBoolean(sendWhitelist);
        if (sendWhitelist) {
            data.writeShort(this.whitelistSlots.length);
            for (int index = 0; index < this.whitelistSlots.length; ++index) {
                data.writeShort(index);
                String username = this.whitelistSlots[index];
                if (StringUtils.isNullOrEmpty((String)username)) {
                    data.writeByte(-1);
                    continue;
                }
                byte[] usernameBytes = username.getBytes(Charsets.UTF_8);
                data.writeByte(usernameBytes.length);
                data.writeBytes(usernameBytes);
                data.writeShort(this.whitelistPerms[index]);
            }
            data.writeShort(-1);
        }
        data.writeShort(this.defaultPerms);
    }

    public void fromBytes(ByteBuf data) {
        this.bannerID = data.readInt();
        this.playerSpecificProtection = data.readBoolean();
        this.selfProtection = data.readBoolean();
        this.alignmentProtection = data.readFloat();
        this.whitelistLength = data.readShort();
        boolean sendWhitelist = data.readBoolean();
        if (sendWhitelist) {
            this.whitelistSlots = new String[data.readShort()];
            this.whitelistPerms = new int[this.whitelistSlots.length];
            short index = 0;
            while ((index = data.readShort()) >= 0) {
                String name;
                byte length = data.readByte();
                if (length == -1) {
                    this.whitelistSlots[index] = null;
                    continue;
                }
                ByteBuf usernameBytes = data.readBytes((int)length);
                this.whitelistSlots[index] = name = usernameBytes.toString(Charsets.UTF_8);
                this.whitelistPerms[index] = data.readShort();
            }
        }
        this.defaultPerms = data.readShort();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketEditBanner, IMessage> {
        public IMessage onMessage(LOTRPacketEditBanner packet, MessageContext context) {
            LOTREntityBanner banner;
            EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
            World world = entityplayer.worldObj;
            Entity bEntity = world.getEntityByID(packet.bannerID);
            if (bEntity instanceof LOTREntityBanner && (banner = (LOTREntityBanner)bEntity).canPlayerEditBanner((EntityPlayer)entityplayer)) {
                banner.setPlayerSpecificProtection(packet.playerSpecificProtection);
                banner.setSelfProtection(packet.selfProtection);
                banner.setAlignmentProtection(packet.alignmentProtection);
                banner.resizeWhitelist(packet.whitelistLength);
                if (packet.whitelistSlots != null) {
                    for (int index = 0; index < packet.whitelistSlots.length; ++index) {
                        if (index == 0) continue;
                        String username = packet.whitelistSlots[index];
                        int perms = packet.whitelistPerms[index];
                        if (StringUtils.isNullOrEmpty((String)username)) {
                            banner.whitelistPlayer(index, null);
                            continue;
                        }
                        if (LOTRFellowshipProfile.hasFellowshipCode(username)) {
                            String fsName = LOTRFellowshipProfile.stripFellowshipCode(username);
                            LOTRFellowship fellowship = banner.getPlacersFellowshipByName(fsName);
                            if (fellowship != null) {
                                banner.whitelistFellowship(index, fellowship);
                            }
                        } else {
                            GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152655_a(username);
                            if (profile != null) {
                                banner.whitelistPlayer(index, profile);
                            }
                        }
                        LOTRBannerWhitelistEntry entry = banner.getWhitelistEntry(index);
                        if (entry == null) continue;
                        entry.decodePermBitFlags(perms);
                    }
                }
                List<LOTRBannerProtection.Permission> defaultPerms = LOTRBannerWhitelistEntry.static_decodePermBitFlags(packet.defaultPerms);
                banner.setDefaultPermissions(defaultPerms);
            }
            return null;
        }
    }

}

