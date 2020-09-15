/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.network.NetHandlerPlayServer
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 */
package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRConfig;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRPlayerData;
import lotr.common.world.map.LOTRAbstractWaypoint;
import lotr.common.world.map.LOTRCustomWaypoint;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class LOTRPacketFastTravel
implements IMessage {
    private boolean isCustom;
    private int wpID;
    private UUID sharingPlayer;

    public LOTRPacketFastTravel() {
    }

    public LOTRPacketFastTravel(LOTRAbstractWaypoint wp) {
        this.isCustom = wp instanceof LOTRCustomWaypoint;
        this.wpID = wp.getID();
        if (wp instanceof LOTRCustomWaypoint) {
            this.sharingPlayer = ((LOTRCustomWaypoint)wp).getSharingPlayerID();
        }
    }

    public void toBytes(ByteBuf data) {
        data.writeBoolean(this.isCustom);
        data.writeInt(this.wpID);
        boolean shared = this.sharingPlayer != null;
        data.writeBoolean(shared);
        if (shared) {
            data.writeLong(this.sharingPlayer.getMostSignificantBits());
            data.writeLong(this.sharingPlayer.getLeastSignificantBits());
        }
    }

    public void fromBytes(ByteBuf data) {
        this.isCustom = data.readBoolean();
        this.wpID = data.readInt();
        boolean shared = data.readBoolean();
        if (shared) {
            this.sharingPlayer = new UUID(data.readLong(), data.readLong());
        }
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketFastTravel, IMessage> {
        public IMessage onMessage(LOTRPacketFastTravel packet, MessageContext context) {
            EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
            if (!LOTRConfig.enableFastTravel) {
                entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.ftDisabled", new Object[0]));
            } else {
                LOTRPlayerData playerData = LOTRLevelData.getData((EntityPlayer)entityplayer);
                boolean isCustom = packet.isCustom;
                int waypointID = packet.wpID;
                LOTRAbstractWaypoint waypoint = null;
                if (!isCustom) {
                    if (waypointID >= 0 && waypointID < LOTRWaypoint.values().length) {
                        waypoint = LOTRWaypoint.values()[waypointID];
                    }
                } else {
                    UUID sharingPlayer = packet.sharingPlayer;
                    waypoint = sharingPlayer != null ? playerData.getSharedCustomWaypointByID(sharingPlayer, waypointID) : playerData.getCustomWaypointByID(waypointID);
                }
                if (waypoint != null && waypoint.hasPlayerUnlocked((EntityPlayer)entityplayer)) {
                    if (playerData.getTimeSinceFT() < playerData.getWaypointFTTime(waypoint, (EntityPlayer)entityplayer)) {
                        entityplayer.closeScreen();
                        entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("lotr.fastTravel.moreTime", new Object[]{waypoint.getDisplayName()}));
                    } else {
                        boolean canTravel = playerData.canFastTravel();
                        if (!canTravel) {
                            entityplayer.closeScreen();
                            entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("lotr.fastTravel.underAttack", new Object[0]));
                        } else {
                            playerData.setTargetFTWaypoint(waypoint);
                        }
                    }
                }
            }
            return null;
        }
    }

}

