package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class LOTRWorldGenBreeStructure extends LOTRWorldGenStructureBase2 {
	protected Block brickBlock;
	protected int brickMeta;
	protected Block brick2Block;
	protected int brick2Meta;
	protected Block brick2SlabBlock;
	protected int brick2SlabMeta;
	protected Block brick2StairBlock;
	protected Block brick2WallBlock;
	protected int brick2WallMeta;
	protected Block floorBlock;
	protected int floorMeta;
	protected Block stoneWallBlock;
	protected int stoneWallMeta;
	protected Block woodBlock;
	protected int woodMeta;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block fenceGateBlock;
	protected Block doorBlock;
	protected Block trapdoorBlock;
	protected Block beamBlock;
	protected int beamMeta;
	protected Block roofBlock;
	protected int roofMeta;
	protected Block roofSlabBlock;
	protected int roofSlabMeta;
	protected Block roofStairBlock;
	protected Block carpetBlock;
	protected int carpetMeta;
	protected Block bedBlock;
	protected Block tableBlock;

	public LOTRWorldGenBreeStructure(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		brickBlock = LOTRMod.cobblebrick;
		brickMeta = 0;
		brick2Block = Blocks.stonebrick;
		brick2Meta = 0;
		brick2SlabBlock = Blocks.stone_slab;
		brick2SlabMeta = 5;
		brick2StairBlock = Blocks.stone_brick_stairs;
		brick2WallBlock = LOTRMod.wallStoneV;
		brick2WallMeta = 1;
		floorBlock = Blocks.cobblestone;
		floorMeta = 0;
		stoneWallBlock = Blocks.cobblestone_wall;
		stoneWallMeta = 0;
		int randomWood = random.nextInt(7);
		if (randomWood == 0) {
			plankBlock = Blocks.planks;
			plankMeta = 0;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 0;
			plankStairBlock = Blocks.oak_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 0;
			fenceGateBlock = Blocks.fence_gate;
			doorBlock = Blocks.wooden_door;
			trapdoorBlock = Blocks.trapdoor;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 0;
		} else if (randomWood == 1) {
			plankBlock = LOTRMod.planks;
			plankMeta = 9;
			plankSlabBlock = LOTRMod.woodSlabSingle2;
			plankSlabMeta = 1;
			plankStairBlock = LOTRMod.stairsBeech;
			fenceBlock = LOTRMod.fence;
			fenceMeta = 9;
			fenceGateBlock = LOTRMod.fenceGateBeech;
			doorBlock = LOTRMod.doorBeech;
			trapdoorBlock = LOTRMod.trapdoorBeech;
			beamBlock = LOTRMod.woodBeam2;
			beamMeta = 1;
		} else if (randomWood == 2) {
			plankBlock = Blocks.planks;
			plankMeta = 2;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 2;
			plankStairBlock = Blocks.birch_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 2;
			fenceGateBlock = LOTRMod.fenceGateBirch;
			doorBlock = LOTRMod.doorBirch;
			trapdoorBlock = LOTRMod.trapdoorBirch;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 2;
		} else if (randomWood == 3) {
			plankBlock = Blocks.planks;
			plankMeta = 1;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 1;
			plankStairBlock = Blocks.spruce_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 1;
			fenceGateBlock = LOTRMod.fenceGateSpruce;
			doorBlock = LOTRMod.doorSpruce;
			trapdoorBlock = LOTRMod.trapdoorSpruce;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 1;
		} else if (randomWood == 4) {
			woodBlock = LOTRMod.wood4;
			woodMeta = 0;
			plankBlock = LOTRMod.planks2;
			plankMeta = 0;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 0;
			plankStairBlock = LOTRMod.stairsChestnut;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 0;
			fenceGateBlock = LOTRMod.fenceGateChestnut;
			doorBlock = LOTRMod.doorChestnut;
			trapdoorBlock = LOTRMod.trapdoorChestnut;
			beamBlock = LOTRMod.woodBeam4;
			beamMeta = 0;
		} else if (randomWood == 5) {
			woodBlock = LOTRMod.wood3;
			woodMeta = 0;
			plankBlock = LOTRMod.planks;
			plankMeta = 12;
			plankSlabBlock = LOTRMod.woodSlabSingle2;
			plankSlabMeta = 4;
			plankStairBlock = LOTRMod.stairsMaple;
			fenceBlock = LOTRMod.fence;
			fenceMeta = 12;
			fenceGateBlock = LOTRMod.fenceGateMaple;
			doorBlock = LOTRMod.doorMaple;
			trapdoorBlock = LOTRMod.trapdoorMaple;
			beamBlock = LOTRMod.woodBeam3;
			beamMeta = 0;
		} else if (randomWood == 6) {
			woodBlock = LOTRMod.wood7;
			woodMeta = 0;
			plankBlock = LOTRMod.planks2;
			plankMeta = 12;
			plankSlabBlock = LOTRMod.woodSlabSingle4;
			plankSlabMeta = 4;
			plankStairBlock = LOTRMod.stairsAspen;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 12;
			fenceGateBlock = LOTRMod.fenceGateAspen;
			doorBlock = LOTRMod.doorAspen;
			trapdoorBlock = LOTRMod.trapdoorAspen;
			beamBlock = LOTRMod.woodBeam7;
			beamMeta = 0;
		}
		doorBlock = LOTRMod.doorBeech;
		trapdoorBlock = LOTRMod.trapdoorBeech;
		roofBlock = LOTRMod.thatch;
		roofMeta = 0;
		roofSlabBlock = LOTRMod.slabSingleThatch;
		roofSlabMeta = 0;
		roofStairBlock = LOTRMod.stairsThatch;
		carpetBlock = Blocks.carpet;
		carpetMeta = 12;
		bedBlock = LOTRMod.strawBed;
		tableBlock = LOTRMod.breeTable;
	}

	public static Block getRandomPieBlock(Random random) {
		int i = random.nextInt(3);
		if (i == 0) {
			return LOTRMod.appleCrumble;
		}
		if (i == 1) {
			return LOTRMod.cherryPie;
		}
		if (i == 2) {
			return LOTRMod.berryPie;
		}
		return LOTRMod.appleCrumble;
	}

	protected ItemStack getRandomBreeWeapon(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(Items.iron_sword), new ItemStack(LOTRMod.daggerIron), new ItemStack(LOTRMod.pikeIron), new ItemStack(LOTRMod.rollingPin) };
		return items[random.nextInt(items.length)].copy();
	}

	protected ItemStack getRandomTavernItem(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.rollingPin), new ItemStack(LOTRMod.mug), new ItemStack(LOTRMod.ceramicMug), new ItemStack(Items.bow), new ItemStack(Items.wooden_axe), new ItemStack(Items.fishing_rod), new ItemStack(Items.feather) };
		return items[random.nextInt(items.length)].copy();
	}

	protected void placeRandomFloor(World world, Random random, int i, int j, int k) {
		float randFloor = random.nextFloat();
		if (randFloor < 0.25f) {
			setBlockAndMetadata(world, i, j, k, Blocks.grass, 0);
		} else if (randFloor < 0.5f) {
			setBlockAndMetadata(world, i, j, k, Blocks.dirt, 1);
		} else if (randFloor < 0.75f) {
			setBlockAndMetadata(world, i, j, k, Blocks.gravel, 0);
		} else {
			setBlockAndMetadata(world, i, j, k, LOTRMod.dirtPath, 0);
		}
	}
}
