package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class LOTRBlockLeekCrop extends BlockCrops {
	@SideOnly(value = Side.CLIENT)
	private IIcon[] leekIcons;

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j < 7) {
			if (j == 6) {
				j = 5;
			}
			return leekIcons[j >> 1];
		}
		return leekIcons[3];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		leekIcons = new IIcon[4];
		for (int i = 0; i < leekIcons.length; ++i) {
			leekIcons[i] = iconregister.registerIcon(getTextureName() + "_" + i);
		}
	}

	@Override
	public Item func_149866_i() {
		return LOTRMod.leek;
	}

	@Override
	public Item func_149865_P() {
		return LOTRMod.leek;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, int i, int j, int k) {
		return EnumPlantType.Crop;
	}
}
