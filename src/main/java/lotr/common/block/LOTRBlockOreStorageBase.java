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
import net.minecraft.world.IBlockAccess;

public abstract class LOTRBlockOreStorageBase extends Block {
	@SideOnly(value = Side.CLIENT)
	protected IIcon[] oreStorageIcons;
	protected String[] oreStorageNames;

	public LOTRBlockOreStorageBase() {
		super(Material.iron);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
		setHardness(5.0f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeMetal);
	}

	protected void setOreStorageNames(String... names) {
		oreStorageNames = names;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		oreStorageIcons = new IIcon[oreStorageNames.length];
		for (int i = 0; i < oreStorageNames.length; ++i) {
			oreStorageIcons[i] = iconregister.registerIcon(getTextureName() + "_" + oreStorageNames[i]);
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= oreStorageNames.length) {
			j = 0;
		}
		return oreStorageIcons[j];
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < oreStorageNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, int i, int j, int k, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}
}
