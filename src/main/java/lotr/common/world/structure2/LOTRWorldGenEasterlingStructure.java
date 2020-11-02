package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.*;

public abstract class LOTRWorldGenEasterlingStructure extends LOTRWorldGenStructureBase2 {
	protected Block brickBlock;
	protected int brickMeta;
	protected Block brickSlabBlock;
	protected int brickSlabMeta;
	protected Block brickStairBlock;
	protected Block brickWallBlock;
	protected int brickWallMeta;
	protected Block brickCarvedBlock;
	protected int brickCarvedMeta;
	protected Block brickFloweryBlock;
	protected int brickFloweryMeta;
	protected Block brickFlowerySlabBlock;
	protected int brickFlowerySlabMeta;
	protected Block brickGoldBlock;
	protected int brickGoldMeta;
	protected Block brickRedBlock;
	protected int brickRedMeta;
	protected Block brickRedSlabBlock;
	protected int brickRedSlabMeta;
	protected Block brickRedStairBlock;
	protected Block brickRedWallBlock;
	protected int brickRedWallMeta;
	protected Block brickRedCarvedBlock;
	protected int brickRedCarvedMeta;
	protected Block pillarBlock;
	protected int pillarMeta;
	protected Block pillarRedBlock;
	protected int pillarRedMeta;
	protected Block logBlock;
	protected int logMeta;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block fenceGateBlock;
	protected Block woodBeamBlock;
	protected int woodBeamMeta;
	protected Block doorBlock;
	protected Block roofBlock;
	protected int roofMeta;
	protected Block roofSlabBlock;
	protected int roofSlabMeta;
	protected Block roofStairBlock;
	protected Block roofWallBlock;
	protected int roofWallMeta;
	protected Block barsBlock;
	protected Block tableBlock;
	protected Block gateBlock;
	protected Block bedBlock;
	protected Block plateBlock;
	protected Block cropBlock;
	protected int cropMeta;
	protected Item seedItem;
	protected LOTRItemBanner.BannerType bannerType;
	protected LOTRChestContents chestContents;

	public LOTRWorldGenEasterlingStructure(boolean flag) {
		super(flag);
	}

