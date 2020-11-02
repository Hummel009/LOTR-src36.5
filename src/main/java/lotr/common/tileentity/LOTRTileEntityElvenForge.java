package lotr.common.tileentity;

import lotr.common.LOTRMod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class LOTRTileEntityElvenForge extends LOTRTileEntityAlloyForgeBase {
	@Override
	public String getForgeName() {
		return StatCollector.translateToLocal("container.lotr.elvenForge");
	}

	@Override
	protected ItemStack getAlloySmeltingResult(ItemStack itemstack, ItemStack alloyItem) {
		if (isIron(itemstack) && isCoal(alloyItem)) {
			return new ItemStack(LOTRMod.elfSteel);
		}
		if (isSilver(itemstack) && isMithrilNugget(alloyItem)) {
			return new ItemStack(LOTRMod.ithildin);
		}
		return super.getAlloySmeltingResult(itemstack, alloyItem);
	}
}
