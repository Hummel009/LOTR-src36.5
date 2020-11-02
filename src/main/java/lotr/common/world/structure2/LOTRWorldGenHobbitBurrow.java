package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenHobbitBurrow extends LOTRWorldGenHobbitStructure {
	protected LOTRChestContents burrowLoot;

	protected LOTRFoods foodPool;

	public LOTRWorldGenHobbitBurrow(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		bedBlock = LOTRMod.strawBed;
		burrowLoot = LOTRChestContents.HOBBIT_HOLE_LARDER;
		foodPool = LOTRFoods.HOBBIT;
	}

	@Override
	protected boolean makeWealthy(Random random) {
		return false;
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		setOriginAndRotation(world, i, j, k, rotation, 8);
		setupRandomBlocks(random);
		if (restrictions) {
			for (int m = -9; m <= 9; m++) {
				for (int k1 = -7; k1 <= 8; k1++) {
					int j1 = getTopBlock(world, m, k1) - 1;
					if (!isSurface(world, m, j1, k1)) {
						return false;
					}
				}
			}
		}
		int i1;
		for (i1 = -3; i1 <= 3; i1++) {
			for (int k1 = -7; k1 <= 3; k1++) {
				for (int j1 = 1; j1 <= 3; j1++) {
					setAir(world, i1, j1, k1);
				}
			}
		}
		loadStrScan("hobbit_burrow");
		associateBlockMetaAlias("BRICK", brickBlock, brickMeta);
		associateBlockMetaAlias("FLOOR", floorBlock, floorMeta);
		associateBlockMetaAlias("COBBLE_WALL", Blocks.cobblestone_wall, 0);
		associateBlockMetaAlias("PLANK", plankBlock, plankMeta);
		associateBlockMetaAlias("PLANK_SLAB", plankSlabBlock, plankSlabMeta);
		associateBlockMetaAlias("PLANK_SLAB_INV", plankSlabBlock, plankSlabMeta | 0x8);
		associateBlockAlias("PLANK_STAIR", plankStairBlock);
		associateBlockMetaAlias("FENCE", fenceBlock, fenceMeta);
		associateBlockAlias("FENCE_GATE", fenceGateBlock);
		associateBlockAlias("DOOR", doorBlock);
		associateBlockMetaAlias("BEAM", beamBlock, beamMeta);
		associateBlockMetaAlias("BEAM|4", beamBlock, beamMeta | 0x4);
		associateBlockMetaAlias("BEAM|8", beamBlock, beamMeta | 0x8);
		associateBlockMetaAlias("TABLE", tableBlock, 0);
		associateBlockMetaAlias("CARPET", carpetBlock, carpetMeta);
		addBlockMetaAliasOption("THATCH_FLOOR", 1, LOTRMod.thatchFloor, 0);
		setBlockAliasChance("THATCH_FLOOR", 0.33F);
		associateBlockMetaAlias("LEAF", Blocks.leaves, 4);
		generateStrScan(world, random, 0, 0, 0);
		setBlockAndMetadata(world, -2, 1, -2, bedBlock, 3);
		setBlockAndMetadata(world, -3, 1, -2, bedBlock, 11);
		setBlockAndMetadata(world, -2, 1, -1, bedBlock, 3);
		setBlockAndMetadata(world, -3, 1, -1, bedBlock, 11);
		placeChest(world, random, -3, 1, 0, 4, burrowLoot, MathHelper.getRandomIntegerInRange(random, 1, 3));
		placeChest(world, random, 1, 1, 2, 2, burrowLoot, MathHelper.getRandomIntegerInRange(random, 1, 3));
		placeChest(world, random, 0, 1, 2, 2, burrowLoot, MathHelper.getRandomIntegerInRange(random, 1, 3));
		placePlateWithCertainty(world, random, 3, 2, -1, plateBlock, foodPool);
		placeSign(world, 0, 2, -4, Blocks.wall_sign, 2, new String[] { "", homeName1, homeName2, "" });
		for (i1 = -8; i1 <= 8; i1++) {
			for (int k1 = -6; k1 <= 8; k1++) {
				int j1 = getTopBlock(world, i1, k1);
				if (j1 >= 1) {
					if (isAir(world, i1, j1, k1) && getBlock(world, i1, j1 - 1, k1) == Blocks.grass) {
						if (random.nextInt(20) == 0) {
							plantFlower(world, random, i1, j1, k1);
						} else if (random.nextInt(7) == 0) {
							plantTallGrass(world, random, i1, j1, k1);
						}
					}
				}
			}
		}
		spawnHobbitCouple(world, 0, 1, 0, 16);
		spawnItemFrame(world, -4, 2, 0, 1, getRandomHobbitDecoration(world, random));
		return true;
	}
}
