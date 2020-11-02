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

public abstract class LOTRBlockBrickBase extends Block {
	@SideOnly(value = Side.CLIENT)
	protected IIcon[] brickIcons;
	protected String[] brickNames;

	public LOTRBlockBrickBase() {
		super(Material.rock);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(1.5f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeStone);
	}

	protected void setBrickNames(String... names) {
		brickNames = names;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= brickNames.length) {
			j = 0;
		}
		return brickIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		brickIcons = new IIcon[brickNames.length];
		for (int i = 0; i < brickNames.length; ++i) {
			brickIcons[i] = iconregister.registerIcon(getTextureName() + "_" + brickNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < brickNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
