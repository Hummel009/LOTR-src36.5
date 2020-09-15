/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.World
 */
package lotr.common.world.structure2;

import java.util.Random;

import lotr.common.entity.npc.LOTREntityNearHaradrimBase;
import lotr.common.entity.npc.LOTREntityUmbarArcher;
import lotr.common.entity.npc.LOTREntityUmbarWarrior;
import net.minecraft.world.World;

public class LOTRWorldGenUmbarBarracks
extends LOTRWorldGenSouthronBarracks {
    public LOTRWorldGenUmbarBarracks(boolean flag) {
        super(flag);
    }

    @Override
    protected boolean isUmbar() {
        return true;
    }

    @Override
    protected LOTREntityNearHaradrimBase createWarrior(World world, Random random) {
        return random.nextInt(3) == 0 ? new LOTREntityUmbarArcher(world) : new LOTREntityUmbarWarrior(world);
    }
}

