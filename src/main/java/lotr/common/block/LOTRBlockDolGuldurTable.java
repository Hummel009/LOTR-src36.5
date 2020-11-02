package lotr.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import lotr.common.fac.LOTRFaction;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class LOTRBlockDolGuldurTable extends LOTRBlockCraftingTable {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] tableIcons;

	public LOTRBlockDolGuldurTable() {
		super(Material.rock, LOTRFaction.DOL_GULDUR, 30);
		setStepSound(Block.soundTypeStone);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (i == 1) {
			return tableIcons[1];
		}
		if (i == 0) {
			return LOTRMod.brick2.getIcon(0, 8);
		}
		return tableIcons[0];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		tableIcons = new IIcon[2];
		tableIcons[0] = iconregister.registerIcon(getTextureName() + "_side");
		tableIcons[1] = iconregister.registerIcon(getTextureName() + "_top");
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (random.nextInt(20) == 0) {
			for (int l = 0; l < 16; ++l) {
				double d = i + 0.25 + random.nextFloat() * 0.5f;
				double d1 = j + 1.0;
				double d2 = k + 0.25 + random.nextFloat() * 0.5f;
				double d3 = -0.05 + random.nextFloat() * 0.1;
				double d4 = 0.1 + random.nextFloat() * 0.1;
				double d5 = -0.05 + random.nextFloat() * 0.1;
				LOTRMod.proxy.spawnParticle("morgulPortal", d, d1, d2, d3, d4, d5);
			}
		}
	}
}
