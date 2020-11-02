package lotr.common.network;

import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class LOTRPacketUtumnoReturn implements IMessage {
	private double posX;
	private double posZ;

	public LOTRPacketUtumnoReturn() {
	}

	public LOTRPacketUtumnoReturn(double x, double z) {
		posX = x;
		posZ = z;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeDouble(posX);
		data.writeDouble(posZ);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		posX = data.readDouble();
		posZ = data.readDouble();
	}

	public static class Handler implements IMessageHandler<LOTRPacketUtumnoReturn, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketUtumnoReturn packet, MessageContext context) {
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			entityplayer.setPosition(packet.posX, entityplayer.posY, packet.posZ);
			LOTRMod.proxy.setUtumnoReturnPortalCoords(entityplayer, MathHelper.floor_double(packet.posX), MathHelper.floor_double(packet.posZ));
			return null;
		}
	}

}
