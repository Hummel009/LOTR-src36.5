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

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.LOTRPlayerData;
import lotr.common.world.map.LOTRAbstractWaypoint;
import lotr.common.world.map.LOTRCustomWaypoint;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketFTScreen
implements IMessage {
    private boolean isCustom;
    private int wpID;
    private UUID sharingPlayer;
    private int startX;
    private int startZ;

    public LOTRPacketFTScreen() {
    }

    public LOTRPacketFTScreen(LOTRAbstractWaypoint wp, int x, int z) {
        this.isCustom = wp instanceof LOTRCustomWaypoint;
        this.wpID = wp.getID();
        if (wp instanceof LOTRCustomWaypoint) {
            this.sharingPlayer = ((LOTRCustomWaypoint)wp).getSharingPlayerID();
        }
        this.startX = x;
        this.startZ = z;
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
        data.writeInt(this.startX);
        data.writeInt(this.startZ);
    }

    public void fromBytes(ByteBuf data) {
        this.isCustom = data.readBoolean();
        this.wpID = data.readInt();
        boolean shared = data.readBoolean();
        if (shared) {
            this.sharingPlayer = new UUID(data.readLong(), data.readLong());
        }
        this.startX = data.readInt();
        this.startZ = data.readInt();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketFTScreen, IMessage> {
        public IMessage onMessage(LOTRPacketFTScreen packet, MessageContext context) {
            boolean custom = packet.isCustom;
            int wpID = packet.wpID;
            EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
            LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
            LOTRAbstractWaypoint waypoint = null;
            if (!custom) {
                if (wpID >= 0 && wpID < LOTRWaypoint.values().length) {
                    waypoint = LOTRWaypoint.values()[wpID];
                }
            } else {
                UUID sharingPlayerID = packet.sharingPlayer;
                waypoint = sharingPlayerID != null ? playerData.getSharedCustomWaypointByID(sharingPlayerID, wpID) : playerData.getCustomWaypointByID(wpID);
            }
            if (waypoint != null) {
                LOTRMod.proxy.displayFTScreen(waypoint, packet.startX, packet.startZ);
            }
            return null;
        }
    }

}

