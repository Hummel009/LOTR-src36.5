/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockSand
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.World
 *  net.minecraft.world.gen.NoiseGeneratorPerlin
 *  net.minecraft.world.gen.feature.WorldGenMinable
 *  net.minecraft.world.gen.feature.WorldGenerator
 */
package lotr.common.world.biome;

import java.util.Random;

import lotr.common.world.biome.variant.LOTRBiomeVariant;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenRhunLandHills
extends LOTRBiomeGenRhunLand {
    private static NoiseGeneratorPerlin noiseStone = new NoiseGeneratorPerlin(new Random(528592609698295062L), 1);
    private static NoiseGeneratorPerlin noiseSand = new NoiseGeneratorPerlin(new Random(23849150950915615L), 1);

    public LOTRBiomeGenRhunLandHills(int i, boolean major) {
        super(i, major);
        this.npcSpawnList.clear();
        this.clearBiomeVariants();
        this.addBiomeVariantSet(LOTRBiomeVariant.SET_MOUNTAINS);
        this.decorator.addOre((WorldGenerator)new WorldGenMinable(Blocks.gold_ore, 4), 2.0f, 0, 48);
        this.decorator.resetTreeCluster();
        this.decorator.flowersPerChunk = 2;
        this.decorator.doubleFlowersPerChunk = 0;
        this.decorator.grassPerChunk = 5;
        this.decorator.doubleGrassPerChunk = 1;
        this.decorator.clearVillages();
        this.biomeColors.resetGrass();
    }

    @Override
    public void generateBiomeTerrain(World world, Random random, Block[] blocks, byte[] meta, int i, int k, double stoneNoise, int height, LOTRBiomeVariant variant) {
        double d4;
        Block topBlock_pre = this.topBlock;
        int topBlockMeta_pre = this.topBlockMeta;
        Block fillerBlock_pre = this.fillerBlock;
        int fillerBlockMeta_pre = this.fillerBlockMeta;
        double d1 = noiseStone.func_151601_a((double)i * 0.09, (double)k * 0.09);
        double d2 = noiseStone.func_151601_a((double)i * 0.4, (double)k * 0.4);
        double d3 = noiseSand.func_151601_a((double)i * 0.09, (double)k * 0.09);
        if (d3 + (d4 = noiseSand.func_151601_a((double)i * 0.4, (double)k * 0.4)) > 1.1) {
            this.topBlock = Blocks.sand;
            this.topBlockMeta = 0;
            this.fillerBlock = this.topBlock;
            this.fillerBlockMeta = this.topBlockMeta;
        } else if (d1 + d2 > 0.4) {
            this.topBlock = Blocks.stone;
            this.topBlockMeta = 0;
            this.fillerBlock = this.topBlock;
            this.fillerBlockMeta = this.topBlockMeta;
        }
        super.generateBiomeTerrain(world, random, blocks, meta, i, k, stoneNoise, height, variant);
        this.topBlock = topBlock_pre;
        this.topBlockMeta = topBlockMeta_pre;
        this.fillerBlock = fillerBlock_pre;
        this.fillerBlockMeta = fillerBlockMeta_pre;
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.25f;
    }
}