	protected boolean useTownBlocks() {
		return false;
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		brickBlock = LOTRMod.brick5;
		brickMeta = 11;
		brickSlabBlock = LOTRMod.slabSingle12;
		brickSlabMeta = 0;
		brickStairBlock = LOTRMod.stairsRhunBrick;
		brickWallBlock = LOTRMod.wall3;
		brickWallMeta = 15;
		brickCarvedBlock = LOTRMod.brick5;
		brickCarvedMeta = 12;
		brickFloweryBlock = LOTRMod.brick5;
		brickFloweryMeta = 15;
		brickFlowerySlabBlock = LOTRMod.slabSingle12;
		brickFlowerySlabMeta = 3;
		brickGoldBlock = LOTRMod.brick6;
		brickGoldMeta = 0;
		brickRedBlock = LOTRMod.brick6;
		brickRedMeta = 1;
		brickRedSlabBlock = LOTRMod.slabSingle12;
		brickRedSlabMeta = 5;
		brickRedStairBlock = LOTRMod.stairsRhunBrickRed;
		brickRedWallBlock = LOTRMod.wall4;
		brickRedWallMeta = 13;
		brickRedCarvedBlock = LOTRMod.brick6;
		brickRedCarvedMeta = 2;
		pillarBlock = LOTRMod.pillar2;
		pillarMeta = 8;
		pillarRedBlock = LOTRMod.pillar2;
		pillarRedMeta = 9;
		if (random.nextBoolean()) {
			logBlock = LOTRMod.wood8;
			logMeta = 1;
			plankBlock = LOTRMod.planks3;
			plankMeta = 1;
			plankSlabBlock = LOTRMod.woodSlabSingle5;
			plankSlabMeta = 1;
			plankStairBlock = LOTRMod.stairsRedwood;
			fenceBlock = LOTRMod.fence3;
			fenceMeta = 1;
			fenceGateBlock = LOTRMod.fenceGateRedwood;
			woodBeamBlock = LOTRMod.woodBeam8;
			woodBeamMeta = 1;
			doorBlock = LOTRMod.doorRedwood;
		} else {
			int randomWood = random.nextInt(4);
			if (randomWood == 0) {
				logBlock = Blocks.log;
				logMeta = 0;
				plankBlock = Blocks.planks;
				plankMeta = 0;
				plankSlabBlock = Blocks.wooden_slab;
				plankSlabMeta = 0;
				plankStairBlock = Blocks.oak_stairs;
				fenceBlock = Blocks.fence;
				fenceMeta = 0;
				fenceGateBlock = Blocks.fence_gate;
				woodBeamBlock = LOTRMod.woodBeamV1;
				woodBeamMeta = 0;
				doorBlock = Blocks.wooden_door;
			} else if (randomWood == 1) {
				logBlock = LOTRMod.wood2;
				logMeta = 1;
				plankBlock = LOTRMod.planks;
				plankMeta = 9;
				plankSlabBlock = LOTRMod.woodSlabSingle2;
				plankSlabMeta = 1;
				plankStairBlock = LOTRMod.stairsBeech;
				fenceBlock = LOTRMod.fence;
				fenceMeta = 9;
				fenceGateBlock = LOTRMod.fenceGateBeech;
				woodBeamBlock = LOTRMod.woodBeam2;
				woodBeamMeta = 1;
				doorBlock = LOTRMod.doorBeech;
			} else if (randomWood == 2) {
				logBlock = LOTRMod.wood6;
				logMeta = 2;
				plankBlock = LOTRMod.planks2;
				plankMeta = 10;
				plankSlabBlock = LOTRMod.woodSlabSingle4;
				plankSlabMeta = 2;
				plankStairBlock = LOTRMod.stairsCypress;
				fenceBlock = LOTRMod.fence2;
				fenceMeta = 10;
				fenceGateBlock = LOTRMod.fenceGateCypress;
				woodBeamBlock = LOTRMod.woodBeam6;
				woodBeamMeta = 2;
				doorBlock = LOTRMod.doorCypress;
			} else if (randomWood == 3) {
				logBlock = LOTRMod.wood6;
				logMeta = 3;
				plankBlock = LOTRMod.planks2;
				plankMeta = 11;
				plankSlabBlock = LOTRMod.woodSlabSingle4;
				plankSlabMeta = 3;
				plankStairBlock = LOTRMod.stairsOlive;
				fenceBlock = LOTRMod.fence2;
				fenceMeta = 11;
				fenceGateBlock = LOTRMod.fenceGateOlive;
				woodBeamBlock = LOTRMod.woodBeam6;
				woodBeamMeta = 3;
				doorBlock = LOTRMod.doorOlive;
			}
		}
		if (useTownBlocks()) {
			if (random.nextBoolean()) {
				roofBlock = LOTRMod.clayTileDyed;
				roofMeta = 14;
				roofSlabBlock = LOTRMod.slabClayTileDyedSingle2;
				roofSlabMeta = 6;
				roofStairBlock = LOTRMod.stairsClayTileDyedRed;
				roofWallBlock = LOTRMod.wallClayTileDyed;
				roofWallMeta = 14;
			} else {
				int randomClay = random.nextInt(2);
				if (randomClay == 0) {
					roofBlock = LOTRMod.clayTileDyed;
					roofMeta = 12;
					roofSlabBlock = LOTRMod.slabClayTileDyedSingle2;
					roofSlabMeta = 4;
					roofStairBlock = LOTRMod.stairsClayTileDyedBrown;
					roofWallBlock = LOTRMod.wallClayTileDyed;
					roofWallMeta = 12;
				} else if (randomClay == 1) {
					roofBlock = LOTRMod.clayTileDyed;
					roofMeta = 1;
					roofSlabBlock = LOTRMod.slabClayTileDyedSingle;
					roofSlabMeta = 1;
					roofStairBlock = LOTRMod.stairsClayTileDyedOrange;
					roofWallBlock = LOTRMod.wallClayTileDyed;
					roofWallMeta = 1;
				}
			}
		} else {
			roofBlock = LOTRMod.thatch;
			roofMeta = 0;
			roofSlabBlock = LOTRMod.slabSingleThatch;
			roofSlabMeta = 0;
			roofStairBlock = LOTRMod.stairsThatch;
			roofWallBlock = fenceBlock;
			roofWallMeta = fenceMeta;
		}
		barsBlock = random.nextBoolean() ? Blocks.iron_bars : LOTRMod.bronzeBars;
		tableBlock = LOTRMod.rhunTable;
		gateBlock = LOTRMod.gateRhun;
		bedBlock = useTownBlocks() ? Blocks.bed : LOTRMod.strawBed;
		plateBlock = useTownBlocks() ? LOTRMod.ceramicPlateBlock : random.nextBoolean() ? LOTRMod.ceramicPlateBlock : LOTRMod.woodPlateBlock;
		if (random.nextBoolean()) {
			cropBlock = Blocks.wheat;
			cropMeta = 7;
			seedItem = Items.wheat_seeds;
		} else {
			int randomCrop = random.nextInt(5);
			if (randomCrop == 0) {
				cropBlock = Blocks.carrots;
				cropMeta = 7;
				seedItem = Items.carrot;
			} else if (randomCrop == 1) {
				cropBlock = Blocks.potatoes;
				cropMeta = 7;
				seedItem = Items.potato;
			} else if (randomCrop == 2) {
				cropBlock = LOTRMod.lettuceCrop;
				cropMeta = 7;
				seedItem = LOTRMod.lettuce;
			} else if (randomCrop == 3) {
				cropBlock = LOTRMod.leekCrop;
				cropMeta = 7;
				seedItem = LOTRMod.leek;
			} else if (randomCrop == 4) {
				cropBlock = LOTRMod.turnipCrop;
				cropMeta = 7;
				seedItem = LOTRMod.turnip;
			}
		}
		bannerType = LOTRItemBanner.BannerType.RHUN;
		chestContents = LOTRChestContents.EASTERLING_HOUSE;
	}

