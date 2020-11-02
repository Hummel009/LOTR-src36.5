package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockOrcPlating extends Block {
	@SideOnly(value = Side.CLIENT)
	protected IIcon[] blockIcons;
	protected String[] blockNames = new String[] { "iron", "rust" };

	public LOTRBlockOrcPlating() {
		super(Material.iron);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(3.0f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeMetal);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= blockNames.length) {
			j = 0;
		}
		return blockIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		blockIcons = new IIcon[blockNames.length];
		for (int i = 0; i < blockNames.length; ++i) {
			blockIcons[i] = iconregister.registerIcon(getTextureName() + "_" + blockNames[i]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	public int getRenderType() {
		return LOTRMod.proxy.getOrcPlatingRenderID();
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < blockNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
