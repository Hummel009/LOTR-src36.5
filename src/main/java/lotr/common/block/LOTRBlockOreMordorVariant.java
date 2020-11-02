package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTRBlockOreMordorVariant extends LOTRBlockOre {
	@SideOnly(value = Side.CLIENT)
	private IIcon mordorIcon;
	private boolean dropsBlock;

	public LOTRBlockOreMordorVariant(boolean flag) {
		dropsBlock = flag;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		if (j == 1) {
			return mordorIcon;
		}
		return super.getIcon(i, j);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		super.registerBlockIcons(iconregister);
		mordorIcon = iconregister.registerIcon(getTextureName() + "_mordor");
	}

	@Override
	public int damageDropped(int i) {
		if (dropsBlock) {
			return i;
		}
		return super.damageDropped(i);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int j = 0; j <= 1; ++j) {
			list.add(new ItemStack(item, 1, j));
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int i, int j, int k, EntityPlayer entityplayer) {
		return new ItemStack(this, 1, world.getBlockMetadata(i, j, k));
	}
}
