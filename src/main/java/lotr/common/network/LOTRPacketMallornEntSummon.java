package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTRPacketMallornEntSummon implements IMessage {
	private int mallornEntID;
	private int summonedID;

	public LOTRPacketMallornEntSummon() {
	}

	public LOTRPacketMallornEntSummon(int id1, int id2) {
		mallornEntID = id1;
		summonedID = id1;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(mallornEntID);
		data.writeInt(summonedID);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		mallornEntID = data.readInt();
		summonedID = data.readInt();
	}

	public static class Handler implements IMessageHandler<LOTRPacketMallornEntSummon, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketMallornEntSummon packet, MessageContext context) {
			World world = LOTRMod.proxy.getClientWorld();
			Entity entity = world.getEntityByID(packet.mallornEntID);
			if (entity instanceof LOTREntityMallornEnt) {
				LOTREntityMallornEnt ent = (LOTREntityMallornEnt) entity;
				Entity summoned = world.getEntityByID(packet.summonedID);
				if (summoned instanceof LOTREntityTree) {
					ent.spawnEntSummonParticles((LOTREntityTree) summoned);
				}
			}
			return null;
		}
	}

}
