/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.world.gen.feature.WorldGenMinable
 *  net.minecraft.world.gen.feature.WorldGenerator
 */
package lotr.common.world.biome;

import lotr.common.LOTRMod;
import lotr.common.world.map.LOTRRoadType;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class LOTRBiomeGenEasternDesolation
extends LOTRBiomeGenMordor {
    public LOTRBiomeGenEasternDesolation(int i, boolean major) {
        super(i, major);
        this.topBlock = LOTRMod.mordorDirt;
        this.fillerBlock = LOTRMod.mordorDirt;
        this.decorator.addSoil((WorldGenerator)new WorldGenMinable(LOTRMod.mordorGravel, 0, 60, LOTRMod.mordorDirt), 6.0f, 60, 100);
        this.decorator.grassPerChunk = 3;
        this.biomeColors.setSky(9538431);
        this.biomeColors.resetClouds();
        this.biomeColors.resetFog();
    }

    @Override
    public LOTRRoadType getRoadBlock() {
        return LOTRRoadType.DIRT;
    }

    @Override
    public LOTRMusicRegion.Sub getBiomeMusic() {
        return LOTRMusicRegion.MORDOR.getSubregion("east");
    }

    @Override
    public float getTreeIncreaseChance() {
        return 0.5f;
    }

    @Override
    public int spawnCountMultiplier() {
        return super.spawnCountMultiplier() * 2;
    }
}

