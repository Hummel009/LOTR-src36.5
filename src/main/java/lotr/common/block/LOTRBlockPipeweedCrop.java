package lotr.common.block;

import java.util.Random;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.*;
import net.minecraftforge.common.EnumPlantType;

public class LOTRBlockPipeweedCrop extends BlockCrops {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] pipeweedIcons;

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j < 7) {
			if (j == 6) {
				j = 5;
			}
			return pipeweedIcons[j >> 1];
		}
		return pipeweedIcons[3];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		pipeweedIcons = new IIcon[4];
		for (int i = 0; i < pipeweedIcons.length; ++i) {
			pipeweedIcons[i] = iconregister.registerIcon(getTextureName() + "_" + i);
		}
	}

	@Override
	public Item func_149866_i() {
		return LOTRMod.pipeweedSeeds;
	}

	@Override
	public Item func_149865_P() {
		return LOTRMod.pipeweedLeaf;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int i, int j, int k) {
		return EnumPlantType.Crop;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void randomDisplayTick(World world, int i, int j, int k, Random random) {
		if (world.getBlockMetadata(i, j, k) == 7) {
			LOTRMod.pipeweedPlant.randomDisplayTick(world, i, j, k, random);
		}
	}
}
