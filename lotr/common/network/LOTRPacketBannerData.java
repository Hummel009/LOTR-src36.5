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
import lotr.common.LOTRMod;
import lotr.common.entity.item.LOTRBannerWhitelistEntry;
import lotr.common.entity.item.LOTREntityBanner;
import lotr.common.fellowship.LOTRFellowshipProfile;
import net.minecraft.entity.Entity;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class LOTRPacketBannerData
implements IMessage {
    private int entityID;
    private boolean openGui;
    public boolean playerSpecificProtection;
    public boolean selfProtection;
    public boolean structureProtection;
    public int customRange;
    public float alignmentProtection;
    public int whitelistLength;
    public String[] whitelistSlots;
    public int[] whitelistPerms;
    public int defaultPerms;

    public LOTRPacketBannerData() {
    }

    public LOTRPacketBannerData(int id, boolean flag) {
        this.entityID = id;
        this.openGui = flag;
    }

    public void toBytes(ByteBuf data) {
        data.writeInt(this.entityID);
        data.writeBoolean(this.openGui);
        data.writeBoolean(this.playerSpecificProtection);
        data.writeBoolean(this.selfProtection);
        data.writeBoolean(this.structureProtection);
        data.writeShort(this.customRange);
        data.writeFloat(this.alignmentProtection);
        data.writeShort(this.whitelistLength);
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
        data.writeShort(this.defaultPerms);
    }

    public void fromBytes(ByteBuf data) {
        this.entityID = data.readInt();
        this.openGui = data.readBoolean();
        this.playerSpecificProtection = data.readBoolean();
        this.selfProtection = data.readBoolean();
        this.structureProtection = data.readBoolean();
        this.customRange = data.readShort();
        this.alignmentProtection = data.readFloat();
        this.whitelistLength = data.readShort();
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
        this.defaultPerms = data.readShort();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketBannerData, IMessage> {
        public IMessage onMessage(LOTRPacketBannerData packet, MessageContext context) {
            World world = LOTRMod.proxy.getClientWorld();
            Entity entity = world.getEntityByID(packet.entityID);
            if (entity instanceof LOTREntityBanner) {
                LOTREntityBanner banner = (LOTREntityBanner)entity;
                banner.setPlayerSpecificProtection(packet.playerSpecificProtection);
                banner.setSelfProtection(packet.selfProtection);
                banner.setStructureProtection(packet.structureProtection);
                banner.setCustomRange(packet.customRange);
                banner.setAlignmentProtection(packet.alignmentProtection);
                banner.resizeWhitelist(packet.whitelistLength);
                for (int index = 0; index < packet.whitelistSlots.length; ++index) {
                    String username = packet.whitelistSlots[index];
                    if (StringUtils.isNullOrEmpty((String)username)) {
                        banner.whitelistPlayer(index, null);
                    } else if (LOTRFellowshipProfile.hasFellowshipCode(username)) {
                        String fsName = LOTRFellowshipProfile.stripFellowshipCode(username);
                        LOTRFellowshipProfile profile = new LOTRFellowshipProfile(banner, null, fsName);
                        banner.whitelistPlayer(index, profile);
                    } else {
                        GameProfile profile = new GameProfile(null, username);
                        banner.whitelistPlayer(index, profile);
                    }
                    LOTRBannerWhitelistEntry entry = banner.getWhitelistEntry(index);
                    if (entry == null) continue;
                    entry.decodePermBitFlags(packet.whitelistPerms[index]);
                }
                List<LOTRBannerProtection.Permission> defaultPerms = LOTRBannerWhitelistEntry.static_decodePermBitFlags(packet.defaultPerms);
                banner.setDefaultPermissions(defaultPerms);
                if (packet.openGui) {
                    LOTRMod.proxy.displayBannerGui(banner);
                }
            }
            return null;
        }
    }

}

