package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.world.map.LOTRCustomWaypoint;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketDeleteCWPClient implements IMessage {
	private int cwpID;
	private UUID sharingPlayer;

	public LOTRPacketDeleteCWPClient() {
	}

	public LOTRPacketDeleteCWPClient(int id) {
		cwpID = id;
	}

	public LOTRPacketDeleteCWPClient setSharingPlayer(UUID player) {
		sharingPlayer = player;
		return this;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(cwpID);
		boolean shared = sharingPlayer != null;
		data.writeBoolean(shared);
		if (shared) {
			data.writeLong(sharingPlayer.getMostSignificantBits());
			data.writeLong(sharingPlayer.getLeastSignificantBits());
		}
	}

	@Override
	public void fromBytes(ByteBuf data) {
		cwpID = data.readInt();
		boolean shared = data.readBoolean();
		if (shared) {
			sharingPlayer = new UUID(data.readLong(), data.readLong());
		}
	}

	public static class Handler implements IMessageHandler<LOTRPacketDeleteCWPClient, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketDeleteCWPClient packet, MessageContext context) {
			LOTRCustomWaypoint cwp;
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
			if (packet.sharingPlayer != null) {
				LOTRCustomWaypoint cwp2;
				if (!LOTRMod.proxy.isSingleplayer() && (cwp2 = pd.getSharedCustomWaypointByID(packet.sharingPlayer, packet.cwpID)) != null) {
					pd.removeSharedCustomWaypoint(cwp2);
				}
			} else if (!LOTRMod.proxy.isSingleplayer() && (cwp = pd.getCustomWaypointByID(packet.cwpID)) != null) {
				pd.removeCustomWaypoint(cwp);
			}
			return null;
		}
	}

}
