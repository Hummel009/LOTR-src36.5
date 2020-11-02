package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityBird;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.world.*;

public class LOTRBlockBirdCage extends LOTRBlockAnimalJar {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] sideIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] topIcons;
	@SideOnly(value = Side.CLIENT)
	private IIcon[] baseIcons;
	private String[] cageTypes;

	public LOTRBlockBirdCage() {
		super(Material.glass);
		setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		setHardness(0.5f);
		setStepSound(Block.soundTypeMetal);
		setCageTypes("bronze", "iron", "silver", "gold");
	}

	protected void setCageTypes(String... s) {
		cageTypes = s;
	}

	@Override
	public boolean canCapture(Entity entity) {
		return entity instanceof LOTREntityBird;
	}

	@Override
	public float getJarEntityHeight() {
		return 0.5f;
	}

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		return true;
	}

	public static boolean isSameBirdCage(IBlockAccess world, int i, int j, int k, int i1, int j1, int k1) {
		Block block = world.getBlock(i, j, k);
		int meta = world.getBlockMetadata(i, j, k);
		Block block1 = world.getBlock(i1, j1, k1);
		int meta1 = world.getBlockMetadata(i1, j1, k1);
		return block instanceof LOTRBlockBirdCage && block == block1 && meta == meta1;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j >= cageTypes.length) {
			j = 0;
		}
		if (i == 0 || i == 1) {
			return topIcons[j];
		}
		if (i == -1) {
			return baseIcons[j];
		}
		return sideIcons[j];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		sideIcons = new IIcon[cageTypes.length];
		topIcons = new IIcon[cageTypes.length];
		baseIcons = new IIcon[cageTypes.length];
		for (int i = 0; i < cageTypes.length; ++i) {
			sideIcons[i] = iconregister.registerIcon(getTextureName() + "_" + cageTypes[i] + "_side");
			topIcons[i] = iconregister.registerIcon(getTextureName() + "_" + cageTypes[i] + "_top");
			baseIcons[i] = iconregister.registerIcon(getTextureName() + "_" + cageTypes[i] + "_base");
		}
	}

	@Override
	public int getRenderType() {
		return LOTRMod.proxy.getBirdCageRenderID();
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < cageTypes.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
