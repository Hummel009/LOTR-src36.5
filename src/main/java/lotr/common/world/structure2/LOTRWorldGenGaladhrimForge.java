package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class LOTRWorldGenGaladhrimForge extends LOTRWorldGenElvenForge {
	public LOTRWorldGenGaladhrimForge(boolean flag) {
		super(flag);
		brickBlock = LOTRMod.brick;
		brickMeta = 11;
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 1;
		slabBlock = LOTRMod.slabSingle2;
		slabMeta = 3;
		carvedBrickBlock = LOTRMod.brick2;
		carvedBrickMeta = 15;
		wallBlock = LOTRMod.wall;
		wallMeta = 10;
		stairBlock = LOTRMod.stairsElvenBrick;
		torchBlock = LOTRMod.mallornTorchSilver;
		tableBlock = LOTRMod.elvenTable;
		barsBlock = LOTRMod.galadhrimBars;
		woodBarsBlock = LOTRMod.galadhrimWoodBars;
		roofBlock = LOTRMod.clayTileDyed;
		roofMeta = 4;
		roofStairBlock = LOTRMod.stairsClayTileDyedYellow;
		chestBlock = LOTRMod.chestMallorn;
	}

	@Override
	protected LOTREntityElf getElf(World world) {
		return new LOTREntityGaladhrimSmith(world);
	}

	@Override
	protected Block getTorchBlock(Random random) {
		return LOTRWorldGenElfHouse.getRandomTorch(random);
	}
}
