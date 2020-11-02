package lotr.common.tileentity;

import lotr.common.item.LOTRWeaponStats;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityWeaponRack extends TileEntity {
	private ItemStack weaponItem;
	private EntityLivingBase rackEntity;

	public boolean canAcceptItem(ItemStack itemstack) {
		if (itemstack != null) {
			Item item = itemstack.getItem();
			if (LOTRWeaponStats.isMeleeWeapon(itemstack)) {
				return true;
			}
			if (LOTRWeaponStats.isRangedWeapon(itemstack)) {
				return true;
			}
			if (item instanceof ItemHoe) {
				return true;
			}
			if (item instanceof ItemFishingRod) {
				return true;
			}
		}
		return false;
	}

	public ItemStack getWeaponItem() {
		return weaponItem;
	}

	public void setWeaponItem(ItemStack item) {
		if (item != null && item.stackSize <= 0) {
			item = null;
		}
		weaponItem = item;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	public EntityLivingBase getEntityForRender() {
		if (rackEntity == null) {
			rackEntity = new EntityLiving(worldObj) {
			};
		}
		return rackEntity;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("HasWeapon", weaponItem != null);
		if (weaponItem != null) {
			nbt.setTag("WeaponItem", weaponItem.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		boolean hasWeapon = nbt.getBoolean("HasWeapon");
		weaponItem = hasWeapon ? ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("WeaponItem")) : null;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, data);
	}

	@Override
	public void onDataPacket(NetworkManager manager, S35PacketUpdateTileEntity packet) {
		NBTTagCompound data = packet.func_148857_g();
		readFromNBT(data);
	}

}
