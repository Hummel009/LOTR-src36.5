/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAIAvoidEntity
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAIOpenDoor
 *  net.minecraft.entity.ai.EntityAIPanic
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWander
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.ai.EntityAIWatchClosest2
 *  net.minecraft.entity.ai.attributes.IAttribute
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.pathfinding.PathNavigate
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.biome.BiomeGenBase
 */
package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFoods;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAvoidHuorn;
import lotr.common.entity.ai.LOTREntityAIDrink;
import lotr.common.entity.ai.LOTREntityAIEat;
import lotr.common.entity.ai.LOTREntityAIHobbitChildFollowGoodPlayer;
import lotr.common.entity.ai.LOTREntityAIHobbitSmoke;
import lotr.common.entity.ai.LOTREntityAINPCAvoidEvilPlayer;
import lotr.common.entity.ai.LOTREntityAINPCFollowParent;
import lotr.common.entity.ai.LOTREntityAINPCFollowSpouse;
import lotr.common.entity.ai.LOTREntityAINPCMarry;
import lotr.common.entity.ai.LOTREntityAINPCMate;
import lotr.common.fac.LOTRFaction;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.world.biome.LOTRBiomeGenShire;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityHobbit
extends LOTREntityMan {
    public LOTREntityHobbit(World world) {
        super(world);
        this.setSize(0.45f, 1.2f);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntityOrc.class, 12.0f, 1.5, 1.8));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntityWarg.class, 12.0f, 1.5, 1.8));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntityTroll.class, 12.0f, 1.5, 1.8));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntitySpiderBase.class, 12.0f, 1.5, 1.8));
        this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntityRuffianBrute.class, 8.0f, 1.0, 1.5));
        this.tasks.addTask(1, (EntityAIBase)new LOTREntityAIAvoidHuorn(this, 12.0f, 1.5, 1.8));
        this.tasks.addTask(2, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.6));
        this.tasks.addTask(3, (EntityAIBase)new LOTREntityAINPCAvoidEvilPlayer(this, 8.0f, 1.5, 1.8));
        this.tasks.addTask(4, (EntityAIBase)new LOTREntityAIHobbitChildFollowGoodPlayer(this, 12.0f, 1.5));
        this.tasks.addTask(5, (EntityAIBase)new LOTREntityAINPCMarry(this, 1.3));
        this.tasks.addTask(6, (EntityAIBase)new LOTREntityAINPCMate(this, 1.3));
        this.tasks.addTask(7, (EntityAIBase)new LOTREntityAINPCFollowParent(this, 1.4));
        this.tasks.addTask(8, (EntityAIBase)new LOTREntityAINPCFollowSpouse(this, 1.1));
        this.tasks.addTask(9, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
        this.tasks.addTask(10, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.1));
        this.tasks.addTask(11, (EntityAIBase)new LOTREntityAIEat(this, this.getHobbitFoods(), 3000));
        this.tasks.addTask(11, (EntityAIBase)new LOTREntityAIDrink(this, this.getHobbitDrinks(), 3000));
        this.tasks.addTask(11, (EntityAIBase)new LOTREntityAIHobbitSmoke(this, 4000));
        this.tasks.addTask(12, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 8.0f, 0.05f));
        this.tasks.addTask(12, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, LOTREntityNPC.class, 5.0f, 0.05f));
        this.tasks.addTask(13, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0f, 0.02f));
        this.tasks.addTask(14, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.familyInfo.marriageEntityClass = LOTREntityHobbit.class;
        this.familyInfo.marriageRing = LOTRMod.hobbitRing;
        this.familyInfo.marriageAlignmentRequired = 100.0f;
        this.familyInfo.marriageAchievement = LOTRAchievement.marryHobbit;
        this.familyInfo.potentialMaxChildren = 4;
        this.familyInfo.timeToMature = 48000;
        this.familyInfo.breedingDelay = 24000;
    }

    protected LOTRFoods getHobbitFoods() {
        return LOTRFoods.HOBBIT;
    }

    protected LOTRFoods getHobbitDrinks() {
        return LOTRFoods.HOBBIT_DRINK;
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(this.rand.nextBoolean());
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getHobbitName(this.rand, this.familyInfo.isMale()));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
    }

    @Override
    protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
        if (mode == LOTREntityNPC.AttackMode.IDLE) {
            this.setCurrentItemOrArmor(0, this.npcItemsInv.getIdleItem());
        } else {
            this.setCurrentItemOrArmor(0, this.npcItemsInv.getMeleeWeapon());
        }
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.HOBBIT;
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public void changeNPCNameForMarriage(LOTREntityNPC spouse) {
        if (this.familyInfo.isMale()) {
            LOTRNames.changeHobbitSurnameForMarriage(this, (LOTREntityHobbit)spouse);
        } else if (spouse.familyInfo.isMale()) {
            LOTRNames.changeHobbitSurnameForMarriage((LOTREntityHobbit)spouse, this);
        }
    }

    @Override
    public void createNPCChildName(LOTREntityNPC maleParent, LOTREntityNPC femaleParent) {
        this.familyInfo.setName(LOTRNames.getHobbitChildNameForParent(this.rand, this.familyInfo.isMale(), (LOTREntityHobbit)maleParent));
    }

    @Override
    public boolean interact(EntityPlayer entityplayer) {
        if (this.familyInfo.interact(entityplayer)) {
            return true;
        }
        return super.interact(entityplayer);
    }

    @Override
    public boolean speakTo(EntityPlayer entityplayer) {
        boolean flag = super.speakTo(entityplayer);
        if (flag && this.isDrunkard() && entityplayer.isPotionActive(Potion.confusion.id)) {
            LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.speakToDrunkard);
        }
        return flag;
    }

    @Override
    protected LOTRAchievement getKillAchievement() {
        return LOTRAchievement.killHobbit;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
    }

    @Override
    protected void dropFewItems(boolean flag, int i) {
        super.dropFewItems(flag, i);
        int bones = this.rand.nextInt(2) + this.rand.nextInt(i + 1);
        for (int l = 0; l < bones; ++l) {
            this.dropItem(LOTRMod.hobbitBone, 1);
        }
        this.dropHobbitItems(flag, i);
    }

    protected void dropHobbitItems(boolean flag, int i) {
        if (this.rand.nextInt(8) == 0) {
            this.dropChestContents(LOTRChestContents.HOBBIT_HOLE_STUDY, 1, 1 + i);
        }
        if (this.rand.nextInt(4) == 0) {
            this.dropChestContents(LOTRChestContents.HOBBIT_HOLE_LARDER, 1, 2 + i);
        }
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityplayer) {
        return 1 + this.rand.nextInt(3);
    }

    @Override
    public boolean getCanSpawnHere() {
        if (super.getCanSpawnHere()) {
            if (this.liftSpawnRestrictions) {
                return true;
            }
            int i = MathHelper.floor_double((double)this.posX);
            int j = MathHelper.floor_double((double)this.boundingBox.minY);
            int k = MathHelper.floor_double((double)this.posZ);
            return j > 62 && this.worldObj.getBlock(i, j - 1, k) == this.worldObj.getBiomeGenForCoords((int)i, (int)k).topBlock;
        }
        return false;
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        float f = 0.0f;
        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(i, k);
        if (biome instanceof LOTRBiomeGenShire) {
            f += 20.0f;
        }
        return f;
    }

    @Override
    public String getSpeechBank(EntityPlayer entityplayer) {
        if (this.isDrunkard()) {
            return "hobbit/drunkard/neutral";
        }
        if (this.isFriendly(entityplayer)) {
            return this.isChild() ? "hobbit/child/friendly" : "hobbit/hobbit/friendly";
        }
        return this.isChild() ? "hobbit/child/hostile" : "hobbit/hobbit/hostile";
    }

    @Override
    public LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.HOBBIT.createQuest(this);
    }

    @Override
    public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
        return LOTRMiniQuestFactory.HOBBIT;
    }

    @Override
    public void onArtificalSpawn() {
        if (this.getClass() == this.familyInfo.marriageEntityClass && this.rand.nextInt(10) == 0) {
            this.familyInfo.setChild();
        }
    }
}

