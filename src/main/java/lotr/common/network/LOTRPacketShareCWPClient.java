package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.fellowship.LOTRFellowshipClient;
import lotr.common.world.map.LOTRCustomWaypoint;

public class LOTRPacketShareCWPClient implements IMessage {
	private int cwpID;
	private UUID fellowshipID;
	private boolean adding;

	public LOTRPacketShareCWPClient() {
	}

	public LOTRPacketShareCWPClient(int id, UUID fsID, boolean add) {
		cwpID = id;
		fellowshipID = fsID;
		adding = add;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(cwpID);
		data.writeLong(fellowshipID.getMostSignificantBits());
		data.writeLong(fellowshipID.getLeastSignificantBits());
		data.writeBoolean(adding);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		cwpID = data.readInt();
		fellowshipID = new UUID(data.readLong(), data.readLong());
		adding = data.readBoolean();
	}

	public static class Handler implements IMessageHandler<LOTRPacketShareCWPClient, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketShareCWPClient packet, MessageContext context) {
			LOTRCustomWaypoint cwp;
			LOTRFellowshipClient fsClient;
			LOTRPlayerData pd;
			if (!LOTRMod.proxy.isSingleplayer() && (cwp = (pd = LOTRLevelData.getData(LOTRMod.proxy.getClientPlayer())).getCustomWaypointByID(packet.cwpID)) != null && (fsClient = pd.getClientFellowshipByID(packet.fellowshipID)) != null) {
				if (packet.adding) {
					pd.customWaypointAddSharedFellowshipClient(cwp, fsClient);
				} else {
					pd.customWaypointRemoveSharedFellowshipClient(cwp, fsClient);
				}
			}
			return null;
		}
	}

}
