package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockStainedGlass extends LOTRBlockGlass {
	private IIcon[] glassIcons = new IIcon[16];

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return glassIcons[j % glassIcons.length];
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconregister) {
		for (int i = 0; i < glassIcons.length; ++i) {
			glassIcons[i] = iconregister.registerIcon(getTextureName() + "_" + ItemDye.field_150921_b[BlockStainedGlass.func_149997_b(i)]);
		}
	}

	@Override
	public int damageDropped(int i) {
		return i;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < glassIcons.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
