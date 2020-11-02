package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LOTRPacketHiredUnitCommand implements IMessage {
	private int entityID;
	private int page;
	private int action;
	private int value;

	public LOTRPacketHiredUnitCommand() {
	}

	public LOTRPacketHiredUnitCommand(int eid, int p, int a, int v) {
		entityID = eid;
		page = p;
		action = a;
		value = v;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(entityID);
		data.writeByte(page);
		data.writeByte(action);
		data.writeByte(value);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		entityID = data.readInt();
		page = data.readByte();
		action = data.readByte();
		value = data.readByte();
	}

	public static class Handler implements IMessageHandler<LOTRPacketHiredUnitCommand, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketHiredUnitCommand packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			World world = entityplayer.worldObj;
			Entity npc = world.getEntityByID(packet.entityID);
			if (npc != null && npc instanceof LOTREntityNPC) {
				LOTREntityNPC hiredNPC = (LOTREntityNPC) npc;
				if (hiredNPC.hiredNPCInfo.isActive && hiredNPC.hiredNPCInfo.getHiringPlayer() == entityplayer) {
					int page = packet.page;
					int action = packet.action;
					int value = packet.value;
					if (action == -1) {
						hiredNPC.hiredNPCInfo.isGuiOpen = false;
					} else {
						LOTRHiredNPCInfo.Task task = hiredNPC.hiredNPCInfo.getTask();
						if (task == LOTRHiredNPCInfo.Task.WARRIOR) {
							if (page == 0) {
								entityplayer.openGui(LOTRMod.instance, 46, world, hiredNPC.getEntityId(), 0, 0);
							} else if (page == 1) {
								if (action == 0) {
									hiredNPC.hiredNPCInfo.teleportAutomatically = !hiredNPC.hiredNPCInfo.teleportAutomatically;
								} else if (action == 1) {
									hiredNPC.hiredNPCInfo.setGuardMode(!hiredNPC.hiredNPCInfo.isGuardMode());
								} else if (action == 2) {
									hiredNPC.hiredNPCInfo.setGuardRange(value);
								}
							}
						} else if (task == LOTRHiredNPCInfo.Task.FARMER) {
							if (action == 0) {
								hiredNPC.hiredNPCInfo.setGuardMode(!hiredNPC.hiredNPCInfo.isGuardMode());
							} else if (action == 1) {
								hiredNPC.hiredNPCInfo.setGuardRange(value);
							} else if (action == 2) {
								entityplayer.openGui(LOTRMod.instance, 22, world, hiredNPC.getEntityId(), 0, 0);
							}
						}
						hiredNPC.hiredNPCInfo.sendClientPacket(false);
					}
				}
			}
			return null;
		}
	}

}
