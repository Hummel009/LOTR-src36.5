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

public class LOTRBlockDirtPath extends Block {
	@SideOnly(value = Side.CLIENT)
	protected IIcon[] pathIcons;
	protected String[] pathNames = new String[] { "dirt", "mud" };

	public LOTRBlockDirtPath() {
		super(Material.ground);
		setHardness(0.5f);
		setStepSound(Block.soundTypeGravel);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= pathNames.length) {
			j = 0;
		}
		return pathIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		pathIcons = new IIcon[pathNames.length];
		for (int i = 0; i < pathNames.length; ++i) {
			pathIcons[i] = iconregister.registerIcon(getTextureName() + "_" + pathNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < pathNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
