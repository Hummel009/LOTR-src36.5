package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.world.map.LOTRCustomWaypoint;

public class LOTRPacketCWPSharedHideClient implements IMessage {
	private int cwpID;
	private UUID sharingPlayer;
	private boolean hideCWP;

	public LOTRPacketCWPSharedHideClient() {
	}

	public LOTRPacketCWPSharedHideClient(int id, UUID player, boolean hide) {
		cwpID = id;
		sharingPlayer = player;
		hideCWP = hide;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(cwpID);
		data.writeLong(sharingPlayer.getMostSignificantBits());
		data.writeLong(sharingPlayer.getLeastSignificantBits());
		data.writeBoolean(hideCWP);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		cwpID = data.readInt();
		sharingPlayer = new UUID(data.readLong(), data.readLong());
		hideCWP = data.readBoolean();
	}

	public static class Handler implements IMessageHandler<LOTRPacketCWPSharedHideClient, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketCWPSharedHideClient packet, MessageContext context) {
			LOTRCustomWaypoint cwp;
			LOTRPlayerData pd;
			if (!LOTRMod.proxy.isSingleplayer() && (cwp = (pd = LOTRLevelData.getData(LOTRMod.proxy.getClientPlayer())).getSharedCustomWaypointByID(packet.sharingPlayer, packet.cwpID)) != null) {
				pd.hideOrUnhideSharedCustomWaypoint(cwp, packet.hideCWP);
			}
			return null;
		}
	}

}
