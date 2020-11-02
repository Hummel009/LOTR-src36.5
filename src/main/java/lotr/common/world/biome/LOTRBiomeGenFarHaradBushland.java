package lotr.common.world.biome;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import lotr.common.world.feature.*;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenFarHaradBushland extends LOTRBiomeGenFarHarad {
	private WorldGenerator boulderGen = new LOTRWorldGenBoulder(Blocks.stone, 0, 1, 3);

	public LOTRBiomeGenFarHaradBushland(int i, boolean major) {
		super(i, major);
		this.addBiomeVariant(LOTRBiomeVariant.FLOWERS);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST);
		this.addBiomeVariant(LOTRBiomeVariant.FOREST_LIGHT);
		this.addBiomeVariant(LOTRBiomeVariant.STEPPE);
		this.addBiomeVariant(LOTRBiomeVariant.STEPPE_BARREN);
		this.addBiomeVariant(LOTRBiomeVariant.HILLS);
		this.addBiomeVariant(LOTRBiomeVariant.HILLS_FOREST);
		this.addBiomeVariant(LOTRBiomeVariant.DEADFOREST_OAK);
		this.addBiomeVariant(LOTRBiomeVariant.SHRUBLAND_OAK);
		decorator.treesPerChunk = 0;
		decorator.setTreeCluster(3, 3);
		decorator.logsPerChunk = 1;
		decorator.grassPerChunk = 16;
		decorator.doubleGrassPerChunk = 10;
		decorator.cornPerChunk = 4;
		decorator.addTree(LOTRTreeType.BIRCH, 100);
		decorator.addTree(LOTRTreeType.BIRCH_TALL, 100);
		decorator.addTree(LOTRTreeType.BIRCH_LARGE, 25);
		biomeColors.setGrass(13414999);
	}

	@Override
	public LOTRMusicRegion.Sub getBiomeMusic() {
		return LOTRMusicRegion.FAR_HARAD.getSubregion("bushland");
	}

	@Override
	public void decorate(World world, Random random, int i, int k) {
		int i1;
		int k1;
		int l;
		super.decorate(world, random, i, k);
		if (random.nextInt(32) == 0) {
			int boulders = 1 + random.nextInt(4);
			for (l = 0; l < boulders; ++l) {
				i1 = i + random.nextInt(16) + 8;
				k1 = k + random.nextInt(16) + 8;
				boulderGen.generate(world, random, i1, world.getHeightValue(i1, k1), k1);
			}
		}
		if (random.nextInt(16) == 0) {
			int termites = 1 + random.nextInt(4);
			for (l = 0; l < termites; ++l) {
				i1 = i + random.nextInt(16) + 8;
				k1 = k + random.nextInt(16) + 8;
				int j1 = world.getHeightValue(i1, k1);
				new LOTRWorldGenBoulder(LOTRMod.termiteMound, 0, 1, 4).generate(world, random, i1, j1, k1);
				for (int x = 0; x < 5; ++x) {
					int k2;
					int j2;
					int i2 = i1 - random.nextInt(5) + random.nextInt(5);
					if (!world.getBlock(i2, (j2 = world.getHeightValue(i2, k2 = k1 - random.nextInt(5) + random.nextInt(5))) - 1, k1).isOpaqueCube()) {
						continue;
					}
					int j3 = j2 + random.nextInt(4);
					for (int j4 = j2; j4 <= j3; ++j4) {
						world.setBlock(i2, j4, k2, LOTRMod.termiteMound);
						world.getBlock(i2, j4 - 1, k2).onPlantGrow(world, i2, j4 - 1, k2, i2, j4 - 1, k2);
					}
				}
			}
		}
	}

	@Override
	public float getTreeIncreaseChance() {
		return 0.05f;
	}

	@Override
	public float getChanceToSpawnAnimals() {
		return 0.75f;
	}
}
