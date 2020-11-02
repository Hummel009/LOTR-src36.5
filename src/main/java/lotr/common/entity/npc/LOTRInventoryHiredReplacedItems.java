package lotr.common.entity.npc;

import lotr.common.inventory.LOTRInventoryNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LOTRInventoryHiredReplacedItems extends LOTRInventoryNPC {
	private boolean[] hasReplacedEquipment = new boolean[7];
	public static final int HELMET = 0;
	public static final int BODY = 1;
	public static final int LEGS = 2;
	public static final int BOOTS = 3;
	public static final int MELEE = 4;
	public static final int BOMB = 5;
	public static final int RANGED = 6;
	private boolean replacedMeleeWeapons = false;

	public LOTRInventoryHiredReplacedItems(LOTREntityNPC npc) {
		super("HiredReplacedItems", npc, 7);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		for (int i = 0; i < hasReplacedEquipment.length; ++i) {
			boolean flag = hasReplacedEquipment[i];
			nbt.setBoolean("ReplacedFlag_" + i, flag);
		}
		nbt.setBoolean("ReplacedMelee", replacedMeleeWeapons);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		for (int i = 0; i < hasReplacedEquipment.length; ++i) {
			hasReplacedEquipment[i] = nbt.getBoolean("ReplacedFlag_" + i);
		}
		replacedMeleeWeapons = nbt.getBoolean("ReplacedMelee");
	}

	private ItemStack getReplacedEquipment(int i) {
		ItemStack item = getStackInSlot(i);
		return item == null ? null : item.copy();
	}

	private void setReplacedEquipment(int i, ItemStack item, boolean flag) {
		setInventorySlotContents(i, item);
		hasReplacedEquipment[i] = flag;
		if (!flag && i == 4) {
			if (replacedMeleeWeapons) {
				theNPC.npcItemsInv.setIdleItem(theNPC.npcItemsInv.getReplacedIdleItem());
				theNPC.npcItemsInv.setMeleeWeaponMounted(theNPC.npcItemsInv.getReplacedMeleeWeaponMounted());
				theNPC.npcItemsInv.setIdleItemMounted(theNPC.npcItemsInv.getReplacedIdleItemMounted());
				theNPC.npcItemsInv.setReplacedMeleeWeaponMounted(null);
				theNPC.npcItemsInv.setReplacedIdleItem(null);
				theNPC.npcItemsInv.setReplacedIdleItemMounted(null);
				replacedMeleeWeapons = false;
			}
			updateHeldItem();
		}
	}

	public boolean hasReplacedEquipment(int i) {
		return hasReplacedEquipment[i];
	}

	private void equipReplacement(int i, ItemStack itemstack) {
		if (i == 4) {
			boolean idleMelee = false;
			if (ItemStack.areItemStacksEqual(theNPC.npcItemsInv.getMeleeWeapon(), theNPC.npcItemsInv.getIdleItem())) {
				idleMelee = true;
			}
			theNPC.npcItemsInv.setMeleeWeapon(itemstack);
			if (!replacedMeleeWeapons) {
				theNPC.npcItemsInv.setReplacedIdleItem(theNPC.npcItemsInv.getIdleItem());
				theNPC.npcItemsInv.setReplacedMeleeWeaponMounted(theNPC.npcItemsInv.getMeleeWeaponMounted());
				theNPC.npcItemsInv.setReplacedIdleItemMounted(theNPC.npcItemsInv.getIdleItemMounted());
				replacedMeleeWeapons = true;
			}
			theNPC.npcItemsInv.setMeleeWeaponMounted(itemstack);
			if (idleMelee) {
				theNPC.npcItemsInv.setIdleItem(itemstack);
				theNPC.npcItemsInv.setIdleItemMounted(itemstack);
			}
			updateHeldItem();
		} else if (i == 6) {
			theNPC.npcItemsInv.setRangedWeapon(itemstack);
			updateHeldItem();
		} else if (i == 5) {
			theNPC.npcItemsInv.setBomb(itemstack);
			updateHeldItem();
		} else {
			theNPC.setCurrentItemOrArmor(getNPCArmorSlot(i), itemstack);
		}
	}

	public ItemStack getEquippedReplacement(int i) {
		if (i == 4) {
			return theNPC.npcItemsInv.getMeleeWeapon();
		}
		if (i == 6) {
			return theNPC.npcItemsInv.getRangedWeapon();
		}
		if (i == 5) {
			return theNPC.npcItemsInv.getBomb();
		}
		return theNPC.getEquipmentInSlot(getNPCArmorSlot(i));
	}

	private int getNPCArmorSlot(int i) {
		return 4 - i;
	}

	public void onEquipmentChanged(int i, ItemStack newItem) {
		if (newItem == null) {
			if (hasReplacedEquipment(i)) {
				ItemStack itemstack = getReplacedEquipment(i);
				equipReplacement(i, itemstack);
				setReplacedEquipment(i, null, false);
			}
		} else {
			if (!hasReplacedEquipment(i)) {
				ItemStack itemstack = getEquippedReplacement(i);
				setReplacedEquipment(i, itemstack, true);
			}
			equipReplacement(i, newItem.copy());
		}
	}

	private void updateHeldItem() {
		if (!theNPC.npcItemsInv.getIsEating()) {
			theNPC.refreshCurrentAttackMode();
		}
	}

	public void dropAllReplacedItems() {
		for (int i = 0; i < 7; ++i) {
			ItemStack itemstack;
			if (!hasReplacedEquipment(i) || (itemstack = getEquippedReplacement(i)) == null) {
				continue;
			}
			theNPC.npcDropItem(itemstack, 0.0f, false, true);
			equipReplacement(i, getReplacedEquipment(i));
			setReplacedEquipment(i, null, false);
		}
	}
}
