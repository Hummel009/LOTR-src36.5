package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class LOTRWorldGenNomadStructure extends LOTRWorldGenStructureBase2 {
	protected Block tentBlock;
	protected int tentMeta;
	protected Block tent2Block;
	protected int tent2Meta;
	protected Block carpetBlock;
	protected int carpetMeta;
	protected Block carpet2Block;
	protected int carpet2Meta;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block fenceBlock;
	protected int fenceMeta;
	protected Block fenceGateBlock;
	protected Block trapdoorBlock;
	protected Block beamBlock;
	protected int beamMeta;
	protected Block bedBlock;

	public LOTRWorldGenNomadStructure(boolean flag) {
		super(flag);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		super.setupRandomBlocks(random);
		tentBlock = Blocks.wool;
		tentMeta = 0;
		tent2Block = Blocks.wool;
		tent2Meta = 12;
		carpetBlock = Blocks.carpet;
		carpetMeta = 0;
		carpet2Block = Blocks.carpet;
		carpet2Meta = 12;
		int randomWood = random.nextInt(3);
		if (randomWood == 0) {
			plankBlock = Blocks.planks;
			plankMeta = 0;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 0;
			plankStairBlock = Blocks.oak_stairs;
			fenceBlock = Blocks.fence;
			fenceMeta = 0;
			fenceGateBlock = Blocks.fence_gate;
			trapdoorBlock = Blocks.trapdoor;
			beamBlock = LOTRMod.woodBeamV1;
			beamMeta = 0;
		} else if (randomWood == 1) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 2;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 2;
			plankStairBlock = LOTRMod.stairsCedar;
			fenceBlock = LOTRMod.fence2;
			fenceMeta = 2;
			fenceGateBlock = LOTRMod.fenceGateCedar;
			trapdoorBlock = LOTRMod.trapdoorCedar;
			beamBlock = LOTRMod.woodBeam4;
			beamMeta = 2;
		} else if (randomWood == 2) {
			plankBlock = LOTRMod.planks;
			plankMeta = 14;
			plankSlabBlock = LOTRMod.woodSlabSingle2;
			plankSlabMeta = 6;
			plankStairBlock = LOTRMod.stairsDatePalm;
			fenceBlock = LOTRMod.fence;
			fenceMeta = 14;
			fenceGateBlock = LOTRMod.fenceGateDatePalm;
			trapdoorBlock = LOTRMod.trapdoorDatePalm;
			beamBlock = LOTRMod.woodBeam3;
			beamMeta = 2;
		}
		bedBlock = LOTRMod.strawBed;
	}

	protected void laySandBase(World world, int i, int j, int k) {
		setBlockAndMetadata(world, i, j, k, Blocks.sand, 0);
		int j1 = j - 1;
		while (getY(j1) >= 0 && !isOpaque(world, i, j1, k)) {
			if (isOpaque(world, i, j1 - 1, k)) {
				setBlockAndMetadata(world, i, j1, k, Blocks.sandstone, 0);
			} else {
				setBlockAndMetadata(world, i, j1, k, Blocks.sand, 0);
			}
			setGrassToDirt(world, i, j1 - 1, k);
			--j1;
		}
	}

	protected ItemStack getRandomNomadWeapon(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.swordHarad), new ItemStack(LOTRMod.daggerHarad), new ItemStack(LOTRMod.spearHarad), new ItemStack(LOTRMod.pikeHarad) };
		return items[random.nextInt(items.length)].copy();
	}

	protected ItemStack getRandomUmbarWeapon(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.scimitarNearHarad), new ItemStack(LOTRMod.spearNearHarad), new ItemStack(LOTRMod.pikeNearHarad), new ItemStack(LOTRMod.poleaxeNearHarad), new ItemStack(LOTRMod.maceNearHarad) };
		return items[random.nextInt(items.length)].copy();
	}
}
