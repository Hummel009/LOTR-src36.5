package lotr.common.entity.animal;

import java.util.List;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityRhino extends LOTREntityHorse {
	public LOTREntityRhino(World world) {
		super(world);
		setSize(1.7f, 1.9f);
	}

	@Override
	protected boolean isMountHostile() {
		return true;
	}

	@Override
	protected EntityAIBase createMountAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.0, true);
	}

	@Override
	public int getHorseType() {
		return 0;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
	}

	@Override
	protected void onLOTRHorseSpawn() {
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		maxHealth *= 1.5;
		maxHealth = Math.max(maxHealth, 40.0);
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed *= 1.2);
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength *= 0.5);
	}

	@Override
	protected double clampChildHealth(double health) {
		return MathHelper.clamp_double(health, 20.0, 50.0);
	}

	@Override
	protected double clampChildJump(double jump) {
		return MathHelper.clamp_double(jump, 0.2, 0.8);
	}

	@Override
	protected double clampChildSpeed(double speed) {
		return MathHelper.clamp_double(speed, 0.12, 0.42);
	}

	@Override
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == Items.wheat;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote) {
			if (riddenByEntity instanceof EntityLivingBase) {
				EntityLivingBase rhinoRider = (EntityLivingBase) riddenByEntity;
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				if (momentum > 0.2f) {
					setSprinting(true);
				} else {
					setSprinting(false);
				}
				if (momentum >= 0.32f) {
					float strength = momentum * 15.0f;
					Vec3.createVectorHelper(posX, posY, posZ);
					Vec3 look = getLookVec();
					float sightWidth = 1.0f;
					double range = 0.5;
					List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.contract(1.0, 1.0, 1.0).addCoord(look.xCoord * range, look.yCoord * range, look.zCoord * range).expand(sightWidth, sightWidth, sightWidth));
					boolean hitAnyEntities = false;
					for (Object element : list) {
						EntityLiving entityliving;
						EntityLivingBase entity;
						Entity obj = (Entity) element;
						if (!(obj instanceof EntityLivingBase) || (entity = (EntityLivingBase) obj) == rhinoRider || rhinoRider instanceof EntityPlayer && !LOTRMod.canPlayerAttackEntity((EntityPlayer) rhinoRider, entity, false) || rhinoRider instanceof EntityCreature && !LOTRMod.canNPCAttackEntity((EntityCreature) rhinoRider, entity, false) || !(entity.attackEntityFrom(DamageSource.causeMobDamage(this), strength))) {
							continue;
						}
						float knockback = strength * 0.05f;
						entity.addVelocity(-MathHelper.sin(rotationYaw * 3.1415927f / 180.0f) * knockback, knockback, MathHelper.cos(rotationYaw * 3.1415927f / 180.0f) * knockback);
						hitAnyEntities = true;
						if (!(entity instanceof EntityLiving) || (entityliving = (EntityLiving) entity).getAttackTarget() != this) {
							continue;
						}
						entityliving.getNavigator().clearPathEntity();
						entityliving.setAttackTarget(rhinoRider);
					}
					if (hitAnyEntities) {
						worldObj.playSoundAtEntity(this, "lotr:troll.ologHai_hammer", 1.0f, (rand.nextFloat() - rand.nextFloat()) * 0.2f + 1.0f);
					}
				}
			} else if (getAttackTarget() != null) {
				float momentum = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
				if (momentum > 0.2f) {
					setSprinting(true);
				} else {
					setSprinting(false);
				}
			} else {
				setSprinting(false);
			}
		}
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int j = rand.nextInt(2) + rand.nextInt(1 + i);
		for (int k = 0; k < j; ++k) {
			dropItem(LOTRMod.rhinoHorn, 1);
		}
		int meat = rand.nextInt(3) + rand.nextInt(1 + i);
		for (int l = 0; l < meat; ++l) {
			if (isBurning()) {
				dropItem(LOTRMod.rhinoCooked, 1);
				continue;
			}
			dropItem(LOTRMod.rhinoRaw, 1);
		}
	}

	@Override
	protected String getLivingSound() {
		return "lotr:rhino.say";
	}

	@Override
	protected String getHurtSound() {
		return "lotr:rhino.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "lotr:rhino.death";
	}

	@Override
	protected String getAngrySoundName() {
		return "lotr:rhino.say";
	}
}
