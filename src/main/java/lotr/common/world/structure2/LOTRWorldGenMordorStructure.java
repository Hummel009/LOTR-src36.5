package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class LOTRWorldGenMordorStructure extends LOTRWorldGenStructureBase2 {
	protected Block brickBlock;
	protected int brickMeta;
	protected Block brickSlabBlock;
	protected int brickSlabMeta;
	protected Block brickStairBlock;
	protected Block brickWallBlock;
	protected int brickWallMeta;
	protected Block brickCarvedBlock;
	protected int brickCarvedMeta;
	protected Block pillarBlock;
	protected int pillarMeta;
	protected Block smoothBlock;
	protected int smoothMeta;
	protected Block smoothSlabBlock;
	protected int smoothSlabMeta;
	protected Block tileBlock;
	protected int tileMeta;
	protected Block tileSlabBlock;
	protected int tileSlabMeta;
	protected Block tileStairBlock;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block trapdoorBlock;
	protected Block beamBlock;
	protected int beamMeta;
	protected Block bedBlock;
	protected Block gateIronBlock;
	protected Block gateOrcBlock;
	protected Block barsBlock;
	protected Block chandelierBlock;
	protected int chandelierMeta;

	public LOTRWorldGenMordorStructure(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		brickBlock = LOTRMod.brick;
		brickMeta = 0;
		brickSlabBlock = LOTRMod.slabSingle;
		brickSlabMeta = 1;
		brickStairBlock = LOTRMod.stairsMordorBrick;
		brickWallBlock = LOTRMod.wall;
		brickWallMeta = 1;
		brickCarvedBlock = LOTRMod.brick2;
		brickCarvedMeta = 10;
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 7;
		smoothBlock = LOTRMod.smoothStone;
		smoothMeta = 0;
		smoothSlabBlock = LOTRMod.slabSingle;
		smoothSlabMeta = 0;
		tileBlock = LOTRMod.clayTileDyed;
		tileMeta = 15;
		tileSlabBlock = LOTRMod.slabClayTileDyedSingle2;
		tileSlabMeta = 7;
		tileStairBlock = LOTRMod.stairsClayTileDyedBlack;
		plankBlock = LOTRMod.planks;
		plankMeta = 3;
		plankSlabBlock = LOTRMod.woodSlabSingle;
		plankSlabMeta = 3;
		plankStairBlock = LOTRMod.stairsCharred;
		fenceBlock = LOTRMod.fence;
		fenceMeta = 3;
		trapdoorBlock = LOTRMod.trapdoorCharred;
		beamBlock = LOTRMod.woodBeam1;
		beamMeta = 3;
		bedBlock = LOTRMod.orcBed;
		gateIronBlock = LOTRMod.gateIronBars;
		gateOrcBlock = LOTRMod.gateOrc;
		barsBlock = LOTRMod.orcSteelBars;
		chandelierBlock = LOTRMod.chandelier;
		chandelierMeta = 7;
	}

	@Override
	protected void placeOrcTorch(World world, int i, int j, int k) {
		setBlockAndMetadata(world, i, j, k, LOTRMod.orcTorch, 0);
		setBlockAndMetadata(world, i, j + 1, k, LOTRMod.orcTorch, 1);
	}
}
