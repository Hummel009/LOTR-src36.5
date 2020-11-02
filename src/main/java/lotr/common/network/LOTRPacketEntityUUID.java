package lotr.common.network;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTRRandomSkinEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTRPacketEntityUUID implements IMessage {
	private int entityID;
	private UUID entityUUID;

	public LOTRPacketEntityUUID() {
	}

	public LOTRPacketEntityUUID(int id, UUID uuid) {
		entityID = id;
		entityUUID = uuid;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(entityID);
		data.writeLong(entityUUID.getMostSignificantBits());
		data.writeLong(entityUUID.getLeastSignificantBits());
	}

	@Override
	public void fromBytes(ByteBuf data) {
		entityID = data.readInt();
		entityUUID = new UUID(data.readLong(), data.readLong());
	}

	public static class Handler implements IMessageHandler<LOTRPacketEntityUUID, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketEntityUUID packet, MessageContext context) {
			World world = LOTRMod.proxy.getClientWorld();
			Entity entity = world.getEntityByID(packet.entityID);
			if (entity instanceof LOTRRandomSkinEntity) {
				LOTRRandomSkinEntity npc = (LOTRRandomSkinEntity) entity;
				npc.setUniqueID(packet.entityUUID);
			}
			return null;
		}
	}

}
