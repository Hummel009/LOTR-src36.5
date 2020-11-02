package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockHaradFlower extends LOTRBlockFlower {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] flowerIcons;
	private static String[] flowerNames = new String[] { "red", "yellow", "daisy", "pink" };

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= flowerNames.length) {
			j = 0;
		}
		return flowerIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		flowerIcons = new IIcon[flowerNames.length];
		for (int i = 0; i < flowerNames.length; ++i) {
			flowerIcons[i] = iconregister.registerIcon(getTextureName() + "_" + flowerNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int j = 0; j < flowerNames.length; ++j) {
			list.add(new ItemStack(item, 1, j));
		}
	}
}
