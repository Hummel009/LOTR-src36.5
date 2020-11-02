package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;

public class LOTRWorldGenDolGuldurForgeTent extends LOTRWorldGenDolGuldurTent {
	public LOTRWorldGenDolGuldurForgeTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tentBlock = LOTRMod.brick2;
		tentMeta = 8;
		fenceBlock = LOTRMod.wall2;
		fenceMeta = 8;
		hasOrcForge = true;
	}
}
