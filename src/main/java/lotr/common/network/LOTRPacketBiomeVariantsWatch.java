package lotr.common.network;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.world.biome.variant.LOTRBiomeVariantStorage;
import net.minecraft.world.*;

public class LOTRPacketBiomeVariantsWatch implements IMessage {
	private int chunkX;
	private int chunkZ;
	private byte[] variants;

	public LOTRPacketBiomeVariantsWatch() {
	}

	public LOTRPacketBiomeVariantsWatch(int x, int z, byte[] v) {
		chunkX = x;
		chunkZ = z;
		variants = v;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeInt(chunkX);
		data.writeInt(chunkZ);
		data.writeInt(variants.length);
		data.writeBytes(variants);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		chunkX = data.readInt();
		chunkZ = data.readInt();
		int length = data.readInt();
		variants = data.readBytes(length).array();
	}

	public static class Handler implements IMessageHandler<LOTRPacketBiomeVariantsWatch, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketBiomeVariantsWatch packet, MessageContext context) {
			int chunkZ;
			int chunkX;
			World world = LOTRMod.proxy.getClientWorld();
			if (world.blockExists((chunkX = packet.chunkX) << 4, 0, (chunkZ = packet.chunkZ) << 4)) {
				LOTRBiomeVariantStorage.setChunkBiomeVariants(world, new ChunkCoordIntPair(chunkX, chunkZ), packet.variants);
			} else {
				FMLLog.severe("Client received LOTR biome variant data for nonexistent chunk at %d, %d", chunkX << 4, chunkZ << 4);
			}
			return null;
		}
	}

}
