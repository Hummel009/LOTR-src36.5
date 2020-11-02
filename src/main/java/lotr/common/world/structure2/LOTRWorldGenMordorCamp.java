package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.npc.*;
import net.minecraft.world.World;

public class LOTRWorldGenMordorCamp extends LOTRWorldGenCampBase {
	public LOTRWorldGenMordorCamp(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tableBlock = LOTRMod.commandTable;
		brickBlock = LOTRMod.brick;
		brickMeta = 0;
		brickSlabBlock = LOTRMod.slabSingle;
		brickSlabMeta = 1;
		fenceBlock = LOTRMod.fence;
		fenceMeta = 3;
		fenceGateBlock = LOTRMod.fenceGateCharred;
		farmBaseBlock = LOTRMod.rock;
		farmBaseMeta = 0;
		farmCropBlock = LOTRMod.morgulShroom;
		farmCropMeta = 0;
		hasOrcTorches = true;
		hasSkulls = true;
	}

	@Override
	protected LOTRWorldGenStructureBase2 createTent(boolean flag, Random random) {
		if (random.nextInt(6) == 0) {
			return new LOTRWorldGenMordorForgeTent(false);
		}
		return new LOTRWorldGenMordorTent(false);
	}

	@Override
	protected LOTREntityNPC getCampCaptain(World world, Random random) {
		if (random.nextBoolean()) {
			return new LOTREntityMordorOrcTrader(world);
		}
		return null;
	}

	@Override
	protected void placeNPCRespawner(World world, Random random, int i, int j, int k) {
		LOTREntityNPCRespawner respawner = new LOTREntityNPCRespawner(world);
		respawner.setSpawnClasses(LOTREntityMordorOrc.class, LOTREntityMordorOrcArcher.class);
		respawner.setCheckRanges(24, -12, 12, 12);
		respawner.setSpawnRanges(8, -4, 4, 16);
		this.placeNPCRespawner(respawner, world, i, j, k);
	}
}
