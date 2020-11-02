package lotr.common.tileentity;

import lotr.common.LOTRMod;
import lotr.common.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class LOTRTileEntityMug extends TileEntity {
	private ItemStack mugItem;
	private LOTRItemMug.Vessel mugVessel;

	public ItemStack getMugItem() {
		if (mugItem == null) {
			return getVessel().getEmptyVessel();
		}
		ItemStack copy = mugItem.copy();
		if (LOTRItemMug.isItemFullDrink(copy)) {
			LOTRItemMug.setVessel(copy, getVessel(), true);
		}
		return copy;
	}

	public void setMugItem(ItemStack itemstack) {
		if (itemstack != null && itemstack.stackSize <= 0) {
			itemstack = null;
		}
		mugItem = itemstack;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	public ItemStack getMugItemForRender() {
		return LOTRItemMug.getEquivalentDrink(getMugItem());
	}

	public void setEmpty() {
		setMugItem(null);
	}

	public boolean isEmpty() {
		return !LOTRItemMug.isItemFullDrink(getMugItem());
	}

	public LOTRItemMug.Vessel getVessel() {
		if (mugVessel == null) {
			for (LOTRItemMug.Vessel v : LOTRItemMug.Vessel.values()) {
				if (!v.canPlace || v.getBlock() != getBlockType()) {
					continue;
				}
				return v;
			}
			return LOTRItemMug.Vessel.MUG;
		}
		return mugVessel;
	}

	public void setVessel(LOTRItemMug.Vessel v) {
		mugVessel = v;
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		markDirty();
	}

	public boolean canPoisonMug() {
		ItemStack itemstack = getMugItem();
		if (itemstack != null) {
			return LOTRPoisonedDrinks.canPoison(itemstack) && !LOTRPoisonedDrinks.isDrinkPoisoned(itemstack);
		}
		return false;
	}

	public void poisonMug(EntityPlayer entityplayer) {
		ItemStack itemstack = getMugItem();
		LOTRPoisonedDrinks.setDrinkPoisoned(itemstack, true);
		LOTRPoisonedDrinks.setPoisonerPlayer(itemstack, entityplayer);
		setMugItem(itemstack);
	}

	@Override
	public void updateEntity() {
		if (!worldObj.isRemote && isEmpty() && worldObj.canLightningStrikeAt(xCoord, yCoord, zCoord) && worldObj.rand.nextInt(6000) == 0) {
			ItemStack waterItem = new ItemStack(LOTRMod.mugWater);
			LOTRItemMug.setVessel(waterItem, getVessel(), false);
			setMugItem(waterItem);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			markDirty();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("HasMugItem", mugItem != null);
		if (mugItem != null) {
			nbt.setTag("MugItem", mugItem.writeToNBT(new NBTTagCompound()));
		}
		if (mugVessel != null) {
			nbt.setByte("Vessel", (byte) mugVessel.id);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("ItemID")) {
			Item item = Item.getItemById(nbt.getInteger("ItemID"));
			if (item != null) {
				int damage = nbt.getInteger("ItemDamage");
				mugItem = new ItemStack(item, 1, damage);
			}
		} else {
			boolean hasItem = nbt.getBoolean("HasMugItem");
			mugItem = !hasItem ? null : ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("MugItem"));
		}
		if (nbt.hasKey("Vessel")) {
			mugVessel = LOTRItemMug.Vessel.forMeta(nbt.getByte("Vessel"));
		}
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
