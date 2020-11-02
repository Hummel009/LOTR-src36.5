package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRPacketUpdateViewingFaction implements IMessage {
	private LOTRFaction viewingFaction;

	public LOTRPacketUpdateViewingFaction() {
	}

	public LOTRPacketUpdateViewingFaction(LOTRFaction f) {
		viewingFaction = f;
	}

	@Override
	public void toBytes(ByteBuf data) {
		int facID = viewingFaction.ordinal();
		data.writeByte(facID);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		byte facID = data.readByte();
		viewingFaction = LOTRFaction.forID(facID);
	}

	public static class Handler implements IMessageHandler<LOTRPacketUpdateViewingFaction, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketUpdateViewingFaction packet, MessageContext context) {
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
			pd.setViewingFaction(packet.viewingFaction);
			return null;
		}
	}

}
