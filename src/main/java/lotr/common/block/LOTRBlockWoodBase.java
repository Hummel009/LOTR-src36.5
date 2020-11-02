package lotr.common.block;

import java.util.*;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public abstract class LOTRBlockWoodBase extends BlockLog {
	@SideOnly(value = Side.CLIENT)
	private IIcon[][] woodIcons;
	private String[] woodNames;

	public LOTRBlockWoodBase() {
		setHardness(2.0f);
		setStepSound(Block.soundTypeWood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	public void setWoodNames(String... s) {
		woodNames = s;
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
		return Item.getItemFromBlock(this);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		int j1 = j & 0xC;
		if ((j &= 3) >= woodNames.length) {
			j = 0;
		}
		if (j1 == 0 && (i == 0 || i == 1) || j1 == 4 && (i == 4 || i == 5) || j1 == 8 && (i == 2 || i == 3)) {
			return woodIcons[j][0];
		}
		return woodIcons[j][1];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		woodIcons = new IIcon[woodNames.length][2];
		for (int i = 0; i < woodNames.length; ++i) {
			woodIcons[i][0] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_top");
			woodIcons[i][1] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_side");
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < woodNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
