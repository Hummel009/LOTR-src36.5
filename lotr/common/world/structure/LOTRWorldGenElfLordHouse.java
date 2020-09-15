/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockGrass
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 */
package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityGaladhrimLord;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.feature.LOTRWorldGenMallornExtreme;
import lotr.common.world.structure2.LOTRWorldGenElfHouse;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class LOTRWorldGenElfLordHouse
extends LOTRWorldGenStructureBase {
    public LOTRWorldGenElfLordHouse(boolean flag) {
        super(flag);
    }

    public boolean generate(World world, Random random, int i, int j, int k) {
        int i1;
        int k1;
        int distSq;
        int j1;
        int i2;
        int k2;
        int k12;
        int j12;
        int k13;
        if (this.restrictions) {
            for (i1 = i - 14; i1 <= i + 14; ++i1) {
                for (j1 = j; j1 <= j + 7; ++j1) {
                    for (k13 = k - 14; k13 <= k + 14; ++k13) {
                        if (Math.abs(i1 - i) <= 2 && Math.abs(k13 - k) <= 2 || world.isAirBlock(i1, j1, k13)) continue;
                        return false;
                    }
                }
            }
            int totalGrass = 0;
            int numGrass = 0;
            for (int i12 = i - 5; i12 <= i + 5; ++i12) {
                block4: for (int k14 = k - 5; k14 <= k + 5; ++k14) {
                    if (Math.abs(i12 - i) <= 2 && Math.abs(k14 - k) <= 2) continue;
                    for (int j13 = j; j13 >= 0; --j13) {
                        if (world.getBlock(i12, j13, k14) != Blocks.grass) continue;
                        totalGrass += j13;
                        ++numGrass;
                        continue block4;
                    }
                }
            }
            int lowestGrass = totalGrass / numGrass;
            for (int i13 = i - 5; i13 <= i + 5; ++i13) {
                for (int k15 = k - 5; k15 <= k + 5; ++k15) {
                    if (Math.abs(i13 - i) <= 2 && Math.abs(k15 - k) <= 2) continue;
                    for (int j14 = j; j14 > lowestGrass; --j14) {
                        this.setBlockAndNotifyAdequately(world, i13, j14, k15, Blocks.air, 0);
                    }
                    this.setBlockAndNotifyAdequately(world, i13, lowestGrass, k15, (Block)Blocks.grass, 0);
                }
            }
        } else if (this.usingPlayer != null) {
            for (int i14 = i - 2; i14 <= i + 2; ++i14) {
                for (k1 = k - 2; k1 <= k + 2; ++k1) {
                    for (j12 = j; !LOTRMod.isOpaque(world, i14, j12, k1) && j12 >= 0; --j12) {
                        this.setBlockAndNotifyAdequately(world, i14, j12, k1, LOTRMod.wood, 1);
                    }
                }
            }
            LOTRWorldGenMallornExtreme treeGen = new LOTRWorldGenMallornExtreme(true);
            j1 = treeGen.generateAndReturnHeight(world, random, i, --j, k, true);
            j += MathHelper.floor_double((double)((float)j1 * MathHelper.randomFloatClamp((Random)random, (float)LOTRWorldGenMallornExtreme.HOUSE_HEIGHT_MIN, (float)LOTRWorldGenMallornExtreme.HOUSE_HEIGHT_MAX)));
        }
        this.buildStaircase(world, random, i, j, k);
        for (i1 = i - 14; i1 <= i + 14; ++i1) {
            for (j1 = j; j1 <= j + 6; ++j1) {
                for (k13 = k - 14; k13 <= k + 14; ++k13) {
                    this.setBlockAndNotifyAdequately(world, i1, j1, k13, Blocks.air, 0);
                }
            }
        }
        for (i1 = i - 2; i1 <= i + 2; ++i1) {
            for (j1 = j; j1 <= j + 7; ++j1) {
                for (k13 = k - 2; k13 <= k + 2; ++k13) {
                    this.setBlockAndNotifyAdequately(world, i1, j1, k13, LOTRMod.wood, 1);
                }
            }
        }
        for (i1 = i - 12; i1 <= i + 12; ++i1) {
            for (k1 = k - 12; k1 <= k + 12; ++k1) {
                i2 = i1 - i;
                k2 = k1 - k;
                if (Math.abs(i2) <= 2 && Math.abs(k2) <= 2) continue;
                distSq = i2 * i2 + k2 * k2;
                if (distSq < 100) {
                    this.setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.planks, 1);
                    continue;
                }
                if (distSq >= 169) continue;
                this.setBlockAndNotifyAdequately(world, i1, j + 1, k1, LOTRMod.planks, 1);
                if (distSq <= 132) continue;
                this.setBlockAndNotifyAdequately(world, i1, j + 2, k1, LOTRMod.fence, 1);
            }
        }
        for (i1 = i - 12; i1 <= i + 12; ++i1) {
            for (k1 = k - 12; k1 <= k + 12; ++k1) {
                i2 = i1 - i;
                k2 = k1 - k;
                distSq = i2 * i2 + k2 * k2;
                if (Math.abs(i2) <= 2 && Math.abs(k2) <= 2 || distSq >= 169) continue;
                this.setBlockAndNotifyAdequately(world, i1, j + 6, k1, LOTRMod.planks, 1);
                int i3 = i1;
                int k3 = k1;
                if (i3 > i) {
                    --i3;
                }
                if (i3 < i) {
                    ++i3;
                }
                if (k3 > k) {
                    --k3;
                }
                if (k3 < k) {
                    ++k3;
                }
                this.setBlockAndNotifyAdequately(world, i3, j + 7, k3, LOTRMod.planks, 1);
            }
        }
        this.buildStairCircle(world, i, j, k, 10, true, false);
        this.buildStairCircle(world, i, j + 1, k, 9, false, true);
        this.buildStairCircle(world, i, j + 6, k, 13, false, false);
        this.buildStairCircle(world, i, j + 7, k, 12, false, false);
        this.setBlockAndNotifyAdequately(world, i + 3, j + 3, k, LOTRWorldGenElfHouse.getRandomTorch(random), 1);
        this.setBlockAndNotifyAdequately(world, i - 3, j + 3, k, LOTRWorldGenElfHouse.getRandomTorch(random), 2);
        this.setBlockAndNotifyAdequately(world, i, j + 3, k + 3, LOTRWorldGenElfHouse.getRandomTorch(random), 3);
        this.setBlockAndNotifyAdequately(world, i, j + 3, k - 3, LOTRWorldGenElfHouse.getRandomTorch(random), 4);
        for (i1 = i - 3; i1 <= i + 3; ++i1) {
            this.setBlockAndNotifyAdequately(world, i1, j + 5, k - 3, LOTRMod.stairsMallorn, 6);
            this.setBlockAndNotifyAdequately(world, i1, j + 5, k + 3, LOTRMod.stairsMallorn, 7);
        }
        for (k12 = k - 2; k12 <= k + 2; ++k12) {
            this.setBlockAndNotifyAdequately(world, i - 3, j + 5, k12, LOTRMod.stairsMallorn, 4);
            this.setBlockAndNotifyAdequately(world, i + 3, j + 5, k12, LOTRMod.stairsMallorn, 5);
        }
        for (i1 = i - 4; i1 <= i + 4; i1 += 8) {
            for (k1 = k - 4; k1 <= k + 4; k1 += 8) {
                for (j12 = j + 1; j12 <= j + 5; ++j12) {
                    this.setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRMod.wood, 1);
                }
                this.setBlockAndNotifyAdequately(world, i1 + 1, j + 3, k1, LOTRWorldGenElfHouse.getRandomTorch(random), 1);
                this.setBlockAndNotifyAdequately(world, i1 - 1, j + 3, k1, LOTRWorldGenElfHouse.getRandomTorch(random), 2);
                this.setBlockAndNotifyAdequately(world, i1, j + 3, k1 + 1, LOTRWorldGenElfHouse.getRandomTorch(random), 3);
                this.setBlockAndNotifyAdequately(world, i1, j + 3, k1 - 1, LOTRWorldGenElfHouse.getRandomTorch(random), 4);
            }
        }
        this.setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 5, LOTRMod.elvenTable, 0);
        this.setBlockAndNotifyAdequately(world, i - 5, j + 1, k - 4, LOTRMod.stairsMallorn, 7);
        this.placeFlowerPot(world, i - 5, j + 2, k - 4, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i - 4, j + 1, k - 5, LOTRMod.stairsMallorn, 5);
        this.placeFlowerPot(world, i - 4, j + 2, k - 5, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 5, LOTRMod.elvenTable, 0);
        this.setBlockAndNotifyAdequately(world, i + 5, j + 1, k - 4, LOTRMod.stairsMallorn, 7);
        this.placeFlowerPot(world, i + 5, j + 2, k - 4, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i + 4, j + 1, k - 5, LOTRMod.stairsMallorn, 4);
        this.placeFlowerPot(world, i + 4, j + 2, k - 5, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 5, LOTRMod.elvenTable, 0);
        this.setBlockAndNotifyAdequately(world, i - 5, j + 1, k + 4, LOTRMod.stairsMallorn, 6);
        this.placeFlowerPot(world, i - 5, j + 2, k + 4, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i - 4, j + 1, k + 5, LOTRMod.stairsMallorn, 5);
        this.placeFlowerPot(world, i - 4, j + 2, k + 5, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 5, LOTRMod.elvenTable, 0);
        this.setBlockAndNotifyAdequately(world, i + 5, j + 1, k + 4, LOTRMod.stairsMallorn, 6);
        this.placeFlowerPot(world, i + 5, j + 2, k + 4, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.setBlockAndNotifyAdequately(world, i + 4, j + 1, k + 5, LOTRMod.stairsMallorn, 4);
        this.placeFlowerPot(world, i + 4, j + 2, k + 5, LOTRWorldGenElfHouse.getRandomPlant(random));
        this.placeRandomChandelier(world, random, i - 8, j + 5, k);
        this.placeRandomChandelier(world, random, i + 8, j + 5, k);
        this.placeRandomChandelier(world, random, i, j + 5, k - 8);
        this.placeRandomChandelier(world, random, i, j + 5, k + 8);
        for (i1 = i - 8; i1 <= i + 8; i1 += 16) {
            for (k1 = k - 8; k1 <= k + 8; k1 += 16) {
                for (j12 = j + 2; j12 <= j + 5; ++j12) {
                    this.setBlockAndNotifyAdequately(world, i1, j12, k1, LOTRMod.planks, 1);
                }
                for (i2 = i1 - 1; i2 <= i1 + 1; ++i2) {
                    this.setBlockAndNotifyAdequately(world, i2, j + 5, k1 - 1, LOTRMod.stairsMallorn, 6);
                    this.setBlockAndNotifyAdequately(world, i2, j + 5, k1 + 1, LOTRMod.stairsMallorn, 7);
                }
                this.setBlockAndNotifyAdequately(world, i1 - 1, j + 5, k1, LOTRMod.stairsMallorn, 4);
                this.setBlockAndNotifyAdequately(world, i1 + 1, j + 5, k1, LOTRMod.stairsMallorn, 5);
            }
        }
        for (int j15 = j + 2; j15 <= j + 5; ++j15) {
            this.setBlockAndNotifyAdequately(world, i - 12, j15, k - 4, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i - 12, j15, k + 4, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i + 12, j15, k - 4, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i + 12, j15, k + 4, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i - 4, j15, k - 12, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i + 4, j15, k - 12, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i - 4, j15, k + 12, LOTRMod.wood, 1);
            this.setBlockAndNotifyAdequately(world, i + 4, j15, k + 12, LOTRMod.wood, 1);
        }
        for (k12 = k - 5; k12 <= k + 5; ++k12) {
            if (Math.abs(k12 - k) <= 2) {
                this.setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRMod.woodSlabSingle, 9);
                this.setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRMod.woodSlabSingle, 9);
            } else {
                this.setBlockAndNotifyAdequately(world, i - 11, j + 5, k12, LOTRMod.stairsMallorn, 5);
                this.setBlockAndNotifyAdequately(world, i + 11, j + 5, k12, LOTRMod.stairsMallorn, 4);
            }
            if (k12 - k == -5 || k12 - k == 3) {
                this.setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRMod.stairsMallorn, 6);
                this.setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRMod.stairsMallorn, 6);
                continue;
            }
            if (k12 - k != -3 && k12 - k != 5) continue;
            this.setBlockAndNotifyAdequately(world, i - 12, j + 5, k12, LOTRMod.stairsMallorn, 7);
            this.setBlockAndNotifyAdequately(world, i + 12, j + 5, k12, LOTRMod.stairsMallorn, 7);
        }
        for (i1 = i - 5; i1 <= i + 5; ++i1) {
            if (Math.abs(i1 - i) <= 2) {
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.woodSlabSingle, 9);
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.woodSlabSingle, 9);
            } else {
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k - 11, LOTRMod.stairsMallorn, 7);
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k + 11, LOTRMod.stairsMallorn, 6);
            }
            if (i1 - i == -5 || i1 - i == 3) {
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.stairsMallorn, 4);
                this.setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.stairsMallorn, 4);
                continue;
            }
            if (i1 - i != -3 && i1 - i != 5) continue;
            this.setBlockAndNotifyAdequately(world, i1, j + 5, k - 12, LOTRMod.stairsMallorn, 5);
            this.setBlockAndNotifyAdequately(world, i1, j + 5, k + 12, LOTRMod.stairsMallorn, 5);
        }
        this.setBlockAndNotifyAdequately(world, i + 6, j + 1, k, LOTRMod.elvenBed, 3);
        this.setBlockAndNotifyAdequately(world, i + 7, j + 1, k, LOTRMod.elvenBed, 11);
        this.setBlockAndNotifyAdequately(world, i, j + 1, k + 7, LOTRMod.commandTable, 0);
        this.placeBanner(world, i, j + 2, k - 11, 0, LOTRItemBanner.BannerType.GALADHRIM);
        this.placeBanner(world, i + 11, j + 2, k, 1, LOTRItemBanner.BannerType.GALADHRIM);
        this.placeBanner(world, i, j + 2, k + 11, 2, LOTRItemBanner.BannerType.GALADHRIM);
        this.placeBanner(world, i - 11, j + 2, k, 3, LOTRItemBanner.BannerType.GALADHRIM);
        this.tryPlaceLight(world, i - 12, j, k - 2, random);
        this.tryPlaceLight(world, i - 12, j, k + 2, random);
        this.tryPlaceLight(world, i - 9, j, k + 9, random);
        this.tryPlaceLight(world, i - 2, j, k + 12, random);
        this.tryPlaceLight(world, i + 2, j, k + 12, random);
        this.tryPlaceLight(world, i + 9, j, k + 9, random);
        this.tryPlaceLight(world, i + 12, j, k + 2, random);
        this.tryPlaceLight(world, i + 12, j, k - 2, random);
        this.tryPlaceLight(world, i + 9, j, k - 9, random);
        this.tryPlaceLight(world, i + 2, j, k - 12, random);
        this.tryPlaceLight(world, i - 2, j, k - 12, random);
        this.tryPlaceLight(world, i - 9, j, k - 9, random);
        for (i1 = i - 4; i1 <= i - 3; ++i1) {
            for (k1 = k - 3; k1 <= k; ++k1) {
                this.setBlockAndNotifyAdequately(world, i1, j, k1, Blocks.air, 0);
            }
            this.setBlockAndNotifyAdequately(world, i1, j, k - 3, LOTRMod.stairsMallorn, 3);
        }
        LOTREntityGaladhrimLord elfLord = new LOTREntityGaladhrimLord(world);
        elfLord.setLocationAndAngles((double)i + 0.5, (double)(j + 1), (double)k + 3.5, 0.0f, 0.0f);
        elfLord.spawnRidingHorse = false;
        ((LOTREntityNPC)elfLord).onSpawnWithEgg(null);
        elfLord.setHomeArea(i, j, k, 8);
        world.spawnEntityInWorld((Entity)elfLord);
        return true;
    }

    private void buildStaircase(World world, Random random, int i, int j, int k) {
        Block block;
        int i1 = i - 3;
        int j1 = j - 1;
        int k1 = k - 2;
        int l = 0;
        while (j1 >= 0 && (!(block = world.getBlock(i1, j1, k1)).isOpaqueCube() || block.isWood((IBlockAccess)world, i1, j1, k1))) {
            int k2;
            int i2;
            int j2;
            int k22;
            int k23;
            int l1 = l % 24;
            if (l1 < 5) {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        this.setBlockAndNotifyAdequately(world, i2, j2, k1, Blocks.air, 0);
                    }
                }
                this.setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 3);
                this.setBlockAndNotifyAdequately(world, i1 - 1, j1, k1, LOTRMod.stairsMallorn, 3);
                this.setBlockAndNotifyAdequately(world, i1 - 2, j1, k1, LOTRMod.stairsMallorn, 4);
                this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k1, LOTRMod.fence, 1);
                if (l1 > 0) {
                    this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRMod.fence, 1);
                }
                --j1;
                ++k1;
            } else if (l1 == 5) {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 <= k1 + 2; ++k22) {
                            this.setBlockAndNotifyAdequately(world, i2, j2, k22, Blocks.air, 0);
                        }
                    }
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    for (k23 = k1; k23 <= k1 + 1; ++k23) {
                        this.setBlockAndNotifyAdequately(world, i2, j1, k23, LOTRMod.planks, 1);
                    }
                }
                for (k2 = k1; k2 <= k1 + 2; ++k2) {
                    this.setBlockAndNotifyAdequately(world, i1 - 2, j1, k2, LOTRMod.stairsMallorn, 4);
                    this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k2, LOTRMod.fence, 1);
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    this.setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
                    this.setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRMod.fence, 1);
                }
                this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1, LOTRMod.fence, 1);
                this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 + 2, LOTRWorldGenElfHouse.getRandomTorch(random), 5);
                ++i1;
            } else if (l1 < 11) {
                for (k2 = k1; k2 <= k1 + 2; ++k2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        this.setBlockAndNotifyAdequately(world, i1, j2, k2, Blocks.air, 0);
                    }
                }
                this.setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 1);
                this.setBlockAndNotifyAdequately(world, i1, j1, k1 + 1, LOTRMod.stairsMallorn, 1);
                this.setBlockAndNotifyAdequately(world, i1, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
                this.setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 + 2, LOTRMod.fence, 1);
                if (l1 > 6) {
                    this.setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRMod.fence, 1);
                }
                --j1;
                ++i1;
            } else if (l1 == 11) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 <= k1 + 2; ++k22) {
                            this.setBlockAndNotifyAdequately(world, i2, j2, k22, Blocks.air, 0);
                        }
                    }
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    for (k23 = k1; k23 <= k1 + 1; ++k23) {
                        this.setBlockAndNotifyAdequately(world, i2, j1, k23, LOTRMod.planks, 1);
                    }
                }
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    this.setBlockAndNotifyAdequately(world, i2, j1, k1 + 2, LOTRMod.stairsMallorn, 7);
                    this.setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 + 2, LOTRMod.fence, 1);
                }
                for (k2 = k1; k2 <= k1 + 1; ++k2) {
                    this.setBlockAndNotifyAdequately(world, i1 + 2, j1, k2, LOTRMod.stairsMallorn, 5);
                    this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k2, LOTRMod.fence, 1);
                }
                this.setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 + 2, LOTRMod.fence, 1);
                this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 + 2, LOTRWorldGenElfHouse.getRandomTorch(random), 5);
                --k1;
            } else if (l1 < 17) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        this.setBlockAndNotifyAdequately(world, i2, j2, k1, Blocks.air, 0);
                    }
                }
                this.setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 2);
                this.setBlockAndNotifyAdequately(world, i1 + 1, j1, k1, LOTRMod.stairsMallorn, 2);
                this.setBlockAndNotifyAdequately(world, i1 + 2, j1, k1, LOTRMod.stairsMallorn, 5);
                this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k1, LOTRMod.fence, 1);
                if (l1 > 12) {
                    this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRMod.fence, 1);
                }
                --j1;
                --k1;
            } else if (l1 == 17) {
                for (i2 = i1; i2 <= i1 + 2; ++i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 >= k1 - 2; --k22) {
                            this.setBlockAndNotifyAdequately(world, i2, j2, k22, Blocks.air, 0);
                        }
                    }
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    for (k23 = k1; k23 >= k1 - 1; --k23) {
                        this.setBlockAndNotifyAdequately(world, i2, j1, k23, LOTRMod.planks, 1);
                    }
                }
                for (k2 = k1; k2 >= k1 - 2; --k2) {
                    this.setBlockAndNotifyAdequately(world, i1 + 2, j1, k2, LOTRMod.stairsMallorn, 5);
                    this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 1, k2, LOTRMod.fence, 1);
                }
                for (i2 = i1; i2 <= i1 + 1; ++i2) {
                    this.setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
                    this.setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRMod.fence, 1);
                }
                this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1, LOTRMod.fence, 1);
                this.setBlockAndNotifyAdequately(world, i1 + 2, j1 + 2, k1 - 2, LOTRWorldGenElfHouse.getRandomTorch(random), 5);
                --i1;
            } else if (l1 < 23) {
                for (k2 = k1; k2 >= k1 - 2; --k2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        this.setBlockAndNotifyAdequately(world, i1, j2, k2, Blocks.air, 0);
                    }
                }
                this.setBlockAndNotifyAdequately(world, i1, j1, k1, LOTRMod.stairsMallorn, 0);
                this.setBlockAndNotifyAdequately(world, i1, j1, k1 - 1, LOTRMod.stairsMallorn, 0);
                this.setBlockAndNotifyAdequately(world, i1, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
                this.setBlockAndNotifyAdequately(world, i1, j1 + 1, k1 - 2, LOTRMod.fence, 1);
                if (l1 > 18) {
                    this.setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRMod.fence, 1);
                }
                --j1;
                --i1;
            } else if (l1 == 23) {
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    for (j2 = j1 + 1; j2 <= j1 + 3; ++j2) {
                        for (k22 = k1; k22 >= k1 - 2; --k22) {
                            this.setBlockAndNotifyAdequately(world, i2, j2, k22, Blocks.air, 0);
                        }
                    }
                }
                for (i2 = i1; i2 >= i1 - 1; --i2) {
                    for (k23 = k1; k23 >= k1 - 1; --k23) {
                        this.setBlockAndNotifyAdequately(world, i2, j1, k23, LOTRMod.planks, 1);
                    }
                }
                for (i2 = i1; i2 >= i1 - 2; --i2) {
                    this.setBlockAndNotifyAdequately(world, i2, j1, k1 - 2, LOTRMod.stairsMallorn, 6);
                    this.setBlockAndNotifyAdequately(world, i2, j1 + 1, k1 - 2, LOTRMod.fence, 1);
                }
                for (k2 = k1; k2 >= k1 - 1; --k2) {
                    this.setBlockAndNotifyAdequately(world, i1 - 2, j1, k2, LOTRMod.stairsMallorn, 4);
                    this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 1, k2, LOTRMod.fence, 1);
                }
                this.setBlockAndNotifyAdequately(world, i1, j1 + 2, k1 - 2, LOTRMod.fence, 1);
                this.setBlockAndNotifyAdequately(world, i1 - 2, j1 + 2, k1 - 2, LOTRWorldGenElfHouse.getRandomTorch(random), 5);
                ++k1;
            }
            ++l;
        }
    }

    private void buildStairCircle(World world, int i, int j, int k, int range, boolean upsideDown, boolean insideOut) {
        for (int i1 = i - range; i1 <= i + range; ++i1) {
            for (int k1 = k - range; k1 <= k + range; ++k1) {
                if (!world.isAirBlock(i1, j, k1)) continue;
                int direction = -1;
                if (this.isMallornPlanks(world, i1 - 1, j, k1)) {
                    direction = 1;
                } else if (this.isMallornPlanks(world, i1 + 1, j, k1)) {
                    direction = 3;
                } else if (this.isMallornPlanks(world, i1, j, k1 - 1)) {
                    direction = 2;
                } else if (this.isMallornPlanks(world, i1, j, k1 + 1)) {
                    direction = 0;
                } else if (this.isMallornPlanks(world, i1 - 1, j, k1 - 1) || this.isMallornPlanks(world, i1 + 1, j, k1 - 1)) {
                    direction = 2;
                } else if (this.isMallornPlanks(world, i1 - 1, j, k1 + 1) || this.isMallornPlanks(world, i1 + 1, j, k1 + 1)) {
                    direction = 0;
                }
                if (direction == -1) continue;
                if (insideOut) {
                    direction += 4;
                    direction &= 3;
                }
                int meta = 0;
                switch (direction) {
                    case 0: {
                        meta = 2;
                        break;
                    }
                    case 1: {
                        meta = 1;
                        break;
                    }
                    case 2: {
                        meta = 3;
                        break;
                    }
                    case 3: {
                        meta = 0;
                    }
                }
                if (upsideDown) {
                    meta |= 4;
                }
                this.setBlockAndNotifyAdequately(world, i1, j, k1, LOTRMod.stairsMallorn, meta);
            }
        }
    }

    private boolean isMallornPlanks(World world, int i, int j, int k) {
        return world.getBlock(i, j, k) == LOTRMod.planks && world.getBlockMetadata(i, j, k) == 1;
    }

    private void tryPlaceLight(World world, int i, int j, int k, Random random) {
        int j1;
        int height = 3 + random.nextInt(7);
        for (j1 = j; j1 >= j - height; --j1) {
            if (!this.restrictions) continue;
            if (!world.isAirBlock(i, j1, k)) {
                return;
            }
            if (j1 != j - height || world.isAirBlock(i, j1, k - 1) && world.isAirBlock(i, j1, k + 1) && world.isAirBlock(i - 1, j1, k) && world.isAirBlock(i + 1, j1, k)) continue;
            return;
        }
        for (j1 = j; j1 >= j - height; --j1) {
            if (j1 == j - height) {
                this.setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.planks, 1);
                this.setBlockAndNotifyAdequately(world, i, j1, k - 1, LOTRWorldGenElfHouse.getRandomTorch(random), 4);
                this.setBlockAndNotifyAdequately(world, i, j1, k + 1, LOTRWorldGenElfHouse.getRandomTorch(random), 3);
                this.setBlockAndNotifyAdequately(world, i - 1, j1, k, LOTRWorldGenElfHouse.getRandomTorch(random), 2);
                this.setBlockAndNotifyAdequately(world, i + 1, j1, k, LOTRWorldGenElfHouse.getRandomTorch(random), 1);
                continue;
            }
            this.setBlockAndNotifyAdequately(world, i, j1, k, LOTRMod.fence, 1);
        }
    }

    private void placeRandomChandelier(World world, Random random, int i, int j, int k) {
        ItemStack itemstack = LOTRWorldGenElfHouse.getRandomChandelier(random);
        this.setBlockAndNotifyAdequately(world, i, j, k, Block.getBlockFromItem((Item)itemstack.getItem()), itemstack.getItemDamage());
    }
}

