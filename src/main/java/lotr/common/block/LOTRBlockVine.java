package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.world.IBlockAccess;

public class LOTRBlockVine extends BlockVine {
	public LOTRBlockVine() {
		setCreativeTab(LOTRCreativeTabs.tabDeco);
		setHardness(0.2f);
		setStepSound(Block.soundTypeGrass);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int getBlockColor() {
		return 16777215;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int getRenderColor(int i) {
		return 16777215;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, int i, int j, int k) {
		return 16777215;
	}
}
