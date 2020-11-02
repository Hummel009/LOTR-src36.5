package lotr.common.entity.npc;

import lotr.common.inventory.LOTRInventoryNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class LOTRInventoryNPCItems extends LOTRInventoryNPC {
	private static int IDLE_ITEM = 0;
	private static int WEAPON_MELEE = 1;
	private static int WEAPON_RANGED = 2;
	private static int SPEAR_BACKUP = 3;
	private static int EATING_BACKUP = 4;
	private static int IDLE_ITEM_MOUNTED = 5;
	private static int WEAPON_MELEE_MOUNTED = 6;
	private static int REPLACED_IDLE = 7;
	private static int REPLACED_MELEE_MOUNTED = 8;
	private static int REPLACED_IDLE_MOUNTED = 9;
	private static int BOMBING_ITEM = 10;
	private static int BOMB = 11;
	private boolean isEating = false;

	public LOTRInventoryNPCItems(LOTREntityNPC npc) {
		super("NPCItemsInv", npc, 12);
	}

	public void setIsEating(boolean flag) {
		isEating = flag;
		theNPC.sendIsEatingToWatchers();
	}

	public boolean getIsEating() {
		return isEating;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("NPCEating", isEating);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isEating = nbt.getBoolean("NPCEating");
		if (isEating) {
			theNPC.setCurrentItemOrArmor(0, getEatingBackup());
			setEatingBackup(null);
			setIsEating(false);
		}
	}

	public ItemStack getIdleItem() {
		ItemStack item = getStackInSlot(IDLE_ITEM);
		return item == null ? null : item.copy();
	}

	public void setIdleItem(ItemStack item) {
		setInventorySlotContents(IDLE_ITEM, item);
	}

	public ItemStack getMeleeWeapon() {
		ItemStack item = getStackInSlot(WEAPON_MELEE);
		return item == null ? null : item.copy();
	}

	public void setMeleeWeapon(ItemStack item) {
		setInventorySlotContents(WEAPON_MELEE, item);
	}

	public ItemStack getRangedWeapon() {
		ItemStack item = getStackInSlot(WEAPON_RANGED);
		return item == null ? null : item.copy();
	}

	public void setRangedWeapon(ItemStack item) {
		setInventorySlotContents(WEAPON_RANGED, item);
	}

	public ItemStack getSpearBackup() {
		ItemStack item = getStackInSlot(SPEAR_BACKUP);
		return item == null ? null : item.copy();
	}

	public void setSpearBackup(ItemStack item) {
		setInventorySlotContents(SPEAR_BACKUP, item);
	}

	public ItemStack getEatingBackup() {
		ItemStack item = getStackInSlot(EATING_BACKUP);
		return item == null ? null : item.copy();
	}

	public void setEatingBackup(ItemStack item) {
		setInventorySlotContents(EATING_BACKUP, item);
	}

	public ItemStack getIdleItemMounted() {
		ItemStack item = getStackInSlot(IDLE_ITEM_MOUNTED);
		return item == null ? null : item.copy();
	}

	public void setIdleItemMounted(ItemStack item) {
		setInventorySlotContents(IDLE_ITEM_MOUNTED, item);
	}

	public ItemStack getMeleeWeaponMounted() {
		ItemStack item = getStackInSlot(WEAPON_MELEE_MOUNTED);
		return item == null ? null : item.copy();
	}

	public void setMeleeWeaponMounted(ItemStack item) {
		setInventorySlotContents(WEAPON_MELEE_MOUNTED, item);
	}

	public ItemStack getReplacedIdleItem() {
		ItemStack item = getStackInSlot(REPLACED_IDLE);
		return item == null ? null : item.copy();
	}

	public void setReplacedIdleItem(ItemStack item) {
		setInventorySlotContents(REPLACED_IDLE, item);
	}

	public ItemStack getReplacedMeleeWeaponMounted() {
		ItemStack item = getStackInSlot(REPLACED_MELEE_MOUNTED);
		return item == null ? null : item.copy();
	}

	public void setReplacedMeleeWeaponMounted(ItemStack item) {
		setInventorySlotContents(REPLACED_MELEE_MOUNTED, item);
	}

	public ItemStack getReplacedIdleItemMounted() {
		ItemStack item = getStackInSlot(REPLACED_IDLE_MOUNTED);
		return item == null ? null : item.copy();
	}

	public void setReplacedIdleItemMounted(ItemStack item) {
		setInventorySlotContents(REPLACED_IDLE_MOUNTED, item);
	}

	public ItemStack getBombingItem() {
		ItemStack item = getStackInSlot(BOMBING_ITEM);
		return item == null ? null : item.copy();
	}

	public void setBombingItem(ItemStack item) {
		setInventorySlotContents(BOMBING_ITEM, item);
	}

	public ItemStack getBomb() {
		ItemStack item = getStackInSlot(BOMB);
		return item == null ? null : item.copy();
	}

	public void setBomb(ItemStack item) {
		setInventorySlotContents(BOMB, item);
	}
}
