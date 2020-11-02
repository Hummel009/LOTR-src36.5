package lotr.common.network;

import java.io.IOException;
import java.util.UUID;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.fellowship.*;
import lotr.common.util.LOTRLog;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public abstract class LOTRPacketFellowshipPartialUpdate implements IMessage {
	protected UUID fellowshipID;

	protected LOTRPacketFellowshipPartialUpdate() {
	}

	protected LOTRPacketFellowshipPartialUpdate(LOTRFellowship fs) {
		fellowshipID = fs.getFellowshipID();
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeLong(fellowshipID.getMostSignificantBits());
		data.writeLong(fellowshipID.getLeastSignificantBits());
	}

	@Override
	public void fromBytes(ByteBuf data) {
		fellowshipID = new UUID(data.readLong(), data.readLong());
	}

	public abstract void updateClient(LOTRFellowshipClient paramLOTRFellowshipClient);

	public static abstract class Handler<P extends LOTRPacketFellowshipPartialUpdate> implements IMessageHandler<P, IMessage> {
		@Override
		public IMessage onMessage(P packet, MessageContext context) {
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
			LOTRFellowshipClient fellowship = pd.getClientFellowshipByID(((LOTRPacketFellowshipPartialUpdate) packet).fellowshipID);
			if (fellowship != null) {
				packet.updateClient(fellowship);
			} else {
				LOTRLog.logger.warn("Client couldn't find fellowship for ID " + ((LOTRPacketFellowshipPartialUpdate) packet).fellowshipID);
			}
			return null;
		}
	}

	public static class Rename extends LOTRPacketFellowshipPartialUpdate {
		private String fellowshipName;

		public Rename() {
		}

		public Rename(LOTRFellowship fs) {
			super(fs);
			fellowshipName = fs.getName();
		}

		@Override
		public void toBytes(ByteBuf data) {
			super.toBytes(data);
			byte[] fsNameBytes = fellowshipName.getBytes(Charsets.UTF_8);
			data.writeByte(fsNameBytes.length);
			data.writeBytes(fsNameBytes);
		}

		@Override
		public void fromBytes(ByteBuf data) {
			super.fromBytes(data);
			int fsNameLength = data.readByte();
			ByteBuf fsNameBytes = data.readBytes(fsNameLength);
			fellowshipName = fsNameBytes.toString(Charsets.UTF_8);
		}

		@Override
		public void updateClient(LOTRFellowshipClient fellowship) {
			fellowship.setName(fellowshipName);
		}

		public static class Handler extends LOTRPacketFellowshipPartialUpdate.Handler<Rename> {
		}
	}

	public static class ChangeIcon extends LOTRPacketFellowshipPartialUpdate {
		private ItemStack fellowshipIcon;

		public ChangeIcon() {
		}

		public ChangeIcon(LOTRFellowship fs) {
			super(fs);
			fellowshipIcon = fs.getIcon();
		}

		@Override
		public void toBytes(ByteBuf data) {
			super.toBytes(data);
			NBTTagCompound iconData = new NBTTagCompound();
			if (fellowshipIcon != null) {
				fellowshipIcon.writeToNBT(iconData);
			}
			try {
				new PacketBuffer(data).writeNBTTagCompoundToBuffer(iconData);
			} catch (IOException e) {
				FMLLog.severe("LOTR: Error writing fellowship icon data", new Object[0]);
				e.printStackTrace();
			}
		}

		@Override
		public void fromBytes(ByteBuf data) {
			super.fromBytes(data);
			NBTTagCompound iconData = new NBTTagCompound();
			try {
				iconData = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
			} catch (IOException e) {
				FMLLog.severe("LOTR: Error reading fellowship icon data", new Object[0]);
				e.printStackTrace();
			}
			fellowshipIcon = ItemStack.loadItemStackFromNBT(iconData);
		}

		@Override
		public void updateClient(LOTRFellowshipClient fellowship) {
			fellowship.setIcon(fellowshipIcon);
		}

		public static class Handler extends LOTRPacketFellowshipPartialUpdate.Handler<ChangeIcon> {
		}
	}

	public static class TogglePvp extends LOTRPacketFellowshipPartialUpdate {
		private boolean preventPVP;

		public TogglePvp() {
		}

		public TogglePvp(LOTRFellowship fs) {
			super(fs);
			preventPVP = fs.getPreventPVP();
		}

		@Override
		public void toBytes(ByteBuf data) {
			super.toBytes(data);
			data.writeBoolean(preventPVP);
		}

		@Override
		public void fromBytes(ByteBuf data) {
			super.fromBytes(data);
			preventPVP = data.readBoolean();
		}

		@Override
		public void updateClient(LOTRFellowshipClient fellowship) {
			fellowship.setPreventPVP(preventPVP);
		}

		public static class Handler extends LOTRPacketFellowshipPartialUpdate.Handler<TogglePvp> {
		}
	}

	public static class ToggleHiredFriendlyFire extends LOTRPacketFellowshipPartialUpdate {
		private boolean preventHiredFF;

		public ToggleHiredFriendlyFire() {
		}

		public ToggleHiredFriendlyFire(LOTRFellowship fs) {
			super(fs);
			preventHiredFF = fs.getPreventHiredFriendlyFire();
		}

		@Override
		public void toBytes(ByteBuf data) {
			super.toBytes(data);
			data.writeBoolean(preventHiredFF);
		}

		@Override
		public void fromBytes(ByteBuf data) {
			super.fromBytes(data);
			preventHiredFF = data.readBoolean();
		}

		@Override
		public void updateClient(LOTRFellowshipClient fellowship) {
			fellowship.setPreventHiredFriendlyFire(preventHiredFF);
		}

		public static class Handler extends LOTRPacketFellowshipPartialUpdate.Handler<ToggleHiredFriendlyFire> {
		}
	}

	public static class ToggleShowMap extends LOTRPacketFellowshipPartialUpdate {
		private boolean showMapLocations;

		public ToggleShowMap() {
		}

		public ToggleShowMap(LOTRFellowship fs) {
			super(fs);
			showMapLocations = fs.getShowMapLocations();
		}

		@Override
		public void toBytes(ByteBuf data) {
			super.toBytes(data);
			data.writeBoolean(showMapLocations);
		}

		@Override
		public void fromBytes(ByteBuf data) {
			super.fromBytes(data);
			showMapLocations = data.readBoolean();
		}

		@Override
		public void updateClient(LOTRFellowshipClient fellowship) {
			fellowship.setShowMapLocations(showMapLocations);
		}

		public static class Handler extends LOTRPacketFellowshipPartialUpdate.Handler<ToggleShowMap> {
		}
	}
}
