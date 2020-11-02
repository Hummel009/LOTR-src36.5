package lotr.common.entity.item;

import java.util.List;

import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import lotr.common.item.LOTRItemBarrel;
import lotr.common.world.biome.LOTRBiomeGenMirkwood;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityBarrel extends Entity {
	private boolean isBoatEmpty = true;
	private double speedMultiplier = minSpeedMultiplier;
	private static double minSpeedMultiplier = 0.04;
	private static double maxSpeedMultiplier = 0.25;
	private int boatPosRotationIncrements;
	private double boatX;
	private double boatY;
	private double boatZ;
	private double boatYaw;
	private double boatPitch;
	private double velocityX;
	private double velocityY;
	private double velocityZ;
	public NBTTagCompound barrelItemData;

	public LOTREntityBarrel(World world) {
		super(world);
		preventEntitySpawning = true;
		setSize(1.0f, 1.0f);
		yOffset = 0.0f;
	}

	public LOTREntityBarrel(World world, double d, double d1, double d2) {
		this(world);
		setPosition(d, d1 + yOffset, d2);
		motionX = 0.0;
		motionY = 0.0;
		motionZ = 0.0;
		prevPosX = d;
		prevPosY = d1;
		prevPosZ = d2;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(17, 0);
		dataWatcher.addObject(18, 1);
		dataWatcher.addObject(19, Float.valueOf(0.0f));
		dataWatcher.addObject(20, new ItemStack(LOTRMod.barrel));
	}

	public void setTimeSinceHit(int i) {
		dataWatcher.updateObject(17, i);
	}

	public int getTimeSinceHit() {
		return dataWatcher.getWatchableObjectInt(17);
	}

	public void setForwardDirection(int i) {
		dataWatcher.updateObject(18, i);
	}

	public int getForwardDirection() {
		return dataWatcher.getWatchableObjectInt(18);
	}

	public void setDamageTaken(float f) {
		dataWatcher.updateObject(19, Float.valueOf(f));
	}

	public float getDamageTaken() {
		return dataWatcher.getWatchableObjectFloat(19);
	}

	private void setBarrelItem(ItemStack itemstack) {
		dataWatcher.updateObject(20, itemstack);
	}

	private ItemStack getBarrelItem() {
		return dataWatcher.getWatchableObjectItemStack(20);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBox(Entity par1Entity) {
		return par1Entity.boundingBox;
	}

	@Override
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	public double getMountedYOffset() {
		return 0.5;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (isEntityInvulnerable()) {
			return false;
		}
		if (!worldObj.isRemote && !isDead) {
			boolean isCreative;
			setForwardDirection(-getForwardDirection());
			setTimeSinceHit(10);
			setDamageTaken(getDamageTaken() + f * 10.0f);
			Block.SoundType stepSound = LOTRMod.barrel.stepSound;
			playSound(stepSound.getBreakSound(), (stepSound.getVolume() + 1.0f) / 2.0f, stepSound.getPitch() * 0.8f);
			setBeenAttacked();
			isCreative = damagesource.getEntity() instanceof EntityPlayer && ((EntityPlayer) damagesource.getEntity()).capabilities.isCreativeMode;
			if (isCreative || getDamageTaken() > 40.0f) {
				if (riddenByEntity != null) {
					riddenByEntity.mountEntity(this);
				}
				if (!isCreative) {
					entityDropItem(getBarrelDrop(), 0.0f);
				}
				setDead();
			}
			return true;
		}
		return true;
	}

	private ItemStack getBarrelDrop() {
		ItemStack itemstack = new ItemStack(LOTRMod.barrel);
		if (barrelItemData != null) {
			LOTRItemBarrel.setBarrelData(itemstack, barrelItemData);
		}
		return itemstack;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public void performHurtAnimation() {
		setForwardDirection(-getForwardDirection());
		setTimeSinceHit(10);
		setDamageTaken(getDamageTaken() * 11.0f);
	}

	@Override
	public boolean canBeCollidedWith() {
		return !isDead;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public void setPositionAndRotation2(double d, double d1, double d2, float f, float f1, int i) {
		if (isBoatEmpty) {
			boatPosRotationIncrements = i + 5;
		} else {
			double d3 = d - posX;
			double d4 = d1 - posY;
			double d5 = d2 - posZ;
			double d6 = d3 * d3 + d4 * d4 + d5 * d5;
			if (d6 <= 1.0) {
				return;
			}
			boatPosRotationIncrements = 3;
		}
		boatX = d;
		boatY = d1;
		boatZ = d2;
		boatYaw = f;
		boatPitch = f1;
		motionX = velocityX;
		motionY = velocityY;
		motionZ = velocityZ;
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public void setVelocity(double par1, double par3, double par5) {
		velocityX = motionX = par1;
		velocityY = motionY = par3;
		velocityZ = motionZ = par5;
	}

	@Override
	public void onUpdate() {
		double d5;
		double d4;
		super.onUpdate();
		if (!worldObj.isRemote) {
			setBarrelItem(getBarrelDrop());
		}
		if (getTimeSinceHit() > 0) {
			setTimeSinceHit(getTimeSinceHit() - 1);
		}
		if (getDamageTaken() > 0.0f) {
			setDamageTaken(getDamageTaken() - 1.0f);
		}
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		int b0 = 5;
		double d0 = 0.0;
		for (int i2 = 0; i2 < b0; ++i2) {
			double d1 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (i2 + 0) / b0 - 0.125;
			double d2 = boundingBox.minY + (boundingBox.maxY - boundingBox.minY) * (i2 + 1) / b0 - 0.125;
			AxisAlignedBB axisalignedbb = AxisAlignedBB.getBoundingBox(boundingBox.minX, d1, boundingBox.minZ, boundingBox.maxX, d2, boundingBox.maxZ);
			if (!worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
				continue;
			}
			d0 += 1.0 / b0;
		}
		double d3 = Math.sqrt(motionX * motionX + motionZ * motionZ);
		if (d3 > 0.2625) {
			d4 = Math.cos(rotationYaw * 3.141592653589793 / 180.0);
			d5 = Math.sin(rotationYaw * 3.141592653589793 / 180.0);
			int j = 0;
			while (j < 1.0 + d3 * 60.0) {
				double d9;
				double d8;
				double d6 = rand.nextFloat() * 2.0f - 1.0f;
				double d7 = (rand.nextInt(2) * 2 - 1) * 0.7;
				if (rand.nextBoolean()) {
					d8 = posX - d4 * d6 * 0.8 + d5 * d7;
					d9 = posZ - d5 * d6 * 0.8 - d4 * d7;
					worldObj.spawnParticle("splash", d8, posY - 0.125, d9, motionX, motionY, motionZ);
				} else {
					d8 = posX + d4 + d5 * d6 * 0.7;
					d9 = posZ + d5 - d4 * d6 * 0.7;
					worldObj.spawnParticle("splash", d8, posY - 0.125, d9, motionX, motionY, motionZ);
				}
				++j;
			}
		}
		if (worldObj.isRemote && isBoatEmpty) {
			if (boatPosRotationIncrements > 0) {
				d4 = posX + (boatX - posX) / boatPosRotationIncrements;
				d5 = posY + (boatY - posY) / boatPosRotationIncrements;
				double d11 = posZ + (boatZ - posZ) / boatPosRotationIncrements;
				double d10 = MathHelper.wrapAngleTo180_double(boatYaw - rotationYaw);
				rotationYaw = (float) (rotationYaw + d10 / boatPosRotationIncrements);
				rotationPitch = (float) (rotationPitch + (boatPitch - rotationPitch) / boatPosRotationIncrements);
				--boatPosRotationIncrements;
				setPosition(d4, d5, d11);
				setRotation(rotationYaw, rotationPitch);
			} else {
				d4 = posX + motionX;
				d5 = posY + motionY;
				double d11 = posZ + motionZ;
				setPosition(d4, d5, d11);
				if (onGround) {
					motionX *= 0.5;
					motionY *= 0.5;
					motionZ *= 0.5;
				}
				motionX *= 0.99;
				motionY *= 0.95;
				motionZ *= 0.99;
			}
		} else {
			double d11;
			double d12;
			if (d0 < 1.0) {
				d4 = d0 * 2.0 - 1.0;
				motionY += 0.04 * d4;
			} else {
				if (motionY < 0.0) {
					motionY /= 2.0;
				}
				motionY += 0.007;
			}
			if (riddenByEntity != null && riddenByEntity instanceof EntityLivingBase && (d4 = ((EntityLivingBase) riddenByEntity).moveForward) > 0.0) {
				d5 = -Math.sin(riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
				d11 = Math.cos(riddenByEntity.rotationYaw * 3.1415927f / 180.0f);
				motionX += d5 * speedMultiplier * 0.05;
				motionZ += d11 * speedMultiplier * 0.05;
			}
			if ((d4 = Math.sqrt(motionX * motionX + motionZ * motionZ)) > maxSpeedMultiplier) {
				d5 = maxSpeedMultiplier / d4;
				motionX *= d5;
				motionZ *= d5;
				d4 = maxSpeedMultiplier;
			}
			if (d4 > d3 && speedMultiplier < maxSpeedMultiplier) {
				speedMultiplier += (maxSpeedMultiplier - speedMultiplier) / (maxSpeedMultiplier / 150.0);
				if (speedMultiplier > maxSpeedMultiplier) {
					speedMultiplier = maxSpeedMultiplier;
				}
			} else {
				speedMultiplier -= (speedMultiplier - minSpeedMultiplier) / (maxSpeedMultiplier / 150.0);
				if (speedMultiplier < minSpeedMultiplier) {
					speedMultiplier = minSpeedMultiplier;
				}
			}
			if (onGround) {
				motionX *= 0.5;
				motionY *= 0.5;
				motionZ *= 0.5;
			}
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.99;
			motionY *= 0.95;
			motionZ *= 0.99;
			rotationPitch = 0.0f;
			d5 = rotationYaw;
			d11 = prevPosX - posX;
			double d10 = prevPosZ - posZ;
			if (d11 * d11 + d10 * d10 > 0.001) {
				d5 = (float) (Math.atan2(d10, d11) * 180.0 / 3.141592653589793);
			}
			if ((d12 = MathHelper.wrapAngleTo180_double(d5 - rotationYaw)) > 20.0) {
				d12 = 20.0;
			}
			if (d12 < -20.0) {
				d12 = -20.0;
			}
			rotationYaw = (float) (rotationYaw + d12);
			setRotation(rotationYaw, rotationPitch);
			if (!worldObj.isRemote) {
				List nearbyEntities = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.2, 0.0, 0.2));
				if (nearbyEntities != null && !nearbyEntities.isEmpty()) {
					for (Object nearbyEntitie : nearbyEntities) {
						Entity entity = (Entity) nearbyEntitie;
						if (entity == riddenByEntity || !entity.canBePushed() || !(entity instanceof LOTREntityBarrel)) {
							continue;
						}
						entity.applyEntityCollision(this);
					}
				}
				if (riddenByEntity != null && riddenByEntity.isDead) {
					riddenByEntity = null;
				}
			}
		}
		if (!worldObj.isRemote && riddenByEntity instanceof EntityPlayer && worldObj.isAABBInMaterial(boundingBox, Material.water) && worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ)) instanceof LOTRBiomeGenMirkwood) {
			LOTRLevelData.getData((EntityPlayer) riddenByEntity).addAchievement(LOTRAchievement.rideBarrelMirkwood);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		if (barrelItemData != null) {
			nbt.setTag("BarrelItemData", barrelItemData);
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("BarrelItemData")) {
			barrelItemData = nbt.getCompoundTag("BarrelItemData");
		}
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public float getShadowSize() {
		return 0.0f;
	}

	@Override
	public boolean interactFirst(EntityPlayer entityplayer) {
		if (riddenByEntity != null && riddenByEntity instanceof EntityPlayer && riddenByEntity != entityplayer) {
			return true;
		}
		if (!worldObj.isRemote) {
			entityplayer.mountEntity(this);
		}
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return getBarrelItem();
	}
}
