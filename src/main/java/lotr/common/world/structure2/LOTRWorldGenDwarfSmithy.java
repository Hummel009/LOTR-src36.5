package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import lotr.common.tileentity.LOTRTileEntityDwarvenForge;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenDwarfSmithy extends LOTRWorldGenStructureBase2 {
	protected Block baseBrickBlock = Blocks.stonebrick;
	protected int baseBrickMeta = 0;
	protected Block brickBlock = LOTRMod.brick;
	protected int brickMeta = 6;
	protected Block brickSlabBlock = LOTRMod.slabSingle;
	protected int brickSlabMeta = 7;
	protected Block brickStairBlock = LOTRMod.stairsDwarvenBrick;
	protected Block carvedBrickBlock = LOTRMod.brick2;
	protected int carvedBrickMeta = 12;
	protected Block pillarBlock = LOTRMod.pillar;
	protected int pillarMeta = 0;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block gateBlock;
	protected Block tableBlock = LOTRMod.dwarvenTable;
	protected Block barsBlock = LOTRMod.dwarfBars;

	public LOTRWorldGenDwarfSmithy(boolean flag) {
		super(flag);
	}

	protected LOTREntityDwarf createSmith(World world) {
		return new LOTREntityDwarfSmith(world);
	}

	protected LOTRChestContents getChestContents() {
		return LOTRChestContents.DWARF_SMITHY;
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		int randomWood = random.nextInt(4);
		if (randomWood == 0) {
			plankBlock = Blocks.planks;
			plankMeta = 1;
			gateBlock = Blocks.fence_gate;
		} else if (randomWood == 1) {
			plankBlock = LOTRMod.planks;
			plankMeta = 13;
			gateBlock = LOTRMod.fenceGateLarch;
		} else if (randomWood == 2) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 4;
			gateBlock = LOTRMod.fenceGatePine;
		} else if (randomWood == 3) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 3;
			gateBlock = LOTRMod.fenceGateFir;
		}
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int k2;
		int k1;
		int i2;
		int i1;
		int j1;
		int k12;
		this.setOriginAndRotation(world, i, j, k, rotation, 5);
		setupRandomBlocks(random);
		if (restrictions) {
			int minHeight = 0;
			int maxHeight = 0;
			for (int i12 = -4; i12 <= 4; ++i12) {
				for (int k13 = -4; k13 <= 4; ++k13) {
					j1 = getTopBlock(world, i12, k13);
					Block block = getBlock(world, i12, j1 - 1, k13);
					if (block != Blocks.grass) {
						return false;
					}
					if (j1 < minHeight) {
						minHeight = j1;
					}
					if (j1 > maxHeight) {
						maxHeight = j1;
					}
					if (maxHeight - minHeight <= 5) {
						continue;
					}
					return false;
				}
			}
		}
		for (i1 = -4; i1 <= 4; ++i1) {
			for (k12 = -4; k12 <= 4; ++k12) {
				i2 = Math.abs(i1);
				if (i2 + (k2 = Math.abs(k12)) > 6) {
					continue;
				}
				layFoundation(world, i1, k12);
				for (j1 = 1; j1 <= 5; ++j1) {
					setAir(world, i1, j1, k12);
				}
				if (i2 == 4 || k2 == 4) {
					setBlockAndMetadata(world, i1, 1, k12, baseBrickBlock, baseBrickMeta);
					setBlockAndMetadata(world, i1, 2, k12, plankBlock, plankMeta);
					setBlockAndMetadata(world, i1, 3, k12, brickBlock, brickMeta);
				}
				if (i2 != 3 || k2 != 3) {
					continue;
				}
				for (j1 = 1; j1 <= 3; ++j1) {
					setBlockAndMetadata(world, i1, j1, k12, pillarBlock, pillarMeta);
				}
			}
		}
		for (i1 = -2; i1 <= 2; ++i1) {
			setBlockAndMetadata(world, i1, 3, -3, brickStairBlock, 7);
			setBlockAndMetadata(world, i1, 3, 3, brickStairBlock, 6);
		}
		for (k1 = -2; k1 <= 2; ++k1) {
			setBlockAndMetadata(world, -3, 3, k1, brickStairBlock, 4);
			setBlockAndMetadata(world, 3, 3, k1, brickStairBlock, 5);
		}
		for (i1 = -3; i1 <= 3; ++i1) {
			for (k12 = -3; k12 <= 3; ++k12) {
				setBlockAndMetadata(world, i1, 4, k12, brickBlock, brickMeta);
			}
		}
		for (i1 = -2; i1 <= 2; ++i1) {
			setBlockAndMetadata(world, i1, 4, -4, brickStairBlock, 2);
			setBlockAndMetadata(world, i1, 4, 4, brickStairBlock, 3);
		}
		for (k1 = -2; k1 <= 2; ++k1) {
			setBlockAndMetadata(world, -4, 4, k1, brickStairBlock, 1);
			setBlockAndMetadata(world, 4, 4, k1, brickStairBlock, 0);
		}
		setBlockAndMetadata(world, -4, 4, 2, brickStairBlock, 3);
		setBlockAndMetadata(world, -3, 4, 2, brickStairBlock, 1);
		setBlockAndMetadata(world, -3, 4, 3, brickStairBlock, 3);
		setBlockAndMetadata(world, -2, 4, 3, brickStairBlock, 1);
		setBlockAndMetadata(world, 4, 4, 2, brickStairBlock, 3);
		setBlockAndMetadata(world, 3, 4, 2, brickStairBlock, 0);
		setBlockAndMetadata(world, 3, 4, 3, brickStairBlock, 3);
		setBlockAndMetadata(world, 2, 4, 3, brickStairBlock, 0);
		setBlockAndMetadata(world, -4, 4, -2, brickStairBlock, 2);
		setBlockAndMetadata(world, -3, 4, -2, brickStairBlock, 1);
		setBlockAndMetadata(world, -3, 4, -3, brickStairBlock, 2);
		setBlockAndMetadata(world, -2, 4, -3, brickStairBlock, 1);
		setBlockAndMetadata(world, 4, 4, -2, brickStairBlock, 2);
		setBlockAndMetadata(world, 3, 4, -2, brickStairBlock, 0);
		setBlockAndMetadata(world, 3, 4, -3, brickStairBlock, 2);
		setBlockAndMetadata(world, 2, 4, -3, brickStairBlock, 0);
		for (i1 = -1; i1 <= 1; ++i1) {
			for (k12 = 2; k12 <= 4; ++k12) {
				i2 = Math.abs(i1 - 0);
				k2 = Math.abs(k12 - 3);
				if (i2 == 1 && k2 == 1) {
					setBlockAndMetadata(world, i1, 5, k12, brickSlabBlock, brickSlabMeta);
					continue;
				}
				if (i2 == 1 || k2 == 1) {
					setBlockAndMetadata(world, i1, 5, k12, brickBlock, brickMeta);
					continue;
				}
				if (i2 != 0 || k2 != 0) {
					continue;
				}
				setAir(world, i1, 3, k12);
				setAir(world, i1, 4, k12);
			}
			setBlockAndMetadata(world, i1, 4, 4, brickBlock, brickMeta);
			for (int j12 = 1; j12 <= 2; ++j12) {
				setBlockAndMetadata(world, i1, j12, 4, brickBlock, brickMeta);
			}
		}
		setBlockAndMetadata(world, 0, 6, 2, brickStairBlock, 2);
		setBlockAndMetadata(world, -1, 6, 3, brickStairBlock, 1);
		setBlockAndMetadata(world, 1, 6, 3, brickStairBlock, 0);
		setBlockAndMetadata(world, 0, 6, 4, brickStairBlock, 3);
		setBlockAndMetadata(world, 0, 1, -4, gateBlock, 0);
		setAir(world, 0, 2, -4);
		setBlockAndMetadata(world, -2, 2, -3, Blocks.torch, 2);
		setBlockAndMetadata(world, 2, 2, -3, Blocks.torch, 1);
		setBlockAndMetadata(world, 0, 1, -1, Blocks.anvil, 1);
		for (int i13 : new int[] { -3, 3 }) {
			setBlockAndMetadata(world, i13, 1, -1, Blocks.anvil, 0);
			setBlockAndMetadata(world, i13, 1, 0, tableBlock, 0);
			setBlockAndMetadata(world, i13, 1, 2, Blocks.crafting_table, 0);
		}
		setBlockAndMetadata(world, -3, 1, -2, LOTRMod.unsmeltery, 4);
		setBlockAndMetadata(world, 3, 1, -2, LOTRMod.unsmeltery, 5);
		this.placeChest(world, random, -3, 1, 1, 4, getChestContents());
		this.placeChest(world, random, 3, 1, 1, 5, getChestContents());
		placeDwarfForge(world, random, 0, 1, 2, 2);
		placeDwarfForge(world, random, -1, 1, 3, 5);
		placeDwarfForge(world, random, 1, 1, 3, 4);
		for (int i13 : new int[] { -1, 1 }) {
			setBlockAndMetadata(world, i13, 1, 2, brickBlock, brickMeta);
			setBlockAndMetadata(world, i13, 2, 2, carvedBrickBlock, carvedBrickMeta);
			setBlockAndMetadata(world, i13, 3, 2, brickStairBlock, 2);
			setBlockAndMetadata(world, i13, 2, 3, barsBlock, 0);
			setBlockAndMetadata(world, i13, 3, 3, brickBlock, brickMeta);
		}
		setBlockAndMetadata(world, 0, 2, 2, barsBlock, 0);
		setBlockAndMetadata(world, 0, 3, 2, brickStairBlock, 2);
		setBlockAndMetadata(world, 0, 1, 3, Blocks.lava, 0);
		LOTREntityDwarf smith = createSmith(world);
		spawnNPCAndSetHome(smith, world, 0, 1, 0, 8);
		return true;
	}

	protected void layFoundation(World world, int i, int k) {
		for (int j = 0; (j == 0 || !isOpaque(world, i, j, k)) && getY(j) >= 0; --j) {
			setBlockAndMetadata(world, i, j, k, baseBrickBlock, baseBrickMeta);
			setGrassToDirt(world, i, j - 1, k);
		}
	}

	protected void placeDwarfForge(World world, Random random, int i, int j, int k, int meta) {
		setBlockAndMetadata(world, i, j, k, LOTRMod.dwarvenForge, meta);
		TileEntity tileentity = getTileEntity(world, i, j, k);
		if (tileentity instanceof LOTRTileEntityDwarvenForge) {
			LOTRTileEntityDwarvenForge forge = (LOTRTileEntityDwarvenForge) tileentity;
			int fuelAmount = MathHelper.getRandomIntegerInRange(random, 0, 4);
			if (fuelAmount > 0) {
				ItemStack fuel = new ItemStack(Items.coal, fuelAmount, 0);
				forge.setInventorySlotContents(forge.fuelSlot, fuel);
			}
		}
	}
}
