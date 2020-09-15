/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityCreature
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.entity.IRangedAttackMob
 *  net.minecraft.entity.SharedMonsterAttributes
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
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.ai.LOTREntityAIFollowHiringPlayer;
import lotr.common.entity.ai.LOTREntityAIHiredRemainStill;
import lotr.common.entity.ai.LOTREntityAIHobbitTargetRuffian;
import lotr.common.entity.ai.LOTREntityAIRangedAttack;
import lotr.common.entity.animal.LOTREntityShirePony;
import lotr.common.entity.projectile.LOTREntityPebble;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHobbitBounder
extends LOTREntityHobbit {
    public EntityAIBase rangedAttackAI = this.createHobbitRangedAttackAI();
    public EntityAIBase meleeAttackAI = this.createHobbitMeleeAttackAI();

    public LOTREntityHobbitBounder(World world) {
        super(world);
        this.tasks.taskEntries.clear();
        this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new LOTREntityAIHiredRemainStill(this));
        this.tasks.addTask(3, (EntityAIBase)new LOTREntityAIFollowHiringPlayer(this));
        this.tasks.addTask(4, (EntityAIBase)new EntityAIOpenDoor((EntityLiving)this, true));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0));
        this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, EntityPlayer.class, 8.0f, 0.02f));
        this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest2((EntityLiving)this, LOTREntityNPC.class, 5.0f, 0.02f));
        this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityLiving.class, 8.0f, 0.02f));
        this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
        int target = this.addTargetTasks(true);
        this.targetTasks.addTask(target + 1, (EntityAIBase)new LOTREntityAIHobbitTargetRuffian(this, LOTREntityBreeRuffian.class, 0, true));
        this.spawnRidingHorse = this.rand.nextInt(3) == 0;
    }

    public EntityAIBase createHobbitRangedAttackAI() {
        return new LOTREntityAIRangedAttack(this, 1.5, 20, 40, 12.0f);
    }

    public EntityAIBase createHobbitMeleeAttackAI() {
        return new LOTREntityAIAttackOnCollide(this, 1.5, false);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        this.getEntityAttribute(horseAttackSpeed).setBaseValue(2.0);
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        data = super.onSpawnWithEgg(data);
        int i = this.rand.nextInt(3);
        if (i == 0 || i == 1) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerIron));
        } else if (i == 2) {
            this.npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerBronze));
        }
        this.npcItemsInv.setRangedWeapon(new ItemStack(LOTRMod.sling));
        this.npcItemsInv.setIdleItem(null);
        ItemStack hat = new ItemStack(LOTRMod.leatherHat);
        LOTRItemLeatherHat.setHatColor(hat, 6834742);
        LOTRItemLeatherHat.setFeatherColor(hat, 16777215);
        this.setCurrentItemOrArmor(4, hat);
        return data;
    }

    @Override
    public LOTRNPCMount createMountToRide() {
        return new LOTREntityShirePony(this.worldObj);
    }

    @Override
    public void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
        if (mode == LOTREntityNPC.AttackMode.IDLE) {
            this.tasks.removeTask(this.meleeAttackAI);
            this.tasks.removeTask(this.rangedAttackAI);
            this.setCurrentItemOrArmor(0, this.npcItemsInv.getIdleItem());
        }
        if (mode == LOTREntityNPC.AttackMode.MELEE) {
            this.tasks.removeTask(this.meleeAttackAI);
            this.tasks.removeTask(this.rangedAttackAI);
            this.tasks.addTask(2, this.meleeAttackAI);
            this.setCurrentItemOrArmor(0, this.npcItemsInv.getMeleeWeapon());
        }
        if (mode == LOTREntityNPC.AttackMode.RANGED) {
            this.tasks.removeTask(this.meleeAttackAI);
            this.tasks.removeTask(this.rangedAttackAI);
            this.tasks.addTask(2, this.rangedAttackAI);
            this.setCurrentItemOrArmor(0, this.npcItemsInv.getRangedWeapon());
        }
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float f) {
        EntityArrow template = new EntityArrow(this.worldObj, (EntityLivingBase)this, target, 1.0f, 0.5f);
        LOTREntityPebble pebble = new LOTREntityPebble(this.worldObj, (EntityLivingBase)this).setSling();
        pebble.setLocationAndAngles(template.posX, template.posY, template.posZ, template.rotationYaw, template.rotationPitch);
        pebble.motionX = template.motionX;
        pebble.motionY = template.motionY;
        pebble.motionZ = template.motionZ;
        this.playSound("random.bow", 1.0f, 1.0f / (this.rand.nextFloat() * 0.4f + 0.8f));
        this.worldObj.spawnEntityInWorld((Entity)pebble);
    }

    @Override
    public float getAlignmentBonus() {
        return 2.0f;
    }

    @Override
    protected void dropFewItems(boolean flag, int i) {
        super.dropFewItems(flag, i);
        int dropChance = 10 - i * 2;
        if (dropChance < 1) {
            dropChance = 1;
        }
        if (this.rand.nextInt(dropChance) == 0) {
            this.dropItem(LOTRMod.pebble, 1 + this.rand.nextInt(3) + this.rand.nextInt(i + 1));
        }
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityplayer) {
        return 2 + this.rand.nextInt(3);
    }

    @Override
    public String getSpeechBank(EntityPlayer entityplayer) {
        if (this.isFriendly(entityplayer)) {
            if (this.hiredNPCInfo.getHiringPlayer() == entityplayer) {
                return "hobbit/bounder/hired";
            }
            return "hobbit/bounder/friendly";
        }
        return "hobbit/bounder/hostile";
    }
}

