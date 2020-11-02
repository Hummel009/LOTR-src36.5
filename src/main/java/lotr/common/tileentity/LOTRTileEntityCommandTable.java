package lotr.common.tileentity;

import cpw.mods.fml.relauncher.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

public class LOTRTileEntityCommandTable extends TileEntity {
	private int zoomExp;

	public int getZoomExp() {
		return zoomExp;
	}

	public void setZoomExp(int i) {
		zoomExp = MathHelper.clamp_int(i, -2, 2);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	public void toggleZoomExp() {
		int z = zoomExp;
		z = z <= -2 ? 2 : --z;
		setZoomExp(z);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeTableToNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readTableFromNBT(nbt);
	}

	private void writeTableToNBT(NBTTagCompound nbt) {
		nbt.setByte("Zoom", (byte) zoomExp);
	}

	private void readTableFromNBT(NBTTagCompound nbt) {
		zoomExp = nbt.getByte("Zoom");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeTableToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		NBTTagCompound data = packet.func_148857_g();
		readTableFromNBT(data);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getBoundingBox(xCoord - 1, yCoord, zCoord - 1, xCoord + 2, yCoord + 2, zCoord + 2);
	}
}
