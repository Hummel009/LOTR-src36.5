package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenHarnedorPalisade extends LOTRWorldGenHarnedorStructure {
	private boolean isTall = false;

	public LOTRWorldGenHarnedorPalisade(boolean flag) {
		super(flag);
	}

	public void setTall() {
		isTall = true;
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		int randomWood = random.nextInt(3);
		if (randomWood == 0) {
			woodBlock = LOTRMod.wood4;
			woodMeta = 2;
		} else if (randomWood == 1) {
			woodBlock = Blocks.log;
			woodMeta = 0;
		} else if (randomWood == 2) {
			woodBlock = LOTRMod.wood6;
			woodMeta = 3;
		}
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int i1;
		int k1;
		this.setOriginAndRotation(world, i, j, k, rotation, 0);
		setupRandomBlocks(random);
		if (restrictions && !isSurface(world, i1 = 0, getTopBlock(world, i1, k1 = 0) - 1, k1)) {
			return false;
		}
		int height = 3 + random.nextInt(2);
		if (isTall) {
			height += 4;
		}
		if (isRuined()) {
			height = Math.max(1, height - 2);
		}
		for (int j12 = height; (j12 >= 0 || !isOpaque(world, 0, j12, 0)) && getY(j12) >= 0; --j12) {
			setBlockAndMetadata(world, 0, j12, 0, woodBlock, woodMeta);
			setGrassToDirt(world, 0, j12 - 1, 0);
		}
		if (isTall || random.nextInt(5) == 0) {
			setBlockAndMetadata(world, 0, height + 1, 0, fenceBlock, fenceMeta);
			this.placeSkull(world, random, 0, height + 2, 0);
		}
		if (!isRuined() && isTall) {
			placeWallBanner(world, 0, height, 0, LOTRItemBanner.BannerType.NEAR_HARAD, 2);
		}
		return true;
	}
}
