package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public abstract class LOTRWorldGenCorsairStructure extends LOTRWorldGenStructureBase2 {
	protected Block brickBlock;
	protected int brickMeta;
	protected Block brickSlabBlock;
	protected int brickSlabMeta;
	protected Block brickStairBlock;
	protected Block brickWallBlock;
	protected int brickWallMeta;
	protected Block pillarBlock;
	protected int pillarMeta;
	protected Block pillarSlabBlock;
	protected int pillarSlabMeta;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block fenceGateBlock;

	public LOTRWorldGenCorsairStructure(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		brickBlock = LOTRMod.brick6;
		brickMeta = 6;
		brickSlabBlock = LOTRMod.slabSingle13;
		brickSlabMeta = 2;
		brickStairBlock = LOTRMod.stairsUmbarBrick;
		brickWallBlock = LOTRMod.wall5;
		brickWallMeta = 0;
		pillarBlock = LOTRMod.pillar2;
		pillarMeta = 10;
		pillarSlabBlock = LOTRMod.slabSingle13;
		pillarSlabMeta = 4;
		int randomWood = random.nextInt(2);
		if (randomWood == 0) {
			plankBlock = LOTRMod.planks3;
			plankMeta = 3;
			plankSlabBlock = LOTRMod.woodSlabSingle5;
			plankSlabMeta = 3;
			plankStairBlock = LOTRMod.stairsPalm;
			fenceBlock = LOTRMod.fence3;
			fenceMeta = 3;
			fenceGateBlock = LOTRMod.fenceGatePalm;
		} else {
			plankBlock = LOTRMod.planks2;
			plankMeta = 2;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 2;
			plankStairBlock = LOTRMod.stairsCedar;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 2;
			fenceGateBlock = LOTRMod.fenceGateCedar;
		}
	}

	protected ItemStack getRandomCorsairWeapon(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.swordCorsair), new ItemStack(LOTRMod.daggerCorsair), new ItemStack(LOTRMod.spearCorsair), new ItemStack(LOTRMod.battleaxeCorsair), new ItemStack(LOTRMod.nearHaradBow) };
		return items[random.nextInt(items.length)].copy();
	}
}
