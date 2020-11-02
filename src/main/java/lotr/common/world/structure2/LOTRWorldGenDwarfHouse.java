package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.*;
import lotr.common.block.LOTRBlockGateDwarvenIthildin;
import lotr.common.entity.npc.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTRWorldGenDwarfHouse extends LOTRWorldGenStructureBase2 {
	protected Block stoneBlock;
	protected int stoneMeta;
	protected Block fillerBlock;
	protected int fillerMeta;
	protected Block topBlock;
	protected int topMeta;
	protected Block brickBlock;
	protected int brickMeta;
	protected Block brickStairBlock;
	protected Block brick2Block;
	protected int brick2Meta;
	protected Block pillarBlock;
	protected int pillarMeta;
	protected Block chandelierBlock;
	protected int chandelierMeta;
	protected Block tableBlock;
	protected Block barsBlock;
	protected Block plankBlock;
	protected int plankMeta;
	protected Block plankSlabBlock;
	protected int plankSlabMeta;
	protected Block plankStairBlock;
	protected Block carpetBlock;
	protected int carpetMeta;
	protected Block plateBlock;
	protected LOTRChestContents larderContents;
	protected LOTRChestContents personalContents;
	protected LOTRFoods plateFoods;
	protected LOTRFoods drinkFoods;

	public LOTRWorldGenDwarfHouse(boolean flag) {
		super(flag);
	}

	protected LOTREntityDwarf createDwarf(World world) {
		return new LOTREntityDwarf(world);
	}

	@Override
	protected void setupRandomBlocks(Random random) {
		stoneBlock = Blocks.stone;
		stoneMeta = 0;
		fillerBlock = Blocks.dirt;
		fillerMeta = 0;
		topBlock = Blocks.grass;
		topMeta = 0;
		brickBlock = LOTRMod.brick;
		brickMeta = 6;
		brickStairBlock = LOTRMod.stairsDwarvenBrick;
		brick2Block = Blocks.stonebrick;
		brick2Meta = 0;
		pillarBlock = LOTRMod.pillar;
		pillarMeta = 0;
		chandelierBlock = LOTRMod.chandelier;
		chandelierMeta = 8;
		tableBlock = LOTRMod.dwarvenTable;
		barsBlock = LOTRMod.dwarfBars;
		int randomWood = random.nextInt(4);
		if (randomWood == 0) {
			plankBlock = Blocks.planks;
			plankMeta = 1;
			plankSlabBlock = Blocks.wooden_slab;
			plankSlabMeta = 1;
			plankStairBlock = Blocks.spruce_stairs;
		} else if (randomWood == 1) {
			plankBlock = LOTRMod.planks;
			plankMeta = 13;
			plankSlabBlock = LOTRMod.woodSlabSingle2;
			plankSlabMeta = 5;
			plankStairBlock = LOTRMod.stairsLarch;
		} else if (randomWood == 2) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 4;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 4;
			plankStairBlock = LOTRMod.stairsPine;
		} else if (randomWood == 3) {
			plankBlock = LOTRMod.planks2;
			plankMeta = 3;
			plankSlabBlock = LOTRMod.woodSlabSingle3;
			plankSlabMeta = 3;
			plankStairBlock = LOTRMod.stairsFir;
		}
		carpetBlock = Blocks.carpet;
		int randomCarpet = random.nextInt(3);
		if (randomCarpet == 0) {
			carpetMeta = 7;
		} else if (randomCarpet == 1) {
			carpetMeta = 12;
		} else if (randomCarpet == 2) {
			carpetMeta = 15;
		}
		plateBlock = random.nextBoolean() ? LOTRMod.ceramicPlateBlock : LOTRMod.woodPlateBlock;
		larderContents = LOTRChestContents.DWARF_HOUSE_LARDER;
		personalContents = LOTRChestContents.DWARVEN_TOWER;
		plateFoods = LOTRFoods.DWARF;
		drinkFoods = LOTRFoods.DWARF_DRINK;
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int i1;
		int k1;
		int i12;
		int k12;
		int i2;
		int j1;
		int k2;
		int j12;
		int i13;
		int j13;
		if (restrictions && usingPlayer == null) {
			this.setOriginAndRotation(world, i, j, k, rotation, 0);
			int xzRange = 5;
			int yRange = 4;
			for (i12 = -xzRange; i12 <= xzRange; ++i12) {
				for (int j14 = -yRange; j14 <= yRange; ++j14) {
					for (int k13 = -xzRange; k13 <= xzRange; ++k13) {
						if (!isAir(world, i12, j14, k13)) {
							continue;
						}
						return false;
					}
				}
			}
		} else {
			this.setOriginAndRotation(world, i, j, k, rotation, 8);
		}
		setupRandomBlocks(random);
		if (restrictions) {
			for (i13 = -1; i13 <= 1; ++i13) {
				for (j13 = 1; j13 <= 2; ++j13) {
					boolean foundAir = false;
					for (int k14 = -8; k14 >= -14; --k14) {
						if (!isAir(world, i13, j13, k14)) {
							continue;
						}
						foundAir = true;
						break;
					}
					if (foundAir) {
						continue;
					}
					return false;
				}
			}
			for (i13 = -1; i13 <= 1; ++i13) {
				for (j13 = 1; j13 <= 2; ++j13) {
					for (int k15 = -8; k15 >= -14 && !isAir(world, i13, j13, k15); --k15) {
						setAir(world, i13, j13, k15);
						if (j13 != 1) {
							continue;
						}
						setBlockAndMetadata(world, i13, j13 - 1, k15, stoneBlock, stoneMeta);
					}
				}
			}
		}
		for (i13 = -7; i13 <= 7; ++i13) {
			for (k12 = -7; k12 <= 7; ++k12) {
				i2 = Math.abs(i13);
				k2 = Math.abs(k12);
				int dist = (int) Math.round(Math.sqrt(i2 * i2 + k2 * k2));
				int top = 13 - dist;
				for (int j15 = top = Math.min(top, 7); (j15 >= -5 || !isOpaque(world, i13, j15, k12)) && getY(j15) >= 0; --j15) {
					if (isOpaque(world, i13, j15, k12)) {
						continue;
					}
					Block block = null;
					int meta = -1;
					if (j15 >= top - 4) {
						if (isOpaque(world, i13, j15 + 1, k12)) {
							block = fillerBlock;
							meta = fillerMeta;
						} else {
							block = topBlock;
							meta = topMeta;
						}
					} else {
						block = stoneBlock;
						meta = stoneMeta;
					}
					if (block == null) {
						continue;
					}
					setBlockAndMetadata(world, i13, j15, k12, block, meta);
					setGrassToDirt(world, i13, j15 - 1, k12);
				}
			}
		}
		for (j1 = 1; j1 <= 3; ++j1) {
			int i22 = 5 - j1;
			if (j1 >= 3) {
				--i22;
			}
			for (i12 = -i22; i12 <= i22; ++i12) {
				setBlockAndMetadata(world, i12, j1, -7, stoneBlock, stoneMeta);
			}
		}
		for (i13 = -11; i13 <= 11; ++i13) {
			for (k12 = -11; k12 <= 11; ++k12) {
				int top;
				i2 = Math.abs(i13);
				k2 = Math.abs(k12);
				if (i2 <= 7 && k2 <= 7) {
					continue;
				}
				int i3 = Math.min(i2, k2);
				int k3 = Math.max(i2, k2);
				int diff = k3 - 8;
				for (int limit : new int[] { 4, 7, 9 }) {
					if (i3 < limit) {
						continue;
					}
					diff += i3 - limit;
				}
				int j16 = top = 0 - (i3 + diff) / 2;
				while (!isOpaque(world, i13, j16, k12) && getY(j16) >= 0) {
					Block block = null;
					int meta = -1;
					if (j16 >= top - 4) {
						if (isOpaque(world, i13, j16 + 1, k12)) {
							block = fillerBlock;
							meta = fillerMeta;
						} else {
							block = topBlock;
							meta = topMeta;
						}
					} else {
						block = stoneBlock;
						meta = stoneMeta;
					}
					if (block != null) {
						setBlockAndMetadata(world, i13, j16, k12, block, meta);
						setGrassToDirt(world, i13, j16 - 1, k12);
					}
					--j16;
				}
			}
		}
		for (i13 = -6; i13 <= 6; ++i13) {
			for (k12 = -6; k12 <= 6; ++k12) {
				for (j12 = -4; j12 <= 4; ++j12) {
					if (Math.abs(i13) == 6 || Math.abs(k12) == 6) {
						if (j12 == 2) {
							setBlockAndMetadata(world, i13, j12, k12, plankBlock, plankMeta);
							continue;
						}
						setBlockAndMetadata(world, i13, j12, k12, brick2Block, brick2Meta);
						continue;
					}
					if (j12 == 0 || Math.abs(j12) == 4) {
						setBlockAndMetadata(world, i13, j12, k12, brick2Block, brick2Meta);
						continue;
					}
					setAir(world, i13, j12, k12);
				}
			}
		}
		for (j1 = -3; j1 <= 3; ++j1) {
			if (j1 == 0) {
				continue;
			}
			setBlockAndMetadata(world, -5, j1, -5, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, -5, j1, 5, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, 5, j1, -5, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, 5, j1, 5, pillarBlock, pillarMeta);
		}
		setBlockAndMetadata(world, -4, 2, -5, Blocks.torch, 2);
		setBlockAndMetadata(world, -5, 2, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, -4, 2, 5, Blocks.torch, 2);
		setBlockAndMetadata(world, -5, 2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, 4, 2, -5, Blocks.torch, 1);
		setBlockAndMetadata(world, 5, 2, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, 4, 2, 5, Blocks.torch, 1);
		setBlockAndMetadata(world, 5, 2, 4, Blocks.torch, 4);
		for (i13 = -4; i13 <= 4; ++i13) {
			setBlockAndMetadata(world, i13, 3, -5, brickStairBlock, 7);
			setBlockAndMetadata(world, i13, 3, 5, brickStairBlock, 6);
		}
		for (k1 = -4; k1 <= 4; ++k1) {
			setBlockAndMetadata(world, -5, 3, k1, brickStairBlock, 4);
			setBlockAndMetadata(world, 5, 3, k1, brickStairBlock, 5);
		}
		for (j1 = 1; j1 <= 2; ++j1) {
			setBlockAndMetadata(world, -1, j1, -6, pillarBlock, pillarMeta);
			setAir(world, 0, j1, -6);
			setBlockAndMetadata(world, 1, j1, -6, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, -1, j1, -7, stoneBlock, stoneMeta);
			setAir(world, 0, j1, -7);
			setBlockAndMetadata(world, 1, j1, -7, stoneBlock, stoneMeta);
		}
		placeIthildinDoor(world, 0, 1, -7, LOTRMod.dwarvenDoorIthildin, 3, LOTRBlockGateDwarvenIthildin.DoorSize._1x2);
		for (k1 = -4; k1 <= -3; ++k1) {
			for (i1 = -3; i1 <= 3; ++i1) {
				setBlockAndMetadata(world, i1, 1, k1, carpetBlock, carpetMeta);
			}
		}
		for (k1 = -1; k1 <= 3; ++k1) {
			for (i1 = -1; i1 <= 1; ++i1) {
				if (Math.abs(i1) == 1 && (k1 == -1 || k1 == 3)) {
					setBlockAndMetadata(world, i1, 1, k1, plankBlock, plankMeta);
				} else {
					setBlockAndMetadata(world, i1, 1, k1, plankSlabBlock, plankSlabMeta | 8);
				}
				if (random.nextInt(3) == 0) {
					this.placeMug(world, random, i1, 2, k1, random.nextInt(4), drinkFoods);
					continue;
				}
				placePlate(world, random, i1, 2, k1, plateBlock, plateFoods);
			}
		}
		setBlockAndMetadata(world, 0, 3, 0, chandelierBlock, chandelierMeta);
		setBlockAndMetadata(world, 0, 3, 2, chandelierBlock, chandelierMeta);
		for (k1 = 0; k1 <= 2; ++k1) {
			setBlockAndMetadata(world, -3, 1, k1, plankStairBlock, 0);
			setBlockAndMetadata(world, 3, 1, k1, plankStairBlock, 1);
		}
		for (k1 = 4; k1 <= 6; ++k1) {
			for (j13 = 1; j13 <= 4; ++j13) {
				for (i12 = -2; i12 <= 2; ++i12) {
					setBlockAndMetadata(world, i12, j13, k1, brickBlock, brickMeta);
				}
			}
		}
		for (j1 = 1; j1 <= 3; ++j1) {
			setBlockAndMetadata(world, -2, j1, 4, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, 2, j1, 4, pillarBlock, pillarMeta);
		}
		for (i13 = -1; i13 <= 1; ++i13) {
			setBlockAndMetadata(world, i13, 2, 4, barsBlock, 0);
			setBlockAndMetadata(world, i13, 3, 4, barsBlock, 0);
			setBlockAndMetadata(world, i13, 1, 5, LOTRMod.hearth, 0);
			setBlockAndMetadata(world, i13, 2, 5, Blocks.fire, 0);
			setAir(world, i13, 3, 5);
		}
		for (k1 = -2; k1 <= 1; ++k1) {
			setAir(world, -5, 0, k1);
			setAir(world, 5, 0, k1);
			int height = 1 - k1;
			for (j12 = -3; j12 < -3 + height; ++j12) {
				setBlockAndMetadata(world, -5, j12, k1, brickBlock, brickMeta);
				setBlockAndMetadata(world, 5, j12, k1, brickBlock, brickMeta);
			}
			setBlockAndMetadata(world, -5, -3 + height, k1, brickStairBlock, 3);
			setBlockAndMetadata(world, 5, -3 + height, k1, brickStairBlock, 3);
		}
		for (k1 = -5; k1 <= 5; ++k1) {
			for (j13 = -3; j13 <= -1; ++j13) {
				for (i12 = -1; i12 <= 1; ++i12) {
					setBlockAndMetadata(world, i12, j13, k1, plankBlock, plankMeta);
				}
			}
		}
		for (j1 = -3; j1 <= -1; ++j1) {
			setBlockAndMetadata(world, -2, j1, -5, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, -2, j1, 5, pillarBlock, pillarMeta);
		}
		setBlockAndMetadata(world, -5, -2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, -2, -2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, -2, -2, -4, Blocks.torch, 3);
		for (k1 = -4; k1 <= 4; ++k1) {
			if (IntMath.mod(k1, 2) == 1) {
				setBlockAndMetadata(world, -2, -3, k1, plankSlabBlock, plankSlabMeta | 8);
				if (random.nextBoolean()) {
					placePlateWithCertainty(world, random, -2, -2, k1, plateBlock, plateFoods);
				} else {
					this.placeMug(world, random, -2, -2, k1, 1, drinkFoods);
				}
			} else {
				setBlockAndMetadata(world, -2, -3, k1, plankBlock, plankMeta);
			}
			setBlockAndMetadata(world, -2, -1, k1, brickStairBlock, 5);
		}
		for (i13 = -4; i13 <= -3; ++i13) {
			setBlockAndMetadata(world, i13, -3, -5, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -2, -6, plankBlock, plankMeta);
			this.placeBarrel(world, random, i13, -2, -5, 3, drinkFoods);
			setBlockAndMetadata(world, i13, -1, -5, brickStairBlock, 7);
			setBlockAndMetadata(world, i13, -3, 5, Blocks.furnace, 2);
			setBlockAndMetadata(world, i13, -2, 6, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -1, 5, brickStairBlock, 6);
		}
		for (k1 = -4; k1 <= -3; ++k1) {
			setBlockAndMetadata(world, -5, -3, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, -6, -2, k1, plankBlock, plankMeta);
			this.placeChest(world, random, -5, -2, k1, 4, larderContents);
			setBlockAndMetadata(world, -5, -1, k1, brickStairBlock, 4);
		}
		setBlockAndMetadata(world, -2, -3, 2, Blocks.cauldron, 3);
		setBlockAndMetadata(world, -2, -3, 0, Blocks.crafting_table, 0);
		setBlockAndMetadata(world, -2, -3, -2, tableBlock, 0);
		for (j1 = -3; j1 <= -1; ++j1) {
			setBlockAndMetadata(world, 2, j1, -5, pillarBlock, pillarMeta);
			setBlockAndMetadata(world, 2, j1, 5, pillarBlock, pillarMeta);
		}
		setBlockAndMetadata(world, 5, -2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, 2, -2, 4, Blocks.torch, 4);
		setBlockAndMetadata(world, 5, -2, -4, Blocks.torch, 3);
		setBlockAndMetadata(world, 2, -2, -4, Blocks.torch, 3);
		for (k1 = -4; k1 <= 4; ++k1) {
			setBlockAndMetadata(world, 2, -3, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, 2, -1, k1, brickStairBlock, 4);
		}
		for (i13 = 3; i13 <= 4; ++i13) {
			setBlockAndMetadata(world, i13, -3, -5, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -2, -6, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -1, -5, brickStairBlock, 7);
			setBlockAndMetadata(world, i13, -3, 5, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -2, 6, plankBlock, plankMeta);
			setBlockAndMetadata(world, i13, -1, 5, brickStairBlock, 6);
			for (k12 = -2; k12 <= 0; ++k12) {
				setBlockAndMetadata(world, i13, -3, k12, carpetBlock, carpetMeta);
			}
			setBlockAndMetadata(world, i13, -3, -3, LOTRMod.dwarvenBed, 2);
			setBlockAndMetadata(world, i13, -3, -4, LOTRMod.dwarvenBed, 10);
			this.placeChest(world, random, i13, -2, -5, 3, personalContents, MathHelper.getRandomIntegerInRange(random, 2, 4));
		}
		for (k1 = -4; k1 <= -3; ++k1) {
			setBlockAndMetadata(world, 5, -3, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, 6, -2, k1, plankBlock, plankMeta);
			setBlockAndMetadata(world, 5, -1, k1, brickStairBlock, 5);
		}
		for (k1 = -2; k1 <= 2; ++k1) {
			if (k1 == 0) {
				ItemStack item = getRandomWeaponItem(random);
				placeWeaponRack(world, 2, -2, k1, 5, item);
				continue;
			}
			if (IntMath.mod(k1, 2) != 0) {
				continue;
			}
			ItemStack item = random.nextBoolean() ? getRandomWeaponItem(random) : getRandomOtherItem(random);
			spawnItemFrame(world, 1, -2, k1, 1, item);
		}
		LOTREntityDwarf dwarfMale = createDwarf(world);
		dwarfMale.familyInfo.setMale(true);
		dwarfMale.familyInfo.setName(LOTRNames.getDwarfName(random, dwarfMale.familyInfo.isMale()));
		spawnNPCAndSetHome(dwarfMale, world, 0, 2, 0, 8);
		LOTREntityDwarf dwarfFemale = createDwarf(world);
		dwarfFemale.familyInfo.setMale(false);
		dwarfFemale.familyInfo.setName(LOTRNames.getDwarfName(random, dwarfFemale.familyInfo.isMale()));
		spawnNPCAndSetHome(dwarfFemale, world, 0, 2, 0, 8);
		int maxChildren = dwarfMale.familyInfo.getRandomMaxChildren();
		dwarfMale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.dwarvenRing));
		dwarfMale.familyInfo.spouseUniqueID = dwarfFemale.getUniqueID();
		dwarfMale.familyInfo.setMaxBreedingDelay();
		dwarfMale.familyInfo.maxChildren = maxChildren;
		dwarfFemale.setCurrentItemOrArmor(4, new ItemStack(LOTRMod.dwarvenRing));
		dwarfFemale.familyInfo.spouseUniqueID = dwarfMale.getUniqueID();
		dwarfFemale.familyInfo.setMaxBreedingDelay();
		dwarfFemale.familyInfo.maxChildren = maxChildren;
		return true;
	}

	protected ItemStack getRandomWeaponItem(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.swordDwarven), new ItemStack(LOTRMod.daggerDwarven), new ItemStack(LOTRMod.hammerDwarven), new ItemStack(LOTRMod.battleaxeDwarven), new ItemStack(LOTRMod.pickaxeDwarven), new ItemStack(LOTRMod.mattockDwarven), new ItemStack(LOTRMod.throwingAxeDwarven), new ItemStack(LOTRMod.pikeDwarven), new ItemStack(LOTRMod.swordDale), new ItemStack(LOTRMod.daggerDale), new ItemStack(LOTRMod.pikeDale), new ItemStack(LOTRMod.spearDale), new ItemStack(LOTRMod.battleaxeDale) };
		return items[random.nextInt(items.length)].copy();
	}

	protected ItemStack getRandomOtherItem(Random random) {
		ItemStack[] items = new ItemStack[] { new ItemStack(LOTRMod.helmetDwarven), new ItemStack(LOTRMod.bodyDwarven), new ItemStack(LOTRMod.legsDwarven), new ItemStack(LOTRMod.bootsDwarven), new ItemStack(LOTRMod.helmetDale), new ItemStack(LOTRMod.bodyDale), new ItemStack(LOTRMod.legsDale), new ItemStack(LOTRMod.bootsDale), new ItemStack(LOTRMod.dwarfSteel), new ItemStack(LOTRMod.bronze), new ItemStack(Items.iron_ingot), new ItemStack(LOTRMod.silver), new ItemStack(LOTRMod.silverNugget), new ItemStack(Items.gold_ingot), new ItemStack(Items.gold_nugget) };
		return items[random.nextInt(items.length)].copy();
	}
}
