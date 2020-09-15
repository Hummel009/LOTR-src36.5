/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockSand
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.biome.BiomeGenBase
 */
package lotr.common.world.biome;

import net.minecraft.init.Blocks;

public class LOTRBiomeGenNearHaradRed
extends LOTRBiomeGenNearHarad {
    public LOTRBiomeGenNearHaradRed(int i, boolean major) {
        super(i, major);
        this.setDisableRain();
        this.topBlock = Blocks.sand;
        this.topBlockMeta = 1;
        this.fillerBlock = Blocks.sand;
        this.fillerBlockMeta = 1;
        this.decorator.clearRandomStructures();
        this.decorator.clearVillages();
    }
}

