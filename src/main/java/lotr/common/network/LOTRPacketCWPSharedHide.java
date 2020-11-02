package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.world.map.LOTRCustomWaypoint;
import net.minecraft.entity.player.EntityPlayerMP;

public class LOTRPacketCWPSharedHide implements IMessage {
	private int cwpID;
	private UUID sharingPlayer;
	private boolean hideCWP;

	public LOTRPacketCWPSharedHide() {
	}

	public LOTRPacketCWPSharedHide(LOTRCustomWaypoint cwp, boolean hide) {
		cwpID = cwp.getID();
		sharingPlayer = cwp.getSharingPlayerID();
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

	public static class Handler implements IMessageHandler<LOTRPacketCWPSharedHide, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketCWPSharedHide packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
			LOTRCustomWaypoint cwp = pd.getSharedCustomWaypointByID(packet.sharingPlayer, packet.cwpID);
			if (cwp != null) {
				pd.hideOrUnhideSharedCustomWaypoint(cwp, packet.hideCWP);
			}
			return null;
		}
	}

}
