/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.command.IEntitySelector
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.biome.BiomeGenBase
 */
package lotr.common.entity.ai;

import lotr.common.world.biome.LOTRBiomeGenShire;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityAIHobbitTargetRuffian
extends LOTREntityAINearestAttackableTargetBasic {
    public LOTREntityAIHobbitTargetRuffian(EntityCreature entity, Class targetClass, int chance, boolean flag) {
        super(entity, targetClass, chance, flag);
    }

    public LOTREntityAIHobbitTargetRuffian(EntityCreature entity, Class targetClass, int chance, boolean flag, IEntitySelector selector) {
        super(entity, targetClass, chance, flag, selector);
    }

    @Override
    protected boolean isSuitableTarget(EntityLivingBase entity, boolean flag) {
        BiomeGenBase biome;
        return super.isSuitableTarget(entity, flag) && (biome = this.taskOwner.worldObj.getBiomeGenForCoords(MathHelper.floor_double((double)this.taskOwner.posX), MathHelper.floor_double((double)this.taskOwner.posZ))) instanceof LOTRBiomeGenShire;
    }
}

