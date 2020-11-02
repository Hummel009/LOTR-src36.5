package lotr.common.block;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;

public class LOTRBlockStainedGlassPane extends LOTRBlockGlassPane {
	private IIcon[] paneIcons = new IIcon[16];

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon func_149735_b(int i, int j) {
		return paneIcons[j % paneIcons.length];
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon func_150097_e() {
		return ((LOTRBlockPane) LOTRMod.glassPane).func_150097_e();
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return func_149735_b(i, ~j & 0xF);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister) {
		for (int i = 0; i < paneIcons.length; ++i) {
			paneIcons[i] = iconregister.registerIcon("lotr:stainedGlass_" + ItemDye.field_150921_b[BlockStainedGlassPane.func_150103_c(i)]);
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
		for (int i = 0; i < paneIcons.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
