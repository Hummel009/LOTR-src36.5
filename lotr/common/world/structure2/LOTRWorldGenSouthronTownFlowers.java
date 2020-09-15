/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockGrass
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.world.structure2;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRWorldGenSouthronTownFlowers
extends LOTRWorldGenSouthronStructure {
    public LOTRWorldGenSouthronTownFlowers(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
        int i1;
        int k1;
        this.setOriginAndRotation(world, i, j, k, rotation, 1);
        this.setupRandomBlocks(random);
        ItemStack flower = this.getRandomFlower(world, random);
        Block flowerBlock = Block.getBlockFromItem((Item)flower.getItem());
        int flowerMeta = flower.getItemDamage();
        if (this.restrictions) {
            for (i1 = -3; i1 <= 3; ++i1) {
                for (k1 = 0; k1 <= 3; ++k1) {
                    int j1 = this.getTopBlock(world, i1, k1) - 1;
                    if (this.isSurface(world, i1, j1, k1)) continue;
                    return false;
                }
            }
        }
        for (i1 = -3; i1 <= 3; ++i1) {
            for (k1 = 0; k1 <= 3; ++k1) {
                int j1;
                int i2 = Math.abs(i1);
                for (j1 = 1; j1 <= 4; ++j1) {
                    this.setAir(world, i1, j1, k1);
                }
                for (j1 = 0; !(j1 < 0 && this.isOpaque(world, i1, j1, k1) || this.getY(j1) < 0); --j1) {
                    this.setBlockAndMetadata(world, i1, j1, k1, this.stoneBlock, this.stoneMeta);
                    this.setGrassToDirt(world, i1, j1 - 1, k1);
                }
                if ((k1 == 0 || k1 == 3) && i2 % 2 == 1) {
                    this.setBlockAndMetadata(world, i1, 1, k1, this.brickSlabBlock, this.brickSlabMeta);
                }
                if (k1 < 1 || k1 > 2 || i2 > 2) continue;
                this.setBlockAndMetadata(world, i1, 0, k1, (Block)Blocks.grass, 0);
                this.setBlockAndMetadata(world, i1, 1, k1, flowerBlock, flowerMeta);
            }
        }
        return true;
    }
}

