/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.world.World
 */
package lotr.common.world.structure;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityElf;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTREntityRivendellElf;
import lotr.common.entity.npc.LOTREntityRivendellLord;
import lotr.common.item.LOTRItemBanner;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class LOTRWorldGenRivendellHall
extends LOTRWorldGenHighElvenHall {
    public LOTRWorldGenRivendellHall(boolean flag) {
        super(flag);
        this.tableBlock = LOTRMod.rivendellTable;
        this.bannerType = LOTRItemBanner.BannerType.RIVENDELL;
        this.chestContents = LOTRChestContents.RIVENDELL_HALL;
    }

    @Override
    protected LOTREntityElf createElf(World world) {
        return new LOTREntityRivendellElf(world);
    }

    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        if (super.generate(world, random, i, j, k)) {
            LOTREntityRivendellLord elfLord = new LOTREntityRivendellLord(world);
            elfLord.setLocationAndAngles((double)(i + 6), (double)(j + 6), (double)(k + 6), 0.0f, 0.0f);
            elfLord.spawnRidingHorse = false;
            ((LOTREntityNPC)elfLord).onSpawnWithEgg(null);
            elfLord.isNPCPersistent = true;
            world.spawnEntityInWorld((Entity)elfLord);
            elfLord.setHomeArea(i + 7, j + 3, k + 7, 16);
        }
        return false;
    }
}

