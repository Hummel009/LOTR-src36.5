package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRLevelData;

public class LOTRPacketEnableAlignmentZones implements IMessage {
	private boolean enable;

	public LOTRPacketEnableAlignmentZones() {
	}

	public LOTRPacketEnableAlignmentZones(boolean flag) {
		enable = flag;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeBoolean(enable);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		enable = data.readBoolean();
	}

	public static class Handler implements IMessageHandler<LOTRPacketEnableAlignmentZones, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketEnableAlignmentZones packet, MessageContext context) {
			LOTRLevelData.setEnableAlignmentZones(packet.enable);
			return null;
		}
	}

}
