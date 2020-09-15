/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.attributes.IAttribute
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class LOTREntityRuffianBrute
extends LOTREntityBreeRuffian {
    public LOTREntityRuffianBrute(World world) {
        super(world);
    }

    @Override
    protected int addBreeAttackAI(int prio) {
        this.tasks.addTask(prio, (EntityAIBase)new LOTREntityAIAttackOnCollide(this, 1.5, false));
        return prio;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        data = super.onSpawnWithEgg(data);
        this.npcItemsInv.setIdleItem(this.npcItemsInv.getMeleeWeapon());
        return data;
    }

    @Override
    protected LOTRAchievement getKillAchievement() {
        return LOTRAchievement.killRuffianBrute;
    }

    @Override
    public LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.RUFFIAN_BRUTE.createQuest(this);
    }

    @Override
    public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
        return LOTRMiniQuestFactory.RUFFIAN_BRUTE;
    }
}

