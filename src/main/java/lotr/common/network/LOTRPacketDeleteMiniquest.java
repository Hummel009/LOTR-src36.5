package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.quest.LOTRMiniQuest;
import net.minecraft.entity.player.EntityPlayerMP;

public class LOTRPacketDeleteMiniquest implements IMessage {
	private UUID questUUID;
	private boolean completed;

	public LOTRPacketDeleteMiniquest() {
	}

	public LOTRPacketDeleteMiniquest(LOTRMiniQuest quest) {
		questUUID = quest.questUUID;
		completed = quest.isCompleted();
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeLong(questUUID.getMostSignificantBits());
		data.writeLong(questUUID.getLeastSignificantBits());
		data.writeBoolean(completed);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		questUUID = new UUID(data.readLong(), data.readLong());
		completed = data.readBoolean();
	}

	public static class Handler implements IMessageHandler<LOTRPacketDeleteMiniquest, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketDeleteMiniquest packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
			LOTRMiniQuest removeQuest = pd.getMiniQuestForID(packet.questUUID, packet.completed);
			if (removeQuest != null) {
				pd.removeMiniQuest(removeQuest, packet.completed);
			} else {
				FMLLog.warning("Tried to remove a LOTR miniquest that doesn't exist", new Object[0]);
			}
			return null;
		}
	}

}
