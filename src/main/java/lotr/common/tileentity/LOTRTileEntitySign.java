package lotr.common.tileentity;

import lotr.common.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public abstract class LOTRTileEntitySign extends TileEntity {
	public String[] signText = new String[getNumLines()];
	public static final int MAX_LINE_LENGTH = 15;
	public int lineBeingEdited = -1;
	private boolean editable = true;
	private EntityPlayer editingPlayer;
	public boolean isFakeGuiSign = false;

	public LOTRTileEntitySign() {
		for (int l = 0; l < signText.length; ++l) {
			signText[l] = "";
		}
	}

	public abstract int getNumLines();

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeSignText(nbt);
	}

	private void writeSignText(NBTTagCompound nbt) {
		for (int i = 0; i < signText.length; ++i) {
			nbt.setString("Text" + (i + 1), signText[i]);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		editable = false;
		super.readFromNBT(nbt);
		readSignText(nbt);
	}

	private void readSignText(NBTTagCompound nbt) {
		for (int i = 0; i < signText.length; ++i) {
			signText[i] = nbt.getString("Text" + (i + 1));
			if (signText[i].length() <= 15) {
				continue;
			}
			signText[i] = signText[i].substring(0, 15);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeSignText(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		NBTTagCompound data = packet.func_148857_g();
		readSignText(data);
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean flag) {
		editable = flag;
		if (!flag) {
			editingPlayer = null;
		}
	}

	public void setEditingPlayer(EntityPlayer entityplayer) {
		editingPlayer = entityplayer;
	}

	public EntityPlayer getEditingPlayer() {
		return editingPlayer;
	}

	public void openEditGUI(EntityPlayerMP entityplayer) {
		setEditingPlayer(entityplayer);
		LOTRPacketOpenSignEditor packet = new LOTRPacketOpenSignEditor(this);
		LOTRPacketHandler.networkWrapper.sendTo(packet, entityplayer);
	}
}
