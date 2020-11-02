package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRWorldGenHobbitHouse extends LOTRWorldGenHobbitStructure {
	public LOTRWorldGenHobbitHouse(boolean flag) {
		super(flag);
	}

	@Override
	protected boolean makeWealthy(Random random) {
		return false;
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		if (random.nextBoolean()) {
			bedBlock = LOTRMod.strawBed;
		} else {
			bedBlock = Blocks.bed;
		}
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		setOriginAndRotation(world, i, j, k, rotation, 11, 1);
		setupRandomBlocks(random);
		if (restrictions) {
			int minHeight = 0;
			int maxHeight = 0;
			for (int i20 = -9; i20 <= 8; i20++) {
				for (int i21 = -10; i21 <= 6; i21++) {
					int i22 = getTopBlock(world, i20, i21) - 1;
					if (!isSurface(world, i20, i22, i21)) {
						return false;
					}
					if (i22 < minHeight) {
						minHeight = i22;
					}
					if (i22 > maxHeight) {
						maxHeight = i22;
					}
				}
			}
			if (maxHeight - minHeight > 6) {
				return false;
			}
		}
		int i19;
		for (i19 = -4; i19 <= 3; i19++) {
			for (int i20 = -10; i20 <= -6; i20++) {
				int i21;
				for (i21 = 0; (i21 >= 0 || !isOpaque(world, i19, i21, i20)) && getY(i21) >= 0; i21--) {
					if (i21 == 0) {
						setBlockAndMetadata(world, i19, i21, i20, Blocks.grass, 0);
					} else {
						setBlockAndMetadata(world, i19, i21, i20, Blocks.dirt, 0);
					}
					setGrassToDirt(world, i19, i21 - 1, i20);
				}
				for (i21 = 1; i21 <= 5; i21++) {
					setAir(world, i19, i21, i20);
				}
				if (i19 == -4 || i19 == 3 || i20 == -10) {
					setBlockAndMetadata(world, i19, 1, i20, outFenceBlock, outFenceMeta);
				}
			}
		}
		for (i19 = -9; i19 <= 8; i19++) {
			for (int i20 = -6; i20 <= 6; i20++) {
				int k2 = Math.abs(i20);
				boolean beam = false;
				boolean wall = false;
				boolean indoors = false;
				if ((i19 == -7 || i19 == 6) && k2 == 4) {
					beam = true;
				} else if ((i19 == -9 || i19 == 8) && k2 == 0) {
					beam = true;
				} else if ((i19 == -3 || i19 == 2) && k2 == 6) {
					beam = true;
				} else if (i19 >= -6 && i19 <= 5 && k2 <= 5 || k2 <= 3 && i19 >= -8 && i19 <= 7) {
					if (i19 == -8 || i19 == 7 || k2 == 5) {
						wall = true;
					} else {
						indoors = true;
					}
				}
				if (beam || wall || indoors) {
					int i21;
					for (i21 = 1; i21 <= 6; i21++) {
						setAir(world, i19, i21, i20);
					}
					if (beam) {
						for (i21 = 2; (i21 >= 0 || !isOpaque(world, i19, i21, i20)) && getY(i21) >= 0; i21--) {
							setBlockAndMetadata(world, i19, i21, i20, beamBlock, beamMeta);
							setGrassToDirt(world, i19, i21 - 1, i20);
						}
					} else if (wall) {
						for (i21 = 0; (i21 >= 0 || !isOpaque(world, i19, i21, i20)) && getY(i21) >= 0; i21--) {
							setBlockAndMetadata(world, i19, i21, i20, plankBlock, plankMeta);
							setGrassToDirt(world, i19, i21 - 1, i20);
						}
						for (i21 = 1; i21 <= 2; i21++) {
							setBlockAndMetadata(world, i19, i21, i20, wallBlock, wallMeta);
						}
					} else if (indoors) {
						for (i21 = 0; (i21 >= 0 || !isOpaque(world, i19, i21, i20)) && getY(i21) >= 0; i21--) {
							setBlockAndMetadata(world, i19, i21, i20, floorBlock, floorMeta);
							setGrassToDirt(world, i19, i21 - 1, i20);
						}
						setBlockAndMetadata(world, i19, 3, i20, plankSlabBlock, plankSlabMeta | 0x8);
					}
				}
			}
		}
		for (i19 = -2; i19 <= 1; i19++) {
			for (int i20 = 1; i20 <= 3; i20++) {
				setBlockAndMetadata(world, i19, i20, -5, Blocks.brick_block, 0);
			}
		}
		for (i19 = -1; i19 <= 0; i19++) {
			setBlockAndMetadata(world, i19, 0, -5, floorBlock, floorMeta);
			for (int i20 = 1; i20 <= 2; i20++) {
				setBlockAndMetadata(world, i19, i20, -5, gateBlock, 2);
			}
		}
		setBlockAndMetadata(world, 1, 2, -4, Blocks.tripwire_hook, 0);
		for (int i18 = 1; i18 <= 3; i18++) {
			setBlockAndMetadata(world, -3, i18, -4, beamBlock, beamMeta);
			setBlockAndMetadata(world, -7, i18, 0, beamBlock, beamMeta);
			for (int i20 = -6; i20 <= -4; i20++) {
				setBlockAndMetadata(world, i20, i18, 0, plankBlock, plankMeta);
			}
			setBlockAndMetadata(world, -3, i18, 0, beamBlock, beamMeta);
			setBlockAndMetadata(world, -3, i18, 4, beamBlock, beamMeta);
			setBlockAndMetadata(world, 2, i18, 4, beamBlock, beamMeta);
			setBlockAndMetadata(world, 6, i18, 0, beamBlock, beamMeta);
			setBlockAndMetadata(world, 2, i18, -4, beamBlock, beamMeta);
		}
		setBlockAndMetadata(world, -2, 3, -4, plankBlock, plankMeta);
		setBlockAndMetadata(world, -3, 3, -3, plankBlock, plankMeta);
		for (int i17 = -6; i17 <= -4; i17++) {
			setBlockAndMetadata(world, i17, 3, -4, plankBlock, plankMeta);
		}
		int i16;
		for (i16 = -3; i16 <= -1; i16++) {
			setBlockAndMetadata(world, -7, 3, i16, plankBlock, plankMeta);
		}
		for (i16 = 1; i16 <= 3; i16++) {
			setBlockAndMetadata(world, -7, 3, i16, plankBlock, plankMeta);
		}
		for (int i15 = -6; i15 <= -4; i15++) {
			setBlockAndMetadata(world, i15, 3, 4, plankBlock, plankMeta);
		}
		for (int i14 = 1; i14 <= 3; i14++) {
			setBlockAndMetadata(world, -3, 3, i14, plankBlock, plankMeta);
		}
		int i13;
		for (i13 = -2; i13 <= 1; i13++) {
			setBlockAndMetadata(world, i13, 3, 4, plankBlock, plankMeta);
		}
		setBlockAndMetadata(world, 2, 3, 3, plankBlock, plankMeta);
		for (i13 = 3; i13 <= 5; i13++) {
			setBlockAndMetadata(world, i13, 3, 4, Blocks.double_stone_slab, 0);
		}
		int i12;
		for (i12 = 1; i12 <= 3; i12++) {
			setBlockAndMetadata(world, 6, 3, i12, Blocks.double_stone_slab, 0);
		}
		setBlockAndMetadata(world, 5, 3, 0, plankBlock, plankMeta);
		for (i12 = -3; i12 <= -1; i12++) {
			setBlockAndMetadata(world, 6, 3, i12, plankBlock, plankMeta);
		}
		for (int i11 = 3; i11 <= 5; i11++) {
			setBlockAndMetadata(world, i11, 3, -4, plankBlock, plankMeta);
		}
		setBlockAndMetadata(world, 2, 3, -3, plankBlock, plankMeta);
		setBlockAndMetadata(world, 1, 3, -4, plankBlock, plankMeta);
		setBlockAndMetadata(world, -4, 1, -4, plank2Block, plank2Meta);
		setBlockAndMetadata(world, -4, 2, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, -5, 2, -5, LOTRMod.glassPane, 0);
		placeChest(world, random, -6, 1, -4, 3, LOTRChestContents.HOBBIT_HOLE_STUDY);
		for (int j1 = 1; j1 <= 2; j1++) {
			setBlockAndMetadata(world, -7, j1, -3, Blocks.bookshelf, 0);
			setBlockAndMetadata(world, -7, j1, -1, Blocks.bookshelf, 0);
		}
		setBlockAndMetadata(world, -7, 1, -2, plank2SlabBlock, plank2SlabMeta | 0x8);
		setBlockAndMetadata(world, -5, 1, -2, Blocks.oak_stairs, 1);
		spawnItemFrame(world, -5, 2, 0, 2, new ItemStack(Items.clock));
		setBlockAndMetadata(world, -3, 1, 1, plankStairBlock, 3);
		setBlockAndMetadata(world, -3, 2, 1, plankStairBlock, 7);
		setBlockAndMetadata(world, -3, 1, 3, plankStairBlock, 2);
		setBlockAndMetadata(world, -3, 2, 3, plankStairBlock, 6);
		for (int i20 : new int[] { -6, -5 }) {
			setBlockAndMetadata(world, i20, 1, 3, bedBlock, 0);
			setBlockAndMetadata(world, i20, 1, 4, bedBlock, 8);
		}
		setBlockAndMetadata(world, -4, 1, 4, plank2Block, plank2Meta);
		setBlockAndMetadata(world, -4, 2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, -7, 1, 1, beamBlock, beamMeta);
		placeBarrel(world, random, -7, 2, 1, 4, LOTRFoods.HOBBIT_DRINK);
		setBlockAndMetadata(world, -1, 2, 5, LOTRMod.glassPane, 0);
		setBlockAndMetadata(world, 0, 2, 5, LOTRMod.glassPane, 0);
		setBlockAndMetadata(world, 2, 2, 3, Blocks.torch, 4);
		setBlockAndMetadata(world, 3, 1, 4, tableBlock, 0);
		int i10;
		for (i10 = 4; i10 <= 5; i10++) {
			placeChest(world, random, i10, 1, 4, 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		}
		setBlockAndMetadata(world, 6, 1, 3, Blocks.crafting_table, 0);
		setBlockAndMetadata(world, 6, 1, 2, Blocks.cauldron, 3);
		setBlockAndMetadata(world, 6, 1, 1, LOTRMod.hobbitOven, 5);
		setBlockAndMetadata(world, 6, 2, -1, Blocks.torch, 1);
		setBlockAndMetadata(world, 4, 2, -5, LOTRMod.glassPane, 0);
		setBlockAndMetadata(world, 3, 2, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, 6, 1, -1, plankStairBlock, 2);
		setBlockAndMetadata(world, 6, 1, -2, plankStairBlock, 1);
		setBlockAndMetadata(world, 6, 1, -3, plankStairBlock, 3);
		setBlockAndMetadata(world, 5, 1, -4, plankStairBlock, 1);
		setBlockAndMetadata(world, 4, 1, -4, plankStairBlock, 3);
		setBlockAndMetadata(world, 3, 1, -4, plankStairBlock, 0);
		for (i10 = 3; i10 <= 4; i10++) {
			for (int i20 = -2; i20 <= -1; i20++) {
				setBlockAndMetadata(world, i10, 1, i20, Blocks.planks, 1);
				if (random.nextBoolean()) {
					placePlateWithCertainty(world, random, i10, 2, i20, plateBlock, LOTRFoods.HOBBIT);
				} else {
					placeMug(world, random, i10, 2, i20, random.nextInt(4), LOTRFoods.HOBBIT_DRINK);
				}
			}
		}
		for (i10 = -1; i10 <= 0; i10++) {
			for (int i20 = -2; i20 <= 1; i20++) {
				setBlockAndMetadata(world, i10, 1, i20, carpetBlock, carpetMeta);
			}
		}
		for (i10 = -2; i10 <= 1; i10++) {
			setBlockAndMetadata(world, i10, 0, -6, pathBlock, pathMeta);
		}
		setBlockAndMetadata(world, -1, 0, -7, pathBlock, pathMeta);
		setBlockAndMetadata(world, 0, 0, -7, pathBlock, pathMeta);
		setBlockAndMetadata(world, 0, 0, -8, pathBlock, pathMeta);
		setBlockAndMetadata(world, 1, 0, -8, pathBlock, pathMeta);
		setBlockAndMetadata(world, 1, 0, -9, pathBlock, pathMeta);
		setBlockAndMetadata(world, 1, 0, -10, pathBlock, pathMeta);
		setAir(world, 1, 1, -10);
		for (i10 = -3; i10 <= 2; i10++) {
			for (int i20 = -9; i20 <= -7; i20++) {
				if (getBlock(world, i10, 0, i20) == Blocks.grass) {
					if (random.nextInt(4) == 0) {
						plantFlower(world, random, i10, 1, i20);
					}
				}
			}
		}
		placeHedge(world, -7, 1, -5);
		placeHedge(world, -8, 1, -4);
		int i9;
		for (i9 = -3; i9 <= -1; i9++) {
			placeHedge(world, -9, 1, i9);
		}
		for (i9 = 1; i9 <= 3; i9++) {
			placeHedge(world, -9, 1, i9);
		}
		placeHedge(world, -8, 1, 4);
		placeHedge(world, -7, 1, 5);
		int i8;
		for (i8 = -6; i8 <= -4; i8++) {
			placeHedge(world, i8, 1, 6);
		}
		for (i8 = 3; i8 <= 5; i8++) {
			placeHedge(world, i8, 1, 6);
		}
		placeHedge(world, 6, 1, 5);
		placeHedge(world, 7, 1, 4);
		int i7;
		for (i7 = 1; i7 <= 3; i7++) {
			placeHedge(world, 8, 1, i7);
		}
		for (i7 = -3; i7 <= -1; i7++) {
			placeHedge(world, 8, 1, i7);
		}
		placeHedge(world, 7, 1, -4);
		placeHedge(world, 6, 1, -5);
		int i6;
		for (i6 = -2; i6 <= 1; i6++) {
			setBlockAndMetadata(world, i6, 1, 6, Blocks.brick_block, 0);
			setGrassToDirt(world, i6, 0, 6);
			placeFlowerPot(world, i6, 2, 6, getRandomFlower(world, random));
		}
		for (i6 = -6; i6 <= 5; i6++) {
			setBlockAndMetadata(world, i6, 3, -6, roofSlabBlock, roofSlabMeta);
			setBlockAndMetadata(world, i6, 3, 6, roofSlabBlock, roofSlabMeta);
		}
		for (int i5 = -3; i5 <= 3; i5++) {
			setBlockAndMetadata(world, -9, 3, i5, roofSlabBlock, roofSlabMeta);
			setBlockAndMetadata(world, 8, 3, i5, roofSlabBlock, roofSlabMeta);
		}
		setBlockAndMetadata(world, -7, 3, -5, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, -8, 3, -4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, -8, 3, 4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, -7, 3, 5, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, 6, 3, 5, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, 7, 3, 4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, 7, 3, -4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, 6, 3, -5, roofSlabBlock, roofSlabMeta);
		for (int i4 = -6; i4 <= 5; i4++) {
			setBlockAndMetadata(world, i4, 3, -5, roofBlock, roofMeta);
			setBlockAndMetadata(world, i4, 3, 5, roofBlock, roofMeta);
		}
		for (int i3 = -3; i3 <= 3; i3++) {
			setBlockAndMetadata(world, -8, 3, i3, roofBlock, roofMeta);
			setBlockAndMetadata(world, 7, 3, i3, roofBlock, roofMeta);
		}
		setBlockAndMetadata(world, -7, 3, -4, roofBlock, roofMeta);
		setBlockAndMetadata(world, 6, 3, -4, roofBlock, roofMeta);
		setBlockAndMetadata(world, -7, 3, 4, roofBlock, roofMeta);
		setBlockAndMetadata(world, 6, 3, 4, roofBlock, roofMeta);
		for (int i2 = -5; i2 <= 4; i2++) {
			setBlockAndMetadata(world, i2, 4, -4, roofStairBlock, 2);
			setBlockAndMetadata(world, i2, 4, 4, roofStairBlock, 3);
		}
		for (int n = -2; n <= 2; n++) {
			setBlockAndMetadata(world, -7, 4, n, roofStairBlock, 1);
			setBlockAndMetadata(world, 6, 4, n, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -6, 4, -4, roofStairBlock, 1);
		setBlockAndMetadata(world, -6, 4, -3, roofStairBlock, 2);
		setBlockAndMetadata(world, -7, 4, -3, roofStairBlock, 1);
		setBlockAndMetadata(world, 5, 4, -4, roofStairBlock, 0);
		setBlockAndMetadata(world, 5, 4, -3, roofStairBlock, 2);
		setBlockAndMetadata(world, 6, 4, -3, roofStairBlock, 0);
		setBlockAndMetadata(world, -6, 4, 4, roofStairBlock, 1);
		setBlockAndMetadata(world, -6, 4, 3, roofStairBlock, 3);
		setBlockAndMetadata(world, -7, 4, 3, roofStairBlock, 1);
		setBlockAndMetadata(world, 5, 4, 4, roofStairBlock, 0);
		setBlockAndMetadata(world, 5, 4, 3, roofStairBlock, 3);
		setBlockAndMetadata(world, 6, 4, 3, roofStairBlock, 0);
		int m;
		for (m = -6; m <= 5; m++) {
			for (int i20 = -3; i20 <= 3; i20++) {
				if (m >= -5 && m <= 4 || i20 >= -2 && i20 <= 2) {
					setBlockAndMetadata(world, m, 4, i20, roofBlock, roofMeta);
					setBlockAndMetadata(world, m, 5, i20, roofSlabBlock, roofSlabMeta);
				}
			}
		}
		for (m = -5; m <= 4; m++) {
			setBlockAndMetadata(world, m, 5, -2, roofStairBlock, 2);
			setBlockAndMetadata(world, m, 5, 2, roofStairBlock, 3);
		}
		for (int k1 = -1; k1 <= 1; k1++) {
			setBlockAndMetadata(world, -5, 5, k1, roofStairBlock, 1);
			setBlockAndMetadata(world, 4, 5, k1, roofStairBlock, 0);
		}
		int i1;
		for (i1 = -4; i1 <= 3; i1++) {
			for (int i20 = -1; i20 <= 1; i20++) {
				setBlockAndMetadata(world, i1, 5, i20, roofBlock, roofMeta);
			}
		}
		setBlockAndMetadata(world, 3, 5, 0, Blocks.brick_block, 0);
		setBlockAndMetadata(world, 3, 6, 0, Blocks.brick_block, 0);
		setBlockAndMetadata(world, 3, 7, 0, Blocks.flower_pot, 0);
		for (i1 = -2; i1 <= 1; i1++) {
			setBlockAndMetadata(world, i1, 3, -5, Blocks.brick_block, 0);
			setBlockAndMetadata(world, i1, 4, -5, roofSlabBlock, roofSlabMeta);
		}
		setBlockAndMetadata(world, -3, 3, -6, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, -2, 3, -6, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, -1, 4, -6, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, 0, 4, -6, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, 1, 3, -6, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 2, 3, -6, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, -3, 2, -7, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, -2, 3, -7, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, -1, 3, -7, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 0, 3, -7, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 1, 3, -7, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, 2, 2, -7, tileSlabBlock, tileSlabMeta | 0x8);
		spawnHobbitCouple(world, 0, 1, 0, 16);
		return true;
	}

	private void placeHedge(World world, int i, int j, int k) {
		int j1 = j;
		for (; !isOpaque(world, i, j1 - 1, k) && j1 >= j - 6; j1--) {

		}
		setBlockAndMetadata(world, i, j1, k, hedgeBlock, hedgeMeta);
	}
}