	protected ItemStack getEasterlingFramedItem(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.helmetRhun), new ItemStack(LOTRMod.bodyRhun), new ItemStack(LOTRMod.legsRhun), new ItemStack(LOTRMod.bootsRhun), new ItemStack(LOTRMod.helmetRhunGold), new ItemStack(LOTRMod.bodyRhunGold), new ItemStack(LOTRMod.legsRhunGold), new ItemStack(LOTRMod.bootsRhunGold), new ItemStack(LOTRMod.daggerRhun), new ItemStack(LOTRMod.swordRhun), new ItemStack(LOTRMod.battleaxeRhun), new ItemStack(LOTRMod.spearRhun), new ItemStack(LOTRMod.rhunBow), new ItemStack(Items.arrow), new ItemStack(Items.skull), new ItemStack(Items.bone), new ItemStack(LOTRMod.gobletGold), new ItemStack(LOTRMod.gobletSilver), new ItemStack(LOTRMod.mug), new ItemStack(LOTRMod.goldRing) };
		return items[random.nextInt(items.length)].copy();
	}

	protected ItemStack getEasterlingWeaponItem(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.swordRhun), new ItemStack(LOTRMod.daggerRhun), new ItemStack(LOTRMod.daggerRhunPoisoned), new ItemStack(LOTRMod.spearRhun), new ItemStack(LOTRMod.battleaxeRhun), new ItemStack(LOTRMod.polearmRhun), new ItemStack(LOTRMod.pikeRhun) };
		return items[random.nextInt(items.length)].copy();
	}
}
