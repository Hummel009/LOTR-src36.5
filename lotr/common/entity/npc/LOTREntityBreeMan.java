/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.entity.ai.EntityAIAvoidEntity
 *  net.minecraft.entity.ai.EntityAIBase
 *  net.minecraft.entity.ai.EntityAILookIdle
 *  net.minecraft.entity.ai.EntityAIOpenDoor
 *  net.minecraft.entity.ai.EntityAISwimming
 *  net.minecraft.entity.ai.EntityAITasks
 *  net.minecraft.entity.ai.EntityAIWander
 *  net.minecraft.entity.ai.EntityAIWatchClosest
 *  net.minecraft.entity.ai.EntityAIWatchClosest2
 *  net.minecraft.entity.ai.attributes.IAttribute
 *  net.minecraft.entity.ai.attributes.IAttributeInstance
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.pathfinding.PathNavigate
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.biome.BiomeGenBase
 */
package lotr.common.entity.npc;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRFoods;
import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIBreeEat;
import lotr.common.entity.ai.LOTREntityAIDrink;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHobbitSmoke;
import lotr.common.fac.LOTRFaction;
import lotr.common.quest.IPickpocketable;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.world.biome.LOTRBiomeGenBreeland;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityBreeMan
extends LOTREntityMan
implements IPickpocketable {
    public static final String CARROT_EATER_NAME = "Peter Jackson";
    private static ItemStack[] weapons = new ItemStack[]{new ItemStack(LOTRMod.daggerIron), new ItemStack(LOTRMod.daggerBronze), new ItemStack(Items.iron_axe), new ItemStack(LOTRMod.axeBronze), new ItemStack(Items.stone_axe)};

    public LOTREntityBreeMan(World world) {
        super(world);
        this.setSize(0.6f, 1.8f);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new LOTREntityAIHiredRemainStill(this));
        int p = this.addBreeAttackAI(2);
        this.addBreeHiringAI(p + 1);
        this.tasks.addTask(p + 2, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
        this.addBreeAvoidAI(p + 3);
        this.tasks.addTask(p + 4, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(p + 5, (EntityAIBase)new LOTREntityAIBreeEat(this, LOTRFoods.BREE, 8000));
        this.tasks.addTask(p + 5, (EntityAIBase)new LOTREntityAIDrink(this, LOTRFoods.BREE_DRINK, 8000));
        this.tasks.addTask(p + 5, (EntityAIBase)new LOTREntityAIHobbitSmoke(this, 12000));
        this.tasks.addTask(p + 6, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 8.0f, 0.02f));
        this.tasks.addTask(p + 6, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, LOTREntityNPC.class, 5.0f, 0.02f));
        this.tasks.addTask(p + 7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0f, 0.02f));
        this.tasks.addTask(p + 8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        this.addTargetTasks(false);
    }

    protected int addBreeAttackAI(int prio) {
        this.tasks.addTask(prio, (EntityAIBase)new LOTREntityAIAttackOnCollide(this, 1.3, false));
        return prio;
    }

    protected void addBreeHiringAI(int prio) {
        this.tasks.addTask(prio, (EntityAIBase)new LOTREntityAIFollowHiringPlayer(this));
    }

    protected void addBreeAvoidAI(int prio) {
        this.tasks.addTask(prio, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, LOTREntityRuffianBrute.class, 8.0f, 1.0, 1.5));
    }

    @Override
    public void setupNPCGender() {
        this.familyInfo.setMale(this.rand.nextBoolean());
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName(LOTRNames.getBreeName(this.rand, this.familyInfo.isMale()));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        data = super.onSpawnWithEgg(data);
        int i = this.rand.nextInt(weapons.length);
        this.npcItemsInv.setMeleeWeapon(weapons[i].copy());
        this.npcItemsInv.setIdleItem(null);
        if (this.familyInfo.isMale() && this.rand.nextInt(2000) == 0) {
            this.familyInfo.setName(CARROT_EATER_NAME);
            this.npcItemsInv.setIdleItem(new ItemStack(Items.carrot));
        }
        return data;
    }

    @Override
    public LOTRFaction getFaction() {
        return LOTRFaction.BREE;
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
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
    protected void dropFewItems(boolean flag, int i) {
        super.dropFewItems(flag, i);
        int bones = this.rand.nextInt(2) + this.rand.nextInt(i + 1);
        for (int l = 0; l < bones; ++l) {
            this.dropItem(Items.bone, 1);
        }
        this.dropBreeItems(flag, i);
    }

    protected void dropBreeItems(boolean flag, int i) {
        if (this.rand.nextInt(6) == 0) {
            this.dropChestContents(LOTRChestContents.BREE_HOUSE, 1, 2 + i);
        }
    }

    @Override
    protected LOTRAchievement getKillAchievement() {
        return LOTRAchievement.killBreelander;
    }

    @Override
    public float getAlignmentBonus() {
        return 1.0f;
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
            if (j > 62 && this.worldObj.getBlock(i, j - 1, k) == this.worldObj.getBiomeGenForCoords((int)i, (int)k).topBlock) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float getBlockPathWeight(int i, int j, int k) {
        float f = 0.0f;
        BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(i, k);
        if (biome instanceof LOTRBiomeGenBreeland) {
            f += 20.0f;
        }
        return f;
    }

    @Override
    public String getSpeechBank(EntityPlayer entityplayer) {
        if (this.isFriendly(entityplayer)) {
            return "bree/man/friendly";
        }
        return "bree/man/hostile";
    }

    @Override
    public LOTRMiniQuest createMiniQuest() {
        return LOTRMiniQuestFactory.BREE.createQuest(this);
    }

    @Override
    public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
        return LOTRMiniQuestFactory.BREE;
    }

    @Override
    public boolean canPickpocket() {
        return true;
    }

    @Override
    public ItemStack createPickpocketItem() {
        return LOTRChestContents.BREE_PICKPOCKET.getOneItem(this.rand, true);
    }
}

