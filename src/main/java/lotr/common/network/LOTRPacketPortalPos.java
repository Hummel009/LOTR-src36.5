package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRLevelData;

public class LOTRPacketPortalPos implements IMessage {
	private int portalX;
	private int portalY;
	private int portalZ;

	public LOTRPacketPortalPos() {
	}

	public LOTRPacketPortalPos(int i, int j, int k) {
		portalX = i;
		portalY = j;
		portalZ = k;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(portalX);
		data.writeInt(portalY);
		data.writeInt(portalZ);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		portalX = data.readInt();
		portalY = data.readInt();
		portalZ = data.readInt();
	}

	public static class Handler implements IMessageHandler<LOTRPacketPortalPos, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketPortalPos packet, MessageContext context) {
			LOTRLevelData.middleEarthPortalX = packet.portalX;
			LOTRLevelData.middleEarthPortalY = packet.portalY;
			LOTRLevelData.middleEarthPortalZ = packet.portalZ;
			return null;
		}
	}

}
