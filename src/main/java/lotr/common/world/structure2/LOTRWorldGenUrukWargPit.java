package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.npc.*;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenUrukWargPit extends LOTRWorldGenWargPitBase {
	public LOTRWorldGenUrukWargPit(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		brickBlock = LOTRMod.brick2;
		brickMeta = 7;
		brickSlabBlock = LOTRMod.slabSingle4;
		brickSlabMeta = 4;
		brickStairBlock = LOTRMod.stairsUrukBrick;
		brickWallBlock = LOTRMod.wall2;
		brickWallMeta = 7;
		pillarBlock = beamBlock;
		pillarMeta = beamMeta;
		woolBlock = Blocks.wool;
		woolMeta = 12;
		carpetBlock = Blocks.carpet;
		carpetMeta = 12;
		barsBlock = LOTRMod.urukBars;
		gateOrcBlock = LOTRMod.gateUruk;
		tableBlock = LOTRMod.urukTable;
		banner = LOTRItemBanner.BannerType.ISENGARD;
		chestContents = LOTRChestContents.URUK_TENT;
	}

	@Override
	protected LOTREntityNPC getOrc(World world) {
		return new LOTREntityIsengardSnaga(world);
	}

	@Override
	protected LOTREntityNPC getWarg(World world) {
		return new LOTREntityUrukWarg(world);
	}

	@Override
	protected void setOrcSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityIsengardSnaga.class);
	}

	@Override
	protected void setWargSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityUrukWarg.class);
	}
}
