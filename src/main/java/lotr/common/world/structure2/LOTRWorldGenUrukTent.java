package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;

public class LOTRWorldGenUrukTent extends LOTRWorldGenTentBase {
	public LOTRWorldGenUrukTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		int randomWool = random.nextInt(3);
		if (randomWool == 0) {
			tentBlock = Blocks.wool;
			tentMeta = 15;
		} else if (randomWool == 1) {
			tentBlock = Blocks.wool;
			tentMeta = 12;
		} else if (randomWool == 2) {
			tentBlock = Blocks.wool;
			tentMeta = 7;
		}
		fenceBlock = LOTRMod.fence;
		fenceMeta = 3;
		tableBlock = LOTRMod.urukTable;
		chestContents = LOTRChestContents.URUK_TENT;
		hasOrcTorches = true;
	}
}
