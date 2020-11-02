package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.*;
import lotr.common.entity.npc.*;
import lotr.common.item.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenHobbitTavern extends LOTRWorldGenHobbitStructure {
	private String[] tavernName;

	private String[] tavernNameSign;

	private String tavernNameNPC;

	public LOTRWorldGenHobbitTavern(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tavernName = LOTRNames.getHobbitTavernName(random);
		tavernNameSign = new String[] { "", tavernName[0], tavernName[1], "" };
		tavernNameNPC = tavernName[0] + " " + tavernName[1];
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		setOriginAndRotation(world, i, j, k, rotation, 12);
		setupRandomBlocks(random);
		if (restrictions) {
			int minHeight = 0;
			int maxHeight = 0;
			for (int i59 = -18; i59 <= 18; i59++) {
				for (int i60 = -12; i60 <= 19; i60++) {
					int i61 = getTopBlock(world, i59, i60) - 1;
					if (!isSurface(world, i59, i61, i60)) {
						return false;
					}
					if (i61 < minHeight) {
						minHeight = i61;
					}
					if (i61 > maxHeight) {
						maxHeight = i61;
					}
					if (maxHeight - minHeight > 8) {
						return false;
					}
				}
			}
		}
		int i58;
		for (i58 = -16; i58 <= 16; i58++) {
			for (int i59 = -12; i59 <= 18; i59++) {
				int i60 = Math.abs(i58);
				int grassHeight = -1;
				if (i60 <= 14) {
					if (i59 <= -6) {
						if (i59 == -7 && i60 <= 1 || i59 == -6 && i60 <= 3) {
							grassHeight = 1;
						} else {
							grassHeight = 0;
						}
					} else if (i59 <= -5 && (i60 == 11 || i60 <= 5)) {
						grassHeight = 1;
					} else if (i59 <= -4 && i60 <= 11 || i59 <= -3 && i60 <= 3) {
						grassHeight = 1;
					}
				}
				if (grassHeight >= 0) {
					int i61;
					for (i61 = grassHeight; (i61 >= -1 || !isOpaque(world, i58, i61, i59)) && getY(i61) >= 0; i61--) {
						if (i61 == grassHeight) {
							setBlockAndMetadata(world, i58, i61, i59, Blocks.grass, 0);
						} else {
							setBlockAndMetadata(world, i58, i61, i59, Blocks.dirt, 0);
						}
						setGrassToDirt(world, i58, i61 - 1, i59);
					}
					for (i61 = grassHeight + 1; i61 <= 12; i61++) {
						setAir(world, i58, i61, i59);
					}
				} else {
					boolean wood = false;
					boolean beam = false;
					if (i59 >= -5 && i59 <= 17) {
						if (i60 >= 15 && (i59 <= -4 || i59 >= 16)) {
							wood = false;
						} else {
							wood = true;
						}
					}
					if (i60 == 15 && (i59 == -4 || i59 == 16)) {
						beam = true;
					}
					if (i59 == 18 && i60 <= 14 && IntMath.mod(i58, 5) == 0) {
						beam = true;
					}
					if (beam || wood) {
						int i61;
						for (i61 = 5; (i61 >= 0 || !isOpaque(world, i58, i61, i59)) && getY(i61) >= 0; i61--) {
							if (beam) {
								setBlockAndMetadata(world, i58, i61, i59, beamBlock, beamMeta);
							} else {
								setBlockAndMetadata(world, i58, i61, i59, plankBlock, plankMeta);
							}
							setGrassToDirt(world, i58, i61 - 1, i59);
						}
						setBlockAndMetadata(world, i58, 6, i59, plankBlock, plankMeta);
						for (i61 = 8; i61 <= 12; i61++) {
							setAir(world, i58, i61, i59);
						}
					}
				}
			}
		}
		for (i58 = -16; i58 <= 16; i58++) {
			int i59 = Math.abs(i58);
			if (i59 <= 4) {
				setBlockAndMetadata(world, i58, 1, -10, outFenceBlock, outFenceMeta);
			}
			if (i59 >= 4 && i59 <= 11) {
				setBlockAndMetadata(world, i58, 1, -9, outFenceBlock, outFenceMeta);
			}
			if (i59 >= 11 && i59 <= 13) {
				setBlockAndMetadata(world, i58, 1, -8, outFenceBlock, outFenceMeta);
			}
			if (i59 == 13) {
				setBlockAndMetadata(world, i58, 1, -7, outFenceBlock, outFenceMeta);
				setBlockAndMetadata(world, i58, 1, -6, outFenceBlock, outFenceMeta);
			}
			if (i59 == 0) {
				setBlockAndMetadata(world, i58, 1, -10, outFenceGateBlock, 0);
			}
			if (i59 == 4) {
				setBlockAndMetadata(world, i58, 2, -10, Blocks.torch, 5);
			}
			if (i59 <= 1) {
				if (i58 == 0) {
					setBlockAndMetadata(world, i58, 0, -12, pathBlock, pathMeta);
					setBlockAndMetadata(world, i58, 0, -11, pathBlock, pathMeta);
					setBlockAndMetadata(world, i58, 0, -10, pathBlock, pathMeta);
				}
				setBlockAndMetadata(world, i58, 0, -9, pathBlock, pathMeta);
				setBlockAndMetadata(world, i58, 0, -8, pathBlock, pathMeta);
				setBlockAndMetadata(world, i58, 1, -7, pathSlabBlock, pathSlabMeta);
				setBlockAndMetadata(world, i58, 1, -6, pathSlabBlock, pathSlabMeta);
				for (int i60 = -5; i60 <= -2; i60++) {
					setBlockAndMetadata(world, i58, 1, i60, pathBlock, pathMeta);
				}
			}
			if (i59 == 5 || i59 == 11) {
				setGrassToDirt(world, i58, 0, -4);
				for (int i60 = 1; i60 <= 5; i60++) {
					setBlockAndMetadata(world, i58, i60, -4, beamBlock, beamMeta);
				}
			}
			if (i59 == 6 || i59 == 10) {
				setBlockAndMetadata(world, i58, 3, -4, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 2, -4, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 1, -5, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 0, -5, Blocks.grass, 0);
			}
			if (i59 >= 7 && i59 <= 9) {
				setBlockAndMetadata(world, i58, 2, -5, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 1, -5, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 0, -5, Blocks.grass, 0);
				setBlockAndMetadata(world, i58, 1, -6, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 2, -4, brickBlock, brickMeta);
				setGrassToDirt(world, i58, 1, -4);
				setBlockAndMetadata(world, i58, 3, -3, LOTRMod.glassPane, 0);
				setBlockAndMetadata(world, i58, 4, -3, LOTRMod.glassPane, 0);
				if (i59 == 7 || i59 == 9) {
					placeFlowerPot(world, i58, 3, -4, getRandomFlower(world, random));
				}
			}
			if (i59 >= 6 && i59 <= 10) {
				setBlockAndMetadata(world, i58, 5, -4, plankStairBlock, 6);
			}
			if (i59 >= 5 && i59 <= 11) {
				setBlockAndMetadata(world, i58, 6, -4, plankBlock, plankMeta);
			}
			if (i59 == 13) {
				setBlockAndMetadata(world, i58, 3, -6, fence2Block, fence2Meta);
				setBlockAndMetadata(world, i58, 4, -6, Blocks.torch, 5);
			}
			if (i59 == 4) {
				setBlockAndMetadata(world, i58, 2, -4, hedgeBlock, hedgeMeta);
			}
			if (i59 == 3) {
				setBlockAndMetadata(world, i58, 2, -4, hedgeBlock, hedgeMeta);
				setBlockAndMetadata(world, i58, 2, -3, hedgeBlock, hedgeMeta);
			}
		}
		for (i58 = -12; i58 <= 12; i58++) {
			for (int i59 = -8; i59 <= -2; i59++) {
				for (int i60 = 0; i60 <= 1; i60++) {
					if (getBlock(world, i58, i60, i59) == Blocks.grass && isAir(world, i58, i60 + 1, i59)) {
						if (random.nextInt(12) == 0) {
							plantFlower(world, random, i58, i60 + 1, i59);
						}
					}
				}
			}
		}
		for (i58 = -2; i58 <= 2; i58++) {
			for (int i59 = 2; i59 <= 4; i59++) {
				setAir(world, i58, i59, -2);
			}
		}
		setBlockAndMetadata(world, -2, 2, -2, plankStairBlock, 0);
		setBlockAndMetadata(world, -2, 4, -2, plankStairBlock, 4);
		setBlockAndMetadata(world, 2, 2, -2, plankStairBlock, 1);
		setBlockAndMetadata(world, 2, 4, -2, plankStairBlock, 5);
		for (i58 = -1; i58 <= 1; i58++) {
			for (int i59 = 2; i59 <= 4; i59++) {
				setAir(world, i58, i59, -1);
				setBlockAndMetadata(world, i58, i59, 0, brickBlock, brickMeta);
			}
		}
		setBlockAndMetadata(world, -1, 2, -1, plankStairBlock, 0);
		setBlockAndMetadata(world, -1, 4, -1, plankStairBlock, 4);
		setBlockAndMetadata(world, 1, 2, -1, plankStairBlock, 1);
		setBlockAndMetadata(world, 1, 4, -1, plankStairBlock, 5);
		setBlockAndMetadata(world, 0, 1, -1, pathBlock, pathMeta);
		setBlockAndMetadata(world, 0, 1, 0, brickBlock, brickMeta);
		setBlockAndMetadata(world, 0, 2, 0, doorBlock, 3);
		setBlockAndMetadata(world, 0, 3, 0, doorBlock, 8);
		placeSign(world, 0, 4, -1, Blocks.wall_sign, 2, tavernNameSign);
		setBlockAndMetadata(world, -2, 3, -2, Blocks.torch, 4);
		setBlockAndMetadata(world, 2, 3, -2, Blocks.torch, 4);
		setBlockAndMetadata(world, -3, 4, -3, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, -2, 5, -3, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, -1, 5, -3, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 0, 5, -3, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 1, 5, -3, tileSlabBlock, tileSlabMeta | 0x8);
		setBlockAndMetadata(world, 2, 5, -3, tileSlabBlock, tileSlabMeta);
		setBlockAndMetadata(world, 3, 4, -3, tileSlabBlock, tileSlabMeta | 0x8);
		if (random.nextBoolean()) {
			placeSign(world, -2, 2, -10, Blocks.standing_sign, MathHelper.getRandomIntegerInRange(random, 7, 9), tavernNameSign);
		} else {
			placeSign(world, 2, 2, -10, Blocks.standing_sign, MathHelper.getRandomIntegerInRange(random, 7, 9), tavernNameSign);
		}
		for (i58 = -3; i58 <= 3; i58++) {
			setBlockAndMetadata(world, i58, 6, -3, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -3, 6, -4, roofStairBlock, 0);
		setBlockAndMetadata(world, -4, 6, -4, roofStairBlock, 2);
		setBlockAndMetadata(world, -4, 6, -5, roofStairBlock, 0);
		setBlockAndMetadata(world, 3, 6, -4, roofStairBlock, 1);
		setBlockAndMetadata(world, 4, 6, -4, roofStairBlock, 2);
		setBlockAndMetadata(world, 4, 6, -5, roofStairBlock, 1);
		for (i58 = -11; i58 <= -5; i58++) {
			setBlockAndMetadata(world, i58, 6, -5, roofStairBlock, 2);
		}
		for (i58 = 5; i58 <= 11; i58++) {
			setBlockAndMetadata(world, i58, 6, -5, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -11, 6, -6, roofStairBlock, 0);
		setBlockAndMetadata(world, 11, 6, -6, roofStairBlock, 1);
		for (i58 = -14; i58 <= -12; i58++) {
			setBlockAndMetadata(world, i58, 6, -6, roofStairBlock, 2);
		}
		for (i58 = 12; i58 <= 14; i58++) {
			setBlockAndMetadata(world, i58, 6, -6, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -15, 6, -6, roofStairBlock, 1);
		setBlockAndMetadata(world, -15, 6, -5, roofStairBlock, 2);
		setBlockAndMetadata(world, -16, 6, -5, roofStairBlock, 1);
		setBlockAndMetadata(world, -16, 6, -4, roofStairBlock, 2);
		setBlockAndMetadata(world, 15, 6, -6, roofStairBlock, 0);
		setBlockAndMetadata(world, 15, 6, -5, roofStairBlock, 2);
		setBlockAndMetadata(world, 16, 6, -5, roofStairBlock, 0);
		setBlockAndMetadata(world, 16, 6, -4, roofStairBlock, 2);
		for (int i57 = -4; i57 <= 16; i57++) {
			setBlockAndMetadata(world, -17, 6, i57, roofStairBlock, 1);
			setBlockAndMetadata(world, 17, 6, i57, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -16, 6, 16, roofStairBlock, 3);
		setBlockAndMetadata(world, -16, 6, 17, roofStairBlock, 1);
		setBlockAndMetadata(world, -15, 6, 17, roofStairBlock, 3);
		setBlockAndMetadata(world, -15, 6, 18, roofStairBlock, 1);
		setBlockAndMetadata(world, 16, 6, 16, roofStairBlock, 3);
		setBlockAndMetadata(world, 16, 6, 17, roofStairBlock, 0);
		setBlockAndMetadata(world, 15, 6, 17, roofStairBlock, 3);
		setBlockAndMetadata(world, 15, 6, 18, roofStairBlock, 0);
		int i56;
		for (i56 = -14; i56 <= -11; i56++) {
			setBlockAndMetadata(world, i56, 6, 18, roofStairBlock, 3);
		}
		for (i56 = 11; i56 <= 14; i56++) {
			setBlockAndMetadata(world, i56, 6, 18, roofStairBlock, 3);
		}
		setBlockAndMetadata(world, -11, 6, 19, roofStairBlock, 1);
		setBlockAndMetadata(world, 11, 6, 19, roofStairBlock, 0);
		for (i56 = -10; i56 <= 10; i56++) {
			setBlockAndMetadata(world, i56, 6, 18, roofBlock, roofMeta);
			setBlockAndMetadata(world, i56, 6, 19, roofStairBlock, 3);
			int i59 = IntMath.mod(i56, 5);
			if (IntMath.mod(i56, 5) != 0) {
				setBlockAndMetadata(world, i56, 5, 18, plankStairBlock, 7);
				setBlockAndMetadata(world, i56, 1, 18, brickBlock, brickMeta);
				setGrassToDirt(world, i56, 0, 18);
				if (i59 == 1 || i59 == 4) {
					setBlockAndMetadata(world, i56, 2, 18, hedgeBlock, hedgeMeta);
				} else {
					placeFlowerPot(world, i56, 2, 18, getRandomFlower(world, random));
				}
				if (!isOpaque(world, i56, 0, 18)) {
					setBlockAndMetadata(world, i56, 0, 18, plankStairBlock, 7);
				}
			}
		}
		for (int i59 : new int[] { -15, 12 }) {
			int i60;
			for (i60 = i59; i60 <= i59 + 3; i60++) {
				setBlockAndMetadata(world, i60, 11, 6, brickStairBlock, 2);
				setBlockAndMetadata(world, i60, 11, 8, brickStairBlock, 3);
			}
			setBlockAndMetadata(world, i59, 11, 7, brickStairBlock, 1);
			setBlockAndMetadata(world, i59 + 3, 11, 7, brickStairBlock, 0);
			for (i60 = i59 + 1; i60 <= i59 + 2; i60++) {
				setBlockAndMetadata(world, i60, 11, 7, brickBlock, brickMeta);
				setBlockAndMetadata(world, i60, 12, 7, Blocks.flower_pot, 0);
			}
		}
		for (int i59 : new int[] { -16, 16 }) {
			for (int i60 = 3; i60 <= 4; i60++) {
				for (int i61 = 2; i61 <= 3; i61++) {
					setBlockAndMetadata(world, i59, i61, i60, LOTRMod.glassPane, 0);
				}
			}
		}
		for (int i59 : new int[] { -17, 17 }) {
			int i60;
			for (i60 = 2; i60 <= 10; i60++) {
				if (i60 != 6) {
					setBlockAndMetadata(world, i59, 1, i60, brickBlock, brickMeta);
					setGrassToDirt(world, i59, 0, i60);
					if (i60 == 2 || i60 == 5 || i60 == 7 || i60 == 10) {
						setBlockAndMetadata(world, i59, 2, i60, hedgeBlock, hedgeMeta);
					} else {
						placeFlowerPot(world, i59, 2, i60, getRandomFlower(world, random));
					}
				}
			}
			for (int i61 : new int[] { 1, 6, 11 }) {
				for (int i62 = 5; (i62 >= 0 || !isOpaque(world, i59, i62, i61)) && getY(i62) >= 0; i62--) {
					setBlockAndMetadata(world, i59, i62, i61, beamBlock, beamMeta);
					setGrassToDirt(world, i59, i62, i61);
				}
			}
			for (i60 = 1; i60 <= 11; i60++) {
				setBlockAndMetadata(world, i59, 6, i60, roofBlock, roofMeta);
			}
		}
		int i55;
		for (i55 = 2; i55 <= 10; i55++) {
			if (i55 != 6) {
				if (!isOpaque(world, -17, 0, i55)) {
					setBlockAndMetadata(world, -17, 0, i55, plankStairBlock, 5);
				}
				setBlockAndMetadata(world, -17, 5, i55, plankStairBlock, 5);
				if (!isOpaque(world, 17, 0, i55)) {
					setBlockAndMetadata(world, 17, 0, i55, plankStairBlock, 4);
				}
				setBlockAndMetadata(world, 17, 5, i55, plankStairBlock, 4);
			}
		}
		for (i55 = 7; i55 <= 10; i55++) {
			setBlockAndMetadata(world, -17, 5, i55, plankStairBlock, 5);
			setBlockAndMetadata(world, 17, 5, i55, plankStairBlock, 4);
		}
		for (i55 = 1; i55 <= 11; i55++) {
			setBlockAndMetadata(world, -18, 6, i55, roofStairBlock, 1);
			setBlockAndMetadata(world, 18, 6, i55, roofStairBlock, 0);
		}
		for (int i59 : new int[] { -18, 18 }) {
			setBlockAndMetadata(world, i59, 6, 0, roofStairBlock, 2);
			setBlockAndMetadata(world, i59, 6, 12, roofStairBlock, 3);
		}
		int i54;
		for (i54 = -4; i54 <= 4; i54++) {
			setBlockAndMetadata(world, i54, 7, -2, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -4, 7, -3, roofStairBlock, 0);
		setBlockAndMetadata(world, -5, 7, -3, roofStairBlock, 2);
		setBlockAndMetadata(world, -5, 7, -4, roofStairBlock, 0);
		setBlockAndMetadata(world, 4, 7, -3, roofStairBlock, 1);
		setBlockAndMetadata(world, 5, 7, -3, roofStairBlock, 2);
		setBlockAndMetadata(world, 5, 7, -4, roofStairBlock, 1);
		for (i54 = -12; i54 <= -6; i54++) {
			setBlockAndMetadata(world, i54, 7, -4, roofStairBlock, 2);
		}
		for (i54 = 6; i54 <= 12; i54++) {
			setBlockAndMetadata(world, i54, 7, -4, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -12, 7, -5, roofStairBlock, 0);
		setBlockAndMetadata(world, -13, 7, -5, roofStairBlock, 2);
		setBlockAndMetadata(world, -14, 7, -5, roofStairBlock, 1);
		setBlockAndMetadata(world, -14, 7, -4, roofStairBlock, 2);
		setBlockAndMetadata(world, -15, 7, -4, roofStairBlock, 1);
		setBlockAndMetadata(world, -15, 7, -3, roofStairBlock, 2);
		setBlockAndMetadata(world, 12, 7, -5, roofStairBlock, 1);
		setBlockAndMetadata(world, 13, 7, -5, roofStairBlock, 2);
		setBlockAndMetadata(world, 14, 7, -5, roofStairBlock, 0);
		setBlockAndMetadata(world, 14, 7, -4, roofStairBlock, 2);
		setBlockAndMetadata(world, 15, 7, -4, roofStairBlock, 0);
		setBlockAndMetadata(world, 15, 7, -3, roofStairBlock, 2);
		int i53;
		for (i53 = -3; i53 <= 0; i53++) {
			setBlockAndMetadata(world, -16, 7, i53, roofStairBlock, 1);
			setBlockAndMetadata(world, 16, 7, i53, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -16, 7, 1, roofStairBlock, 2);
		setBlockAndMetadata(world, 16, 7, 1, roofStairBlock, 2);
		for (i53 = 1; i53 <= 11; i53++) {
			setBlockAndMetadata(world, -17, 7, i53, roofStairBlock, 1);
			setBlockAndMetadata(world, 17, 7, i53, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -16, 7, 11, roofStairBlock, 3);
		setBlockAndMetadata(world, 16, 7, 11, roofStairBlock, 3);
		for (i53 = 12; i53 <= 15; i53++) {
			setBlockAndMetadata(world, -16, 7, i53, roofStairBlock, 1);
			setBlockAndMetadata(world, 16, 7, i53, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -15, 7, 15, roofStairBlock, 3);
		setBlockAndMetadata(world, -15, 7, 16, roofStairBlock, 1);
		setBlockAndMetadata(world, -14, 7, 16, roofStairBlock, 3);
		setBlockAndMetadata(world, -14, 7, 17, roofStairBlock, 1);
		setBlockAndMetadata(world, 15, 7, 15, roofStairBlock, 3);
		setBlockAndMetadata(world, 15, 7, 16, roofStairBlock, 0);
		setBlockAndMetadata(world, 14, 7, 16, roofStairBlock, 3);
		setBlockAndMetadata(world, 14, 7, 17, roofStairBlock, 0);
		int i52;
		for (i52 = -13; i52 <= -11; i52++) {
			setBlockAndMetadata(world, i52, 7, 17, roofStairBlock, 3);
		}
		for (i52 = 11; i52 <= 13; i52++) {
			setBlockAndMetadata(world, i52, 7, 17, roofStairBlock, 3);
		}
		setBlockAndMetadata(world, -10, 7, 17, roofStairBlock, 1);
		setBlockAndMetadata(world, 10, 7, 17, roofStairBlock, 0);
		for (i52 = -10; i52 <= 10; i52++) {
			setBlockAndMetadata(world, i52, 7, 18, roofStairBlock, 3);
		}
		for (i52 = -5; i52 <= 5; i52++) {
			setBlockAndMetadata(world, i52, 8, -1, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -5, 8, -2, roofStairBlock, 0);
		setBlockAndMetadata(world, -6, 8, -2, roofStairBlock, 2);
		setBlockAndMetadata(world, -6, 8, -3, roofStairBlock, 0);
		setBlockAndMetadata(world, 5, 8, -2, roofStairBlock, 1);
		setBlockAndMetadata(world, 6, 8, -2, roofStairBlock, 2);
		setBlockAndMetadata(world, 6, 8, -3, roofStairBlock, 1);
		for (i52 = -13; i52 <= -7; i52++) {
			setBlockAndMetadata(world, i52, 8, -3, roofStairBlock, 2);
		}
		for (i52 = 7; i52 <= 13; i52++) {
			setBlockAndMetadata(world, i52, 8, -3, roofStairBlock, 2);
		}
		setBlockAndMetadata(world, -13, 8, -4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, 13, 8, -4, roofSlabBlock, roofSlabMeta);
		setBlockAndMetadata(world, -14, 8, -3, roofStairBlock, 1);
		setBlockAndMetadata(world, -14, 8, -2, roofStairBlock, 2);
		setBlockAndMetadata(world, 14, 8, -3, roofStairBlock, 0);
		setBlockAndMetadata(world, 14, 8, -2, roofStairBlock, 2);
		int i51;
		for (i51 = -2; i51 <= 1; i51++) {
			setBlockAndMetadata(world, -15, 8, i51, roofStairBlock, 1);
			setBlockAndMetadata(world, 15, 8, i51, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -15, 8, 2, roofStairBlock, 2);
		setBlockAndMetadata(world, 15, 8, 2, roofStairBlock, 2);
		for (i51 = 2; i51 <= 10; i51++) {
			setBlockAndMetadata(world, -16, 8, i51, roofStairBlock, 1);
			setBlockAndMetadata(world, 16, 8, i51, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -15, 8, 10, roofStairBlock, 3);
		setBlockAndMetadata(world, 15, 8, 10, roofStairBlock, 3);
		for (i51 = 11; i51 <= 14; i51++) {
			setBlockAndMetadata(world, -15, 8, i51, roofStairBlock, 1);
			setBlockAndMetadata(world, 15, 8, i51, roofStairBlock, 0);
		}
		setBlockAndMetadata(world, -14, 8, 14, roofStairBlock, 3);
		setBlockAndMetadata(world, -14, 8, 15, roofStairBlock, 1);
		setBlockAndMetadata(world, -13, 8, 15, roofStairBlock, 3);
		setBlockAndMetadata(world, -13, 8, 16, roofStairBlock, 1);
		setBlockAndMetadata(world, 14, 8, 14, roofStairBlock, 3);
		setBlockAndMetadata(world, 14, 8, 15, roofStairBlock, 0);
		setBlockAndMetadata(world, 13, 8, 15, roofStairBlock, 3);
		setBlockAndMetadata(world, 13, 8, 16, roofStairBlock, 0);
		int i50;
		for (i50 = -12; i50 <= -10; i50++) {
			setBlockAndMetadata(world, i50, 8, 16, roofStairBlock, 3);
		}
		for (i50 = 10; i50 <= 12; i50++) {
			setBlockAndMetadata(world, i50, 8, 16, roofStairBlock, 3);
		}
		setBlockAndMetadata(world, -9, 8, 16, roofStairBlock, 1);
		setBlockAndMetadata(world, 9, 8, 16, roofStairBlock, 0);
		for (i50 = -9; i50 <= 9; i50++) {
			setBlockAndMetadata(world, i50, 8, 17, roofStairBlock, 3);
		}
		for (i50 = -16; i50 <= 16; i50++) {
			int i59 = Math.abs(i50);
			int i60;
			for (i60 = -4; i60 <= 17; i60++) {
				boolean roof = false;
				if (i60 == -4) {
					roof = i59 == 13;
				}
				if (i60 == -3) {
					roof = i59 >= 6 && i59 <= 14;
				}
				if (i60 == -2) {
					roof = i59 >= 5 && i59 <= 15;
				}
				if (i60 >= -1 && i60 <= 1) {
					roof = i59 <= 15;
				}
				if (i60 >= 2 && i60 <= 10) {
					roof = i59 <= 16;
				}
				if (i60 >= 11 && i60 <= 14) {
					roof = i59 <= 15;
				}
				if (i60 == 15) {
					roof = i59 <= 14;
				}
				if (i60 == 16) {
					roof = i59 <= 13;
				}
				if (i60 == 17) {
					roof = i59 <= 9;
				}
				if (roof) {
					setBlockAndMetadata(world, i50, 7, i60, roofBlock, roofMeta);
				}
			}
			for (i60 = -2; i60 <= 16; i60++) {
				boolean roof = false;
				if (i60 == -2) {
					roof = i59 >= 7 && i59 <= 13;
				}
				if (i60 == -1) {
					roof = i59 >= 6 && i59 <= 14;
				}
				if (i60 >= 0 && i60 <= 2) {
					roof = i59 <= 14;
				}
				if (i60 >= 3 && i60 <= 9) {
					roof = i59 <= 15;
				}
				if (i60 >= 10 && i60 <= 13) {
					roof = i59 <= 14;
				}
				if (i60 == 14) {
					roof = i59 <= 13;
				}
				if (i60 == 15) {
					roof = i59 <= 12;
				}
				if (i60 == 16) {
					roof = i59 <= 8;
				}
				if (roof) {
					setBlockAndMetadata(world, i50, 8, i60, roofBlock, roofMeta);
					setBlockAndMetadata(world, i50, 9, i60, roofSlabBlock, roofSlabMeta);
				}
			}
		}
		for (i50 = -6; i50 <= 6; i50++) {
			int i59 = Math.abs(i50);
			for (int i61 = 1; i61 <= 15; i61++) {
				boolean room = false;
				if (i61 == 1 && i59 <= 1) {
					room = true;
				}
				if (i61 == 2 && i59 <= 2) {
					room = true;
				}
				if (i61 == 3 && i59 <= 3) {
					room = true;
				}
				if (i61 == 4 && i59 <= 4) {
					room = true;
				}
				if (i61 == 5 && i59 <= 5) {
					room = true;
				}
				if (i61 >= 6 && i61 <= 10 && i59 <= 6) {
					room = true;
				}
				if (i61 >= 11 && i61 <= 12 && i59 <= 5) {
					room = true;
				}
				if (i61 == 13 && i59 <= 4) {
					room = true;
				}
				if (i61 >= 14 && i61 <= 15 && i59 <= 2) {
					room = true;
				}
				if (room) {
					setBlockAndMetadata(world, i50, 1, i61, floorBlock, floorMeta);
					for (int i62 = 2; i62 <= 5; i62++) {
						setAir(world, i50, i62, i61);
					}
				}
			}
			for (int i60 = 2; i60 <= 4; i60++) {
				if (i59 == 2) {
					setBlockAndMetadata(world, i50, i60, 1, brickBlock, brickMeta);
				}
				if (i59 == 4) {
					setBlockAndMetadata(world, i50, i60, 3, beamBlock, beamMeta);
				}
				if (i59 == 6) {
					setBlockAndMetadata(world, i50, i60, 5, beamBlock, beamMeta);
					setBlockAndMetadata(world, i50, i60, 11, beamBlock, beamMeta);
				}
				if (i59 == 5) {
					setBlockAndMetadata(world, i50, i60, 13, beamBlock, beamMeta);
				}
				if (i59 == 3) {
					setBlockAndMetadata(world, i50, i60, 14, beamBlock, beamMeta);
				}
			}
		}
		for (i50 = -5; i50 <= 5; i50++) {
			for (int i59 = 11; i59 <= 15; i59++) {
				setBlockAndMetadata(world, i50, 5, i59, plankBlock, plankMeta);
			}
			setBlockAndMetadata(world, i50, 5, 10, plankStairBlock, 6);
		}
		for (i50 = -1; i50 <= 1; i50++) {
			setBlockAndMetadata(world, i50, 5, 1, plankBlock, plankMeta);
		}
		for (i50 = -2; i50 <= 2; i50++) {
			setBlockAndMetadata(world, i50, 5, 2, plankStairBlock, 7);
		}
		setBlockAndMetadata(world, -2, 5, 3, plankStairBlock, 4);
		setBlockAndMetadata(world, 2, 5, 3, plankStairBlock, 5);
		setBlockAndMetadata(world, -3, 5, 3, plankStairBlock, 7);
		setBlockAndMetadata(world, 3, 5, 3, plankStairBlock, 7);
		setBlockAndMetadata(world, -3, 5, 4, plankStairBlock, 4);
		setBlockAndMetadata(world, 3, 5, 4, plankStairBlock, 5);
		setBlockAndMetadata(world, -4, 5, 4, plankStairBlock, 7);
		setBlockAndMetadata(world, 4, 5, 4, plankStairBlock, 7);
		setBlockAndMetadata(world, -4, 5, 5, plankStairBlock, 4);
		setBlockAndMetadata(world, 4, 5, 5, plankStairBlock, 5);
		setBlockAndMetadata(world, -5, 5, 5, plankStairBlock, 7);
		setBlockAndMetadata(world, 5, 5, 5, plankStairBlock, 7);
		setBlockAndMetadata(world, -5, 5, 6, plankStairBlock, 4);
		setBlockAndMetadata(world, 5, 5, 6, plankStairBlock, 5);
		setBlockAndMetadata(world, -6, 5, 6, plankStairBlock, 7);
		setBlockAndMetadata(world, 6, 5, 6, plankStairBlock, 7);
		for (int i49 = 7; i49 <= 10; i49++) {
			setBlockAndMetadata(world, -6, 5, i49, plankStairBlock, 4);
			setBlockAndMetadata(world, 6, 5, i49, plankStairBlock, 5);
		}
		setBlockAndMetadata(world, 0, 4, 1, Blocks.torch, 3);
		setBlockAndMetadata(world, -6, 3, 6, Blocks.torch, 3);
		setBlockAndMetadata(world, 6, 3, 6, Blocks.torch, 3);
		setBlockAndMetadata(world, -6, 3, 10, Blocks.torch, 4);
		setBlockAndMetadata(world, 6, 3, 10, Blocks.torch, 4);
		setBlockAndMetadata(world, 0, 5, 5, chandelierBlock, chandelierMeta);
		setBlockAndMetadata(world, -4, 5, 8, chandelierBlock, chandelierMeta);
		setBlockAndMetadata(world, 4, 5, 8, chandelierBlock, chandelierMeta);
		setBlockAndMetadata(world, -2, 3, 2, Blocks.tripwire_hook, 0);
		setBlockAndMetadata(world, 2, 3, 2, Blocks.tripwire_hook, 0);
		setBlockAndMetadata(world, -3, 3, 3, Blocks.tripwire_hook, 0);
		setBlockAndMetadata(world, 3, 3, 3, Blocks.tripwire_hook, 0);
		setBlockAndMetadata(world, -4, 3, 4, Blocks.tripwire_hook, 1);
		setBlockAndMetadata(world, 4, 3, 4, Blocks.tripwire_hook, 3);
		int i48;
		for (i48 = -1; i48 <= 1; i48++) {
			for (int i59 = 1; i59 <= 2; i59++) {
				setBlockAndMetadata(world, i48, 2, i59, carpetBlock, carpetMeta);
			}
		}
		for (i48 = -2; i48 <= 2; i48++) {
			for (int i59 = 5; i59 <= 7; i59++) {
				setBlockAndMetadata(world, i48, 2, i59, carpetBlock, carpetMeta);
			}
		}
		for (i48 = -3; i48 <= 3; i48++) {
			int i59 = Math.abs(i48);
			setBlockAndMetadata(world, i48, 2, 11, plank2Block, plank2Meta);
			setBlockAndMetadata(world, i48, 4, 11, fence2Block, fence2Meta);
			if (IntMath.mod(i48, 2) == 1) {
				setBlockAndMetadata(world, i48, 2, 9, fence2Block, fence2Meta);
			}
			if (i59 == 2) {
				placeBarrel(world, random, i48, 3, 11, 3, LOTRFoods.HOBBIT_DRINK);
			}
			if (i59 == 1) {
				placeMug(world, random, i48, 3, 11, 0, LOTRFoods.HOBBIT_DRINK);
			}
		}
		for (int i47 = 12; i47 <= 13; i47++) {
			for (int i59 : new int[] { -3, 3 }) {
				setBlockAndMetadata(world, i59, 2, i47, plank2Block, plank2Meta);
				setBlockAndMetadata(world, i59, 4, i47, fence2Block, fence2Meta);
			}
		}
		setBlockAndMetadata(world, 3, 2, 12, fenceGate2Block, 1);
		for (int i46 = -2; i46 <= 2; i46++) {
			setBlockAndMetadata(world, i46, 4, 15, plankStairBlock, 6);
		}
		setBlockAndMetadata(world, -2, 4, 14, Blocks.torch, 2);
		setBlockAndMetadata(world, 2, 4, 14, Blocks.torch, 1);
		setBlockAndMetadata(world, -2, 2, 15, Blocks.crafting_table, 0);
		placeChest(world, random, -1, 2, 15, 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
		setBlockAndMetadata(world, 0, 2, 15, plankBlock, plankMeta);
		placeFlowerPot(world, 0, 3, 15, new ItemStack(LOTRMod.shireHeather));
		setBlockAndMetadata(world, 1, 2, 15, Blocks.cauldron, 3);
		setBlockAndMetadata(world, 2, 2, 15, LOTRMod.hobbitOven, 2);
		for (int i59 : new int[] { -7, 7 }) {
			setBlockAndMetadata(world, i59, 1, 8, floorBlock, floorMeta);
			setBlockAndMetadata(world, i59, 2, 8, carpetBlock, carpetMeta);
			setAir(world, i59, 3, 8);
			setBlockAndMetadata(world, i59, 2, 7, plankStairBlock, 3);
			setBlockAndMetadata(world, i59, 3, 7, plankStairBlock, 7);
			setBlockAndMetadata(world, i59, 2, 9, plankStairBlock, 2);
			setBlockAndMetadata(world, i59, 3, 9, plankStairBlock, 6);
		}
		for (int i45 = 7; i45 <= 9; i45++) {
			setBlockAndMetadata(world, -6, 2, i45, carpetBlock, carpetMeta);
			setBlockAndMetadata(world, -5, 2, i45, carpetBlock, carpetMeta);
			setBlockAndMetadata(world, 5, 2, i45, carpetBlock, carpetMeta);
			setBlockAndMetadata(world, 6, 2, i45, carpetBlock, carpetMeta);
		}
		int i44;
		for (i44 = -15; i44 <= -8; i44++) {
			for (int i59 = 3; i59 <= 14; i59++) {
				setBlockAndMetadata(world, i44, 0, i59, floorBlock, floorMeta);
				for (int i60 = 1; i60 <= 5; i60++) {
					setAir(world, i44, i60, i59);
				}
			}
		}
		for (i44 = -15; i44 <= -11; i44++) {
			for (int i59 = -3; i59 <= 3; i59++) {
				setBlockAndMetadata(world, i44, 0, i59, floorBlock, floorMeta);
				for (int i60 = 1; i60 <= 5; i60++) {
					setAir(world, i44, i60, i59);
				}
			}
		}
		for (i44 = -10; i44 <= -5; i44++) {
			for (int i59 = -2; i59 <= 3; i59++) {
				setBlockAndMetadata(world, i44, 1, i59, floorBlock, floorMeta);
				for (int i60 = 2; i60 <= 5; i60++) {
					setAir(world, i44, i60, i59);
				}
			}
		}
		for (int i43 = 1; i43 <= 5; i43++) {
			setBlockAndMetadata(world, -15, i43, 14, beamBlock, beamMeta);
			setBlockAndMetadata(world, -9, i43, 14, beamBlock, beamMeta);
			setBlockAndMetadata(world, -8, i43, 14, plankBlock, plankMeta);
			setBlockAndMetadata(world, -8, i43, 11, beamBlock, beamMeta);
			setBlockAndMetadata(world, -8, i43, 5, beamBlock, beamMeta);
			setBlockAndMetadata(world, -8, i43, 4, plankBlock, plankMeta);
			setBlockAndMetadata(world, -9, i43, 3, beamBlock, beamMeta);
		}
		setBlockAndMetadata(world, -8, 3, 6, Blocks.torch, 3);
		setBlockAndMetadata(world, -8, 3, 10, Blocks.torch, 4);
		for (int i42 = 6; i42 <= 10; i42++) {
			setBlockAndMetadata(world, -8, 1, i42, floorStairBlock, 1);
			setBlockAndMetadata(world, -8, 5, i42, plankStairBlock, 5);
		}
		setBlockAndMetadata(world, -9, 1, 11, plank2Block, plank2Meta);
		for (int i41 = 2; i41 <= 4; i41++) {
			setBlockAndMetadata(world, -9, i41, 11, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, -9, 5, 11, plank2Block, plank2Meta);
		for (int i40 = 12; i40 <= 13; i40++) {
			setBlockAndMetadata(world, -9, 1, i40, plank2StairBlock, 1);
			setBlockAndMetadata(world, -8, 1, i40, plankBlock, plankMeta);
			setBlockAndMetadata(world, -8, 2, i40, plankStairBlock, 5);
			placeFlowerPot(world, -8, 3, i40, getRandomFlower(world, random));
			setBlockAndMetadata(world, -8, 4, i40, plankStairBlock, 5);
			setBlockAndMetadata(world, -8, 5, i40, plankBlock, plankMeta);
			setBlockAndMetadata(world, -9, 5, i40, plank2StairBlock, 5);
		}
		int i39;
		for (i39 = -14; i39 <= -10; i39++) {
			setBlockAndMetadata(world, i39, 1, 14, plank2StairBlock, 2);
			setBlockAndMetadata(world, i39, 5, 14, plank2StairBlock, 6);
		}
		for (i39 = -13; i39 <= -11; i39++) {
			setBlockAndMetadata(world, i39, 2, 15, plankStairBlock, 6);
			setBlockAndMetadata(world, i39, 3, 15, LOTRMod.barrel, 2);
			setBlockAndMetadata(world, i39, 4, 15, plankStairBlock, 6);
		}
		int i38;
		for (i38 = 9; i38 <= 13; i38++) {
			setBlockAndMetadata(world, -15, 1, i38, plank2StairBlock, 0);
			setBlockAndMetadata(world, -15, 5, i38, plank2StairBlock, 4);
		}
		for (i38 = 10; i38 <= 12; i38++) {
			spawnItemFrame(world, -16, 3, i38, 1, getTavernFramedItem(random));
		}
		int i37;
		for (i37 = -13; i37 <= -11; i37++) {
			for (int i59 = 10; i59 <= 12; i59++) {
				setBlockAndMetadata(world, i37, 1, i59, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i37, 2, i59);
			}
		}
		setBlockAndMetadata(world, -12, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, -13, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, -11, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, -12, 5, 10, fence2Block, fence2Meta);
		setBlockAndMetadata(world, -12, 5, 12, fence2Block, fence2Meta);
		setBlockAndMetadata(world, -12, 4, 11, chandelierBlock, chandelierMeta);
		for (i37 = -15; i37 <= -12; i37++) {
			for (int i59 = 6; i59 <= 8; i59++) {
				setBlockAndMetadata(world, i37, 1, i59, Blocks.stonebrick, 0);
				int i60;
				for (i60 = 2; i60 <= 4; i60++) {
					setBlockAndMetadata(world, i37, i60, i59, brickBlock, brickMeta);
				}
				setBlockAndMetadata(world, i37, 5, i59, Blocks.stonebrick, 0);
				for (i60 = 6; i60 <= 10; i60++) {
					setBlockAndMetadata(world, i37, i60, i59, brickBlock, brickMeta);
				}
			}
		}
		for (i37 = -14; i37 <= -13; i37++) {
			setBlockAndMetadata(world, i37, 0, 7, LOTRMod.hearth, 0);
			setBlockAndMetadata(world, i37, 1, 7, Blocks.fire, 0);
			for (int i59 = 2; i59 <= 10; i59++) {
				setAir(world, i37, i59, 7);
			}
		}
		for (int i36 = 1; i36 <= 3; i36++) {
			setBlockAndMetadata(world, -12, i36, 7, barsBlock, 0);
		}
		setBlockAndMetadata(world, -10, 5, 7, chandelierBlock, chandelierMeta);
		for (int i35 = 2; i35 <= 5; i35++) {
			setBlockAndMetadata(world, -15, 1, i35, plank2StairBlock, 0);
			setBlockAndMetadata(world, -15, 5, i35, plank2StairBlock, 4);
		}
		int i34;
		for (i34 = 1; i34 <= 5; i34++) {
			setBlockAndMetadata(world, -15, i34, 1, beamBlock, beamMeta);
		}
		setBlockAndMetadata(world, -14, 1, 1, plank2Block, plank2Meta);
		for (i34 = 2; i34 <= 4; i34++) {
			setBlockAndMetadata(world, -14, i34, 1, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, -14, 5, 1, plank2Block, plank2Meta);
		int i33;
		for (i33 = 3; i33 <= 4; i33++) {
			setBlockAndMetadata(world, -13, 1, i33, plank2Block, plank2Meta);
			placePlateOrMug(world, random, -13, 2, i33);
		}
		setBlockAndMetadata(world, -13, 5, 4, chandelierBlock, chandelierMeta);
		for (i33 = -3; i33 <= 0; i33++) {
			setBlockAndMetadata(world, -15, 1, i33, plank2StairBlock, 0);
			setBlockAndMetadata(world, -15, 5, i33, plank2StairBlock, 4);
		}
		for (i33 = -2; i33 <= -1; i33++) {
			for (int i59 = -13; i59 <= -12; i59++) {
				setBlockAndMetadata(world, i59, 1, i33, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i59, 2, i33);
			}
			spawnItemFrame(world, -16, 3, i33, 1, getTavernFramedItem(random));
		}
		for (int i32 = -14; i32 <= -12; i32++) {
			setBlockAndMetadata(world, i32, 1, -4, plank2StairBlock, 3);
			for (int i59 = 2; i59 <= 4; i59++) {
				setAir(world, i32, i59, -4);
			}
			setBlockAndMetadata(world, i32, 5, -4, plank2StairBlock, 7);
		}
		spawnItemFrame(world, -13, 3, -5, 0, getTavernFramedItem(random));
		setBlockAndMetadata(world, -12, 5, -1, chandelierBlock, chandelierMeta);
		for (int i31 = -1; i31 <= 2; i31++) {
			setBlockAndMetadata(world, -10, 1, i31, floorStairBlock, 1);
		}
		setBlockAndMetadata(world, -10, 1, 3, floorStairBlock, 3);
		for (int i30 = 2; i30 <= 5; i30++) {
			setBlockAndMetadata(world, -5, i30, 3, beamBlock, beamMeta);
			setBlockAndMetadata(world, -5, i30, -2, beamBlock, beamMeta);
		}
		for (int i29 = -8; i29 <= -6; i29++) {
			setBlockAndMetadata(world, i29, 2, 3, plank2StairBlock, 2);
			setBlockAndMetadata(world, i29, 5, 3, plank2StairBlock, 6);
		}
		setBlockAndMetadata(world, -7, 3, 4, LOTRMod.barrel, 2);
		setBlockAndMetadata(world, -7, 4, 4, plankStairBlock, 6);
		for (int i28 = -1; i28 <= 2; i28++) {
			setBlockAndMetadata(world, -5, 2, i28, plank2StairBlock, 1);
			setBlockAndMetadata(world, -5, 5, i28, plank2StairBlock, 5);
		}
		setBlockAndMetadata(world, -4, 3, 2, plankStairBlock, 2);
		setBlockAndMetadata(world, -4, 4, 2, plankStairBlock, 6);
		setBlockAndMetadata(world, -4, 3, -1, plankStairBlock, 3);
		setBlockAndMetadata(world, -4, 4, -1, plankStairBlock, 7);
		placeFlowerPot(world, -4, 3, 1, getRandomFlower(world, random));
		placeFlowerPot(world, -4, 3, 0, getRandomFlower(world, random));
		setBlockAndMetadata(world, -4, 4, 1, plankSlabBlock, plankSlabMeta | 0x8);
		setBlockAndMetadata(world, -4, 4, 0, plankSlabBlock, plankSlabMeta | 0x8);
		for (int i27 = -9; i27 <= -6; i27++) {
			setBlockAndMetadata(world, i27, 2, -2, plank2StairBlock, 3);
			setBlockAndMetadata(world, i27, 5, -2, plank2StairBlock, 7);
		}
		setBlockAndMetadata(world, -10, 1, -2, plank2Block, plank2Meta);
		setBlockAndMetadata(world, -10, 2, -2, plank2Block, plank2Meta);
		for (int i26 = 3; i26 <= 4; i26++) {
			setBlockAndMetadata(world, -10, i26, -2, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, -10, 5, -2, plank2Block, plank2Meta);
		int i25;
		for (i25 = -8; i25 <= -7; i25++) {
			for (int i59 = 0; i59 <= 1; i59++) {
				setBlockAndMetadata(world, i25, 2, i59, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i25, 3, i59);
			}
		}
		setBlockAndMetadata(world, -8, 5, 1, chandelierBlock, chandelierMeta);
		for (i25 = 8; i25 <= 15; i25++) {
			for (int i59 = 3; i59 <= 14; i59++) {
				setBlockAndMetadata(world, i25, 0, i59, floorBlock, floorMeta);
				for (int i60 = 1; i60 <= 5; i60++) {
					setAir(world, i25, i60, i59);
				}
			}
		}
		for (i25 = 11; i25 <= 15; i25++) {
			for (int i59 = -3; i59 <= 3; i59++) {
				setBlockAndMetadata(world, i25, 0, i59, floorBlock, floorMeta);
				for (int i60 = 1; i60 <= 5; i60++) {
					setAir(world, i25, i60, i59);
				}
			}
		}
		for (i25 = 5; i25 <= 10; i25++) {
			for (int i59 = -2; i59 <= 3; i59++) {
				setBlockAndMetadata(world, i25, 1, i59, floorBlock, floorMeta);
				for (int i60 = 2; i60 <= 5; i60++) {
					setAir(world, i25, i60, i59);
				}
			}
		}
		for (int i24 = 1; i24 <= 5; i24++) {
			setBlockAndMetadata(world, 15, i24, 14, beamBlock, beamMeta);
			setBlockAndMetadata(world, 9, i24, 14, beamBlock, beamMeta);
			setBlockAndMetadata(world, 8, i24, 14, plankBlock, plankMeta);
			setBlockAndMetadata(world, 8, i24, 11, beamBlock, beamMeta);
			setBlockAndMetadata(world, 8, i24, 5, beamBlock, beamMeta);
			setBlockAndMetadata(world, 8, i24, 4, plankBlock, plankMeta);
			setBlockAndMetadata(world, 9, i24, 3, beamBlock, beamMeta);
		}
		setBlockAndMetadata(world, 8, 3, 6, Blocks.torch, 3);
		setBlockAndMetadata(world, 8, 3, 10, Blocks.torch, 4);
		for (int i23 = 6; i23 <= 10; i23++) {
			setBlockAndMetadata(world, 8, 1, i23, floorStairBlock, 0);
			setBlockAndMetadata(world, 8, 5, i23, plankStairBlock, 4);
		}
		setBlockAndMetadata(world, 9, 1, 11, plank2Block, plank2Meta);
		for (int i22 = 2; i22 <= 4; i22++) {
			setBlockAndMetadata(world, 9, i22, 11, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, 9, 5, 11, plank2Block, plank2Meta);
		for (int i21 = 12; i21 <= 13; i21++) {
			setBlockAndMetadata(world, 9, 1, i21, plank2StairBlock, 0);
			setBlockAndMetadata(world, 8, 1, i21, plankBlock, plankMeta);
			for (int i59 = 2; i59 <= 4; i59++) {
				setBlockAndMetadata(world, 8, i59, i21, Blocks.bookshelf, 0);
			}
			setBlockAndMetadata(world, 8, 5, i21, plankBlock, plankMeta);
			setBlockAndMetadata(world, 9, 5, i21, plank2StairBlock, 4);
		}
		int i20;
		for (i20 = 10; i20 <= 14; i20++) {
			setBlockAndMetadata(world, i20, 1, 14, plank2StairBlock, 2);
			setBlockAndMetadata(world, i20, 5, 14, plank2StairBlock, 6);
		}
		for (i20 = 10; i20 <= 14; i20++) {
			for (int i59 = 2; i59 <= 4; i59++) {
				setBlockAndMetadata(world, i20, i59, 15, Blocks.bookshelf, 0);
			}
		}
		for (int i19 = 9; i19 <= 13; i19++) {
			setBlockAndMetadata(world, 15, 1, i19, plank2StairBlock, 1);
			setBlockAndMetadata(world, 15, 5, i19, plank2StairBlock, 5);
		}
		placeWallBanner(world, 16, 4, 11, LOTRItemBanner.BannerType.HOBBIT, 3);
		int i18;
		for (i18 = 11; i18 <= 13; i18++) {
			for (int i59 = 10; i59 <= 12; i59++) {
				setBlockAndMetadata(world, i18, 1, i59, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i18, 2, i59);
			}
		}
		setBlockAndMetadata(world, 12, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, 13, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, 11, 5, 11, fence2Block, fence2Meta);
		setBlockAndMetadata(world, 12, 5, 10, fence2Block, fence2Meta);
		setBlockAndMetadata(world, 12, 5, 12, fence2Block, fence2Meta);
		setBlockAndMetadata(world, 12, 4, 11, chandelierBlock, chandelierMeta);
		for (i18 = 12; i18 <= 15; i18++) {
			for (int i59 = 6; i59 <= 8; i59++) {
				setBlockAndMetadata(world, i18, 1, i59, Blocks.stonebrick, 0);
				int i60;
				for (i60 = 2; i60 <= 4; i60++) {
					setBlockAndMetadata(world, i18, i60, i59, brickBlock, brickMeta);
				}
				setBlockAndMetadata(world, i18, 5, i59, Blocks.stonebrick, 0);
				for (i60 = 6; i60 <= 10; i60++) {
					setBlockAndMetadata(world, i18, i60, i59, brickBlock, brickMeta);
				}
			}
		}
		for (i18 = 13; i18 <= 14; i18++) {
			setBlockAndMetadata(world, i18, 0, 7, LOTRMod.hearth, 0);
			setBlockAndMetadata(world, i18, 1, 7, Blocks.fire, 0);
			for (int i59 = 2; i59 <= 10; i59++) {
				setAir(world, i18, i59, 7);
			}
		}
		for (int i17 = 1; i17 <= 3; i17++) {
			setBlockAndMetadata(world, 12, i17, 7, barsBlock, 0);
		}
		setBlockAndMetadata(world, 10, 5, 7, chandelierBlock, chandelierMeta);
		for (int i16 = 2; i16 <= 5; i16++) {
			setBlockAndMetadata(world, 15, 1, i16, plank2StairBlock, 1);
			setBlockAndMetadata(world, 15, 5, i16, plank2StairBlock, 5);
		}
		int i15;
		for (i15 = 1; i15 <= 5; i15++) {
			setBlockAndMetadata(world, 15, i15, 1, beamBlock, beamMeta);
		}
		setBlockAndMetadata(world, 14, 1, 1, plank2Block, plank2Meta);
		for (i15 = 2; i15 <= 4; i15++) {
			setBlockAndMetadata(world, 14, i15, 1, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, 14, 5, 1, plank2Block, plank2Meta);
		int i14;
		for (i14 = 3; i14 <= 4; i14++) {
			setBlockAndMetadata(world, 13, 1, i14, plank2Block, plank2Meta);
			placePlateOrMug(world, random, 13, 2, i14);
		}
		setBlockAndMetadata(world, 13, 5, 4, chandelierBlock, chandelierMeta);
		for (i14 = -3; i14 <= 0; i14++) {
			setBlockAndMetadata(world, 15, 1, i14, plank2StairBlock, 1);
			setBlockAndMetadata(world, 15, 5, i14, plank2StairBlock, 5);
		}
		for (i14 = -2; i14 <= -1; i14++) {
			for (int i59 = 12; i59 <= 13; i59++) {
				setBlockAndMetadata(world, i59, 1, i14, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i59, 2, i14);
			}
		}
		placeWallBanner(world, 16, 4, -2, LOTRItemBanner.BannerType.HOBBIT, 3);
		for (int i13 = 12; i13 <= 14; i13++) {
			setBlockAndMetadata(world, i13, 1, -4, plank2StairBlock, 3);
			for (int i59 = 2; i59 <= 4; i59++) {
				setAir(world, i13, i59, -4);
			}
			setBlockAndMetadata(world, i13, 5, -4, plank2StairBlock, 7);
		}
		placeWallBanner(world, 13, 4, -5, LOTRItemBanner.BannerType.HOBBIT, 0);
		setBlockAndMetadata(world, 12, 5, -1, chandelierBlock, chandelierMeta);
		for (int i12 = -1; i12 <= 2; i12++) {
			setBlockAndMetadata(world, 10, 1, i12, floorStairBlock, 0);
		}
		setBlockAndMetadata(world, 10, 1, 3, floorStairBlock, 3);
		for (int i11 = 2; i11 <= 5; i11++) {
			setBlockAndMetadata(world, 5, i11, 3, beamBlock, beamMeta);
			setBlockAndMetadata(world, 5, i11, -2, beamBlock, beamMeta);
		}
		for (int i10 = 6; i10 <= 8; i10++) {
			setBlockAndMetadata(world, i10, 2, 3, plank2StairBlock, 2);
			setBlockAndMetadata(world, i10, 5, 3, plank2StairBlock, 6);
		}
		placeWallBanner(world, 7, 4, 4, LOTRItemBanner.BannerType.HOBBIT, 2);
		for (int i9 = -1; i9 <= 2; i9++) {
			setBlockAndMetadata(world, 5, 2, i9, plank2StairBlock, 0);
			setBlockAndMetadata(world, 4, 3, i9, Blocks.bookshelf, 0);
			setBlockAndMetadata(world, 4, 4, i9, Blocks.bookshelf, 0);
			setBlockAndMetadata(world, 5, 5, i9, plank2StairBlock, 4);
		}
		for (int i8 = 6; i8 <= 9; i8++) {
			setBlockAndMetadata(world, i8, 2, -2, plank2StairBlock, 3);
			setBlockAndMetadata(world, i8, 5, -2, plank2StairBlock, 7);
		}
		setBlockAndMetadata(world, 10, 1, -2, plank2Block, plank2Meta);
		setBlockAndMetadata(world, 10, 2, -2, plank2Block, plank2Meta);
		for (int i7 = 3; i7 <= 4; i7++) {
			setBlockAndMetadata(world, 10, i7, -2, fence2Block, fence2Meta);
		}
		setBlockAndMetadata(world, 10, 5, -2, plank2Block, plank2Meta);
		int i6;
		for (i6 = 7; i6 <= 8; i6++) {
			for (int i59 = 0; i59 <= 1; i59++) {
				setBlockAndMetadata(world, i6, 2, i59, plank2Block, plank2Meta);
				placePlateOrMug(world, random, i6, 3, i59);
			}
		}
		setBlockAndMetadata(world, 8, 5, 1, chandelierBlock, chandelierMeta);
		for (i6 = -3; i6 <= 4; i6++) {
			for (int i59 = 11; i59 <= 15; i59++) {
				setBlockAndMetadata(world, i6, -4, i59, floorBlock, floorMeta);
				for (int i60 = -3; i60 <= 0; i60++) {
					setAir(world, i6, i60, i59);
				}
			}
		}
		for (i6 = -3; i6 <= 4; i6++) {
			for (int i60 : new int[] { 10, 16 }) {
				setBlockAndMetadata(world, i6, -3, i60, plankBlock, plankMeta);
				setBlockAndMetadata(world, i6, -2, i60, beamBlock, beamMeta | 0x4);
				setBlockAndMetadata(world, i6, -1, i60, plankBlock, plankMeta);
			}
			int i59;
			for (i59 = 11; i59 <= 13; i59++) {
				if (i6 >= 0) {
					setBlockAndMetadata(world, i6, 0, i59, beamBlock, beamMeta | 0x4);
				}
			}
			for (i59 = 14; i59 <= 15; i59++) {
				setBlockAndMetadata(world, i6, 0, i59, beamBlock, beamMeta | 0x4);
			}
		}
		for (int i5 = 11; i5 <= 15; i5++) {
			for (int i59 : new int[] { -4, 5 }) {
				setBlockAndMetadata(world, i59, -3, i5, plankBlock, plankMeta);
				setBlockAndMetadata(world, i59, -2, i5, beamBlock, beamMeta | 0x8);
				setBlockAndMetadata(world, i59, -1, i5, plankBlock, plankMeta);
			}
		}
		for (int j1 = -3; j1 <= -1; j1++) {
			setBlockAndMetadata(world, -3, j1, 15, beamBlock, beamMeta);
			setBlockAndMetadata(world, 4, j1, 15, beamBlock, beamMeta);
			setBlockAndMetadata(world, 4, j1, 11, beamBlock, beamMeta);
			setBlockAndMetadata(world, 0, j1, 11, beamBlock, beamMeta);
		}
		placeBarrel(world, random, 4, -3, 14, 5, LOTRFoods.HOBBIT_DRINK);
		int i4;
		for (i4 = 12; i4 <= 13; i4++) {
			placeChest(world, random, 4, -3, i4, 5, LOTRChestContents.HOBBIT_HOLE_LARDER);
		}
		for (i4 = 12; i4 <= 14; i4++) {
			setBlockAndMetadata(world, 4, -2, i4, plankSlabBlock, plankSlabMeta | 0x8);
			placeBarrel(world, random, 4, -1, i4, 5, LOTRFoods.HOBBIT_DRINK);
		}
		placeBarrel(world, random, 1, -3, 11, 3, LOTRFoods.HOBBIT_DRINK);
		int i3;
		for (i3 = 2; i3 <= 3; i3++) {
			placeChest(world, random, i3, -3, 11, 3, LOTRChestContents.HOBBIT_HOLE_LARDER);
		}
		for (i3 = 1; i3 <= 3; i3++) {
			setBlockAndMetadata(world, i3, -2, 11, plankSlabBlock, plankSlabMeta | 0x8);
			Block cakeBlock = LOTRWorldGenHobbitStructure.getRandomCakeBlock(random);
			setBlockAndMetadata(world, i3, -1, 11, cakeBlock, 0);
		}
		int i2;
		for (i2 = 11; i2 <= 13; i2++) {
			setAir(world, -2, 1, i2);
			setAir(world, -3, 1, i2);
			setAir(world, -3, 0, i2);
		}
		for (i2 = 10; i2 <= 12; i2++) {
			setAir(world, -3, 0, i2);
		}
		setBlockAndMetadata(world, -3, 1, 14, floorBlock, floorMeta);
		for (int n = -3; n <= -1; n++) {
			for (int i59 = 11; i59 <= 12; i59++) {
				for (int i60 = -3; i60 <= -1; i60++) {
					setBlockAndMetadata(world, n, i60, i59, brickBlock, brickMeta);
				}
			}
		}
		for (int step = 0; step <= 2; step++) {
			setBlockAndMetadata(world, -2, 1 - step, 14 - step, floorStairBlock, 2);
		}
		int m;
		for (m = -3; m <= -2; m++) {
			setAir(world, m, -1, 11);
			setBlockAndMetadata(world, m, -2, 11, floorBlock, floorMeta);
		}
		setAir(world, -3, -1, 12);
		setBlockAndMetadata(world, -3, -2, 12, floorStairBlock, 3);
		for (m = -2; m <= -1; m++) {
			setBlockAndMetadata(world, m, -1, 13, floorStairBlock, 7);
		}
		int k1;
		for (k1 = 13; k1 <= 14; k1++) {
			setBlockAndMetadata(world, -3, -3, k1, floorBlock, floorMeta);
		}
		for (k1 = 13; k1 <= 15; k1++) {
			setBlockAndMetadata(world, -2, -3, k1, floorStairBlock, 0);
		}
		setBlockAndMetadata(world, -2, -1, 15, Blocks.torch, 2);
		for (k1 = 11; k1 <= 13; k1++) {
			setBlockAndMetadata(world, -4, 0, k1, beamBlock, beamMeta | 0x8);
			setBlockAndMetadata(world, -1, 0, k1, beamBlock, beamMeta | 0x8);
		}
		for (int i1 = -3; i1 <= -2; i1++) {
			setBlockAndMetadata(world, i1, 0, 10, beamBlock, beamMeta | 0x4);
		}
		LOTREntityHobbitBartender bartender = new LOTREntityHobbitBartender(world);
		bartender.setSpecificLocationName(tavernNameNPC);
		spawnNPCAndSetHome(bartender, world, 1, 2, 13, 2);
		for (int i59 : new int[] { -10, 10 }) {
			int i60 = 1;
			int i61 = 7;
			int hobbits = 3 + random.nextInt(6);
			for (int l = 0; l < hobbits; l++) {
				LOTREntityHobbit hobbit = new LOTREntityHobbit(world);
				spawnNPCAndSetHome(hobbit, world, i59, i60, i61, 16);
			}
			if (random.nextInt(4) == 0) {
				LOTREntityHobbitShirriff lOTREntityHobbitShirriff = new LOTREntityHobbitShirriff(world);
				((LOTREntityHobbit) lOTREntityHobbitShirriff).spawnRidingHorse = false;
				spawnNPCAndSetHome(lOTREntityHobbitShirriff, world, i59, i60, i61, 16);
			}
		}
		placeSign(world, -8, 4, 8, Blocks.wall_sign, 5, LOTRNames.getHobbitTavernQuote(random));
		placeSign(world, 8, 4, 8, Blocks.wall_sign, 4, LOTRNames.getHobbitTavernQuote(random));
		return true;
	}

	private void placePlateOrMug(World world, Random random, int i, int j, int k) {
		if (random.nextBoolean()) {
			placeMug(world, random, i, j, k, random.nextInt(4), LOTRFoods.HOBBIT_DRINK);
		} else {
			placePlate(world, random, i, j, k, plateBlock, LOTRFoods.HOBBIT);
		}
	}

	private ItemStack getTavernFramedItem(Random random) {
		ItemStack[] items = { new ItemStack(LOTRMod.daggerIron), new ItemStack(LOTRMod.leatherHat), LOTRItemLeatherHat.setFeatherColor(new ItemStack(LOTRMod.leatherHat), 16777215), LOTRItemLeatherHat.setHatColor(new ItemStack(LOTRMod.leatherHat), 2301981), LOTRItemLeatherHat.setFeatherColor(LOTRItemLeatherHat.setHatColor(new ItemStack(LOTRMod.leatherHat), 2301981), 3381529), new ItemStack(LOTRMod.hobbitPipe), new ItemStack(Items.book), new ItemStack(Items.feather), new ItemStack(Items.wooden_sword), new ItemStack(Items.bow), new ItemStack(LOTRMod.mug), new ItemStack(LOTRMod.mugAle), new ItemStack(LOTRMod.mugCider), new ItemStack(LOTRMod.ceramicMug), new ItemStack(Items.glass_bottle), new ItemStack(Items.arrow), new ItemStack(LOTRMod.shireHeather), new ItemStack(LOTRMod.bluebell), new ItemStack(Blocks.yellow_flower, 1, 0), new ItemStack(Blocks.red_flower, 1, 0), new ItemStack(Blocks.red_flower, 1, 3) };
		return items[random.nextInt(items.length)].copy();
	}
}
