package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.npc.*;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenAngmarWargPit extends LOTRWorldGenWargPitBase {
	public LOTRWorldGenAngmarWargPit(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		brickBlock = LOTRMod.brick2;
		brickMeta = 0;
		brickSlabBlock = LOTRMod.slabSingle3;
		brickSlabMeta = 3;
		brickStairBlock = LOTRMod.stairsAngmarBrick;
		brickWallBlock = LOTRMod.wall2;
		brickWallMeta = 0;
		pillarBlock = LOTRMod.pillar2;
		pillarMeta = 4;
		woolBlock = Blocks.wool;
		woolMeta = 15;
		carpetBlock = Blocks.carpet;
		carpetMeta = 15;
		tableBlock = LOTRMod.angmarTable;
		banner = LOTRItemBanner.BannerType.ANGMAR;
		chestContents = LOTRChestContents.ANGMAR_TENT;
	}

	@Override
	protected LOTREntityNPC getOrc(World world) {
		return new LOTREntityAngmarOrc(world);
	}

	@Override
	protected LOTREntityNPC getWarg(World world) {
		return new LOTREntityAngmarWarg(world);
	}

	@Override
	protected void setOrcSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityAngmarOrc.class);
	}

	@Override
	protected void setWargSpawner(LOTREntityNPCRespawner spawner) {
		spawner.setSpawnClass(LOTREntityAngmarWarg.class);
	}

	@Override
	protected void associateGroundBlocks() {
		super.associateGroundBlocks();
		clearScanAlias("GROUND_COVER");
		addBlockMetaAliasOption("GROUND_COVER", 1, Blocks.snow_layer, 0);
		setBlockAliasChance("GROUND_COVER", 0.25f);
	}
}
