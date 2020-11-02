package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.npc.*;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenMordorWargPit extends LOTRWorldGenWargPitBase {
	public LOTRWorldGenMordorWargPit(boolean flag) {
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
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 7;
		woolBlock = Blocks.wool;
		woolMeta = 12;
		carpetBlock = Blocks.carpet;
		carpetMeta = 12;
		gateMetalBlock = LOTRMod.gateIronBars;
		tableBlock = LOTRMod.morgulTable;
		banner = LOTRItemBanner.BannerType.MORDOR;
		chestContents = LOTRChestContents.ORC_TENT;
	}

	@Override
	protected LOTREntityNPC getOrc(World world) {
		return new LOTREntityMordorOrc(world);
	}

	@Override
	protected LOTREntityNPC getWarg(World world) {
		return new LOTREntityMordorWarg(world);
	}

	@Override
	protected void setOrcSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityMordorOrc.class);
	}

	@Override
	protected void setWargSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityMordorWarg.class);
	}

	@Override
	protected void associateGroundBlocks() {
		addBlockMetaAliasOption("GROUND", 4, LOTRMod.rock, 0);
		addBlockMetaAliasOption("GROUND", 4, LOTRMod.mordorDirt, 0);
		addBlockMetaAliasOption("GROUND", 4, LOTRMod.mordorGravel, 0);
		addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRMod.slabSingle10, 7);
		addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRMod.slabSingleDirt, 3);
		addBlockMetaAliasOption("GROUND_SLAB", 4, LOTRMod.slabSingleGravel, 1);
		addBlockMetaAliasOption("GROUND_COVER", 1, LOTRMod.mordorMoss, 0);
		setBlockAliasChance("GROUND_COVER", 0.25f);
	}
}
