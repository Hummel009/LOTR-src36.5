package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public abstract class LOTRBlockWoodBeam extends BlockRotatedPillar {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] sideIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] topIcons;
	private String[] woodNames;

	public LOTRBlockWoodBeam() {
		super(Material.wood);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(2.0f);
		setStepSound(Block.soundTypeWood);
	}

	protected void setWoodNames(String... s) {
		woodNames = s;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		sideIcons = new IIcon[woodNames.length];
		topIcons = new IIcon[woodNames.length];
		for (int i = 0; i < woodNames.length; ++i) {
			topIcons[i] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_top");
			sideIcons[i] = iconregister.registerIcon(getTextureName() + "_" + woodNames[i] + "_side");
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int j = 0; j < woodNames.length; ++j) {
			list.add(new ItemStack(item, 1, j));
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	protected IIcon getSideIcon(int i) {
		if (i < 0 || i >= woodNames.length) {
			i = 0;
		}
		return sideIcons[i];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	protected IIcon getTopIcon(int i) {
		if (i < 0 || i >= woodNames.length) {
			i = 0;
		}
		return topIcons[i];
	}

	@Override
	public int getRenderType() {
		return LOTRMod.proxy.getBeamRenderID();
	}
}
