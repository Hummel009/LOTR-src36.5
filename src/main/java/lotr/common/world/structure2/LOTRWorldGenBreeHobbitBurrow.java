package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.entity.npc.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenBreeHobbitBurrow extends LOTRWorldGenHobbitBurrow {
	public LOTRWorldGenBreeHobbitBurrow(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		LOTRWorldGenBreeHouse breeBlockProxy = new LOTRWorldGenBreeHouse(false);
		breeBlockProxy.setupRandomBlocks(random);
		brickBlock = breeBlockProxy.brickBlock;
		brickMeta = breeBlockProxy.brickMeta;
		floorBlock = Blocks.cobblestone;
		floorMeta = 0;
		plankBlock = breeBlockProxy.plankBlock;
		plankMeta = breeBlockProxy.plankMeta;
		plankSlabBlock = breeBlockProxy.plankSlabBlock;
		plankSlabMeta = breeBlockProxy.plankSlabMeta;
		plankStairBlock = breeBlockProxy.plankStairBlock;
		fenceBlock = breeBlockProxy.fenceBlock;
		fenceMeta = breeBlockProxy.fenceMeta;
		fenceGateBlock = breeBlockProxy.fenceGateBlock;
		doorBlock = breeBlockProxy.doorBlock;
		beamBlock = breeBlockProxy.beamBlock;
		beamMeta = breeBlockProxy.beamMeta;
		tableBlock = breeBlockProxy.tableBlock;
		burrowLoot = LOTRChestContents.BREE_HOUSE;
		foodPool = LOTRFoods.BREE;
	}

	@Override
	protected LOTREntityHobbit createHobbit(World world) {
		return new LOTREntityBreeHobbit(world);
	}

	@Override
	protected String[] getHobbitCoupleAndHomeNames(Random random) {
		return LOTRNames.getBreeHobbitCoupleAndHomeNames(random);
	}
}
