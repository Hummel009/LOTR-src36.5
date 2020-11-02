package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRLevelData;

public class LOTRPacketFTCooldown implements IMessage {
	private int cooldownMax;
	private int cooldownMin;

	public LOTRPacketFTCooldown() {
	}

	public LOTRPacketFTCooldown(int max, int min) {
		cooldownMax = max;
		cooldownMin = min;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(cooldownMax);
		data.writeInt(cooldownMin);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		cooldownMax = data.readInt();
		cooldownMin = data.readInt();
	}

	public static class Handler implements IMessageHandler<LOTRPacketFTCooldown, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketFTCooldown packet, MessageContext context) {
			LOTRLevelData.setWaypointCooldown(packet.cooldownMax, packet.cooldownMin);
			return null;
		}
	}

}
