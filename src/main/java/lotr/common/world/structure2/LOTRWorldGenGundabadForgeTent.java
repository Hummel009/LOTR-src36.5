package lotr.common.world.structure2;

import java.util.Random;

import net.minecraft.init.Blocks;

public class LOTRWorldGenGundabadForgeTent extends LOTRWorldGenGundabadTent {
	public LOTRWorldGenGundabadForgeTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tentBlock = Blocks.cobblestone;
		tentMeta = 0;
		fenceBlock = Blocks.cobblestone_wall;
		fenceMeta = 0;
		hasOrcForge = true;
	}
}
