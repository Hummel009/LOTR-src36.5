package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;

public class LOTRWorldGenUrukForgeTent extends LOTRWorldGenUrukTent {
	public LOTRWorldGenUrukForgeTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tentBlock = LOTRMod.brick2;
		tentMeta = 7;
		fenceBlock = LOTRMod.wall2;
		fenceMeta = 7;
		hasOrcForge = true;
	}
}
