package lotr.common.item;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRItemLeaves extends ItemBlock {
	public LOTRItemLeaves(Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int i) {
		return i | 4;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIconFromDamage(int i) {
		return field_150939_a.getIcon(0, i);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemstack, int i) {
		return 16777215;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return super.getUnlocalizedName() + "." + itemstack.getItemDamage();
	}
}
