package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;

public class LOTRWorldGenMordorForgeTent extends LOTRWorldGenMordorTent {
	public LOTRWorldGenMordorForgeTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tentBlock = LOTRMod.brick;
		tentMeta = 0;
		fenceBlock = LOTRMod.wall;
		fenceMeta = 1;
		hasOrcForge = true;
	}
}
