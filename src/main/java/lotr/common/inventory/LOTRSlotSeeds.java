package lotr.common.inventory;

import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.minecraftforge.common.*;

public class LOTRSlotSeeds extends Slot {
	private World worldObj;

	public LOTRSlotSeeds(IInventory inv, int i, int j, int k, World world) {
		super(inv, i, j, k);
		worldObj = world;
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		Item item = itemstack.getItem();
		return item instanceof IPlantable && ((IPlantable) item).getPlantType(worldObj, -1, -1, -1) == EnumPlantType.Crop;
	}
}
