package lotr.common.inventory;

import cpw.mods.fml.relauncher.*;
import lotr.common.entity.npc.*;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class LOTRSlotHiredReplaceItem extends Slot {
	private LOTREntityNPC theNPC;
	private LOTRInventoryHiredReplacedItems npcInv;
	private Slot parentSlot;

	public LOTRSlotHiredReplaceItem(Slot slot, LOTREntityNPC npc) {
		super(slot.inventory, slot.getSlotIndex(), slot.xDisplayPosition, slot.yDisplayPosition);
		int i;
		parentSlot = slot;
		theNPC = npc;
		npcInv = theNPC.hiredReplacedInv;
		if (!theNPC.worldObj.isRemote && npcInv.hasReplacedEquipment(i = getSlotIndex())) {
			inventory.setInventorySlotContents(i, npcInv.getEquippedReplacement(i));
		}
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return parentSlot.isItemValid(itemstack) && theNPC.canReEquipHired(getSlotIndex(), itemstack);
	}

	@Override
	public int getSlotStackLimit() {
		return parentSlot.getSlotStackLimit();
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getBackgroundIconIndex() {
		return parentSlot.getBackgroundIconIndex();
	}

	@Override
	public void onSlotChanged() {
		super.onSlotChanged();
		if (!theNPC.worldObj.isRemote) {
			npcInv.onEquipmentChanged(getSlotIndex(), getStack());
		}
	}
}
