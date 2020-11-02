package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.*;
import lotr.common.entity.npc.LOTREntityDorwinionElf;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class LOTRWorldGenDorwinionElfHouse extends LOTRWorldGenDorwinionHouse {
	private Block grapevineBlock;
	private Item wineItem;
	private Item grapeItem;

	public LOTRWorldGenDorwinionElfHouse(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		if (random.nextBoolean()) {
			grapevineBlock = LOTRMod.grapevineRed;
			wineItem = LOTRMod.mugRedWine;
			grapeItem = LOTRMod.grapeRed;
		} else {
			grapevineBlock = LOTRMod.grapevineWhite;
			wineItem = LOTRMod.mugWhiteWine;
			grapeItem = LOTRMod.grapeWhite;
		}
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int j1;
		int i1;
		int i12;
		int i13;
		int k1;
		int j12;
		int k2;
		int i14;
		int k12;
		int k13;
		this.setOriginAndRotation(world, i, j, k, rotation, 1);
		setupRandomBlocks(random);
		if (restrictions) {
			for (int i15 = -4; i15 <= 8; ++i15) {
				for (k13 = -1; k13 <= 20; ++k13) {
					j12 = getTopBlock(world, i15, k13) - 1;
					Block block = getBlock(world, i15, j12, k13);
					if (block == Blocks.grass) {
						continue;
					}
					return false;
				}
			}
		}
		boolean generateBackGate = true;
		for (i1 = 1; i1 <= 3; ++i1) {
			k1 = 20;
			j1 = getTopBlock(world, i1, k1) - 1;
			if (j1 == 0) {
				continue;
			}
			generateBackGate = false;
		}
		for (i1 = -4; i1 <= 8; ++i1) {
			for (k1 = 0; k1 <= 20; ++k1) {
				for (j1 = 1; j1 <= 6; ++j1) {
					setAir(world, i1, j1, k1);
				}
				setBlockAndMetadata(world, i1, 0, k1, Blocks.grass, 0);
				j1 = -1;
				while (!isOpaque(world, i1, j1, k1) && getY(j1) >= 0) {
					setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
					--j1;
				}
			}
		}
		for (i1 = -3; i1 <= 7; ++i1) {
			for (k1 = 0; k1 <= 8; ++k1) {
				if (i1 >= 3 && k1 <= 2) {
					if (random.nextInt(3) != 0) {
						continue;
					}
					plantFlower(world, random, i1, 1, k1);
					continue;
				}
				if (k1 == 0 && (i1 == -3 || i1 == 2) || k1 == 3 && (i1 == 2 || i1 == 7) || k1 == 8 && (i1 == -3 || i1 == 7)) {
					for (j1 = 0; j1 <= 4; ++j1) {
						setBlockAndMetadata(world, i1, j1, k1, woodBeamBlock, woodBeamMeta);
					}
					continue;
				}
				if (i1 == -3 || i1 == 2 && k1 <= 3 || i1 == 7 || k1 == 0 || k1 == 3 && i1 >= 2 || k1 == 8) {
					for (j1 = 0; j1 <= 1; ++j1) {
						setBlockAndMetadata(world, i1, j1, k1, wallBlock, wallMeta);
					}
					for (j1 = 2; j1 <= 4; ++j1) {
						setBlockAndMetadata(world, i1, j1, k1, brickBlock, brickMeta);
					}
					continue;
				}
				setBlockAndMetadata(world, i1, 0, k1, floorBlock, floorMeta);
			}
		}
		for (k13 = 1; k13 <= 7; ++k13) {
			k2 = IntMath.mod(k13, 3);
			if (k2 == 1) {
				setBlockAndMetadata(world, -4, 1, k13, brickStairBlock, 1);
				setGrassToDirt(world, -4, 0, k13);
				continue;
			}
			if (k2 == 2) {
				setAir(world, -3, 2, k13);
				setBlockAndMetadata(world, -3, 3, k13, brickStairBlock, 7);
				setBlockAndMetadata(world, -4, 1, k13, leafBlock, leafMeta);
				continue;
			}
			if (k2 != 0) {
				continue;
			}
			setAir(world, -3, 2, k13);
			setBlockAndMetadata(world, -3, 3, k13, brickStairBlock, 6);
			setBlockAndMetadata(world, -4, 1, k13, leafBlock, leafMeta);
		}
		for (int k14 : new int[] { 0, 8 }) {
			setAir(world, -1, 2, k14);
			setAir(world, 0, 2, k14);
			setBlockAndMetadata(world, -1, 3, k14, brickStairBlock, 4);
			setBlockAndMetadata(world, 0, 3, k14, brickStairBlock, 5);
		}
		for (int k14 : new int[] { 3, 8 }) {
			setAir(world, 4, 2, k14);
			setAir(world, 5, 2, k14);
			setBlockAndMetadata(world, 4, 3, k14, brickStairBlock, 4);
			setBlockAndMetadata(world, 5, 3, k14, brickStairBlock, 5);
		}
		setBlockAndMetadata(world, 3, 1, 2, brickStairBlock, 2);
		setGrassToDirt(world, 3, 0, 2);
		setBlockAndMetadata(world, 4, 1, 2, leafBlock, leafMeta);
		setBlockAndMetadata(world, 5, 1, 2, leafBlock, leafMeta);
		setBlockAndMetadata(world, 6, 1, 2, brickStairBlock, 2);
		setGrassToDirt(world, 6, 0, 2);
		setBlockAndMetadata(world, 8, 1, 4, brickStairBlock, 0);
		setGrassToDirt(world, 8, 0, 4);
		setBlockAndMetadata(world, 8, 1, 5, leafBlock, leafMeta);
		setBlockAndMetadata(world, 8, 1, 6, leafBlock, leafMeta);
		setBlockAndMetadata(world, 8, 1, 7, brickStairBlock, 0);
		setGrassToDirt(world, 8, 0, 7);
		setAir(world, 7, 2, 5);
		setAir(world, 7, 2, 6);
		setBlockAndMetadata(world, 7, 3, 5, brickStairBlock, 7);
		setBlockAndMetadata(world, 7, 3, 6, brickStairBlock, 6);
		for (int i16 : new int[] { -1, 0 }) {
			setBlockAndMetadata(world, i16, 0, 0, floorBlock, floorMeta);
			setAir(world, i16, 1, 0);
		}
		for (i12 = -3; i12 <= 2; ++i12) {
			setBlockAndMetadata(world, i12, 4, -1, brickStairBlock, 6);
		}
		for (k12 = -1; k12 <= 2; ++k12) {
			setBlockAndMetadata(world, 3, 4, k12, brickStairBlock, 4);
			if (IntMath.mod(k12, 2) != 1) {
				continue;
			}
			setBlockAndMetadata(world, 3, 5, k12, brickSlabBlock, brickSlabMeta);
		}
		for (i12 = 4; i12 <= 8; ++i12) {
			setBlockAndMetadata(world, i12, 4, 2, brickStairBlock, 6);
			if (IntMath.mod(i12, 2) != 0) {
				continue;
			}
			setBlockAndMetadata(world, i12, 5, 2, brickSlabBlock, brickSlabMeta);
		}
		for (k12 = 3; k12 <= 8; ++k12) {
			setBlockAndMetadata(world, 8, 4, k12, brickStairBlock, 4);
		}
		for (i12 = 8; i12 >= -4; --i12) {
			setBlockAndMetadata(world, i12, 4, 9, brickStairBlock, 7);
			if (IntMath.mod(i12, 2) != 0) {
				continue;
			}
			setBlockAndMetadata(world, i12, 5, 9, brickSlabBlock, brickSlabMeta);
		}
		for (k12 = 8; k12 >= -1; --k12) {
			setBlockAndMetadata(world, -4, 4, k12, brickStairBlock, 5);
			if (IntMath.mod(k12, 2) != 1) {
				continue;
			}
			setBlockAndMetadata(world, -4, 5, k12, brickSlabBlock, brickSlabMeta);
		}
		for (k12 = 1; k12 <= 7; ++k12) {
			setBlockAndMetadata(world, -2, 4, k12, plankSlabBlock, plankSlabMeta | 8);
			if (k12 <= 3) {
				setBlockAndMetadata(world, 1, 4, k12, plankSlabBlock, plankSlabMeta | 8);
			}
			if (k12 < 4) {
				continue;
			}
			setBlockAndMetadata(world, 2, 4, k12, plankSlabBlock, plankSlabMeta | 8);
		}
		for (i12 = -2; i12 <= 6; ++i12) {
			setBlockAndMetadata(world, i12, 4, 7, plankSlabBlock, plankSlabMeta | 8);
			if (i12 <= 1) {
				setBlockAndMetadata(world, i12, 4, 3, plankSlabBlock, plankSlabMeta | 8);
			}
			if (i12 < 2) {
				continue;
			}
			setBlockAndMetadata(world, i12, 4, 4, plankSlabBlock, plankSlabMeta | 8);
		}
		for (k12 = 1; k12 <= 6; ++k12) {
			setBlockAndMetadata(world, -2, 5, k12, plankStairBlock, 4);
			if (k12 <= 5) {
				setBlockAndMetadata(world, -1, 6, k12, plankStairBlock, 4);
			}
			if (k12 <= 4) {
				setBlockAndMetadata(world, 0, 6, k12, plankStairBlock, 5);
			}
			if (k12 > 3) {
				continue;
			}
			setBlockAndMetadata(world, 1, 5, k12, plankStairBlock, 5);
		}
		for (i12 = -2; i12 <= 6; ++i12) {
			setBlockAndMetadata(world, i12, 5, 7, plankStairBlock, 6);
			if (i12 >= -1) {
				setBlockAndMetadata(world, i12, 6, 6, plankStairBlock, 6);
			}
			if (i12 >= 0) {
				setBlockAndMetadata(world, i12, 6, 5, plankStairBlock, 7);
			}
			if (i12 < 1) {
				continue;
			}
			setBlockAndMetadata(world, i12, 5, 4, plankStairBlock, 7);
		}
		setBlockAndMetadata(world, -2, 5, 0, plankBlock, plankMeta);
		setBlockAndMetadata(world, -1, 5, 0, plankStairBlock, 4);
		setBlockAndMetadata(world, -1, 6, 0, plankBlock, plankMeta);
		setBlockAndMetadata(world, 0, 6, 0, plankBlock, plankMeta);
		setBlockAndMetadata(world, 0, 5, 0, plankStairBlock, 5);
		setBlockAndMetadata(world, 1, 5, 0, plankBlock, plankMeta);
		setBlockAndMetadata(world, 7, 5, 4, plankBlock, plankMeta);
		setBlockAndMetadata(world, 7, 5, 5, plankStairBlock, 7);
		setBlockAndMetadata(world, 7, 6, 5, plankBlock, plankMeta);
		setBlockAndMetadata(world, 7, 6, 6, plankBlock, plankMeta);
		setBlockAndMetadata(world, 7, 5, 6, plankStairBlock, 6);
		setBlockAndMetadata(world, 7, 5, 7, plankBlock, plankMeta);
		for (k12 = -1; k12 <= 7; ++k12) {
			setBlockAndMetadata(world, -3, 5, k12, clayStairBlock, 1);
			if (k12 <= 6) {
				setBlockAndMetadata(world, -2, 6, k12, clayStairBlock, 1);
			}
			if (k12 <= 5) {
				setBlockAndMetadata(world, -1, 7, k12, clayStairBlock, 1);
			}
			if (k12 <= 4) {
				setBlockAndMetadata(world, 0, 7, k12, clayStairBlock, 0);
			}
			if (k12 <= 3) {
				setBlockAndMetadata(world, 1, 6, k12, clayStairBlock, 0);
			}
			if (k12 > 2) {
				continue;
			}
			setBlockAndMetadata(world, 2, 5, k12, clayStairBlock, 0);
		}
		for (i12 = -3; i12 <= 8; ++i12) {
			setBlockAndMetadata(world, i12, 5, 8, clayStairBlock, 3);
			if (i12 >= -2) {
				setBlockAndMetadata(world, i12, 6, 7, clayStairBlock, 3);
			}
			if (i12 >= -1) {
				setBlockAndMetadata(world, i12, 7, 6, clayStairBlock, 3);
			}
			if (i12 >= 0) {
				setBlockAndMetadata(world, i12, 7, 5, clayStairBlock, 2);
			}
			if (i12 >= 1) {
				setBlockAndMetadata(world, i12, 6, 4, clayStairBlock, 2);
			}
			if (i12 < 2) {
				continue;
			}
			setBlockAndMetadata(world, i12, 5, 3, clayStairBlock, 2);
		}
		setBlockAndMetadata(world, -2, 5, -1, clayStairBlock, 4);
		setBlockAndMetadata(world, -1, 6, -1, clayStairBlock, 4);
		setBlockAndMetadata(world, 0, 6, -1, clayStairBlock, 5);
		setBlockAndMetadata(world, 1, 5, -1, clayStairBlock, 5);
		setBlockAndMetadata(world, 8, 5, 4, clayStairBlock, 7);
		setBlockAndMetadata(world, 8, 6, 5, clayStairBlock, 7);
		setBlockAndMetadata(world, 8, 6, 6, clayStairBlock, 6);
		setBlockAndMetadata(world, 8, 5, 7, clayStairBlock, 6);
		setBlockAndMetadata(world, -2, 3, 1, Blocks.torch, 3);
		setBlockAndMetadata(world, -2, 3, 4, Blocks.torch, 2);
		setBlockAndMetadata(world, -2, 3, 7, Blocks.torch, 4);
		setBlockAndMetadata(world, 6, 3, 7, Blocks.torch, 4);
		setBlockAndMetadata(world, 6, 3, 4, Blocks.torch, 3);
		setBlockAndMetadata(world, 1, 3, 1, Blocks.torch, 3);
		setBlockAndMetadata(world, -2, 1, 4, Blocks.crafting_table, 0);
		this.placeChest(world, random, -2, 1, 5, 5, LOTRChestContents.DORWINION_HOUSE);
		this.placeChest(world, random, -2, 1, 6, 5, LOTRChestContents.DORWINION_HOUSE);
		setBlockAndMetadata(world, -2, 1, 7, LOTRMod.dorwinionTable, 0);
		setBlockAndMetadata(world, -1, 1, 6, Blocks.bed, 0);
		setBlockAndMetadata(world, -1, 1, 7, Blocks.bed, 8);
		setBlockAndMetadata(world, 2, 1, 4, Blocks.furnace, 3);
		setBlockAndMetadata(world, 3, 1, 4, Blocks.cauldron, 3);
		setBlockAndMetadata(world, 4, 1, 4, plankStairBlock, 4);
		setBlockAndMetadata(world, 5, 1, 4, plankSlabBlock, plankSlabMeta | 8);
		setBlockAndMetadata(world, 6, 1, 4, plankBlock, plankMeta);
		setBlockAndMetadata(world, 6, 1, 5, plankSlabBlock, plankSlabMeta | 8);
		setBlockAndMetadata(world, 6, 1, 6, plankSlabBlock, plankSlabMeta | 8);
		setBlockAndMetadata(world, 6, 1, 7, plankBlock, plankMeta);
		setBlockAndMetadata(world, 5, 1, 7, plankSlabBlock, plankSlabMeta | 8);
		setBlockAndMetadata(world, 4, 1, 7, plankStairBlock, 4);
		int[] i17 = new int[] { 4, 7 };
		k2 = i17.length;
		for (j1 = 0; j1 < k2; ++j1) {
			int k14;
			k14 = i17[j1];
			for (int i18 = 4; i18 <= 5; ++i18) {
				placePlate(world, random, i18, 2, k14, plateBlock, LOTRFoods.DORWINION);
			}
			this.placeBarrel(world, random, 6, 2, k14, 5, new ItemStack(wineItem));
		}
		this.placeMug(world, random, 6, 2, 5, 1, new ItemStack(wineItem), LOTRFoods.DORWINION_DRINK);
		this.placeMug(world, random, 6, 2, 6, 1, new ItemStack(wineItem), LOTRFoods.DORWINION_DRINK);
		setBlockAndMetadata(world, 2, 0, 8, floorBlock, floorMeta);
		setBlockAndMetadata(world, 2, 1, 8, doorBlock, 3);
		setBlockAndMetadata(world, 2, 2, 8, doorBlock, 8);
		spawnItemFrame(world, 2, 3, 8, 2, new ItemStack(grapeItem));
		setBlockAndMetadata(world, 2, 3, 9, Blocks.torch, 3);
		for (i13 = -3; i13 <= 7; ++i13) {
			for (k1 = 9; k1 <= 19; ++k1) {
				if (i13 == -3 || i13 == 7 || k1 == 19) {
					setGrassToDirt(world, i13, 0, k1);
					setBlockAndMetadata(world, i13, 1, k1, wallBlock, wallMeta);
					setBlockAndMetadata(world, i13, 2, k1, brickBlock, brickMeta);
					if (IntMath.mod(i13 + k1, 2) != 0) {
						continue;
					}
					setBlockAndMetadata(world, i13, 3, k1, brickSlabBlock, brickSlabMeta);
					continue;
				}
				setBlockAndMetadata(world, i13, 0, k1, LOTRMod.dirtPath, 0);
				if (IntMath.mod(i13, 2) != 1) {
					continue;
				}
				if (k1 == 14) {
					setBlockAndMetadata(world, i13, 0, k1, Blocks.water, 0);
					setBlockAndMetadata(world, i13, 1, k1, fenceBlock, fenceMeta);
					setBlockAndMetadata(world, i13, 2, k1, Blocks.torch, 5);
					continue;
				}
				if (k1 < 11 || k1 > 17) {
					continue;
				}
				setBlockAndMetadata(world, i13, 0, k1, Blocks.farmland, 7);
				setBlockAndMetadata(world, i13, 1, k1, grapevineBlock, 7);
				setBlockAndMetadata(world, i13, 2, k1, grapevineBlock, 7);
			}
		}
		for (i13 = 0; i13 <= 4; ++i13) {
			setBlockAndMetadata(world, i13, 3, 19, brickBlock, brickMeta);
		}
		for (i13 = 1; i13 <= 3; ++i13) {
			setBlockAndMetadata(world, i13, 4, 19, brickBlock, brickMeta);
		}
		setBlockAndMetadata(world, 0, 4, 19, brickStairBlock, 1);
		setBlockAndMetadata(world, 4, 4, 19, brickStairBlock, 0);
		setBlockAndMetadata(world, 1, 5, 19, brickSlabBlock, brickSlabMeta);
		setBlockAndMetadata(world, 3, 5, 19, brickSlabBlock, brickSlabMeta);
		for (int i16 : new int[] { -3, 4 }) {
			setGrassToDirt(world, i16, 0, 20);
			setBlockAndMetadata(world, i16, 1, 20, brickStairBlock, 3);
			setBlockAndMetadata(world, i16 + 1, 1, 20, leafBlock, leafMeta);
			setBlockAndMetadata(world, i16 + 2, 1, 20, leafBlock, leafMeta);
			setGrassToDirt(world, i16 + 3, 0, 20);
			setBlockAndMetadata(world, i16 + 3, 1, 20, brickStairBlock, 3);
		}
		if (generateBackGate) {
			for (i14 = 1; i14 <= 3; ++i14) {
				setBlockAndMetadata(world, i14, 0, 19, LOTRMod.dirtPath, 0);
				for (j12 = 1; j12 <= 3; ++j12) {
					setBlockAndMetadata(world, i14, j12, 19, LOTRMod.gateWooden, 2);
				}
			}
		} else {
			for (i14 = 1; i14 <= 3; ++i14) {
				setBlockAndMetadata(world, i14, 1, 20, leafBlock, leafMeta);
			}
		}
		LOTREntityDorwinionElf dorwinionElf = new LOTREntityDorwinionElf(world);
		spawnNPCAndSetHome(dorwinionElf, world, 0, 1, 5, 16);
		return true;
	}
}
