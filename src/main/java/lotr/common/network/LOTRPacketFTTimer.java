package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketFTTimer implements IMessage {
	private int timer;

	public LOTRPacketFTTimer() {
	}

	public LOTRPacketFTTimer(int i) {
		timer = i;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(timer);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		timer = data.readInt();
	}

	public static class Handler implements IMessageHandler<LOTRPacketFTTimer, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketFTTimer packet, MessageContext context) {
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			LOTRLevelData.getData(entityplayer).setTimeSinceFT(packet.timer);
			return null;
		}
	}

}
