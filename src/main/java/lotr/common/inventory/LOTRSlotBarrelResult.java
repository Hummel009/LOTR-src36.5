package lotr.common.inventory;

import cpw.mods.fml.relauncher.*;
import lotr.common.item.LOTRItemMug;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class LOTRSlotBarrelResult extends Slot {
	public LOTRSlotBarrelResult(IInventory inv, int i, int j, int k) {
		super(inv, i, j, k);
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		return false;
	}

	@Override
	public boolean canTakeStack(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getBackgroundIconIndex() {
		IIcon iIcon;
		if (getSlotIndex() > 5) {
			iIcon = LOTRItemMug.barrelGui_emptyMugSlotIcon;
		} else {
			iIcon = null;
		}
		return iIcon;
	}
}
