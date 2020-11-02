package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockThatch extends Block {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] thatchIcons;
	private static String[] thatchNames = new String[] { "thatch", "reed" };

	public LOTRBlockThatch() {
		super(Material.grass);
		setHardness(0.5f);
		setStepSound(Block.soundTypeGrass);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= thatchNames.length) {
			j = 0;
		}
		return thatchIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		thatchIcons = new IIcon[thatchNames.length];
		for (int i = 0; i < thatchNames.length; ++i) {
			thatchIcons[i] = iconregister.registerIcon(getTextureName() + "_" + thatchNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < thatchNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
