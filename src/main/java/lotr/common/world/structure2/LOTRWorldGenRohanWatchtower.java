package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenRohanWatchtower extends LOTRWorldGenRohanStructure {
	public LOTRWorldGenRohanWatchtower(boolean flag) {
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int i1;
		int j1;
		int k1;
		int j12;
		int k12;
		this.setOriginAndRotation(world, i, j, k, rotation, 0);
		setupRandomBlocks(random);
		int height = 7 + random.nextInt(3);
		originY += height;
		if (restrictions) {
			for (int i12 = -4; i12 <= 4; ++i12) {
				for (k1 = -4; k1 <= 4; ++k1) {
					j1 = getTopBlock(world, i12, k1) - 1;
					if (isSurface(world, i12, j1, k1)) {
						continue;
					}
					return false;
				}
			}
		}
		int[] i12 = new int[] { -3, 3 };
		k1 = i12.length;
		for (j1 = 0; j1 < k1; ++j1) {
			int i13 = i12[j1];
			for (int k13 : new int[] { -3, 3 }) {
				int j13 = 3;
				while (!isOpaque(world, i13, j13, k13) && getY(j13) >= 0) {
					setBlockAndMetadata(world, i13, j13, k13, plank2Block, plank2Meta);
					setGrassToDirt(world, i13, j13 - 1, k13);
					--j13;
				}
			}
		}
		for (i1 = -2; i1 <= 2; ++i1) {
			for (k1 = -2; k1 <= 2; ++k1) {
				setBlockAndMetadata(world, i1, 0, k1, plankBlock, plankMeta);
			}
		}
		for (i1 = -3; i1 <= 3; ++i1) {
			for (k1 = -3; k1 <= 3; ++k1) {
				setBlockAndMetadata(world, i1, 4, k1, plankBlock, plankMeta);
			}
		}
		for (i1 = -2; i1 <= 2; ++i1) {
			setBlockAndMetadata(world, i1, 0, -3, logBlock, logMeta | 4);
			setBlockAndMetadata(world, i1, 0, 3, logBlock, logMeta | 4);
			setBlockAndMetadata(world, i1, 4, -3, logBlock, logMeta | 4);
			setBlockAndMetadata(world, i1, 4, 3, logBlock, logMeta | 4);
			setBlockAndMetadata(world, i1, 0, -4, plankStairBlock, 6);
			setBlockAndMetadata(world, i1, 0, 4, plankStairBlock, 7);
			setBlockAndMetadata(world, i1, 1, -4, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, i1, 1, 4, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, i1, 3, -3, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, i1, 3, 3, fenceBlock, fenceMeta);
		}
		for (k12 = -2; k12 <= 2; ++k12) {
			setBlockAndMetadata(world, -3, 0, k12, logBlock, logMeta | 8);
			setBlockAndMetadata(world, 3, 0, k12, logBlock, logMeta | 8);
			setBlockAndMetadata(world, -3, 4, k12, logBlock, logMeta | 8);
			setBlockAndMetadata(world, 3, 4, k12, logBlock, logMeta | 8);
			setBlockAndMetadata(world, -4, 0, k12, plankStairBlock, 5);
			setBlockAndMetadata(world, 4, 0, k12, plankStairBlock, 4);
			setBlockAndMetadata(world, -4, 1, k12, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, 4, 1, k12, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, -3, 3, k12, fenceBlock, fenceMeta);
			setBlockAndMetadata(world, 3, 3, k12, fenceBlock, fenceMeta);
		}
		setBlockAndMetadata(world, -3, 1, -2, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, -3, 1, 2, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, 3, 1, -2, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, 3, 1, 2, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, -2, 1, -3, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, 2, 1, -3, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, -2, 1, 3, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, 2, 1, 3, fenceBlock, fenceMeta);
		setBlockAndMetadata(world, -3, 2, -2, Blocks.torch, 3);
		setBlockAndMetadata(world, -3, 2, 2, Blocks.torch, 4);
		setBlockAndMetadata(world, 3, 2, -2, Blocks.torch, 3);
		setBlockAndMetadata(world, 3, 2, 2, Blocks.torch, 4);
		setBlockAndMetadata(world, -2, 2, -3, Blocks.torch, 2);
		setBlockAndMetadata(world, 2, 2, -3, Blocks.torch, 1);
		setBlockAndMetadata(world, -2, 2, 3, Blocks.torch, 2);
		setBlockAndMetadata(world, 2, 2, 3, Blocks.torch, 1);
		setBlockAndMetadata(world, -1, 4, 0, logBlock, logMeta | 8);
		setBlockAndMetadata(world, 1, 4, 0, logBlock, logMeta | 8);
		setBlockAndMetadata(world, 0, 4, -1, logBlock, logMeta | 4);
		setBlockAndMetadata(world, 0, 4, 1, logBlock, logMeta | 4);
		for (i1 = -4; i1 <= 4; ++i1) {
			setBlockAndMetadata(world, i1, 4, -4, plankStairBlock, 2);
			setBlockAndMetadata(world, i1, 4, 4, plankStairBlock, 3);
		}
		for (k12 = -4; k12 <= 4; ++k12) {
			setBlockAndMetadata(world, -4, 4, k12, plankStairBlock, 1);
			setBlockAndMetadata(world, 4, 4, k12, plankStairBlock, 0);
		}
		for (j12 = 0; j12 <= 3; ++j12) {
			setBlockAndMetadata(world, 0, j12, 3, plank2Block, plank2Meta);
			setBlockAndMetadata(world, 0, j12, 2, Blocks.ladder, 2);
		}
		j12 = -1;
		while (!isOpaque(world, 0, j12, 3) && getY(j12) >= 0) {
			setBlockAndMetadata(world, 0, j12, 3, plank2Block, plank2Meta);
			setGrassToDirt(world, 0, j12 - 1, 3);
			if (!isOpaque(world, 0, j12, 2)) {
				setBlockAndMetadata(world, 0, j12, 2, Blocks.ladder, 2);
			}
			--j12;
		}
		this.placeChest(world, random, -2, 1, 2, 2, LOTRChestContents.ROHAN_WATCHTOWER);
		setBlockAndMetadata(world, 2, 1, 2, tableBlock, 0);
		for (k12 = -2; k12 <= 2; ++k12) {
			int k2 = Math.abs(k12);
			for (int i14 : new int[] { -3, 3 }) {
				int j14 = -1;
				while (!isOpaque(world, i14, j14, k12) && getY(j14) >= 0) {
					if (k2 == 2 && IntMath.mod(j14, 4) == 1 || k2 == 1 && IntMath.mod(j14, 2) == 0 || k2 == 0 && IntMath.mod(j14, 4) == 3) {
						setBlockAndMetadata(world, i14, j14, k12, logBlock, logMeta);
						if (k2 == 0) {
							setBlockAndMetadata(world, i14 - 1 * Integer.signum(i14), j14, k12, Blocks.torch, i14 > 0 ? 1 : 2);
						}
					}
					--j14;
				}
			}
		}
		int belowTop = getBelowTop(world, 2, -1, 2);
		this.placeChest(world, random, 2, belowTop, 2, 5, LOTRChestContents.ROHAN_WATCHTOWER);
		belowTop = getBelowTop(world, 2, -1, 0);
		setBlockAndMetadata(world, 2, belowTop, 0, plankBlock, plankMeta);
		setGrassToDirt(world, 2, belowTop - 1, 0);
		this.placeBarrel(world, random, 2, belowTop + 1, 0, 5, LOTRFoods.ROHAN_DRINK);
		belowTop = getBelowTop(world, -2, -1, 1);
		setBlockAndMetadata(world, -2, belowTop, 1, Blocks.hay_block, 0);
		setGrassToDirt(world, -2, belowTop - 1, 1);
		belowTop = getBelowTop(world, -2, -1, 2);
		setBlockAndMetadata(world, -2, belowTop, 2, Blocks.hay_block, 0);
		setBlockAndMetadata(world, -2, belowTop + 1, 2, Blocks.hay_block, 0);
		setGrassToDirt(world, -2, belowTop - 1, 2);
		belowTop = getBelowTop(world, -1, -1, 2);
		setBlockAndMetadata(world, -1, belowTop, 2, Blocks.hay_block, 0);
		setGrassToDirt(world, -1, belowTop - 1, 2);
		int soldiers = 1 + random.nextInt(3);
		for (int l = 0; l < soldiers; ++l) {
			LOTREntityRohirrimWarrior rohirrim = random.nextInt(3) == 0 ? new LOTREntityRohirrimArcher(world) : new LOTREntityRohirrimWarrior(world);
			rohirrim.spawnRidingHorse = false;
			spawnNPCAndSetHome(rohirrim, world, 0, 1, 0, 4);
		}
		return true;
	}

	private int getBelowTop(World world, int i, int j, int k) {
		while (!isOpaque(world, i, j, k) && getY(j) >= 0) {
			--j;
		}
		return j + 1;
	}
}
