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

public abstract class LOTRBlockPillarBase extends Block {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pillarFaceIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pillarSideIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pillarSideTopIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pillarSideMiddleIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pillarSideBottomIcons;
	private String[] pillarNames;

	public LOTRBlockPillarBase() {
		this(Material.rock);
		setHardness(1.5f);
		setResistance(10.0f);
		setStepSound(Block.soundTypeStone);
	}

	public LOTRBlockPillarBase(Material material) {
		super(material);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	protected void setPillarNames(String... names) {
		pillarNames = names;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side) {
		boolean pillarAbove = isPillarAt(world, i, j + 1, k);
		boolean pillarBelow = isPillarAt(world, i, j - 1, k);
		int meta = world.getBlockMetadata(i, j, k);
		if (meta >= pillarNames.length) {
			meta = 0;
		}
		if (side == 0 || side == 1) {
			return pillarFaceIcons[meta];
		}
		if (pillarAbove && pillarBelow) {
			return pillarSideMiddleIcons[meta];
		}
		if (pillarAbove) {
			return pillarSideBottomIcons[meta];
		}
		if (pillarBelow) {
			return pillarSideTopIcons[meta];
		}
		return pillarSideIcons[meta];
	}

	private boolean isPillarAt(IBlockAccess world, int i, int j, int k) {
		Block block = world.getBlock(i, j, k);
		return block instanceof LOTRBlockPillarBase;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= pillarNames.length) {
			j = 0;
		}
		if (i == 0 || i == 1) {
			return pillarFaceIcons[j];
		}
		return pillarSideIcons[j];
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < pillarNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		pillarFaceIcons = new IIcon[pillarNames.length];
		pillarSideIcons = new IIcon[pillarNames.length];
		pillarSideTopIcons = new IIcon[pillarNames.length];
		pillarSideMiddleIcons = new IIcon[pillarNames.length];
		pillarSideBottomIcons = new IIcon[pillarNames.length];
		for (int i = 0; i < pillarNames.length; ++i) {
			String s = getTextureName() + "_" + pillarNames[i];
			pillarFaceIcons[i] = iconregister.registerIcon(s + "_face");
			pillarSideIcons[i] = iconregister.registerIcon(s + "_side");
			pillarSideTopIcons[i] = iconregister.registerIcon(s + "_sideTop");
			pillarSideMiddleIcons[i] = iconregister.registerIcon(s + "_sideMiddle");
			pillarSideBottomIcons[i] = iconregister.registerIcon(s + "_sideBottom");
		}
	}
}
