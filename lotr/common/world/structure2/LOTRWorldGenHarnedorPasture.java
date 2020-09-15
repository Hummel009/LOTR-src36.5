/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockGrass
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityChicken
 *  net.minecraft.entity.passive.EntityCow
 *  net.minecraft.entity.passive.EntityPig
 *  net.minecraft.entity.passive.EntitySheep
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.World
 */
package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.entity.animal.LOTREntityCamel;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenHarnedorPasture
extends LOTRWorldGenHarnedorStructure {
    public LOTRWorldGenHarnedorPasture(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
        int j1;
        this.setOriginAndRotation(world, i, j, k, rotation, 4);
        this.setupRandomBlocks(random);
        if (this.restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (int i1 = -3; i1 <= 3; ++i1) {
                for (int k1 = -3; k1 <= 6; ++k1) {
                    j1 = this.getTopBlock(world, i1, k1) - 1;
                    if (!this.isSurface(world, i1, j1, k1)) {
                        return false;
                    }
                    if (j1 < minHeight) {
                        minHeight = j1;
                    }
                    if (j1 > maxHeight) {
                        maxHeight = j1;
                    }
                    if (maxHeight - minHeight <= 6) continue;
                    return false;
                }
            }
        }
        for (int i1 = -3; i1 <= 3; ++i1) {
            for (int k1 = -3; k1 <= 6; ++k1) {
                int j12 = -1;
                while (!this.isOpaque(world, i1, j12, k1) && this.getY(j12) >= 0) {
                    this.setBlockAndMetadata(world, i1, j12, k1, Blocks.dirt, 0);
                    this.setGrassToDirt(world, i1, j12 - 1, k1);
                    --j12;
                }
                for (j12 = 1; j12 <= 4; ++j12) {
                    this.setAir(world, i1, j12, k1);
                }
            }
        }
        this.loadStrScan("harnedor_pasture");
        this.associateBlockMetaAlias("WOOD", this.woodBlock, this.woodMeta);
        this.associateBlockMetaAlias("FENCE", this.fenceBlock, this.fenceMeta);
        this.associateBlockAlias("FENCE_GATE", this.fenceGateBlock);
        this.generateStrScan(world, random, 0, 0, 0);
        block6: for (int i1 : new int[]{-2, -1, 1, 2}) {
            j1 = 0;
            for (int step = 0; step < 6; ++step) {
                int j2;
                int k1 = -4 - step;
                if (this.isOpaque(world, i1, j1 + 1, k1)) {
                    this.setAir(world, i1, j1 + 1, k1);
                    this.setAir(world, i1, j1 + 2, k1);
                    this.setAir(world, i1, j1 + 3, k1);
                    this.setBlockAndMetadata(world, i1, j1, k1, (Block)Blocks.grass, 0);
                    this.setGrassToDirt(world, i1, j1 - 1, k1);
                    j2 = j1 - 1;
                    while (!this.isOpaque(world, i1, j2, k1) && this.getY(j2) >= 0) {
                        this.setBlockAndMetadata(world, i1, j2, k1, Blocks.dirt, 0);
                        this.setGrassToDirt(world, i1, j2 - 1, k1);
                        --j2;
                    }
                    ++j1;
                    continue;
                }
                if (this.isOpaque(world, i1, j1, k1)) continue block6;
                this.setAir(world, i1, j1 + 1, k1);
                this.setAir(world, i1, j1 + 2, k1);
                this.setAir(world, i1, j1 + 3, k1);
                this.setBlockAndMetadata(world, i1, j1, k1, (Block)Blocks.grass, 0);
                this.setGrassToDirt(world, i1, j1 - 1, k1);
                j2 = j1 - 1;
                while (!this.isOpaque(world, i1, j2, k1) && this.getY(j2) >= 0) {
                    this.setBlockAndMetadata(world, i1, j2, k1, Blocks.dirt, 0);
                    this.setGrassToDirt(world, i1, j2 - 1, k1);
                    --j2;
                }
                --j1;
            }
        }
        int animals = 2 + random.nextInt(4);
        for (int l = 0; l < animals; ++l) {
            EntityAnimal animal = LOTRWorldGenHarnedorPasture.getRandomAnimal(world, random);
            this.spawnNPCAndSetHome((EntityCreature)animal, world, 0, 1, 0, 0);
            animal.detachHome();
        }
        return true;
    }

    public static EntityAnimal getRandomAnimal(World world, Random random) {
        int animal = random.nextInt(5);
        if (animal == 0) {
            return new EntityCow(world);
        }
        if (animal == 1) {
            return new EntityPig(world);
        }
        if (animal == 2) {
            return new EntitySheep(world);
        }
        if (animal == 3) {
            return new EntityChicken(world);
        }
        if (animal == 4) {
            return new LOTREntityCamel(world);
        }
        return null;
    }
}

