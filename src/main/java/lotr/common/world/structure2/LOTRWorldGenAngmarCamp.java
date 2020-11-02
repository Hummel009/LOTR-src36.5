package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.npc.*;
import net.minecraft.world.World;

public class LOTRWorldGenAngmarCamp extends LOTRWorldGenCampBase {
	public LOTRWorldGenAngmarCamp(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tableBlock = LOTRMod.commandTable;
		brickBlock = LOTRMod.brick2;
		brickMeta = 0;
		brickSlabBlock = LOTRMod.slabSingle3;
		brickSlabMeta = 3;
		fenceBlock = LOTRMod.fence;
		fenceMeta = 3;
		fenceGateBlock = LOTRMod.fenceGateCharred;
		hasOrcTorches = true;
		hasSkulls = true;
	}

	@Override
	protected LOTRWorldGenStructureBase2 createTent(boolean flag, Random random) {
		if (random.nextInt(6) == 0) {
			return new LOTRWorldGenAngmarForgeTent(false);
		}
		return new LOTRWorldGenAngmarTent(false);
	}

	@Override
	protected LOTREntityNPC getCampCaptain(World world, Random random) {
		if (random.nextBoolean()) {
			return new LOTREntityAngmarOrcTrader(world);
		}
		return null;
	}

	@Override
	protected void placeNPCRespawner(World world, Random random, int i, int j, int k) {
		LOTREntityNPCRespawner respawner = new LOTREntityNPCRespawner(world);
		respawner.setSpawnClasses(LOTREntityAngmarOrc.class, LOTREntityAngmarOrcArcher.class);
		respawner.setCheckRanges(24, -12, 12, 12);
		respawner.setSpawnRanges(8, -4, 4, 16);
		this.placeNPCRespawner(respawner, world, i, j, k);
	}
}
