package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class LOTRWorldGenHobbitStructure extends LOTRWorldGenStructureBase2 {
	protected boolean isWealthy;

	protected Block plankBlock;

	protected int plankMeta;

	protected Block plankSlabBlock;

	protected int plankSlabMeta;

	protected Block plankStairBlock;

	protected Block fenceBlock;

	protected int fenceMeta;

	protected Block fenceGateBlock;

	protected Block beamBlock;

	protected int beamMeta;

	protected Block doorBlock;

	protected Block plank2Block;

	protected int plank2Meta;

	protected Block plank2SlabBlock;

	protected int plank2SlabMeta;

	protected Block plank2StairBlock;

	protected Block fence2Block;

	protected int fence2Meta;

	protected Block fenceGate2Block;

	protected Block floorBlock;

	protected int floorMeta;

	protected Block floorStairBlock;

	protected Block brickBlock;

	protected int brickMeta;

	protected Block brickStairBlock;

	protected Block wallBlock;

	protected int wallMeta;

	protected Block roofBlock;

	protected int roofMeta;

	protected Block roofSlabBlock;

	protected int roofSlabMeta;

	protected Block roofStairBlock;

	protected Block carpetBlock;

	protected int carpetMeta;

	protected Block chandelierBlock;

	protected int chandelierMeta;

	protected Block barsBlock;

	protected Block outFenceBlock;

	protected int outFenceMeta;

	protected Block outFenceGateBlock;

	protected Block pathBlock;

	protected int pathMeta;

	protected Block pathSlabBlock;

	protected int pathSlabMeta;

	protected Block hedgeBlock;

	protected int hedgeMeta;

	protected Block tileSlabBlock;

	protected int tileSlabMeta;

	protected Block bedBlock;

	protected Block tableBlock;

	protected Block gateBlock;

	protected Block plateBlock;

	protected String maleName;

	protected String femaleName;

	protected String homeName1;

	protected String homeName2;

	public LOTRWorldGenHobbitStructure(boolean flag) {
		super(flag);
	}

	protected boolean makeWealthy(Random random) {
		return random.nextInt(5) == 0;
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		isWealthy = makeWealthy(random);
		int randomWood = random.nextInt(5);
		if (randomWood == 0) {
			plankBlock = LOTRMod.planks;
			plankMeta = 0;
			plankSlabBlock = LOTRMod.woodSlabSingle;
			plankSlabMeta = 0;
			plankStairBlock = LOTRMod.stairsShirePine;
			fenceBlock = LOTRMod.fence;
			fenceMeta = 0;
			fenceGateBlock = LOTRMod.fenceGateShirePine;
			beamBlock = LOTRMod.woodBeam1;
			beamMeta = 0;
			doorBlock = Blocks.wooden_door;
		} else if (randomWood == 1) {
			plankBlock = Blocks.planks;
			plankMeta = 0;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 0;
			plankStairBlock = Blocks.oak_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 0;
			fenceGateBlock = Blocks.fence_gate;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 0;
			doorBlock = Blocks.wooden_door;
		} else if (randomWood == 2) {
			plankBlock = Blocks.planks;
			plankMeta = 2;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 2;
			plankStairBlock = Blocks.birch_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 2;
			fenceGateBlock = LOTRMod.fenceGateBirch;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 0;
			doorBlock = LOTRMod.doorBirch;
		} else if (randomWood == 3) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 0;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 0;
			plankStairBlock = LOTRMod.stairsChestnut;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 0;
			fenceGateBlock = LOTRMod.fenceGateChestnut;
			beamBlock = LOTRMod.woodBeam4;
			beamMeta = 0;
			doorBlock = LOTRMod.doorChestnut;
		} else if (randomWood == 4) {
			plankBlock = LOTRMod.planks;
			plankMeta = 9;
			plankSlabBlock = LOTRMod.woodSlabSingle2;
			plankSlabMeta = 1;
			plankStairBlock = LOTRMod.stairsBeech;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 9;
			fenceGateBlock = LOTRMod.fenceGateBeech;
			beamBlock = LOTRMod.woodBeam2;
			beamMeta = 1;
			doorBlock = LOTRMod.doorBeech;
		}
		if (random.nextBoolean()) {
			doorBlock = LOTRMod.doorShirePine;
		}
		if (isWealthy) {
			int randomWood2 = random.nextInt(3);
			if (randomWood2 == 0) {
				plank2Block = LOTRMod.planks;
				plank2Meta = 4;
				plank2SlabBlock = LOTRMod.woodSlabSingle;
				plank2SlabMeta = 4;
				plank2StairBlock = LOTRMod.stairsApple;
				fence2Block = LOTRMod.fence;
				fence2Meta = 4;
				fenceGate2Block = LOTRMod.fenceGateApple;
			} else if (randomWood2 == 1) {
				plank2Block = LOTRMod.planks;
				plank2Meta = 5;
				plank2SlabBlock = LOTRMod.woodSlabSingle;
				plank2SlabMeta = 5;
				plank2StairBlock = LOTRMod.stairsPear;
				fence2Block = LOTRMod.fence;
				fence2Meta = 5;
				fenceGate2Block = LOTRMod.fenceGatePear;
			} else if (randomWood2 == 2) {
				plank2Block = LOTRMod.planks;
				plank2Meta = 6;
				plank2SlabBlock = LOTRMod.woodSlabSingle;
				plank2SlabMeta = 6;
				plank2StairBlock = LOTRMod.stairsCherry;
				fence2Block = LOTRMod.fence;
				fence2Meta = 6;
				fenceGate2Block = LOTRMod.fenceGateCherry;
			}
		} else {
			int randomWood2 = random.nextInt(3);
			if (randomWood2 == 0) {
				plank2Block = Blocks.planks;
				plank2Meta = 0;
				plank2SlabBlock = Blocks.wooden_slab;
				plank2SlabMeta = 0;
				plank2StairBlock = Blocks.oak_stairs;
				fence2Block = Blocks.fence;
				fence2Meta = 0;
				fenceGate2Block = Blocks.fence_gate;
			} else if (randomWood2 == 1) {
				plank2Block = Blocks.planks;
				plank2Meta = 1;
				plank2SlabBlock = Blocks.wooden_slab;
				plank2SlabMeta = 1;
				plank2StairBlock = Blocks.spruce_stairs;
				fence2Block = Blocks.fence;
				fence2Meta = 1;
				fenceGate2Block = LOTRMod.fenceGateSpruce;
			} else if (randomWood2 == 2) {
				plank2Block = LOTRMod.planks2;
				plank2Meta = 9;
				plank2SlabBlock = LOTRMod.woodSlabSingle4;
				plank2SlabMeta = 1;
				plank2StairBlock = LOTRMod.stairsWillow;
				fence2Block = LOTRMod.fence2;
				fence2Meta = 9;
				fenceGate2Block = LOTRMod.fenceGateWillow;
			}
		}
		int randomFloor = random.nextInt(3);
		if (randomFloor == 0) {
			floorBlock = Blocks.brick_block;
			floorMeta = 0;
			floorStairBlock = Blocks.brick_stairs;
		} else if (randomFloor == 1) {
			floorBlock = Blocks.cobblestone;
			floorMeta = 0;
			floorStairBlock = Blocks.stone_stairs;
		} else if (randomFloor == 2) {
			floorBlock = Blocks.stonebrick;
			floorMeta = 0;
			floorStairBlock = Blocks.stone_brick_stairs;
		}
		brickBlock = Blocks.brick_block;
		brickMeta = 0;
		brickStairBlock = Blocks.brick_stairs;
		if (random.nextBoolean()) {
			wallBlock = LOTRMod.daub;
			wallMeta = 0;
		} else {
			wallBlock = plankBlock;
			wallMeta = plankMeta;
		}
		roofBlock = LOTRMod.thatch;
		roofMeta = 0;
		roofSlabBlock = LOTRMod.slabSingleThatch;
		roofSlabMeta = 0;
		roofStairBlock = LOTRMod.stairsThatch;
		int randomCarpet = random.nextInt(5);
		if (randomCarpet == 0) {
			carpetBlock = Blocks.carpet;
			carpetMeta = 15;
		} else if (randomCarpet == 1) {
			carpetBlock = Blocks.carpet;
			carpetMeta = 14;
		} else if (randomCarpet == 2) {
			carpetBlock = Blocks.carpet;
			carpetMeta = 13;
		} else if (randomCarpet == 3) {
			carpetBlock = Blocks.carpet;
			carpetMeta = 12;
		} else if (randomCarpet == 4) {
			carpetBlock = Blocks.carpet;
			carpetMeta = 7;
		}
		if (isWealthy) {
			int randomChandelier = random.nextInt(2);
			if (randomChandelier == 0) {
				chandelierBlock = LOTRMod.chandelier;
				chandelierMeta = 2;
			} else if (randomChandelier == 1) {
				chandelierBlock = LOTRMod.chandelier;
				chandelierMeta = 3;
			}
		} else {
			int randomChandelier = random.nextInt(2);
			if (randomChandelier == 0) {
				chandelierBlock = LOTRMod.chandelier;
				chandelierMeta = 0;
			} else if (randomChandelier == 1) {
				chandelierBlock = LOTRMod.chandelier;
				chandelierMeta = 1;
			}
		}
		if (random.nextBoolean()) {
			barsBlock = Blocks.iron_bars;
		} else {
			barsBlock = LOTRMod.bronzeBars;
		}
		if (random.nextInt(3) == 0) {
			outFenceBlock = Blocks.fence;
			outFenceMeta = 0;
			outFenceGateBlock = Blocks.fence_gate;
		} else {
			outFenceBlock = Blocks.cobblestone_wall;
			outFenceMeta = 0;
			outFenceGateBlock = Blocks.fence_gate;
		}
		if (random.nextInt(3) == 0) {
			pathBlock = LOTRMod.dirtPath;
			pathMeta = 0;
			pathSlabBlock = LOTRMod.slabSingleDirt;
			pathSlabMeta = 1;
		} else {
			pathBlock = Blocks.gravel;
			pathMeta = 0;
			pathSlabBlock = LOTRMod.slabSingleGravel;
			pathSlabMeta = 0;
		}
		hedgeBlock = Blocks.leaves;
		hedgeMeta = 4;
		if (random.nextBoolean()) {
			tileSlabBlock = LOTRMod.slabClayTileDyedSingle2;
			tileSlabMeta = 4;
		} else {
			tileSlabBlock = LOTRMod.slabClayTileDyedSingle2;
			tileSlabMeta = 5;
		}
		bedBlock = Blocks.bed;
		tableBlock = LOTRMod.hobbitTable;
		int randomGate = random.nextInt(4);
		if (randomGate == 0) {
			gateBlock = LOTRMod.gateHobbitGreen;
		} else if (randomGate == 1) {
			gateBlock = LOTRMod.gateHobbitBlue;
		} else if (randomGate == 2) {
			gateBlock = LOTRMod.gateHobbitRed;
		} else if (randomGate == 3) {
			gateBlock = LOTRMod.gateHobbitYellow;
		}
		if (random.nextBoolean()) {
			plateBlock = LOTRMod.ceramicPlateBlock;
		} else {
			plateBlock = LOTRMod.plateBlock;
		}
		String[] hobbitNames = getHobbitCoupleAndHomeNames(random);
		maleName = hobbitNames[0];
		femaleName = hobbitNames[1];
		homeName1 = hobbitNames[2];
		homeName2 = hobbitNames[3];
	}

	protected String[] getHobbitCoupleAndHomeNames(Random random) {
		return LOTRNames.getHobbitCoupleAndHomeNames(random);
	}

	protected void spawnHobbitCouple(World world, int i, int j, int k, int homeRange) {
		LOTREntityHobbit hobbitMale = createHobbit(world);
		hobbitMale.familyInfo.setName(maleName);
		hobbitMale.familyInfo.setMale(true);
		spawnNPCAndSetHome(hobbitMale, world, i, j, k, homeRange);
		LOTREntityHobbit hobbitFemale = createHobbit(world);
		hobbitFemale.familyInfo.setName(femaleName);
		hobbitFemale.familyInfo.setMale(false);
		spawnNPCAndSetHome(hobbitFemale, world, i, j, k, homeRange);
		int maxChildren = hobbitMale.familyInfo.getRandomMaxChildren();
		hobbitMale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.hobbitRing));
		hobbitMale.familyInfo.spouseUniqueID = hobbitFemale.getUniqueID();
		hobbitMale.familyInfo.setMaxBreedingDelay();
		hobbitMale.familyInfo.maxChildren = maxChildren;
		hobbitFemale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.hobbitRing));
		hobbitFemale.familyInfo.spouseUniqueID = hobbitMale.getUniqueID();
		hobbitFemale.familyInfo.setMaxBreedingDelay();
		hobbitFemale.familyInfo.maxChildren = maxChildren;
	}

	protected LOTREntityHobbit createHobbit(World world) {
		return new LOTREntityHobbit(world);
	}

	public static Block getRandomCakeBlock(Random random) {
		int i = random.nextInt(5);
		if (i == 0) {
			return Blocks.cake;
		}
		if (i == 1) {
			return LOTRMod.appleCrumble;
		}
		if (i == 2) {
			return LOTRMod.cherryPie;
		}
		if (i == 3) {
			return LOTRMod.berryPie;
		}
		if (i == 4) {
			return LOTRMod.marzipanBlock;
		}
		return Blocks.cake;
	}

	protected ItemStack getRandomHobbitDecoration(World world, Random random) {
		if (random.nextInt(3) == 0) {
			return getRandomFlower(world, random);
		}
		ItemStack[] items = { new ItemStack(LOTRMod.rollingPin), new ItemStack(LOTRMod.mug), new ItemStack(LOTRMod.ceramicMug), new ItemStack(Items.bow), new ItemStack(Items.fishing_rod), new ItemStack(Items.feather), new ItemStack(Items.clock), new ItemStack(LOTRMod.leatherHat), new ItemStack(LOTRMod.hobbitPipe), new ItemStack(Blocks.brown_mushroom), new ItemStack(Blocks.red_mushroom) };
		return items[random.nextInt(items.length)].copy();
	}
}
