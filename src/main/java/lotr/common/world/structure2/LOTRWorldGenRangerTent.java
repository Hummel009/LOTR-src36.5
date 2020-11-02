package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;

public class LOTRWorldGenRangerTent extends LOTRWorldGenTentBase {
	public LOTRWorldGenRangerTent(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		int randomWool = random.nextInt(3);
		if (randomWool == 0) {
			tentBlock = Blocks.wool;
			tentMeta = 13;
		} else if (randomWool == 1) {
			tentBlock = Blocks.wool;
			tentMeta = 12;
		} else if (randomWool == 2) {
			tentBlock = Blocks.wool;
			tentMeta = 7;
		}
		fenceBlock = Blocks.fence;
		fenceMeta = 0;
		tableBlock = LOTRMod.rangerTable;
		chestContents = LOTRChestContents.RANGER_TENT;
	}
}
