package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public abstract class LOTRWorldGenRohanMarketStall extends LOTRWorldGenRohanStructure {
	private static final Class[] allStallTypes = new Class[] { Blacksmith.class, Farmer.class, Lumber.class, Builder.class, Brewer.class, Butcher.class, Fish.class, Baker.class, Orcharder.class };

	public static LOTRWorldGenStructureBase2 getRandomStall(Random random, boolean flag) {
		try {
			Class cls = allStallTypes[random.nextInt(allStallTypes.length)];
			return (LOTRWorldGenStructureBase2) cls.getConstructor(Boolean.TYPE).newInstance(flag);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LOTRWorldGenRohanMarketStall(boolean flag) {
		super(flag);
	}

	@Override
	public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
		int i1;
		int k1;
		this.setOriginAndRotation(world, i, j, k, rotation, 3);
		setupRandomBlocks(random);
		if (restrictions) {
			for (i1 = -2; i1 <= 2; ++i1) {
				for (k1 = -2; k1 <= 2; ++k1) {
					int j1 = getTopBlock(world, i1, k1) - 1;
					if (isSurface(world, i1, j1, k1)) {
						continue;
					}
					return false;
				}
			}
		}
		for (i1 = -2; i1 <= 2; ++i1) {
			for (k1 = -2; k1 <= 2; ++k1) {
				int j1;
				int i2 = Math.abs(i1);
				int k2 = Math.abs(k1);
				for (j1 = 0; (j1 >= 0 || !isOpaque(world, i1, j1, k1)) && getY(j1) >= 0; --j1) {
					setBlockAndMetadata(world, i1, j1, k1, LOTRMod.dirtPath, 0);
					setGrassToDirt(world, i1, j1 - 1, k1);
				}
				for (j1 = 1; j1 <= 4; ++j1) {
					setAir(world, i1, j1, k1);
				}
				if (i2 == 2 && k2 == 2) {
					if (k1 < 0) {
						for (j1 = 1; j1 <= 4; ++j1) {
							setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
						}
						continue;
					}
					for (j1 = 1; j1 <= 3; ++j1) {
						setBlockAndMetadata(world, i1, j1, k1, fenceBlock, fenceMeta);
					}
					continue;
				}
				int j2 = 4;
				if (k1 == 2 || k1 == 1 && i2 == 2) {
					j2 = 3;
				}
				generateRoof(world, random, i1, j2, k1);
			}
		}
		setBlockAndMetadata(world, -1, 1, -2, plankStairBlock, 4);
		setBlockAndMetadata(world, 0, 1, -2, plankStairBlock, 6);
		setBlockAndMetadata(world, 1, 1, -2, plankStairBlock, 5);
		setBlockAndMetadata(world, -1, 1, 2, plankStairBlock, 4);
		setBlockAndMetadata(world, 0, 1, 2, plankStairBlock, 7);
		setBlockAndMetadata(world, 1, 1, 2, plankStairBlock, 5);
		setBlockAndMetadata(world, 2, 1, -1, plankStairBlock, 7);
		setBlockAndMetadata(world, 2, 1, 0, plankStairBlock, 4);
		setBlockAndMetadata(world, 2, 1, 1, plankStairBlock, 6);
		setBlockAndMetadata(world, -2, 1, -1, plankBlock, plankMeta);
		setBlockAndMetadata(world, -2, 1, 0, fenceGateBlock, 1);
		setBlockAndMetadata(world, -2, 1, 1, plankBlock, plankMeta);
		for (i1 = -1; i1 <= 1; ++i1) {
			setBlockAndMetadata(world, i1, 1, 1, plank2StairBlock, 6);
			setBlockAndMetadata(world, i1, 3, 1, plankSlabBlock, plankSlabMeta | 8);
		}
		for (int k12 = -1; k12 <= 0; ++k12) {
			setBlockAndMetadata(world, -2, 3, k12, plankSlabBlock, plankSlabMeta | 8);
			setBlockAndMetadata(world, 2, 3, k12, plankSlabBlock, plankSlabMeta | 8);
		}
		setBlockAndMetadata(world, 1, 1, -1, Blocks.chest, 3);
		for (i1 = -1; i1 <= 1; ++i1) {
			setBlockAndMetadata(world, i1, 3, -2, fenceBlock, fenceMeta);
		}
		LOTREntityRohanMan trader = createTrader(world);
		spawnNPCAndSetHome(trader, world, 0, 1, 0, 4);
		return true;
	}

	protected abstract void generateRoof(World var1, Random var2, int var3, int var4, int var5);

	protected abstract LOTREntityRohanMan createTrader(World var1);

	public static class Orcharder extends LOTRWorldGenRohanMarketStall {
		public Orcharder(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			if (IntMath.mod(i2 + Math.abs(k1), 2) == 0) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 14);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 5);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanOrcharder(world);
		}
	}

	public static class Baker extends LOTRWorldGenRohanMarketStall {
		public Baker(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			Math.abs(k1);
			if (i2 % 2 == 0) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 12);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 1);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanBaker(world);
		}
	}

	public static class Fish extends LOTRWorldGenRohanMarketStall {
		public Fish(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			int k2 = Math.abs(k1);
			if (k2 % 2 == 1) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 11);
			} else if (i2 % 2 == k2 / 2 % 2) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 0);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 3);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanFishmonger(world);
		}
	}

	public static class Butcher extends LOTRWorldGenRohanMarketStall {
		public Butcher(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			if (random.nextInt(3) == 0) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 14);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 6);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanButcher(world);
		}
	}

	public static class Brewer extends LOTRWorldGenRohanMarketStall {
		public Brewer(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			Math.abs(k1);
			if (i2 % 2 == 1) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 12);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 4);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanBrewer(world);
		}
	}

	public static class Builder extends LOTRWorldGenRohanMarketStall {
		public Builder(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			int k2 = Math.abs(k1);
			if (k2 % 2 == 0 && i2 % 2 == k2 / 2 % 2) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 12);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 7);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanBuilder(world);
		}
	}

	public static class Lumber extends LOTRWorldGenRohanMarketStall {
		public Lumber(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			if (i2 + Math.abs(k1) >= 3) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 13);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 12);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanLumberman(world);
		}
	}

	public static class Farmer extends LOTRWorldGenRohanMarketStall {
		public Farmer(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			if (random.nextInt(3) == 0) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 0);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 8);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanFarmer(world);
		}
	}

	public static class Blacksmith extends LOTRWorldGenRohanMarketStall {
		public Blacksmith(boolean flag) {
			super(flag);
		}

		@Override
		protected void generateRoof(World world, Random random, int i1, int j1, int k1) {
			int i2 = Math.abs(i1);
			if (i2 + Math.abs(k1) >= 3) {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 7);
			} else {
				setBlockAndMetadata(world, i1, j1, k1, Blocks.wool, 15);
			}
		}

		@Override
		protected LOTREntityRohanMan createTrader(World world) {
			return new LOTREntityRohanBlacksmith(world);
		}
	}

}
