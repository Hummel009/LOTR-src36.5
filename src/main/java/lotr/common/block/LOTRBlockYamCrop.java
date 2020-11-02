package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.*;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTRBlockYamCrop extends BlockCrops {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] yamIcons;

	@Override
	public boolean canBlockStay(World world, int i, int j, int k) {
		if (world.getBlockMetadata(i, j, k) == 8) {
			return world.getBlock(i, j - 1, k).canSustainPlant(world, i, j - 1, k, ForgeDirection.UP, Blocks.tallgrass);
		}
		return super.canBlockStay(world, i, j, k);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j < 7) {
			if (j == 6) {
				j = 5;
			}
			return yamIcons[j >> 1];
		}
		return yamIcons[3];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		yamIcons = new IIcon[4];
		for (int i = 0; i < yamIcons.length; ++i) {
			yamIcons[i] = iconregister.registerIcon(getTextureName() + "_" + i);
		}
	}

	@Override
	public Item func_149866_i() {
		return LOTRMod.yam;
	}

	@Override
	public Item func_149865_P() {
		return LOTRMod.yam;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int i, int j, int k) {
		return EnumPlantType.Crop;
	}
}
