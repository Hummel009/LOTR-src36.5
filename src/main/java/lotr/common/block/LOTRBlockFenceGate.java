package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRCreativeTabs;
import net.minecraft.block.*;
import net.minecraft.util.IIcon;

public class LOTRBlockFenceGate extends BlockFenceGate {
	private Block plankBlock;
	private int plankMeta;

	public LOTRBlockFenceGate(Block block, int meta) {
		plankBlock = block;
		plankMeta = meta;
		setCreativeTab(LOTRCreativeTabs.tabUtil);
		setHardness(2.0f);
		setResistance(5.0f);
		setStepSound(soundTypeWood);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return plankBlock.getIcon(i, plankMeta);
	}
}
