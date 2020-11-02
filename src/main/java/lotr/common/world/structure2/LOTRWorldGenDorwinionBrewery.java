package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.*;
import lotr.common.entity.npc.LOTREntityDorwinionElfVintner;
import lotr.common.item.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.*;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenDorwinionBrewery extends LOTRWorldGenDorwinionHouse {
	public LOTRWorldGenDorwinionBrewery(boolean flag) {
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int k1;
		int k12;
		int i1;
		int j1;
		int i12;
		int i13;
		int k13;
		this.setOriginAndRotation(world, i, j, k, rotation, 1);
		setupRandomBlocks(random);
		if (restrictions) {
			for (i13 = -6; i13 <= 6; ++i13) {
				for (k13 = 0; k13 <= 19; ++k13) {
					j1 = getTopBlock(world, i13, k13) - 1;
					Block block = getBlock(world, i13, j1, k13);
					if (block == Blocks.grass) {
						continue;
					}
					return false;
				}
			}
		}
		for (i13 = -6; i13 <= 6; ++i13) {
			for (k13 = 0; k13 <= 19; ++k13) {
				setBlockAndMetadata(world, i13, 0, k13, Blocks.grass, 0);
				j1 = -1;
				while (!isOpaque(world, i13, j1, k13) && getY(j1) >= 0) {
					setBlockAndMetadata(world, i13, j1, k13, Blocks.dirt, 0);
					setGrassToDirt(world, i13, j1 - 1, k13);
					--j1;
				}
				for (j1 = 1; j1 <= 10; ++j1) {
					setAir(world, i13, j1, k13);
				}
				if (i13 < -5 || i13 > 5 || k13 < 1 || k13 > 18) {
					continue;
				}
				if ((i13 == -5 || i13 == 5) && (k13 == 1 || k13 == 18)) {
					for (j1 = 0; j1 <= 5; ++j1) {
						setBlockAndMetadata(world, i13, j1, k13, woodBeamBlock, woodBeamMeta);
					}
					continue;
				}
				if (i13 == -5 || i13 == 5 || k13 == 1 || k13 == 18) {
					for (j1 = 0; j1 <= 5; ++j1) {
						setBlockAndMetadata(world, i13, j1, k13, brickBlock, brickMeta);
					}
					continue;
				}
				if (i13 >= -2 && i13 <= 2) {
					setBlockAndMetadata(world, i13, 0, k13, floorBlock, floorMeta);
					continue;
				}
				setBlockAndMetadata(world, i13, 0, k13, plankBlock, plankMeta);
			}
		}
		for (i13 = -1; i13 <= 1; ++i13) {
			setBlockAndMetadata(world, i13, 0, 1, floorBlock, floorMeta);
			for (int j12 = 1; j12 <= 3; ++j12) {
				setBlockAndMetadata(world, i13, j12, 1, LOTRMod.gateWooden, 2);
			}
		}
		for (k1 = 2; k1 <= 17; ++k1) {
			if (k1 % 3 == 2) {
				setBlockAndMetadata(world, -6, 1, k1, brickStairBlock, 1);
				setGrassToDirt(world, -6, 0, k1);
				setBlockAndMetadata(world, 6, 1, k1, brickStairBlock, 0);
				setGrassToDirt(world, 6, 0, k1);
				continue;
			}
			setBlockAndMetadata(world, -6, 1, k1, leafBlock, leafMeta);
			setBlockAndMetadata(world, 6, 1, k1, leafBlock, leafMeta);
		}
		for (i13 = -4; i13 <= 4; ++i13) {
			if (Math.abs(i13) == 4) {
				setBlockAndMetadata(world, i13, 1, 19, brickStairBlock, 3);
				setGrassToDirt(world, i13, 0, 19);
				continue;
			}
			setBlockAndMetadata(world, i13, 1, 19, leafBlock, leafMeta);
		}
		for (i13 = -4; i13 <= 4; ++i13) {
			if (Math.abs(i13) == 4 || Math.abs(i13) == 2) {
				setBlockAndMetadata(world, i13, 1, 0, brickStairBlock, 2);
				setGrassToDirt(world, i13, 0, 0);
				continue;
			}
			if (Math.abs(i13) != 3) {
				continue;
			}
			setBlockAndMetadata(world, i13, 1, 0, leafBlock, leafMeta);
		}
		for (i13 = -5; i13 <= 5; ++i13) {
			setBlockAndMetadata(world, i13, 5, 0, brickStairBlock, 6);
			setBlockAndMetadata(world, i13, 5, 19, brickStairBlock, 7);
		}
		for (k1 = 0; k1 <= 19; ++k1) {
			if (k1 >= 3 && k1 <= 16) {
				if (k1 % 3 == 0) {
					setAir(world, -5, 3, k1);
					setBlockAndMetadata(world, -5, 4, k1, brickStairBlock, 7);
					setAir(world, 5, 3, k1);
					setBlockAndMetadata(world, 5, 4, k1, brickStairBlock, 7);
				} else if (k1 % 3 == 1) {
					setAir(world, -5, 3, k1);
					setBlockAndMetadata(world, -5, 4, k1, brickStairBlock, 6);
					setAir(world, 5, 3, k1);
					setBlockAndMetadata(world, 5, 4, k1, brickStairBlock, 6);
				}
			}
			setBlockAndMetadata(world, -6, 5, k1, brickStairBlock, 5);
			setBlockAndMetadata(world, 6, 5, k1, brickStairBlock, 4);
			if (k1 <= 7 && k1 % 2 == 0 || k1 >= 12 && k1 % 2 == 1) {
				setBlockAndMetadata(world, -6, 6, k1, brickSlabBlock, brickSlabMeta);
				setBlockAndMetadata(world, 6, 6, k1, brickSlabBlock, brickSlabMeta);
			}
			if (k1 == 8 || k1 == 11) {
				setBlockAndMetadata(world, -6, 4, k1, brickSlabBlock, brickSlabMeta | 8);
				setBlockAndMetadata(world, -6, 5, k1, brickBlock, brickMeta);
				setBlockAndMetadata(world, -6, 6, k1, brickSlabBlock, brickSlabMeta);
				setBlockAndMetadata(world, 6, 4, k1, brickSlabBlock, brickSlabMeta | 8);
				setBlockAndMetadata(world, 6, 5, k1, brickBlock, brickMeta);
				setBlockAndMetadata(world, 6, 6, k1, brickSlabBlock, brickSlabMeta);
				placeWallBanner(world, -5, 3, k1, LOTRItemBanner.BannerType.DORWINION, 3);
				placeWallBanner(world, 5, 3, k1, LOTRItemBanner.BannerType.DORWINION, 1);
			}
			if (k1 != 9 && k1 != 10) {
				continue;
			}
			setBlockAndMetadata(world, -6, 6, k1, brickBlock, brickMeta);
			setBlockAndMetadata(world, 6, 6, k1, brickBlock, brickMeta);
		}
		for (i13 = -3; i13 <= 3; ++i13) {
			if (Math.abs(i13) == 3) {
				setBlockAndMetadata(world, i13, 2, 1, brickSlabBlock, brickSlabMeta);
				setAir(world, i13, 3, 1);
			}
			if (Math.abs(i13) == 2) {
				placeWallBanner(world, i13, 4, 1, LOTRItemBanner.BannerType.DORWINION, 2);
			}
			if (IntMath.mod(i13, 2) != 1) {
				continue;
			}
			setBlockAndMetadata(world, i13, 2, 18, brickSlabBlock, brickSlabMeta);
			setAir(world, i13, 3, 18);
		}
		for (int k14 : new int[] { 1, 18 }) {
			int i14;
			setBlockAndMetadata(world, -4, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, -3, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, -2, 6, k14, brickSlabBlock, brickSlabMeta);
			setBlockAndMetadata(world, -1, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, 0, 6, k14, brickSlabBlock, brickSlabMeta);
			setBlockAndMetadata(world, 1, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, 2, 6, k14, brickSlabBlock, brickSlabMeta);
			setBlockAndMetadata(world, 3, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, 4, 6, k14, brickBlock, brickMeta);
			setBlockAndMetadata(world, -3, 7, k14, brickBlock, brickMeta);
			setAir(world, -2, 7, k14);
			setBlockAndMetadata(world, -1, 7, k14, brickBlock, brickMeta);
			setAir(world, 0, 7, k14);
			setBlockAndMetadata(world, 1, 7, k14, brickBlock, brickMeta);
			setAir(world, 2, 7, k14);
			setBlockAndMetadata(world, 3, 7, k14, brickBlock, brickMeta);
			for (i14 = -2; i14 <= 2; ++i14) {
				setBlockAndMetadata(world, i14, 8, k14, brickBlock, brickMeta);
			}
			for (i14 = -1; i14 <= 1; ++i14) {
				setBlockAndMetadata(world, i14, 9, k14, brickBlock, brickMeta);
			}
			setBlockAndMetadata(world, 0, 10, k14, brickBlock, brickMeta);
		}
		for (k12 = 2; k12 <= 17; ++k12) {
			setBlockAndMetadata(world, -4, 6, k12, plankStairBlock, 4);
			setBlockAndMetadata(world, -3, 7, k12, plankStairBlock, 4);
			setBlockAndMetadata(world, -2, 8, k12, plankStairBlock, 4);
			setBlockAndMetadata(world, -1, 9, k12, plankStairBlock, 4);
			setBlockAndMetadata(world, 0, 10, k12, plankBlock, plankMeta);
			setBlockAndMetadata(world, 1, 9, k12, plankStairBlock, 5);
			setBlockAndMetadata(world, 2, 8, k12, plankStairBlock, 5);
			setBlockAndMetadata(world, 3, 7, k12, plankStairBlock, 5);
			setBlockAndMetadata(world, 4, 6, k12, plankStairBlock, 5);
		}
		for (k12 = 0; k12 <= 19; ++k12) {
			setBlockAndMetadata(world, -5, 6, k12, clayStairBlock, 1);
			setBlockAndMetadata(world, -4, 7, k12, clayStairBlock, 1);
			setBlockAndMetadata(world, -3, 8, k12, clayStairBlock, 1);
			setBlockAndMetadata(world, -2, 9, k12, clayStairBlock, 1);
			setBlockAndMetadata(world, -1, 10, k12, clayStairBlock, 1);
			setBlockAndMetadata(world, 0, 11, k12, claySlabBlock, claySlabMeta);
			setBlockAndMetadata(world, 1, 10, k12, clayStairBlock, 0);
			setBlockAndMetadata(world, 2, 9, k12, clayStairBlock, 0);
			setBlockAndMetadata(world, 3, 8, k12, clayStairBlock, 0);
			setBlockAndMetadata(world, 4, 7, k12, clayStairBlock, 0);
			setBlockAndMetadata(world, 5, 6, k12, clayStairBlock, 0);
		}
		for (int k14 : new int[] { 0, 19 }) {
			setBlockAndMetadata(world, -4, 6, k14, clayStairBlock, 4);
			setBlockAndMetadata(world, -3, 7, k14, clayStairBlock, 4);
			setBlockAndMetadata(world, -2, 8, k14, clayStairBlock, 4);
			setBlockAndMetadata(world, -1, 9, k14, clayStairBlock, 4);
			setBlockAndMetadata(world, 0, 10, k14, clayBlock, clayMeta);
			setBlockAndMetadata(world, 1, 9, k14, clayStairBlock, 5);
			setBlockAndMetadata(world, 2, 8, k14, clayStairBlock, 5);
			setBlockAndMetadata(world, 3, 7, k14, clayStairBlock, 5);
			setBlockAndMetadata(world, 4, 6, k14, clayStairBlock, 5);
		}
		for (int k15 = 2; k15 <= 17; ++k15) {
			if (k15 % 3 != 2) {
				continue;
			}
			for (i1 = -4; i1 <= 4; ++i1) {
				setBlockAndMetadata(world, i1, 6, k15, woodBeamBlock, woodBeamMeta | 4);
			}
			setBlockAndMetadata(world, -4, 5, k15, Blocks.torch, 2);
			setBlockAndMetadata(world, 4, 5, k15, Blocks.torch, 1);
		}
		setBlockAndMetadata(world, -2, 5, 2, Blocks.torch, 3);
		setBlockAndMetadata(world, 2, 5, 2, Blocks.torch, 3);
		setBlockAndMetadata(world, -2, 5, 17, Blocks.torch, 4);
		setBlockAndMetadata(world, 2, 5, 17, Blocks.torch, 4);
		placeWallBanner(world, 0, 5, 1, LOTRItemBanner.BannerType.DORWINION, 0);
		placeWallBanner(world, 0, 5, 18, LOTRItemBanner.BannerType.DORWINION, 2);
		ItemStack drink = LOTRFoods.DORWINION_DRINK.getRandomBrewableDrink(random);
		for (k13 = 2; k13 <= 17; ++k13) {
			for (i12 = -4; i12 <= 4; ++i12) {
				if (Math.abs(i12) < 3) {
					continue;
				}
				if (k13 == 2 || k13 == 17) {
					setBlockAndMetadata(world, i12, 1, k13, plankBlock, plankMeta);
					continue;
				}
				if (k13 % 3 == 0) {
					setBlockAndMetadata(world, i12, 1, k13, Blocks.spruce_stairs, 6);
					setBlockAndMetadata(world, i12, 2, k13, Blocks.spruce_stairs, 2);
					continue;
				}
				if (k13 % 3 != 1) {
					continue;
				}
				setBlockAndMetadata(world, i12, 1, k13, Blocks.spruce_stairs, 7);
				setBlockAndMetadata(world, i12, 2, k13, Blocks.spruce_stairs, 3);
			}
			if (k13 < 5 || k13 > 15 || k13 % 3 != 2) {
				continue;
			}
			setBlockAndMetadata(world, -4, 1, k13, plankBlock, plankMeta);
			this.placeBarrel(world, random, -4, 2, k13, 4, drink);
			setBlockAndMetadata(world, 4, 1, k13, plankBlock, plankMeta);
			this.placeBarrel(world, random, 4, 2, k13, 5, drink);
		}
		for (k13 = 8; k13 <= 11; ++k13) {
			for (i12 = -1; i12 <= 1; ++i12) {
				if (Math.abs(i12) == 1 && (k13 == 8 || k13 == 11)) {
					setBlockAndMetadata(world, i12, 1, k13, plankBlock, plankMeta);
					continue;
				}
				setBlockAndMetadata(world, i12, 1, k13, plankSlabBlock, plankSlabMeta | 8);
			}
			this.placeMug(world, random, -1, 2, k13, 1, drink, LOTRFoods.DORWINION_DRINK);
			this.placeMug(world, random, 1, 2, k13, 3, drink, LOTRFoods.DORWINION_DRINK);
		}
		setBlockAndMetadata(world, 0, 1, 17, Blocks.crafting_table, 0);
		for (i1 = -2; i1 <= 2; ++i1) {
			if (Math.abs(i1) < 1) {
				continue;
			}
			setBlockAndMetadata(world, i1, 1, 17, Blocks.chest, 2);
			TileEntity tileentity = getTileEntity(world, i1, 1, 17);
			if (!(tileentity instanceof TileEntityChest)) {
				continue;
			}
			TileEntityChest chest = (TileEntityChest) tileentity;
			int wines = MathHelper.getRandomIntegerInRange(random, 3, 6);
			for (int l = 0; l < wines; ++l) {
				ItemStack chestDrinkItem = drink.copy();
				chestDrinkItem.stackSize = 1;
				LOTRItemMug.setStrengthMeta(chestDrinkItem, MathHelper.getRandomIntegerInRange(random, 1, 4));
				LOTRItemMug.Vessel[] chestVessels = LOTRFoods.DORWINION_DRINK.getDrinkVessels();
				LOTRItemMug.Vessel v = chestVessels[random.nextInt(chestVessels.length)];
				LOTRItemMug.setVessel(chestDrinkItem, v, true);
				chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), chestDrinkItem);
			}
		}
		LOTREntityDorwinionElfVintner vintner = new LOTREntityDorwinionElfVintner(world);
		spawnNPCAndSetHome(vintner, world, 0, 1, 13, 16);
		return true;
	}
}
