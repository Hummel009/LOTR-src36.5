package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityTermite extends EntityMob {
	private int fuseTime;
	private static int maxFuseTime = 20;
	public static float explosionSize = 2.0f;

	public LOTREntityTermite(World world) {
		super(world);
		setSize(0.4f, 0.4f);
		renderDistanceWeight = 2.0;
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.0, true));
		tasks.addTask(2, new EntityAIWander(this, 1.0));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, LOTREntityNPC.class, 0, true));
		experienceValue = 2;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!worldObj.isRemote) {
			EntityLivingBase target = getAttackTarget();
			if (target == null) {
				--fuseTime;
			} else {
				float dist = getDistanceToEntity(target);
				if (dist < 3.0f) {
					if (fuseTime == 0) {
						worldObj.playSoundAtEntity(this, "creeper.primed", 1.0f, 0.5f);
					}
					++fuseTime;
					if (fuseTime >= 20) {
						explode();
					}
				} else {
					--fuseTime;
				}
			}
			fuseTime = Math.min(Math.max(fuseTime, 0), maxFuseTime);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		return true;
	}

	private void explode() {
		if (!worldObj.isRemote) {
			worldObj.createExplosion(this, posX, posY, posZ, explosionSize, LOTRMod.canGrief(worldObj));
			setDead();
		}
	}

	@Override
	protected String getLivingSound() {
		return "mob.silverfish.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.silverfish.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.silverfish.kill";
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer) {
			dropItem(LOTRMod.termite, 1);
			setDead();
		}
	}

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
	}
}
