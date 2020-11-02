package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class LOTRBlockSlabClayTile extends LOTRBlockSlabBase {
	public LOTRBlockSlabClayTile(boolean flag) {
		super(flag, Material.rock, 1);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(1.25f);
		setResistance(7.0f);
		setStepSound(Block.soundTypeStone);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return LOTRMod.clayTile.getIcon(i, j &= 7);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
	}
}
