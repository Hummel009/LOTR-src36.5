package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTRNames;
import lotr.common.item.LOTRItemBanner;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class LOTRWorldGenHobbitHole extends LOTRWorldGenHobbitStructure {
  public LOTRWorldGenHobbitHole(boolean flag) {
    super(flag);
  }
  
  public boolean generateWithSetRotation(World world, Random random, int i, int j, int k, int rotation) {
    setOriginAndRotation(world, i, j, k, rotation, 17);
    setupRandomBlocks(random);
    int radius = 16;
    int height = 7;
    int extraRadius = 2;
    if (this.restrictions) {
      int minHeight = 0;
      int maxHeight = 0;
      for (int i36 = -radius; i36 <= radius; i36++) {
        for (int i37 = -radius; i37 <= radius; i37++) {
          if (i36 * i36 + i37 * i37 <= radius * radius) {
            int i38 = getTopBlock(world, i36, i37) - 1;
            if (!isSurface(world, i36, i38, i37))
              return false; 
            if (i38 < minHeight)
              minHeight = i38; 
            if (i38 > maxHeight)
              maxHeight = i38; 
          } 
        } 
      } 
      if (maxHeight - minHeight > 8)
        return false; 
    } 
    int i3;
    for (i3 = -radius; i3 <= radius; i3++) {
      for (int i36 = -radius; i36 <= radius; i36++) {
        for (int i37 = height; i37 >= 0; i37--) {
          int i38 = i3;
          int j2 = i37 + radius - height;
          int k2 = i36;
          if (i38 * i38 + j2 * j2 + k2 * k2 < (radius + extraRadius) * (radius + extraRadius)) {
            boolean bool = !isOpaque(world, i3, i37 + 1, i36);
            setBlockAndMetadata(world, i3, i37, i36, bool ? (Block)Blocks.grass : Blocks.dirt, 0);
            setGrassToDirt(world, i3, i37 - 1, i36);
          } 
        } 
      } 
    } 
    for (i3 = -radius; i3 <= radius; i3++) {
      for (int i36 = -radius; i36 <= radius; i36++) {
        if (i3 * i3 + i36 * i36 < radius * radius)
          for (int i37 = -1; !isOpaque(world, i3, i37, i36) && getY(i37) >= 0; i37--) {
            boolean bool = !isOpaque(world, i3, i37 + 1, i36);
            setBlockAndMetadata(world, i3, i37, i36, bool ? (Block)Blocks.grass : Blocks.dirt, 0);
            setGrassToDirt(world, i3, i37 - 1, i36);
          }  
      } 
    } 
    setGrassToDirt(world, 0, 7, 0);
    setBlockAndMetadata(world, 0, 8, 0, Blocks.brick_block, 0);
    setBlockAndMetadata(world, 0, 9, 0, Blocks.brick_block, 0);
    setBlockAndMetadata(world, 0, 10, 0, Blocks.flower_pot, 0);
    for (int i2 = -16; i2 <= -13; i2++) {
      for (int i36 = 1; i36 <= 4; i36++) {
        for (int i37 = -3; i37 <= 3; i37++)
          setAir(world, i37, i36, i2); 
      } 
    } 
    for (int j1 = 1; j1 <= 3; j1++) {
      for (int i36 = -2; i36 <= 2; i36++)
        setAir(world, i36, j1, -12); 
    } 
    int n;
    for (n = -17; n <= -13; n++) {
      for (int i36 = -5; i36 <= 5; i36++) {
        int i37;
        for (i37 = 0; i37 == 0 || (!isOpaque(world, i36, i37, n) && getY(i37) >= 0); i37--) {
          boolean bool = (i37 == 0);
          setBlockAndMetadata(world, i36, i37, n, bool ? (Block)Blocks.grass : Blocks.dirt, 0);
        } 
        for (i37 = 1; i37 <= 3; i37++)
          setAir(world, i36, i37, n); 
      } 
    } 
    for (n = -16; n <= -13; n++) {
      setBlockAndMetadata(world, 4, 1, n, this.outFenceBlock, this.outFenceMeta);
      setBlockAndMetadata(world, -4, 1, n, this.outFenceBlock, this.outFenceMeta);
      setBlockAndMetadata(world, 0, 0, n, this.pathBlock, this.pathMeta);
    } 
    int m;
    for (m = -1; m <= 1; m++) {
      setBlockAndMetadata(world, m, 0, -12, this.pathBlock, this.pathMeta);
      setBlockAndMetadata(world, m, 0, -11, this.pathBlock, this.pathMeta);
    } 
    for (m = -3; m <= 3; m++)
      setBlockAndMetadata(world, m, 1, -16, this.outFenceBlock, this.outFenceMeta); 
    setBlockAndMetadata(world, 0, 1, -16, this.outFenceGateBlock, 0);
    if (random.nextInt(5) == 0) {
      String[] signLines = LOTRNames.getHobbitSign(random);
      int[] signPos = { -3, -2, -1, 1, 2, 3 };
      int i36 = signPos[random.nextInt(signPos.length)];
      int signMeta = MathHelper.getRandomIntegerInRange(random, 6, 10) & 0xF;
      placeSign(world, i36, 2, -16, Blocks.standing_sign, signMeta, signLines);
    } 
    int k1;
    for (k1 = -15; k1 <= -13; k1++) {
      for (int i36 : new int[] { -1, 1 }) {
        int i37 = 1;
        plantFlower(world, random, i36, i37, k1);
      } 
    } 
    if (random.nextInt(3) == 0)
      for (k1 = -14; k1 <= -13; k1++) {
        for (int i36 : new int[] { -2, 2 })
          setBlockAndMetadata(world, i36, 1, k1, this.hedgeBlock, this.hedgeMeta); 
      }  
    for (int i1 = -2; i1 <= 2; i1++) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, i1, i36, -10, Blocks.brick_block, 0); 
    } 
    boolean gateFlip = random.nextBoolean();
    if (gateFlip) {
      for (int i36 = 0; i36 <= 1; i36++) {
        setBlockAndMetadata(world, i36, 0, -10, this.floorBlock, this.floorMeta);
        for (int i37 = 1; i37 <= 2; i37++) {
          setAir(world, i36, i37, -11);
          setBlockAndMetadata(world, i36, i37, -10, this.gateBlock, 2);
        } 
      } 
      setBlockAndMetadata(world, -2, 1, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -2, 2, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -2, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -2, 1, -12, this.plank2StairBlock, 2);
      setBlockAndMetadata(world, -2, 3, -12, this.plank2StairBlock, 6);
      setBlockAndMetadata(world, -1, 3, -12, this.plank2StairBlock, 4);
      setBlockAndMetadata(world, -1, 1, -11, this.plank2StairBlock, 0);
      setBlockAndMetadata(world, -1, 2, -11, this.plank2StairBlock, 4);
      setBlockAndMetadata(world, -1, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 0, 3, -11, this.plank2StairBlock, 4);
      setBlockAndMetadata(world, 0, 3, -12, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, 1, 3, -11, this.plank2StairBlock, 5);
      setBlockAndMetadata(world, 1, 3, -12, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, 2, 1, -11, this.plank2StairBlock, 1);
      setBlockAndMetadata(world, 2, 2, -11, this.plank2StairBlock, 5);
      setBlockAndMetadata(world, 2, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 2, 1, -12, this.plank2StairBlock, 2);
      setBlockAndMetadata(world, 2, 3, -12, this.plank2StairBlock, 6);
      placeSign(world, -2, 2, -12, Blocks.wall_sign, 2, new String[] { "", this.homeName1, this.homeName2, "" });
    } else {
      for (int i36 = -1; i36 <= 0; i36++) {
        setBlockAndMetadata(world, i36, 0, -10, this.floorBlock, this.floorMeta);
        for (int i37 = 1; i37 <= 2; i37++) {
          setAir(world, i36, i37, -11);
          setBlockAndMetadata(world, i36, i37, -10, this.gateBlock, 2);
        } 
      } 
      setBlockAndMetadata(world, 2, 1, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 2, 2, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 2, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 2, 1, -12, this.plank2StairBlock, 2);
      setBlockAndMetadata(world, 2, 3, -12, this.plank2StairBlock, 6);
      setBlockAndMetadata(world, 1, 3, -12, this.plank2StairBlock, 5);
      setBlockAndMetadata(world, 1, 1, -11, this.plank2StairBlock, 1);
      setBlockAndMetadata(world, 1, 2, -11, this.plank2StairBlock, 5);
      setBlockAndMetadata(world, 1, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 0, 3, -11, this.plank2StairBlock, 5);
      setBlockAndMetadata(world, 0, 3, -12, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, -1, 3, -11, this.plank2StairBlock, 4);
      setBlockAndMetadata(world, -1, 3, -12, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, -2, 1, -11, this.plank2StairBlock, 0);
      setBlockAndMetadata(world, -2, 2, -11, this.plank2StairBlock, 4);
      setBlockAndMetadata(world, -2, 3, -11, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -2, 1, -12, this.plank2StairBlock, 2);
      setBlockAndMetadata(world, -2, 3, -12, this.plank2StairBlock, 6);
      placeSign(world, 2, 2, -12, Blocks.wall_sign, 2, new String[] { "", this.homeName1, this.homeName2, "" });
    } 
    for (int i34 = 1; i34 <= 3; i34++) {
      setBlockAndMetadata(world, -3, i34, -12, LOTRMod.woodBeamV1, 0);
      setBlockAndMetadata(world, 3, i34, -12, LOTRMod.woodBeamV1, 0);
    } 
    for (int i33 = -3; i33 <= 3; i33++) {
      if (Math.abs(i33) <= 1) {
        setBlockAndMetadata(world, i33, 4, -13, LOTRMod.slabClayTileDyedSingle2, 5);
      } else {
        setBlockAndMetadata(world, i33, 3, -13, LOTRMod.slabClayTileDyedSingle2, 13);
      } 
    } 
    setBlockAndMetadata(world, -4, 3, -13, LOTRMod.slabClayTileDyedSingle2, 5);
    setBlockAndMetadata(world, 4, 3, -13, LOTRMod.slabClayTileDyedSingle2, 5);
    for (int i32 = -9; i32 <= 1; i32++) {
      for (int i37 = -2; i37 <= 2; i37++) {
        for (int i38 = 1; i38 <= 3; i38++)
          setAir(world, i37, i38, i32); 
      } 
      setBlockAndMetadata(world, 1, 0, i32, this.floorBlock, this.floorMeta);
      setBlockAndMetadata(world, 0, 0, i32, this.plankBlock, this.plankMeta);
      setBlockAndMetadata(world, -1, 0, i32, this.floorBlock, this.floorMeta);
      setBlockAndMetadata(world, 2, 1, i32, this.plank2StairBlock, 1);
      setBlockAndMetadata(world, -2, 1, i32, this.plank2StairBlock, 0);
      for (int i36 = 1; i36 <= 3; i36++) {
        setBlockAndMetadata(world, 3, i36, i32, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, -3, i36, i32, this.plank2Block, this.plank2Meta);
      } 
      setBlockAndMetadata(world, 2, 3, i32, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -2, 3, i32, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 1, 3, i32, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, -1, 3, i32, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, 0, 4, i32, this.plank2Block, this.plank2Meta);
    } 
    for (int i36 : new int[] { -8, -4, 0 })
      setBlockAndMetadata(world, 0, 3, i36, this.chandelierBlock, this.chandelierMeta); 
    for (int i31 = 1; i31 <= 3; i31++) {
      for (int i36 = -2; i36 <= 2; i36++)
        setBlockAndMetadata(world, i36, i31, 2, this.plank2Block, this.plank2Meta); 
    } 
    setBlockAndMetadata(world, 0, 0, 2, this.plankBlock, this.plankMeta);
    setAir(world, 0, 1, 2);
    setAir(world, 0, 2, 2);
    setBlockAndMetadata(world, -1, 1, 2, this.plank2StairBlock, 0);
    setBlockAndMetadata(world, 1, 1, 2, this.plank2StairBlock, 1);
    setBlockAndMetadata(world, -1, 2, 2, this.plank2StairBlock, 4);
    setBlockAndMetadata(world, 1, 2, 2, this.plank2StairBlock, 5);
    int i30;
    for (i30 = 3; i30 <= 9; i30++) {
      for (int i36 = -3; i36 <= 3; i36++) {
        for (int i37 = 1; i37 <= 3; i37++)
          setAir(world, i36, i37, i30); 
        setBlockAndMetadata(world, i36, 4, i30, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, i36, 0, i30, this.plankBlock, this.plankMeta);
      } 
    } 
    setBlockAndMetadata(world, 0, 3, 6, this.chandelierBlock, this.chandelierMeta);
    for (i30 = 5; i30 <= 7; i30++) {
      for (int i36 = -1; i36 <= 1; i36++)
        setBlockAndMetadata(world, i36, 1, i30, this.carpetBlock, this.carpetMeta); 
    } 
    if (this.isWealthy && random.nextBoolean())
      placeChest(world, random, 0, 0, 6, 2, LOTRChestContents.HOBBIT_HOLE_TREASURE); 
    int i29;
    for (i29 = -3; i29 <= 3; i29++) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, i29, i36, 10, this.plank2Block, this.plank2Meta); 
    } 
    for (i29 = -1; i29 <= 1; i29++) {
      setBlockAndMetadata(world, i29, 2, 10, LOTRMod.glassPane, 0);
      setBlockAndMetadata(world, i29, 3, 10, LOTRMod.glassPane, 0);
      for (int i36 = 11; i36 <= 14; i36++) {
        setBlockAndMetadata(world, i29, 1, i36, (Block)Blocks.grass, 0);
        for (int i37 = 2; i37 <= 3; i37++)
          setAir(world, i29, i37, i36); 
      } 
      setBlockAndMetadata(world, i29, 4, 10, this.plank2Block, this.plank2Meta);
    } 
    for (int i28 = 1; i28 <= 3; i28++) {
      setBlockAndMetadata(world, -3, i28, 3, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 3, i28, 3, this.plank2Block, this.plank2Meta);
    } 
    for (int i27 = -2; i27 <= 2; i27++)
      setBlockAndMetadata(world, i27, 3, 3, this.plank2SlabBlock, this.plank2SlabMeta | 0x8); 
    for (int i26 = 4; i26 <= 9; i26++) {
      setBlockAndMetadata(world, -3, 3, i26, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, 3, 3, i26, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    } 
    for (int i25 = -3; i25 <= 3; i25++)
      setBlockAndMetadata(world, i25, 1, 9, Blocks.oak_stairs, 2); 
    setBlockAndMetadata(world, -3, 1, 8, Blocks.oak_stairs, 0);
    setBlockAndMetadata(world, 3, 1, 8, Blocks.oak_stairs, 1);
    int i24;
    for (i24 = 4; i24 <= 9; i24++) {
      for (int i36 = 1; i36 <= 3; i36++) {
        setBlockAndMetadata(world, -4, i36, i24, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, 4, i36, i24, this.plank2Block, this.plank2Meta);
      } 
    } 
    setAir(world, -4, 1, 6);
    setAir(world, -4, 2, 6);
    setBlockAndMetadata(world, -4, 1, 5, this.plank2StairBlock, 3);
    setBlockAndMetadata(world, -4, 1, 7, this.plank2StairBlock, 2);
    setBlockAndMetadata(world, -4, 2, 5, this.plank2StairBlock, 7);
    setBlockAndMetadata(world, -4, 2, 7, this.plank2StairBlock, 6);
    setAir(world, 4, 1, 6);
    setAir(world, 4, 2, 6);
    setBlockAndMetadata(world, 4, 1, 5, this.plank2StairBlock, 3);
    setBlockAndMetadata(world, 4, 1, 7, this.plank2StairBlock, 2);
    setBlockAndMetadata(world, 4, 2, 5, this.plank2StairBlock, 7);
    setBlockAndMetadata(world, 4, 2, 7, this.plank2StairBlock, 6);
    setBlockAndMetadata(world, -3, 2, 4, Blocks.torch, 3);
    setBlockAndMetadata(world, 3, 2, 4, Blocks.torch, 3);
    setBlockAndMetadata(world, -3, 2, 9, Blocks.torch, 2);
    setBlockAndMetadata(world, 3, 2, 9, Blocks.torch, 1);
    setAir(world, 2, 1, -6);
    setBlockAndMetadata(world, 2, 0, -6, this.floorBlock, this.floorMeta);
    setBlockAndMetadata(world, 3, 0, -6, this.floorBlock, this.floorMeta);
    setAir(world, 3, 1, -6);
    setAir(world, 3, 2, -6);
    setBlockAndMetadata(world, 3, 1, -7, this.plank2StairBlock, 3);
    setBlockAndMetadata(world, 3, 1, -5, this.plank2StairBlock, 2);
    setBlockAndMetadata(world, 3, 2, -7, this.plank2StairBlock, 7);
    setBlockAndMetadata(world, 3, 2, -5, this.plank2StairBlock, 6);
    for (i24 = -8; i24 <= -3; i24++) {
      for (int i36 = 4; i36 <= 8; i36++) {
        if (i36 != 8 || i24 != -8) {
          for (int i37 = 1; i37 <= 3; i37++)
            setAir(world, i36, i37, i24); 
          setBlockAndMetadata(world, i36, 0, i24, this.floorBlock, this.floorMeta);
          if (i36 < 7 || i24 > -7)
            setBlockAndMetadata(world, i36, 4, i24, this.plank2Block, this.plank2Meta); 
        } 
      } 
    } 
    for (int i23 = 4; i23 <= 7; i23++) {
      for (int i36 = 1; i36 <= 3; i36++) {
        setBlockAndMetadata(world, i23, i36, -2, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, i23, i36, -8, this.plank2Block, this.plank2Meta);
      } 
      setBlockAndMetadata(world, i23, 3, -7, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, i23, 3, -3, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    } 
    for (int i22 = -7; i22 <= -3; i22++) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, 8, i36, i22, this.plank2Block, this.plank2Meta); 
    } 
    for (int i21 = 1; i21 <= 2; i21++) {
      for (int i37 = 5; i37 <= 6; i37++) {
        setAir(world, i37, i21, -8);
        setBlockAndMetadata(world, i37, i21, -9, Blocks.bookshelf, 0);
      } 
      for (int i36 = -6; i36 <= -4; i36++) {
        setAir(world, 8, i21, i36);
        setBlockAndMetadata(world, 9, i21, i36, Blocks.bookshelf, 0);
      } 
    } 
    setBlockAndMetadata(world, 6, 3, -5, this.chandelierBlock, this.chandelierMeta);
    setBlockAndMetadata(world, 5, 1, -5, Blocks.oak_stairs, 3);
    setBlockAndMetadata(world, 5, 1, -3, (Block)Blocks.wooden_slab, 8);
    placeChest(world, random, 7, 1, -3, 2, LOTRChestContents.HOBBIT_HOLE_STUDY);
    if (random.nextBoolean())
      placeWallBanner(world, 3, 3, -4, LOTRItemBanner.BannerType.HOBBIT, 1); 
    setAir(world, -2, 1, -6);
    setBlockAndMetadata(world, -2, 0, -6, this.floorBlock, this.floorMeta);
    setBlockAndMetadata(world, -3, 0, -6, this.floorBlock, this.floorMeta);
    setAir(world, -3, 1, -6);
    setAir(world, -3, 2, -6);
    setBlockAndMetadata(world, -3, 1, -7, this.plank2StairBlock, 3);
    setBlockAndMetadata(world, -3, 1, -5, this.plank2StairBlock, 2);
    setBlockAndMetadata(world, -3, 2, -7, this.plank2StairBlock, 7);
    setBlockAndMetadata(world, -3, 2, -5, this.plank2StairBlock, 6);
    for (int i20 = -7; i20 <= -4; i20++) {
      for (int i36 = -4; i36 >= -7; i36--) {
        setBlockAndMetadata(world, i36, 0, i20, this.floorBlock, this.floorMeta);
        for (int i37 = 1; i37 <= 3; i37++)
          setAir(world, i36, i37, i20); 
        setBlockAndMetadata(world, i36, 4, i20, this.plank2Block, this.plank2Meta);
      } 
    } 
    for (int i19 = -4; i19 >= -7; i19--) {
      for (int i36 = 1; i36 <= 3; i36++) {
        setBlockAndMetadata(world, i19, i36, -8, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, i19, i36, -3, this.plank2Block, this.plank2Meta);
      } 
      setBlockAndMetadata(world, i19, 3, -7, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, i19, 3, -4, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    } 
    int i18;
    for (i18 = -7; i18 <= -3; i18++) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, -8, i36, i18, this.plank2Block, this.plank2Meta); 
    } 
    for (i18 = -7; i18 <= -6; i18++) {
      for (int i36 = -5; i36 >= -6; i36--)
        setBlockAndMetadata(world, i36, 1, i18, this.carpetBlock, this.carpetMeta); 
    } 
    int i17;
    for (i17 = -5; i17 >= -6; i17--) {
      setBlockAndMetadata(world, i17, 0, -8, this.floorBlock, this.floorMeta);
      setBlockAndMetadata(world, i17, 1, -8, (Block)Blocks.wooden_slab, 8);
      setBlockAndMetadata(world, i17, 2, -8, Blocks.bookshelf, 0);
      setBlockAndMetadata(world, i17, 1, -9, this.plank2Block, this.plank2Meta);
    } 
    setBlockAndMetadata(world, -4, 1, -4, Blocks.planks, 0);
    setBlockAndMetadata(world, -7, 1, -4, Blocks.planks, 0);
    setBlockAndMetadata(world, -4, 2, -4, Blocks.torch, 5);
    setBlockAndMetadata(world, -7, 2, -4, Blocks.torch, 5);
    setBlockAndMetadata(world, -5, 1, -5, this.bedBlock, 0);
    setBlockAndMetadata(world, -5, 1, -4, this.bedBlock, 8);
    setBlockAndMetadata(world, -6, 1, -5, this.bedBlock, 0);
    setBlockAndMetadata(world, -6, 1, -4, this.bedBlock, 8);
    spawnItemFrame(world, -8, 2, -6, 1, new ItemStack(Items.clock));
    setBlockAndMetadata(world, 4, 0, 6, this.plankBlock, this.plankMeta);
    for (i17 = 5; i17 <= 6; i17++) {
      setBlockAndMetadata(world, i17, 0, 7, this.floorBlock, this.floorMeta);
      for (int i36 = 1; i36 <= 3; i36++)
        setAir(world, i17, i36, 7); 
      setBlockAndMetadata(world, i17, 4, 7, this.plank2Block, this.plank2Meta);
    } 
    for (i17 = 5; i17 <= 7; i17++) {
      setBlockAndMetadata(world, i17, 0, 6, this.floorBlock, this.floorMeta);
      for (int i36 = 1; i36 <= 3; i36++)
        setAir(world, i17, i36, 6); 
      setBlockAndMetadata(world, i17, 4, 6, this.plank2Block, this.plank2Meta);
    } 
    for (i17 = 5; i17 <= 8; i17++) {
      setBlockAndMetadata(world, i17, 0, 5, this.floorBlock, this.floorMeta);
      for (int i36 = 1; i36 <= 3; i36++)
        setAir(world, i17, i36, 5); 
      setBlockAndMetadata(world, i17, 4, 5, this.plank2Block, this.plank2Meta);
    } 
    for (int i16 = 1; i16 <= 3; i16++) {
      setBlockAndMetadata(world, 7, i16, 7, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 8, i16, 6, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, 9, i16, 5, this.plank2Block, this.plank2Meta);
    } 
    setBlockAndMetadata(world, 7, 2, 6, Blocks.torch, 4);
    setBlockAndMetadata(world, 8, 2, 5, Blocks.torch, 1);
    int i15;
    for (i15 = 4; i15 >= -1; i15--) {
      for (int i37 = 4; i37 <= 9; i37++) {
        setBlockAndMetadata(world, i37, 0, i15, this.floorBlock, this.floorMeta);
        for (int i38 = 1; i38 <= 3; i38++)
          setAir(world, i37, i38, i15); 
        setBlockAndMetadata(world, i37, 4, i15, this.plank2Block, this.plank2Meta);
      } 
      for (int i36 = 1; i36 <= 3; i36++) {
        setBlockAndMetadata(world, 3, i36, i15, this.plank2Block, this.plank2Meta);
        setBlockAndMetadata(world, 10, i36, i15, this.plank2Block, this.plank2Meta);
      } 
      setBlockAndMetadata(world, 4, 3, i15, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
      setBlockAndMetadata(world, 9, 3, i15, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    } 
    for (i15 = 2; i15 >= 0; i15--) {
      setBlockAndMetadata(world, 4, 1, i15, Blocks.oak_stairs, 0);
      setBlockAndMetadata(world, 9, 1, i15, Blocks.oak_stairs, 1);
    } 
    setBlockAndMetadata(world, 4, 1, -1, Blocks.oak_stairs, 3);
    setBlockAndMetadata(world, 4, 1, 3, Blocks.oak_stairs, 2);
    setBlockAndMetadata(world, 9, 1, -1, Blocks.oak_stairs, 3);
    setBlockAndMetadata(world, 9, 1, 3, Blocks.oak_stairs, 2);
    setBlockAndMetadata(world, 6, 3, 1, this.chandelierBlock, this.chandelierMeta);
    setBlockAndMetadata(world, 7, 3, 1, this.chandelierBlock, this.chandelierMeta);
    setBlockAndMetadata(world, 6, 1, 2, Blocks.planks, 1);
    setBlockAndMetadata(world, 7, 1, 2, Blocks.planks, 1);
    setBlockAndMetadata(world, 6, 1, 1, (Block)Blocks.wooden_slab, 9);
    setBlockAndMetadata(world, 7, 1, 1, (Block)Blocks.wooden_slab, 9);
    setBlockAndMetadata(world, 6, 1, 0, Blocks.planks, 1);
    setBlockAndMetadata(world, 7, 1, 0, Blocks.planks, 1);
    for (int i14 = 6; i14 <= 7; i14++) {
      for (int i36 = 2; i36 >= 0; i36--) {
        if (random.nextInt(3) == 0) {
          placeMug(world, random, i14, 2, i36, random.nextInt(4), LOTRFoods.HOBBIT_DRINK);
        } else if (random.nextBoolean()) {
          placePlateWithCertainty(world, random, i14, 2, i36, this.plateBlock, LOTRFoods.HOBBIT);
        } else {
          placePlate(world, random, i14, 2, i36, this.plateBlock, LOTRFoods.HOBBIT);
        } 
      } 
    } 
    setBlockAndMetadata(world, 5, 3, 7, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    setBlockAndMetadata(world, 6, 3, 7, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    setBlockAndMetadata(world, 7, 3, 6, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    setBlockAndMetadata(world, 8, 3, 5, this.plank2SlabBlock, this.plank2SlabMeta | 0x8);
    for (int i13 = 1; i13 <= 3; i13++) {
      int i36;
      for (i36 = 5; i36 <= 6; i36++)
        setBlockAndMetadata(world, i36, i13, 8, this.plank2Block, this.plank2Meta); 
      for (i36 = 8; i36 <= 9; i36++)
        setBlockAndMetadata(world, i36, i13, -2, this.plank2Block, this.plank2Meta); 
    } 
    setBlockAndMetadata(world, -4, 0, 6, this.plankBlock, this.plankMeta);
    int i12;
    for (i12 = 7; i12 >= 3; i12--) {
      for (int i36 = -5; i36 >= -7; i36--) {
        setBlockAndMetadata(world, i36, 0, i12, (Block)Blocks.double_stone_slab, 0);
        for (int i37 = 1; i37 <= 3; i37++)
          setAir(world, i36, i37, i12); 
        setBlockAndMetadata(world, i36, 4, i12, this.plank2Block, this.plank2Meta);
      } 
    } 
    for (i12 = 6; i12 >= 3; i12--) {
      for (int i36 = -5; i36 >= -6; i36--)
        setBlockAndMetadata(world, i36, 0, i12, this.floorBlock, this.floorMeta); 
    } 
    setBlockAndMetadata(world, -5, 1, 8, Blocks.crafting_table, 0);
    setBlockAndMetadata(world, -6, 1, 8, Blocks.crafting_table, 0);
    setBlockAndMetadata(world, -7, 1, 8, this.tableBlock, 0);
    for (int i11 = -7; i11 <= -5; i11++) {
      setAir(world, i11, 2, 8);
      setBlockAndMetadata(world, i11, 2, 9, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, i11, 3, 8, (Block)Blocks.double_stone_slab, 0);
    } 
    setBlockAndMetadata(world, -8, 1, 8, this.plank2Block, this.plank2Meta);
    setBlockAndMetadata(world, -8, 2, 8, this.plank2Block, this.plank2Meta);
    int i10;
    for (i10 = 6; i10 <= 7; i10++) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, -8, i36, i10, this.plank2Block, this.plank2Meta); 
    } 
    for (i10 = 3; i10 <= 5; i10++) {
      setBlockAndMetadata(world, -8, 0, i10, (Block)Blocks.double_stone_slab, 0);
      setAir(world, -8, 2, i10);
      setBlockAndMetadata(world, -9, 2, i10, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -8, 3, i10, (Block)Blocks.double_stone_slab, 0);
    } 
    setBlockAndMetadata(world, -8, 1, 4, this.plank2Block, this.plank2Meta);
    setBlockAndMetadata(world, -8, 1, 5, LOTRMod.hobbitOven, 4);
    setBlockAndMetadata(world, -8, 1, 3, LOTRMod.hobbitOven, 4);
    setBlockAndMetadata(world, -8, 1, 4, (Block)Blocks.cauldron, 3);
    setBlockAndMetadata(world, -6, 3, 5, this.chandelierBlock, this.chandelierMeta);
    for (int i9 = -4; i9 >= -9; i9--) {
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, i9, i36, 2, this.plank2Block, this.plank2Meta); 
    } 
    setBlockAndMetadata(world, -6, 0, 2, this.plankBlock, this.plankMeta);
    setAir(world, -6, 1, 2);
    setAir(world, -6, 2, 2);
    setBlockAndMetadata(world, -7, 1, 2, this.plank2StairBlock, 0);
    setBlockAndMetadata(world, -5, 1, 2, this.plank2StairBlock, 1);
    setBlockAndMetadata(world, -7, 2, 2, this.plank2StairBlock, 4);
    setBlockAndMetadata(world, -5, 2, 2, this.plank2StairBlock, 5);
    for (int i8 = -2; i8 <= 1; i8++) {
      for (int i37 = -9; i37 <= -4; i37++) {
        setBlockAndMetadata(world, i37, 0, i8, this.plankBlock, this.plankMeta);
        for (int i38 = 1; i38 <= 3; i38++)
          setAir(world, i37, i38, i8); 
        setBlockAndMetadata(world, i37, 4, i8, (Block)Blocks.double_stone_slab, 0);
      } 
      for (int i36 = 1; i36 <= 3; i36++)
        setBlockAndMetadata(world, -10, i36, i8, this.plank2Block, this.plank2Meta); 
    } 
    for (int i7 = -9; i7 <= -4; i7++) {
      setBlockAndMetadata(world, i7, 1, -2, (Block)Blocks.wooden_slab, 8);
      setBlockAndMetadata(world, i7, 3, -2, (Block)Blocks.wooden_slab, 0);
    } 
    int i6;
    for (i6 = -2; i6 <= 1; i6++) {
      setBlockAndMetadata(world, -4, 1, i6, (Block)Blocks.wooden_slab, 8);
      setBlockAndMetadata(world, -4, 3, i6, (Block)Blocks.wooden_slab, 0);
      setBlockAndMetadata(world, -9, 1, i6, (Block)Blocks.wooden_slab, 8);
      setBlockAndMetadata(world, -9, 3, i6, (Block)Blocks.wooden_slab, 0);
    } 
    setBlockAndMetadata(world, -8, 1, 1, (Block)Blocks.wooden_slab, 8);
    setBlockAndMetadata(world, -8, 3, 1, (Block)Blocks.wooden_slab, 0);
    setBlockAndMetadata(world, -6, 3, 1, Blocks.torch, 4);
    for (i6 = -2; i6 <= 1; i6++) {
      if (random.nextInt(3) != 0) {
        Block cakeBlock = LOTRWorldGenHobbitStructure.getRandomCakeBlock(random);
        setBlockAndMetadata(world, -4, 2, i6, cakeBlock, 0);
      } 
    } 
    for (int i5 = -7; i5 <= -6; i5++)
      placePlateWithCertainty(world, random, i5, 2, -2, this.plateBlock, LOTRFoods.HOBBIT); 
    placeBarrel(world, random, -5, 2, -2, 3, LOTRFoods.HOBBIT_DRINK);
    for (int i4 = 1; i4 <= 3; i4++) {
      setBlockAndMetadata(world, -9, i4, -3, this.plank2Block, this.plank2Meta);
      setBlockAndMetadata(world, -4, i4, 3, this.plank2Block, this.plank2Meta);
    } 
    placeChest(world, random, -8, 2, -2, (Block)Blocks.chest, 3, LOTRChestContents.HOBBIT_HOLE_LARDER);
    placeChest(world, random, -9, 2, -1, (Block)Blocks.chest, 4, LOTRChestContents.HOBBIT_HOLE_LARDER);
    placeChest(world, random, -9, 2, 0, (Block)Blocks.chest, 4, LOTRChestContents.HOBBIT_HOLE_LARDER);
    placeChest(world, random, -8, 2, 1, (Block)Blocks.chest, 2, LOTRChestContents.HOBBIT_HOLE_LARDER);
    if (gateFlip) {
      setBlockAndMetadata(world, -1, 2, -9, (Block)Blocks.tripwire_hook, 0);
    } else {
      setBlockAndMetadata(world, 1, 2, -9, (Block)Blocks.tripwire_hook, 0);
    } 
    int grassRadius = radius - 3;
    int grass = MathHelper.getRandomIntegerInRange(random, 80, 120);
    for (int l = 0; l < grass; l++) {
      int i36 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i37 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i38 = getTopBlock(world, i36, i37);
      plantTallGrass(world, random, i36, i38, i37);
    } 
    int flowers = MathHelper.getRandomIntegerInRange(random, 8, 16);
    for (int i35 = 0; i35 < flowers; i35++) {
      int i36 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i37 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i38 = getTopBlock(world, i36, i37);
      plantFlower(world, random, i36, i38, i37);
    } 
    if (random.nextInt(4) == 0) {
      int i36 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i37 = MathHelper.getRandomIntegerInRange(random, -grassRadius, grassRadius);
      int i38 = getTopBlock(world, i36, i37);
      WorldGenAbstractTree worldGenAbstractTree = LOTRBiome.shire.func_150567_a(random);
      worldGenAbstractTree.generate(world, random, getX(i36, i37), getY(i38), getZ(i36, i37));
    } 
    int homeRadius = MathHelper.floor_double(radius * 1.5D);
    spawnHobbitCouple(world, 0, 1, 0, homeRadius);
    return true;
  }
}
