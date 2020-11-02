package lotr.common.block;

import java.util.Random;

import lotr.common.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class LOTRBlockRedClay extends Block {
	public LOTRBlockRedClay() {
		super(Material.clay);
		setHardness(0.6f);
		setStepSound(Block.soundTypeGravel);
		setCreativeTab(LOTRCreativeTabs.tabBlock);
	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {
		return LOTRMod.redClayBall;
	}

	@Override
	public int quantityDropped(Random random) {
		return 4;
	}
}
