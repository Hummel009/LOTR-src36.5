package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockFangornPlant extends LOTRBlockFlower {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] plantIcons;
	private String[] plantNames = new String[] { "green", "brown", "gold", "yellow", "red", "silver" };

	public LOTRBlockFangornPlant() {
		setFlowerBounds(0.2f, 0.0f, 0.2f, 0.8f, 0.8f, 0.8f);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= plantNames.length) {
			j = 0;
		}
		return plantIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		plantIcons = new IIcon[plantNames.length];
		for (int i = 0; i < plantNames.length; ++i) {
			plantIcons[i] = iconregister.registerIcon(getTextureName() + "_" + plantNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int j = 0; j < plantNames.length; ++j) {
			list.add(new ItemStack(item, 1, j));
		}
	}
}
