/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockGrass
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.init.Blocks
 *  net.minecraft.world.World
 */
package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDolGuldurOrcChieftain;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class LOTRWorldGenDolGuldurTower
extends LOTRWorldGenStructureBase2 {
    public LOTRWorldGenDolGuldurTower(boolean flag) {
        super(flag);
    }

    @Override
    public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
        int k1;
        int i1;
        int step;
        int j2;
        int j1;
        int j12;
        int distSq;
        int k12;
        int i12;
        int i13;
        int k13;
        int k14;
        int j13;
        int radius = 6;
        int radiusPlusOne = radius + 1;
        this.setOriginAndRotation(world, i, j, k, rotation, radiusPlusOne);
        int sections = 3 + random.nextInt(3);
        int sectionHeight = 6;
        int topHeight = sections * sectionHeight;
        double radiusD = (double)radius - 0.5;
        double radiusDPlusOne = radiusD + 1.0;
        int wallThresholdMin = (int)(radiusD * radiusD);
        int wallThresholdMax = (int)(radiusDPlusOne * radiusDPlusOne);
        if (this.restrictions) {
            int minHeight = 0;
            int maxHeight = 0;
            for (i12 = -radiusPlusOne; i12 <= radiusPlusOne; ++i12) {
                for (k12 = -radiusPlusOne; k12 <= radiusPlusOne; ++k12) {
                    int distSq2 = i12 * i12 + k12 * k12;
                    if (distSq2 >= wallThresholdMax) continue;
                    int j14 = this.getTopBlock(world, i12, k12) - 1;
                    Block block = this.getBlock(world, i12, j14, k12);
                    if (block != Blocks.grass && block != Blocks.dirt && block != Blocks.stone) {
                        return false;
                    }
                    if (j14 < minHeight) {
                        minHeight = j14;
                    }
                    if (j14 > maxHeight) {
                        maxHeight = j14;
                    }
                    if (maxHeight - minHeight <= 16) continue;
                    return false;
                }
            }
        }
        for (i1 = -radius; i1 <= radius; ++i1) {
            for (k1 = -radius; k1 <= radius; ++k1) {
                distSq = i1 * i1 + k1 * k1;
                if (distSq >= wallThresholdMax) continue;
                for (j1 = 0; !(j1 != 0 && this.isOpaque(world, i1, j1, k1) || this.getY(j1) < 0); --j1) {
                    if (distSq >= wallThresholdMin) {
                        this.placeRandomBrick(world, random, i1, j1, k1);
                    } else {
                        this.setBlockAndMetadata(world, i1, j1, k1, Blocks.stonebrick, 0);
                    }
                    this.setGrassToDirt(world, i1, j1 - 1, k1);
                }
            }
        }
        for (int l = 0; l < sections; ++l) {
            int step2;
            int sectionBase = l * sectionHeight;
            for (j12 = sectionBase + 1; j12 <= sectionBase + sectionHeight; ++j12) {
                for (i13 = -radius; i13 <= radius; ++i13) {
                    for (int k15 = -radius; k15 <= radius; ++k15) {
                        int distSq3 = i13 * i13 + k15 * k15;
                        if (distSq3 >= wallThresholdMax) continue;
                        if (distSq3 >= wallThresholdMin) {
                            this.placeRandomBrick(world, random, i13, j12, k15);
                            continue;
                        }
                        if (j12 == sectionBase + sectionHeight) {
                            this.setBlockAndMetadata(world, i13, j12, k15, Blocks.stonebrick, 0);
                            continue;
                        }
                        this.setAir(world, i13, j12, k15);
                    }
                }
            }
            for (j12 = sectionBase + 2; j12 <= sectionBase + 3; ++j12) {
                for (k12 = -1; k12 <= 1; ++k12) {
                    this.setBlockAndMetadata(world, -radius, j12, k12, LOTRMod.orcSteelBars, 0);
                    this.setBlockAndMetadata(world, radius, j12, k12, LOTRMod.orcSteelBars, 0);
                }
                for (i13 = -1; i13 <= 1; ++i13) {
                    this.setBlockAndMetadata(world, i13, j12, -radius, LOTRMod.orcSteelBars, 0);
                }
            }
            if (l > 0) {
                this.setAir(world, 0, sectionBase, 0);
                for (i12 = -1; i12 <= 1; ++i12) {
                    for (k12 = -1; k12 <= 1; ++k12) {
                        int i2 = Math.abs(i12);
                        int k2 = Math.abs(k12);
                        if (i2 == 1 || k2 == 1) {
                            this.setBlockAndMetadata(world, i12, sectionBase + 1, k12, LOTRMod.wall2, 8);
                        }
                        if (i2 != 1 || k2 != 1) continue;
                        this.setBlockAndMetadata(world, i12, sectionBase + 2, k12, LOTRMod.wall2, 8);
                        this.placeSkull(world, random, i12, sectionBase + 2, k12);
                    }
                }
            } else {
                for (i12 = -1; i12 <= 1; ++i12) {
                    for (j1 = sectionBase + 1; j1 <= sectionBase + 3; ++j1) {
                        this.setAir(world, i12, j1, -radius);
                    }
                    this.setBlockAndMetadata(world, i12, sectionBase, -radius, Blocks.stonebrick, 0);
                }
                this.placeRandomStairs(world, random, -1, sectionBase + 3, -radius, 4);
                this.placeRandomStairs(world, random, 1, sectionBase + 3, -radius, 5);
                this.placeWallBanner(world, 0, sectionBase + 6, -radius, LOTRItemBanner.BannerType.DOL_GULDUR, 2);
                for (i12 = -5; i12 <= 5; ++i12) {
                    this.setBlockAndMetadata(world, i12, sectionBase, 0, LOTRMod.guldurilBrick, 4);
                }
                for (k14 = -6; k14 <= 3; ++k14) {
                    this.setBlockAndMetadata(world, 0, sectionBase, k14, LOTRMod.guldurilBrick, 4);
                }
                this.setBlockAndMetadata(world, 0, sectionBase + 1, 0, LOTRMod.guldurilBrick, 4);
                this.setBlockAndMetadata(world, 0, sectionBase + 2, 0, LOTRMod.wall2, 8);
                this.placeSkull(world, random, 0, sectionBase + 3, 0);
            }
            for (j12 = sectionBase + 1; j12 <= sectionBase + 5; ++j12) {
                this.setBlockAndMetadata(world, -2, j12, -5, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, 2, j12, -5, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, 5, j12, -2, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, 5, j12, 2, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, -3, j12, 4, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, 3, j12, 4, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, -5, j12, -2, LOTRMod.wood, 2);
                this.setBlockAndMetadata(world, -5, j12, 2, LOTRMod.wood, 2);
            }
            this.setBlockAndMetadata(world, -3, sectionBase + 4, 3, LOTRMod.morgulTorch, 4);
            this.setBlockAndMetadata(world, 3, sectionBase + 4, 3, LOTRMod.morgulTorch, 4);
            this.setBlockAndMetadata(world, 4, sectionBase + 4, -2, LOTRMod.morgulTorch, 1);
            this.setBlockAndMetadata(world, 4, sectionBase + 4, 2, LOTRMod.morgulTorch, 1);
            this.setBlockAndMetadata(world, -2, sectionBase + 4, -4, LOTRMod.morgulTorch, 3);
            this.setBlockAndMetadata(world, 2, sectionBase + 4, -4, LOTRMod.morgulTorch, 3);
            this.setBlockAndMetadata(world, -4, sectionBase + 4, -2, LOTRMod.morgulTorch, 2);
            this.setBlockAndMetadata(world, -4, sectionBase + 4, 2, LOTRMod.morgulTorch, 2);
            this.setBlockAndMetadata(world, -3, sectionBase + 5, 3, Blocks.stone_brick_stairs, 6);
            this.setBlockAndMetadata(world, 3, sectionBase + 5, 3, Blocks.stone_brick_stairs, 6);
            this.setBlockAndMetadata(world, 4, sectionBase + 5, -2, Blocks.stone_brick_stairs, 5);
            this.setBlockAndMetadata(world, 5, sectionBase + 5, -1, Blocks.stone_brick_stairs, 7);
            this.setBlockAndMetadata(world, 5, sectionBase + 5, 1, Blocks.stone_brick_stairs, 6);
            this.setBlockAndMetadata(world, 4, sectionBase + 5, 2, Blocks.stone_brick_stairs, 5);
            this.setBlockAndMetadata(world, -2, sectionBase + 5, -4, Blocks.stone_brick_stairs, 7);
            this.setBlockAndMetadata(world, -1, sectionBase + 5, -5, Blocks.stone_brick_stairs, 4);
            this.setBlockAndMetadata(world, 1, sectionBase + 5, -5, Blocks.stone_brick_stairs, 5);
            this.setBlockAndMetadata(world, 2, sectionBase + 5, -4, Blocks.stone_brick_stairs, 7);
            this.setBlockAndMetadata(world, -4, sectionBase + 5, -2, Blocks.stone_brick_stairs, 4);
            this.setBlockAndMetadata(world, -5, sectionBase + 5, -1, Blocks.stone_brick_stairs, 7);
            this.setBlockAndMetadata(world, -5, sectionBase + 5, 1, Blocks.stone_brick_stairs, 6);
            this.setBlockAndMetadata(world, -4, sectionBase + 5, 2, Blocks.stone_brick_stairs, 4);
            for (step2 = 0; step2 <= 2; ++step2) {
                this.setBlockAndMetadata(world, 1 - step2, sectionBase + 1 + step2, 4, Blocks.stone_brick_stairs, 0);
                for (j1 = sectionBase + 1; j1 <= sectionBase + step2; ++j1) {
                    this.setBlockAndMetadata(world, 1 - step2, j1, 4, Blocks.stonebrick, 0);
                }
            }
            for (k14 = 4; k14 <= 5; ++k14) {
                for (j1 = sectionBase + 1; j1 <= sectionBase + 3; ++j1) {
                    this.setBlockAndMetadata(world, -2, j1, k14, Blocks.stonebrick, 0);
                }
            }
            for (i12 = -2; i12 <= 0; ++i12) {
                this.setAir(world, i12, sectionBase + sectionHeight, 5);
            }
            for (step2 = 0; step2 <= 2; ++step2) {
                this.setBlockAndMetadata(world, -1 + step2, sectionBase + 4 + step2, 5, Blocks.stone_brick_stairs, 1);
                this.setBlockAndMetadata(world, -1 + step2, sectionBase + 3 + step2, 5, Blocks.stonebrick, 0);
                this.setBlockAndMetadata(world, -1 + step2, sectionBase + 2 + step2, 5, Blocks.stone_brick_stairs, 4);
            }
            this.setBlockAndMetadata(world, 2, sectionBase + 5, 5, Blocks.stone_brick_stairs, 4);
        }
        this.placeChest(world, random, -1, 1, 5, 0, LOTRChestContents.DOL_GULDUR_TOWER);
        for (k13 = -3; k13 <= 3; k13 += 6) {
            for (step = 0; step <= 3; ++step) {
                this.placeBrickSupports(world, random, -9 + step, k13);
                this.placeBrickSupports(world, random, 9 - step, k13);
                this.placeRandomStairs(world, random, -9 + step, 1 + step * 2, k13, 1);
                this.placeRandomStairs(world, random, 9 - step, 1 + step * 2, k13, 0);
                for (j12 = 1; j12 <= step * 2; ++j12) {
                    this.placeRandomBrick(world, random, -9 + step, j12, k13);
                    this.placeRandomBrick(world, random, 9 - step, j12, k13);
                }
            }
        }
        for (i1 = -3; i1 <= 3; i1 += 6) {
            for (step = 0; step <= 3; ++step) {
                this.placeBrickSupports(world, random, i1, -9 + step);
                this.placeBrickSupports(world, random, i1, 9 - step);
                this.placeRandomStairs(world, random, i1, 1 + step * 2, -9 + step, 2);
                this.placeRandomStairs(world, random, i1, 1 + step * 2, 9 - step, 3);
                for (j12 = 1; j12 <= step * 2; ++j12) {
                    this.placeRandomBrick(world, random, i1, j12, -9 + step);
                    this.placeRandomBrick(world, random, i1, j12, 9 - step);
                }
            }
        }
        for (i1 = -radius; i1 <= radius; ++i1) {
            for (k1 = -radius; k1 <= radius; ++k1) {
                distSq = i1 * i1 + k1 * k1;
                if (distSq >= wallThresholdMax || distSq < (int)Math.pow(radiusD - 0.25, 2.0)) continue;
                int i2 = Math.abs(i1);
                int k2 = Math.abs(k1);
                this.setBlockAndMetadata(world, i1, topHeight + 1, k1, LOTRMod.wall2, 8);
                if (i2 < 3 || k2 < 3) continue;
                this.setBlockAndMetadata(world, i1, topHeight + 2, k1, LOTRMod.wall2, 8);
                if (i2 != 4 || k2 != 4) continue;
                this.setBlockAndMetadata(world, i1, topHeight + 3, k1, LOTRMod.wall2, 8);
                this.setBlockAndMetadata(world, i1, topHeight + 4, k1, LOTRMod.wall2, 8);
                this.setBlockAndMetadata(world, i1, topHeight + 5, k1, LOTRMod.morgulTorch, 5);
            }
        }
        this.setAir(world, -2, topHeight + 1, 5);
        for (i1 = -2; i1 <= 2; i1 += 4) {
            for (step = 0; step <= 4; ++step) {
                j12 = topHeight + 1 + step * 2;
                k12 = -9 + step;
                this.placeRandomStairs(world, random, i1, j12 - 2, k12, 7);
                for (j2 = j12 - 1; j2 <= j12 + 1; ++j2) {
                    this.placeRandomBrick(world, random, i1, j2, k12);
                }
                this.placeRandomStairs(world, random, i1, j12 + 2, k12, 2);
                k12 = 9 - step;
                this.placeRandomStairs(world, random, i1, j12 - 2, k12, 6);
                for (j2 = j12 - 1; j2 <= j12 + 1; ++j2) {
                    this.placeRandomBrick(world, random, i1, j2, k12);
                }
                this.placeRandomStairs(world, random, i1, j12 + 2, k12, 3);
            }
            for (j13 = topHeight - 4; j13 <= topHeight + 2; ++j13) {
                for (k14 = -9; k14 <= -8; ++k14) {
                    this.placeRandomBrick(world, random, i1, j13, k14);
                }
                for (k14 = 8; k14 <= 9; ++k14) {
                    this.placeRandomBrick(world, random, i1, j13, k14);
                }
            }
            this.placeRandomBrick(world, random, i1, topHeight - 1, -7);
            this.placeRandomBrick(world, random, i1, topHeight, -7);
            this.setBlockAndMetadata(world, i1, topHeight + 1, -7, LOTRMod.wall2, 8);
            this.placeRandomBrick(world, random, i1, topHeight - 1, 7);
            this.placeRandomBrick(world, random, i1, topHeight, 7);
            this.setBlockAndMetadata(world, i1, topHeight + 1, 7, LOTRMod.wall2, 8);
            this.placeRandomStairs(world, random, i1, topHeight - 4, -9, 6);
            this.placeRandomStairs(world, random, i1, topHeight - 5, -8, 6);
            this.placeRandomStairs(world, random, i1, topHeight - 4, 9, 7);
            this.placeRandomStairs(world, random, i1, topHeight - 5, 8, 7);
        }
        for (k13 = -2; k13 <= 2; k13 += 4) {
            for (step = 0; step <= 4; ++step) {
                j12 = topHeight + 1 + step * 2;
                i13 = -9 + step;
                this.placeRandomStairs(world, random, i13, j12 - 2, k13, 4);
                for (j2 = j12 - 1; j2 <= j12 + 1; ++j2) {
                    this.placeRandomBrick(world, random, i13, j2, k13);
                }
                this.placeRandomStairs(world, random, i13, j12 + 2, k13, 1);
                i13 = 9 - step;
                this.placeRandomStairs(world, random, i13, j12 - 2, k13, 5);
                for (j2 = j12 - 1; j2 <= j12 + 1; ++j2) {
                    this.placeRandomBrick(world, random, i13, j2, k13);
                }
                this.placeRandomStairs(world, random, i13, j12 + 2, k13, 0);
            }
            for (j13 = topHeight - 4; j13 <= topHeight + 2; ++j13) {
                for (i12 = -9; i12 <= -8; ++i12) {
                    this.placeRandomBrick(world, random, i12, j13, k13);
                }
                for (i12 = 8; i12 <= 9; ++i12) {
                    this.placeRandomBrick(world, random, i12, j13, k13);
                }
            }
            this.placeRandomBrick(world, random, -7, topHeight - 1, k13);
            this.placeRandomBrick(world, random, -7, topHeight, k13);
            this.setBlockAndMetadata(world, -7, topHeight + 1, k13, LOTRMod.wall2, 8);
            this.placeRandomBrick(world, random, 7, topHeight - 1, k13);
            this.placeRandomBrick(world, random, 7, topHeight, k13);
            this.setBlockAndMetadata(world, 7, topHeight + 1, k13, LOTRMod.wall2, 8);
            this.placeRandomStairs(world, random, -9, topHeight - 4, k13, 5);
            this.placeRandomStairs(world, random, -8, topHeight - 5, k13, 5);
            this.placeRandomStairs(world, random, 9, topHeight - 4, k13, 4);
            this.placeRandomStairs(world, random, 8, topHeight - 5, k13, 4);
        }
        this.spawnNPCAndSetHome(new LOTREntityDolGuldurOrcChieftain(world), world, 0, topHeight + 1, 0, 16);
        this.setBlockAndMetadata(world, 0, topHeight + 1, -4, LOTRMod.commandTable, 0);
        return true;
    }

    private void placeBrickSupports(World world, Random random, int i, int k) {
        int j = 0;
        while (!this.isOpaque(world, i, j, k) && this.getY(j) >= 0) {
            this.placeRandomBrick(world, random, i, j, k);
            this.setGrassToDirt(world, i, j - 1, k);
            --j;
        }
    }

    private void placeRandomBrick(World world, Random random, int i, int j, int k) {
        if (random.nextInt(6) == 0) {
            this.setBlockAndMetadata(world, i, j, k, LOTRMod.brick2, 9);
        } else {
            this.setBlockAndMetadata(world, i, j, k, LOTRMod.brick2, 8);
        }
    }

    private void placeRandomStairs(World world, Random random, int i, int j, int k, int meta) {
        if (random.nextInt(6) == 0) {
            this.setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrickCracked, meta);
        } else {
            this.setBlockAndMetadata(world, i, j, k, LOTRMod.stairsDolGuldurBrick, meta);
        }
    }
}

