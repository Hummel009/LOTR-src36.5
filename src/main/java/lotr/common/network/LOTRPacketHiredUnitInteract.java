package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LOTRPacketHiredUnitInteract implements IMessage {
	private int entityID;
	private int entityAction;

	public LOTRPacketHiredUnitInteract() {
	}

	public LOTRPacketHiredUnitInteract(int id, int a) {
		entityID = id;
		entityAction = a;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(entityID);
		data.writeByte(entityAction);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		entityID = data.readInt();
		entityAction = data.readByte();
	}

	public static class Handler implements IMessageHandler<LOTRPacketHiredUnitInteract, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketHiredUnitInteract packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			World world = entityplayer.worldObj;
			Entity npc = world.getEntityByID(packet.entityID);
			if (npc != null && npc instanceof LOTREntityNPC) {
				LOTREntityNPC hiredNPC = (LOTREntityNPC) npc;
				if (hiredNPC.hiredNPCInfo.isActive && hiredNPC.hiredNPCInfo.getHiringPlayer() == entityplayer) {
					int action = packet.entityAction;
					boolean closeScreen = false;
					if (action == 0) {
						hiredNPC.npcTalkTick = hiredNPC.getNPCTalkInterval();
						closeScreen = hiredNPC.speakTo(entityplayer);
					} else if (action == 1) {
						hiredNPC.hiredNPCInfo.sendClientPacket(true);
					} else if (action == 2) {
						// empty if block
					}
					if (closeScreen) {
						entityplayer.closeScreen();
					}
				}
			}
			return null;
		}
	}

}
