package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class LOTRWorldGenDunlandStructure extends LOTRWorldGenStructureBase2 {
	protected Block floorBlock;
	protected int floorMeta;
	protected Block woodBlock;
	protected int woodMeta;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block fenceGateBlock;
	protected Block doorBlock;
	protected Block roofBlock;
	protected int roofMeta;
	protected Block roofSlabBlock;
	protected int roofSlabMeta;
	protected Block roofStairBlock;
	protected Block barsBlock;
	protected int barsMeta;
	protected Block bedBlock;

	public LOTRWorldGenDunlandStructure(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		int randomFloor = random.nextInt(5);
		if (randomFloor == 0) {
			floorBlock = Blocks.cobblestone;
			floorMeta = 0;
		} else if (randomFloor == 1) {
			floorBlock = Blocks.hardened_clay;
			floorMeta = 0;
		} else if (randomFloor == 2) {
			floorBlock = Blocks.stained_hardened_clay;
			floorMeta = 7;
		} else if (randomFloor == 3) {
			floorBlock = Blocks.stained_hardened_clay;
			floorMeta = 12;
		} else if (randomFloor == 4) {
			floorBlock = Blocks.stained_hardened_clay;
			floorMeta = 15;
		}
		if (random.nextBoolean()) {
			woodBlock = Blocks.log;
			woodMeta = 1;
			plankBlock = Blocks.planks;
			plankMeta = 1;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 1;
			plankStairBlock = Blocks.spruce_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 1;
			fenceGateBlock = LOTRMod.fenceGateSpruce;
			doorBlock = LOTRMod.doorSpruce;
		} else {
			int randomWood = random.nextInt(2);
			if (randomWood == 0) {
				woodBlock = Blocks.log;
				woodMeta = 0;
				plankBlock = Blocks.planks;
				plankMeta = 0;
				plankSlabBlock = Blocks.wooden_slab;
				plankSlabMeta = 0;
				plankStairBlock = Blocks.oak_stairs;
				fenceBlock = Blocks.fence;
				fenceMeta = 0;
				fenceGateBlock = Blocks.fence_gate;
				doorBlock = Blocks.wooden_door;
			} else if (randomWood == 1) {
				woodBlock = LOTRMod.wood5;
				woodMeta = 0;
				plankBlock = LOTRMod.planks2;
				plankMeta = 4;
				plankSlabBlock = LOTRMod.woodSlabSingle3;
				plankSlabMeta = 4;
				plankStairBlock = LOTRMod.stairsPine;
				fenceBlock = LOTRMod.fence2;
				fenceMeta = 4;
				fenceGateBlock = LOTRMod.fenceGatePine;
				doorBlock = LOTRMod.doorPine;
			}
		}
		roofBlock = LOTRMod.thatch;
		roofMeta = 0;
		roofSlabBlock = LOTRMod.slabSingleThatch;
		roofSlabMeta = 0;
		roofStairBlock = LOTRMod.stairsThatch;
		if (random.nextBoolean()) {
			barsBlock = Blocks.iron_bars;
			barsMeta = 0;
		} else {
			barsBlock = LOTRMod.bronzeBars;
			barsMeta = 0;
		}
		bedBlock = random.nextBoolean() ? LOTRMod.furBed : LOTRMod.strawBed;
	}

	protected ItemStack getRandomDunlandWeapon(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(Items.iron_sword), new ItemStack(LOTRMod.spearIron), new ItemStack(LOTRMod.daggerIron), new ItemStack(Items.stone_sword), new ItemStack(LOTRMod.spearStone), new ItemStack(LOTRMod.dunlendingClub), new ItemStack(LOTRMod.dunlendingTrident) };
		return items[random.nextInt(items.length)].copy();
	}

	protected void placeDunlandItemFrame(World world, Random random, int i, int j, int k, int direction) {
		ItemStack[] items = new ItemStack[] { new ItemStack(Items.bone), new ItemStack(LOTRMod.fur), new ItemStack(Items.flint), new ItemStack(Items.iron_sword), new ItemStack(Items.stone_sword), new ItemStack(LOTRMod.spearIron), new ItemStack(LOTRMod.spearStone), new ItemStack(Items.bow), new ItemStack(Items.arrow), new ItemStack(LOTRMod.mug), new ItemStack(LOTRMod.skullCup) };
		ItemStack item = items[random.nextInt(items.length)].copy();
		spawnItemFrame(world, i, j, k, direction, item);
	}
}
