/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.math.IntMath
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockCauldron
 *  net.minecraft.block.BlockGrass
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.world.structure2;

import java.util.Random;

import com.google.common.math.IntMath;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityDorwinionElf;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTRWorldGenDorwinionElfHouse
extends LOTRWorldGenDorwinionHouse {
    private Block grapevineBlock;
    private Item wineItem;
    private Item grapeItem;

    public LOTRWorldGenDorwinionElfHouse(boolean flag) {
        super(flag);
    }

    @Override
    protected void setupRandomBlocks(Random random) {
        super.setupRandomBlocks(random);
        if (random.nextBoolean()) {
            this.grapevineBlock = LOTRMod.grapevineRed;
            this.wineItem = LOTRMod.mugRedWine;
            this.grapeItem = LOTRMod.grapeRed;
        } else {
            this.grapevineBlock = LOTRMod.grapevineWhite;
            this.wineItem = LOTRMod.mugWhiteWine;
            this.grapeItem = LOTRMod.grapeWhite;
        }
    }

    @Override
    public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
        int j1;
        int i1;
        int i12;
        int i13;
        int k1;
        int j12;
        int k2;
        int i14;
        int k12;
        int k13;
        this.setOriginAndRotation(world, i, j, k, rotation, 1);
        this.setupRandomBlocks(random);
        if (this.restrictions) {
            for (int i15 = -4; i15 <= 8; ++i15) {
                for (k13 = -1; k13 <= 20; ++k13) {
                    j12 = this.getTopBlock(world, i15, k13) - 1;
                    Block block = this.getBlock(world, i15, j12, k13);
                    if (block == Blocks.grass) continue;
                    return false;
                }
            }
        }
        boolean generateBackGate = true;
        for (i1 = 1; i1 <= 3; ++i1) {
            k1 = 20;
            j1 = this.getTopBlock(world, i1, k1) - 1;
            if (j1 == 0) continue;
            generateBackGate = false;
        }
        for (i1 = -4; i1 <= 8; ++i1) {
            for (k1 = 0; k1 <= 20; ++k1) {
                for (j1 = 1; j1 <= 6; ++j1) {
                    this.setAir(world, i1, j1, k1);
                }
                this.setBlockAndMetadata(world, i1, 0, k1, (Block)Blocks.grass, 0);
                j1 = -1;
                while (!this.isOpaque(world, i1, j1, k1) && this.getY(j1) >= 0) {
                    this.setBlockAndMetadata(world, i1, j1, k1, Blocks.dirt, 0);
                    this.setGrassToDirt(world, i1, j1 - 1, k1);
                    --j1;
                }
            }
        }
        for (i1 = -3; i1 <= 7; ++i1) {
            for (k1 = 0; k1 <= 8; ++k1) {
                if (i1 >= 3 && k1 <= 2) {
                    if (random.nextInt(3) != 0) continue;
                    this.plantFlower(world, random, i1, 1, k1);
                    continue;
                }
                if (k1 == 0 && (i1 == -3 || i1 == 2) || k1 == 3 && (i1 == 2 || i1 == 7) || k1 == 8 && (i1 == -3 || i1 == 7)) {
                    for (j1 = 0; j1 <= 4; ++j1) {
                        this.setBlockAndMetadata(world, i1, j1, k1, this.woodBeamBlock, this.woodBeamMeta);
                    }
                    continue;
                }
                if (i1 == -3 || i1 == 2 && k1 <= 3 || i1 == 7 || k1 == 0 || k1 == 3 && i1 >= 2 || k1 == 8) {
                    for (j1 = 0; j1 <= 1; ++j1) {
                        this.setBlockAndMetadata(world, i1, j1, k1, this.wallBlock, this.wallMeta);
                    }
                    for (j1 = 2; j1 <= 4; ++j1) {
                        this.setBlockAndMetadata(world, i1, j1, k1, this.brickBlock, this.brickMeta);
                    }
                    continue;
                }
                this.setBlockAndMetadata(world, i1, 0, k1, this.floorBlock, this.floorMeta);
            }
        }
        for (k13 = 1; k13 <= 7; ++k13) {
            k2 = IntMath.mod((int)k13, (int)3);
            if (k2 == 1) {
                this.setBlockAndMetadata(world, -4, 1, k13, this.brickStairBlock, 1);
                this.setGrassToDirt(world, -4, 0, k13);
                continue;
            }
            if (k2 == 2) {
                this.setAir(world, -3, 2, k13);
                this.setBlockAndMetadata(world, -3, 3, k13, this.brickStairBlock, 7);
                this.setBlockAndMetadata(world, -4, 1, k13, this.leafBlock, this.leafMeta);
                continue;
            }
            if (k2 != 0) continue;
            this.setAir(world, -3, 2, k13);
            this.setBlockAndMetadata(world, -3, 3, k13, this.brickStairBlock, 6);
            this.setBlockAndMetadata(world, -4, 1, k13, this.leafBlock, this.leafMeta);
        }
        for (int k14 : new int[]{0, 8}) {
            this.setAir(world, -1, 2, k14);
            this.setAir(world, 0, 2, k14);
            this.setBlockAndMetadata(world, -1, 3, k14, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, 0, 3, k14, this.brickStairBlock, 5);
        }
        for (int k14 : new int[]{3, 8}) {
            this.setAir(world, 4, 2, k14);
            this.setAir(world, 5, 2, k14);
            this.setBlockAndMetadata(world, 4, 3, k14, this.brickStairBlock, 4);
            this.setBlockAndMetadata(world, 5, 3, k14, this.brickStairBlock, 5);
        }
        this.setBlockAndMetadata(world, 3, 1, 2, this.brickStairBlock, 2);
        this.setGrassToDirt(world, 3, 0, 2);
        this.setBlockAndMetadata(world, 4, 1, 2, this.leafBlock, this.leafMeta);
        this.setBlockAndMetadata(world, 5, 1, 2, this.leafBlock, this.leafMeta);
        this.setBlockAndMetadata(world, 6, 1, 2, this.brickStairBlock, 2);
        this.setGrassToDirt(world, 6, 0, 2);
        this.setBlockAndMetadata(world, 8, 1, 4, this.brickStairBlock, 0);
        this.setGrassToDirt(world, 8, 0, 4);
        this.setBlockAndMetadata(world, 8, 1, 5, this.leafBlock, this.leafMeta);
        this.setBlockAndMetadata(world, 8, 1, 6, this.leafBlock, this.leafMeta);
        this.setBlockAndMetadata(world, 8, 1, 7, this.brickStairBlock, 0);
        this.setGrassToDirt(world, 8, 0, 7);
        this.setAir(world, 7, 2, 5);
        this.setAir(world, 7, 2, 6);
        this.setBlockAndMetadata(world, 7, 3, 5, this.brickStairBlock, 7);
        this.setBlockAndMetadata(world, 7, 3, 6, this.brickStairBlock, 6);
        for (int i16 : new int[]{-1, 0}) {
            this.setBlockAndMetadata(world, i16, 0, 0, this.floorBlock, this.floorMeta);
            this.setAir(world, i16, 1, 0);
        }
        for (i12 = -3; i12 <= 2; ++i12) {
            this.setBlockAndMetadata(world, i12, 4, -1, this.brickStairBlock, 6);
        }
        for (k12 = -1; k12 <= 2; ++k12) {
            this.setBlockAndMetadata(world, 3, 4, k12, this.brickStairBlock, 4);
            if (IntMath.mod((int)k12, (int)2) != 1) continue;
            this.setBlockAndMetadata(world, 3, 5, k12, this.brickSlabBlock, this.brickSlabMeta);
        }
        for (i12 = 4; i12 <= 8; ++i12) {
            this.setBlockAndMetadata(world, i12, 4, 2, this.brickStairBlock, 6);
            if (IntMath.mod((int)i12, (int)2) != 0) continue;
            this.setBlockAndMetadata(world, i12, 5, 2, this.brickSlabBlock, this.brickSlabMeta);
        }
        for (k12 = 3; k12 <= 8; ++k12) {
            this.setBlockAndMetadata(world, 8, 4, k12, this.brickStairBlock, 4);
        }
        for (i12 = 8; i12 >= -4; --i12) {
            this.setBlockAndMetadata(world, i12, 4, 9, this.brickStairBlock, 7);
            if (IntMath.mod((int)i12, (int)2) != 0) continue;
            this.setBlockAndMetadata(world, i12, 5, 9, this.brickSlabBlock, this.brickSlabMeta);
        }
        for (k12 = 8; k12 >= -1; --k12) {
            this.setBlockAndMetadata(world, -4, 4, k12, this.brickStairBlock, 5);
            if (IntMath.mod((int)k12, (int)2) != 1) continue;
            this.setBlockAndMetadata(world, -4, 5, k12, this.brickSlabBlock, this.brickSlabMeta);
        }
        for (k12 = 1; k12 <= 7; ++k12) {
            this.setBlockAndMetadata(world, -2, 4, k12, this.plankSlabBlock, this.plankSlabMeta | 8);
            if (k12 <= 3) {
                this.setBlockAndMetadata(world, 1, 4, k12, this.plankSlabBlock, this.plankSlabMeta | 8);
            }
            if (k12 < 4) continue;
            this.setBlockAndMetadata(world, 2, 4, k12, this.plankSlabBlock, this.plankSlabMeta | 8);
        }
        for (i12 = -2; i12 <= 6; ++i12) {
            this.setBlockAndMetadata(world, i12, 4, 7, this.plankSlabBlock, this.plankSlabMeta | 8);
            if (i12 <= 1) {
                this.setBlockAndMetadata(world, i12, 4, 3, this.plankSlabBlock, this.plankSlabMeta | 8);
            }
            if (i12 < 2) continue;
            this.setBlockAndMetadata(world, i12, 4, 4, this.plankSlabBlock, this.plankSlabMeta | 8);
        }
        for (k12 = 1; k12 <= 6; ++k12) {
            this.setBlockAndMetadata(world, -2, 5, k12, this.plankStairBlock, 4);
            if (k12 <= 5) {
                this.setBlockAndMetadata(world, -1, 6, k12, this.plankStairBlock, 4);
            }
            if (k12 <= 4) {
                this.setBlockAndMetadata(world, 0, 6, k12, this.plankStairBlock, 5);
            }
            if (k12 > 3) continue;
            this.setBlockAndMetadata(world, 1, 5, k12, this.plankStairBlock, 5);
        }
        for (i12 = -2; i12 <= 6; ++i12) {
            this.setBlockAndMetadata(world, i12, 5, 7, this.plankStairBlock, 6);
            if (i12 >= -1) {
                this.setBlockAndMetadata(world, i12, 6, 6, this.plankStairBlock, 6);
            }
            if (i12 >= 0) {
                this.setBlockAndMetadata(world, i12, 6, 5, this.plankStairBlock, 7);
            }
            if (i12 < 1) continue;
            this.setBlockAndMetadata(world, i12, 5, 4, this.plankStairBlock, 7);
        }
        this.setBlockAndMetadata(world, -2, 5, 0, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, -1, 5, 0, this.plankStairBlock, 4);
        this.setBlockAndMetadata(world, -1, 6, 0, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 0, 6, 0, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 0, 5, 0, this.plankStairBlock, 5);
        this.setBlockAndMetadata(world, 1, 5, 0, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 7, 5, 4, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 7, 5, 5, this.plankStairBlock, 7);
        this.setBlockAndMetadata(world, 7, 6, 5, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 7, 6, 6, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 7, 5, 6, this.plankStairBlock, 6);
        this.setBlockAndMetadata(world, 7, 5, 7, this.plankBlock, this.plankMeta);
        for (k12 = -1; k12 <= 7; ++k12) {
            this.setBlockAndMetadata(world, -3, 5, k12, this.clayStairBlock, 1);
            if (k12 <= 6) {
                this.setBlockAndMetadata(world, -2, 6, k12, this.clayStairBlock, 1);
            }
            if (k12 <= 5) {
                this.setBlockAndMetadata(world, -1, 7, k12, this.clayStairBlock, 1);
            }
            if (k12 <= 4) {
                this.setBlockAndMetadata(world, 0, 7, k12, this.clayStairBlock, 0);
            }
            if (k12 <= 3) {
                this.setBlockAndMetadata(world, 1, 6, k12, this.clayStairBlock, 0);
            }
            if (k12 > 2) continue;
            this.setBlockAndMetadata(world, 2, 5, k12, this.clayStairBlock, 0);
        }
        for (i12 = -3; i12 <= 8; ++i12) {
            this.setBlockAndMetadata(world, i12, 5, 8, this.clayStairBlock, 3);
            if (i12 >= -2) {
                this.setBlockAndMetadata(world, i12, 6, 7, this.clayStairBlock, 3);
            }
            if (i12 >= -1) {
                this.setBlockAndMetadata(world, i12, 7, 6, this.clayStairBlock, 3);
            }
            if (i12 >= 0) {
                this.setBlockAndMetadata(world, i12, 7, 5, this.clayStairBlock, 2);
            }
            if (i12 >= 1) {
                this.setBlockAndMetadata(world, i12, 6, 4, this.clayStairBlock, 2);
            }
            if (i12 < 2) continue;
            this.setBlockAndMetadata(world, i12, 5, 3, this.clayStairBlock, 2);
        }
        this.setBlockAndMetadata(world, -2, 5, -1, this.clayStairBlock, 4);
        this.setBlockAndMetadata(world, -1, 6, -1, this.clayStairBlock, 4);
        this.setBlockAndMetadata(world, 0, 6, -1, this.clayStairBlock, 5);
        this.setBlockAndMetadata(world, 1, 5, -1, this.clayStairBlock, 5);
        this.setBlockAndMetadata(world, 8, 5, 4, this.clayStairBlock, 7);
        this.setBlockAndMetadata(world, 8, 6, 5, this.clayStairBlock, 7);
        this.setBlockAndMetadata(world, 8, 6, 6, this.clayStairBlock, 6);
        this.setBlockAndMetadata(world, 8, 5, 7, this.clayStairBlock, 6);
        this.setBlockAndMetadata(world, -2, 3, 1, Blocks.torch, 3);
        this.setBlockAndMetadata(world, -2, 3, 4, Blocks.torch, 2);
        this.setBlockAndMetadata(world, -2, 3, 7, Blocks.torch, 4);
        this.setBlockAndMetadata(world, 6, 3, 7, Blocks.torch, 4);
        this.setBlockAndMetadata(world, 6, 3, 4, Blocks.torch, 3);
        this.setBlockAndMetadata(world, 1, 3, 1, Blocks.torch, 3);
        this.setBlockAndMetadata(world, -2, 1, 4, Blocks.crafting_table, 0);
        this.placeChest(world, random, -2, 1, 5, 5, LOTRChestContents.DORWINION_HOUSE);
        this.placeChest(world, random, -2, 1, 6, 5, LOTRChestContents.DORWINION_HOUSE);
        this.setBlockAndMetadata(world, -2, 1, 7, LOTRMod.dorwinionTable, 0);
        this.setBlockAndMetadata(world, -1, 1, 6, Blocks.bed, 0);
        this.setBlockAndMetadata(world, -1, 1, 7, Blocks.bed, 8);
        this.setBlockAndMetadata(world, 2, 1, 4, Blocks.furnace, 3);
        this.setBlockAndMetadata(world, 3, 1, 4, (Block)Blocks.cauldron, 3);
        this.setBlockAndMetadata(world, 4, 1, 4, this.plankStairBlock, 4);
        this.setBlockAndMetadata(world, 5, 1, 4, this.plankSlabBlock, this.plankSlabMeta | 8);
        this.setBlockAndMetadata(world, 6, 1, 4, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 6, 1, 5, this.plankSlabBlock, this.plankSlabMeta | 8);
        this.setBlockAndMetadata(world, 6, 1, 6, this.plankSlabBlock, this.plankSlabMeta | 8);
        this.setBlockAndMetadata(world, 6, 1, 7, this.plankBlock, this.plankMeta);
        this.setBlockAndMetadata(world, 5, 1, 7, this.plankSlabBlock, this.plankSlabMeta | 8);
        this.setBlockAndMetadata(world, 4, 1, 7, this.plankStairBlock, 4);
        int[] i17 = new int[]{4, 7};
        k2 = i17.length;
        for (j1 = 0; j1 < k2; ++j1) {
            int k14;
            k14 = i17[j1];
            for (int i18 = 4; i18 <= 5; ++i18) {
                this.placePlate(world, random, i18, 2, k14, this.plateBlock, LOTRFoods.DORWINION);
            }
            this.placeBarrel(world, random, 6, 2, k14, 5, new ItemStack(this.wineItem));
        }
        this.placeMug(world, random, 6, 2, 5, 1, new ItemStack(this.wineItem), LOTRFoods.DORWINION_DRINK);
        this.placeMug(world, random, 6, 2, 6, 1, new ItemStack(this.wineItem), LOTRFoods.DORWINION_DRINK);
        this.setBlockAndMetadata(world, 2, 0, 8, this.floorBlock, this.floorMeta);
        this.setBlockAndMetadata(world, 2, 1, 8, this.doorBlock, 3);
        this.setBlockAndMetadata(world, 2, 2, 8, this.doorBlock, 8);
        this.spawnItemFrame(world, 2, 3, 8, 2, new ItemStack(this.grapeItem));
        this.setBlockAndMetadata(world, 2, 3, 9, Blocks.torch, 3);
        for (i13 = -3; i13 <= 7; ++i13) {
            for (k1 = 9; k1 <= 19; ++k1) {
                if (i13 == -3 || i13 == 7 || k1 == 19) {
                    this.setGrassToDirt(world, i13, 0, k1);
                    this.setBlockAndMetadata(world, i13, 1, k1, this.wallBlock, this.wallMeta);
                    this.setBlockAndMetadata(world, i13, 2, k1, this.brickBlock, this.brickMeta);
                    if (IntMath.mod((int)(i13 + k1), (int)2) != 0) continue;
                    this.setBlockAndMetadata(world, i13, 3, k1, this.brickSlabBlock, this.brickSlabMeta);
                    continue;
                }
                this.setBlockAndMetadata(world, i13, 0, k1, LOTRMod.dirtPath, 0);
                if (IntMath.mod((int)i13, (int)2) != 1) continue;
                if (k1 == 14) {
                    this.setBlockAndMetadata(world, i13, 0, k1, Blocks.water, 0);
                    this.setBlockAndMetadata(world, i13, 1, k1, this.fenceBlock, this.fenceMeta);
                    this.setBlockAndMetadata(world, i13, 2, k1, Blocks.torch, 5);
                    continue;
                }
                if (k1 < 11 || k1 > 17) continue;
                this.setBlockAndMetadata(world, i13, 0, k1, Blocks.farmland, 7);
                this.setBlockAndMetadata(world, i13, 1, k1, this.grapevineBlock, 7);
                this.setBlockAndMetadata(world, i13, 2, k1, this.grapevineBlock, 7);
            }
        }
        for (i13 = 0; i13 <= 4; ++i13) {
            this.setBlockAndMetadata(world, i13, 3, 19, this.brickBlock, this.brickMeta);
        }
        for (i13 = 1; i13 <= 3; ++i13) {
            this.setBlockAndMetadata(world, i13, 4, 19, this.brickBlock, this.brickMeta);
        }
        this.setBlockAndMetadata(world, 0, 4, 19, this.brickStairBlock, 1);
        this.setBlockAndMetadata(world, 4, 4, 19, this.brickStairBlock, 0);
        this.setBlockAndMetadata(world, 1, 5, 19, this.brickSlabBlock, this.brickSlabMeta);
        this.setBlockAndMetadata(world, 3, 5, 19, this.brickSlabBlock, this.brickSlabMeta);
        for (int i16 : new int[]{-3, 4}) {
            this.setGrassToDirt(world, i16, 0, 20);
            this.setBlockAndMetadata(world, i16, 1, 20, this.brickStairBlock, 3);
            this.setBlockAndMetadata(world, i16 + 1, 1, 20, this.leafBlock, this.leafMeta);
            this.setBlockAndMetadata(world, i16 + 2, 1, 20, this.leafBlock, this.leafMeta);
            this.setGrassToDirt(world, i16 + 3, 0, 20);
            this.setBlockAndMetadata(world, i16 + 3, 1, 20, this.brickStairBlock, 3);
        }
        if (generateBackGate) {
            for (i14 = 1; i14 <= 3; ++i14) {
                this.setBlockAndMetadata(world, i14, 0, 19, LOTRMod.dirtPath, 0);
                for (j12 = 1; j12 <= 3; ++j12) {
                    this.setBlockAndMetadata(world, i14, j12, 19, LOTRMod.gateWooden, 2);
                }
            }
        } else {
            for (i14 = 1; i14 <= 3; ++i14) {
                this.setBlockAndMetadata(world, i14, 1, 20, this.leafBlock, this.leafMeta);
            }
        }
        LOTREntityDorwinionElf dorwinionElf = new LOTREntityDorwinionElf(world);
        this.spawnNPCAndSetHome(dorwinionElf, world, 0, 1, 5, 16);
        return true;
    }
}

