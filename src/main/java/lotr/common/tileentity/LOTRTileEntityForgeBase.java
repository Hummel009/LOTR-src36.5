package lotr.common.tileentity;

import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.*;
import lotr.common.block.LOTRBlockForgeBase;
import lotr.common.inventory.LOTRSlotStackSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.*;

public abstract class LOTRTileEntityForgeBase extends TileEntity implements IInventory, ISidedInventory {
	protected ItemStack[] inventory = new ItemStack[getForgeInvSize()];
	private String specialForgeName;
	public int forgeSmeltTime = 0;
	public int currentItemFuelValue = 0;
	public int currentSmeltTime = 0;
	public int[] inputSlots;
	public int[] outputSlots;
	public int fuelSlot;

	public LOTRTileEntityForgeBase() {
		setupForgeSlots();
	}

	public abstract int getForgeInvSize();

	public abstract void setupForgeSlots();

	public abstract int getSmeltingDuration();

	protected boolean canMachineInsertInput(ItemStack itemstack) {
		return true;
	}

	protected boolean canMachineInsertFuel(ItemStack itemstack) {
		return TileEntityFurnace.isItemFuel(itemstack);
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (inventory[i] != null) {
			if (inventory[i].stackSize <= j) {
				ItemStack itemstack = inventory[i];
				inventory[i] = null;
				return itemstack;
			}
			ItemStack itemstack = inventory[i].splitStack(j);
			if (inventory[i].stackSize == 0) {
				inventory[i] = null;
			}
			return itemstack;
		}
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (inventory[i] != null) {
			ItemStack itemstack = inventory[i];
			inventory[i] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return hasCustomInventoryName() ? specialForgeName : getForgeName();
	}

	public abstract String getForgeName();

	@Override
	public boolean hasCustomInventoryName() {
		return specialForgeName != null && specialForgeName.length() > 0;
	}

	public void setSpecialForgeName(String s) {
		specialForgeName = s;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		NBTTagList items = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < items.tagCount(); ++i) {
			NBTTagCompound itemData = items.getCompoundTagAt(i);
			byte slot = itemData.getByte("Slot");
			if (slot < 0 || slot >= inventory.length) {
				continue;
			}
			inventory[slot] = ItemStack.loadItemStackFromNBT(itemData);
		}
		forgeSmeltTime = nbt.getShort("BurnTime");
		currentSmeltTime = nbt.getShort("SmeltTime");
		currentItemFuelValue = TileEntityFurnace.getItemBurnTime(inventory[fuelSlot]);
		if (nbt.hasKey("CustomName")) {
			specialForgeName = nbt.getString("CustomName");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < inventory.length; ++i) {
			if (inventory[i] == null) {
				continue;
			}
			NBTTagCompound itemData = new NBTTagCompound();
			itemData.setByte("Slot", (byte) i);
			inventory[i].writeToNBT(itemData);
			items.appendTag(itemData);
		}
		nbt.setTag("Items", items);
		nbt.setShort("BurnTime", (short) forgeSmeltTime);
		nbt.setShort("SmeltTime", (short) currentSmeltTime);
		if (hasCustomInventoryName()) {
			nbt.setString("CustomName", specialForgeName);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@SideOnly(value = Side.CLIENT)
	public int getSmeltProgressScaled(int i) {
		return currentSmeltTime * i / getSmeltingDuration();
	}

	@SideOnly(value = Side.CLIENT)
	public int getSmeltTimeRemainingScaled(int i) {
		if (currentItemFuelValue == 0) {
			currentItemFuelValue = getSmeltingDuration();
		}
		return forgeSmeltTime * i / currentItemFuelValue;
	}

	public boolean isSmelting() {
		return forgeSmeltTime > 0;
	}

	protected void toggleForgeActive() {
		LOTRBlockForgeBase.toggleForgeActive(worldObj, xCoord, yCoord, zCoord);
	}

	protected abstract boolean canDoSmelting();

	protected abstract void doSmelt();

	@Override
	public void updateEntity() {
		boolean smelting = forgeSmeltTime > 0;
		boolean needUpdate = false;
		if (forgeSmeltTime > 0) {
			--forgeSmeltTime;
		}
		if (!worldObj.isRemote) {
			if (forgeSmeltTime == 0 && canDoSmelting()) {
				currentItemFuelValue = forgeSmeltTime = TileEntityFurnace.getItemBurnTime(inventory[fuelSlot]);
				if (forgeSmeltTime > 0) {
					needUpdate = true;
					if (inventory[fuelSlot] != null) {
						--inventory[fuelSlot].stackSize;
						if (inventory[fuelSlot].stackSize == 0) {
							inventory[fuelSlot] = inventory[fuelSlot].getItem().getContainerItem(inventory[fuelSlot]);
						}
					}
				}
			}
			if (isSmelting() && canDoSmelting()) {
				++currentSmeltTime;
				if (currentSmeltTime == getSmeltingDuration()) {
					currentSmeltTime = 0;
					doSmelt();
					needUpdate = true;
				}
			} else {
				currentSmeltTime = 0;
			}
			if (smelting != forgeSmeltTime > 0) {
				needUpdate = true;
				toggleForgeActive();
			}
		}
		if (needUpdate) {
			markDirty();
		}
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64.0;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if (ArrayUtils.contains(inputSlots, slot)) {
			return canMachineInsertInput(itemstack);
		}
		if (slot == fuelSlot) {
			return canMachineInsertFuel(itemstack);
		}
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		if (side == 0) {
			ArrayList<Integer> list = new ArrayList<>();
			for (int i : outputSlots) {
				list.add(i);
			}
			list.add(fuelSlot);
			int[] temp = new int[list.size()];
			for (int i = 0; i < temp.length; ++i) {
				temp[i] = list.get(i);
			}
			return temp;
		}
		if (side == 1) {
			ArrayList<LOTRSlotStackSize> slotsWithStackSize = new ArrayList<>();
			int[] temp = inputSlots;
			int i = temp.length;
			for (int j = 0; j < i; ++j) {
				int slot = temp[j];
				int size = getStackInSlot(slot) == null ? 0 : getStackInSlot(slot).stackSize;
				slotsWithStackSize.add(new LOTRSlotStackSize(slot, size));
			}
			Collections.sort(slotsWithStackSize);
			int[] sortedSlots = new int[inputSlots.length];
			for (i = 0; i < sortedSlots.length; ++i) {
				LOTRSlotStackSize slotAndStack = slotsWithStackSize.get(i);
				sortedSlots[i] = slotAndStack.slot;
			}
			return sortedSlots;
		}
		return new int[] { fuelSlot };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemstack, int side) {
		return isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
		if (side == 0) {
			if (slot == fuelSlot) {
				return itemstack.getItem() == Items.bucket;
			}
			return true;
		}
		return true;
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
		if (packet.func_148857_g() != null && packet.func_148857_g().hasKey("CustomName")) {
			specialForgeName = packet.func_148857_g().getString("CustomName");
		}
	}
}
