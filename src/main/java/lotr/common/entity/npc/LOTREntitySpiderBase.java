package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.entity.ai.*;
import lotr.common.fac.LOTRAlignmentValues;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.*;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class LOTREntitySpiderBase extends LOTREntityNPCRideable {
	public static int VENOM_NONE = 0;
	public static int VENOM_SLOWNESS = 1;
	public static int VENOM_POISON = 2;

	public LOTREntitySpiderBase(World world) {
		super(world);
		setSize(1.4f, 0.8f);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4f));
		tasks.addTask(3, new LOTREntityAIAttackOnCollide(this, 1.2, false));
		tasks.addTask(4, new LOTREntityAIUntamedPanic(this, 1.2));
		tasks.addTask(5, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(6, new EntityAIWander(this, 1.0));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f, 0.02f));
		tasks.addTask(8, new EntityAILookIdle(this));
		this.addTargetTasks(true);
		spawnsInDarkness = true;
	}

	protected abstract int getRandomSpiderScale();

	protected abstract int getRandomSpiderType();

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, (byte) 0);
		dataWatcher.addObject(21, (byte) 0);
		dataWatcher.addObject(22, (byte) getRandomSpiderScale());
		setSpiderType(getRandomSpiderType());
		dataWatcher.addObject(23, (short) 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0 + getSpiderScale() * 6.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35 - getSpiderScale() * 0.03);
		getEntityAttribute(npcAttackDamage).setBaseValue(2.0 + getSpiderScale());
	}

	public boolean isSpiderClimbing() {
		return (dataWatcher.getWatchableObjectByte(20) & 1) != 0;
	}

	public void setSpiderClimbing(boolean flag) {
		byte b = dataWatcher.getWatchableObjectByte(20);
		b = flag ? (byte) (b | 1) : (byte) (b & 0xFFFFFFFE);
		dataWatcher.updateObject(20, b);
	}

	public int getSpiderType() {
		return dataWatcher.getWatchableObjectByte(21);
	}

	public void setSpiderType(int i) {
		dataWatcher.updateObject(21, (byte) i);
	}

	public int getSpiderScale() {
		return dataWatcher.getWatchableObjectByte(22);
	}

	public void setSpiderScale(int i) {
		dataWatcher.updateObject(22, (byte) i);
	}

	public float getSpiderScaleAmount() {
		return 0.5f + getSpiderScale() / 2.0f;
	}

	public int getSpiderClimbTime() {
		return dataWatcher.getWatchableObjectShort(23);
	}

	public void setSpiderClimbTime(int i) {
		dataWatcher.updateObject(23, (short) i);
	}

	public boolean shouldRenderClimbingMeter() {
		return !onGround && getSpiderClimbTime() > 0;
	}

	public float getClimbFractionRemaining() {
		float f = getSpiderClimbTime() / 100.0f;
		f = Math.min(f, 1.0f);
		f = 1.0f - f;
		return f;
	}

	@Override
	public boolean isMountSaddled() {
		return isNPCTamed() && riddenByEntity instanceof EntityPlayer;
	}

	@Override
	public boolean getBelongsToNPC() {
		return false;
	}

	@Override
	public void setBelongsToNPC(boolean flag) {
	}

	@Override
	public String getMountArmorTexture() {
		return null;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("SpiderType", (byte) getSpiderType());
		nbt.setByte("SpiderScale", (byte) getSpiderScale());
		nbt.setShort("SpiderRideTime", (short) getSpiderClimbTime());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setSpiderType(nbt.getByte("SpiderType"));
		setSpiderScale(nbt.getByte("SpiderScale"));
		getEntityAttribute(npcAttackDamage).setBaseValue(2.0 + getSpiderScale());
		setSpiderClimbTime(nbt.getShort("SpiderRideTime"));
	}

	@Override
	protected float getNPCScale() {
		return getSpiderScaleAmount();
	}

	@Override
	public float getRenderSizeModifier() {
		return getSpiderScaleAmount();
	}

	protected boolean canRideSpider() {
		return getSpiderScale() > 0;
	}

	@Override
	protected double getBaseMountedYOffset() {
		return height - 0.7;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote) {
			Entity rider = riddenByEntity;
			if (rider instanceof EntityPlayer && !onGround) {
				if (isCollidedHorizontally) {
					setSpiderClimbTime(getSpiderClimbTime() + 1);
				}
			} else {
				setSpiderClimbTime(0);
			}
			if (getSpiderClimbTime() >= 100) {
				setSpiderClimbing(false);
				if (onGround) {
					setSpiderClimbTime(0);
				}
			} else {
				setSpiderClimbing(isCollidedHorizontally);
			}
		}
		if (!worldObj.isRemote && riddenByEntity instanceof EntityPlayer && LOTRLevelData.getData((EntityPlayer) riddenByEntity).getAlignment(getFaction()) < 50.0f) {
			riddenByEntity.mountEntity(null);
		}
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		ItemStack itemstack = entityplayer.inventory.getCurrentItem();
		if (getSpiderType() == VENOM_POISON && itemstack != null && itemstack.getItem() == Items.glass_bottle) {
			--itemstack.stackSize;
			if (itemstack.stackSize <= 0) {
				entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, new ItemStack(LOTRMod.bottlePoison));
			} else if (!entityplayer.inventory.addItemStackToInventory(new ItemStack(LOTRMod.bottlePoison)) && !entityplayer.capabilities.isCreativeMode) {
				entityplayer.dropPlayerItemWithRandomChoice(new ItemStack(LOTRMod.bottlePoison), false);
			}
			return true;
		}
		if (worldObj.isRemote || hiredNPCInfo.isActive) {
			return false;
		}
		if (LOTRMountFunctions.interact(this, entityplayer)) {
			return true;
		}
		if (canRideSpider() && getAttackTarget() != entityplayer) {
			boolean hasRequiredAlignment = LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 50.0f;
			boolean notifyNotEnoughAlignment = false;
			if (!notifyNotEnoughAlignment && itemstack != null && LOTRMod.isOreNameEqual(itemstack, "bone") && isNPCTamed() && getHealth() < getMaxHealth()) {
				if (hasRequiredAlignment) {
					if (!entityplayer.capabilities.isCreativeMode) {
						--itemstack.stackSize;
						if (itemstack.stackSize == 0) {
							entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
						}
					}
					heal(4.0f);
					playSound(getLivingSound(), getSoundVolume(), getSoundPitch() * 1.5f);
					return true;
				}
				notifyNotEnoughAlignment = true;
			}
			if (!notifyNotEnoughAlignment && riddenByEntity == null) {
				if (itemstack != null && itemstack.interactWithEntity(entityplayer, this)) {
					return true;
				}
				if (hasRequiredAlignment) {
					entityplayer.mountEntity(this);
					this.setAttackTarget(null);
					getNavigator().clearPathEntity();
					return true;
				}
				notifyNotEnoughAlignment = true;
			}
			if (notifyNotEnoughAlignment) {
				LOTRAlignmentValues.notifyAlignmentNotHighEnough(entityplayer, 50.0f, getFaction());
				return true;
			}
		}
		return super.interact(entityplayer);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		if (super.attackEntityAsMob(entity)) {
			int difficulty;
			int duration;
			if (entity instanceof EntityLivingBase && (duration = (difficulty = worldObj.difficultySetting.getDifficultyId()) * (difficulty + 5) / 2) > 0) {
				if (getSpiderType() == VENOM_SLOWNESS) {
					((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, duration * 20, 0));
				} else if (getSpiderType() == VENOM_POISON) {
					((EntityLivingBase) entity).addPotionEffect(new PotionEffect(Potion.poison.id, duration * 20, 0));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		if (damagesource == DamageSource.fall) {
			return false;
		}
		return super.attackEntityFrom(damagesource, f);
	}

	@Override
	protected String getLivingSound() {
		return "mob.spider.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.spider.say";
	}

	@Override
	protected String getDeathSound() {
		return "mob.spider.death";
	}

	@Override
	protected void func_145780_a(int i, int j, int k, Block block) {
		playSound("mob.spider.step", 0.15f, 1.0f);
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int string = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int j = 0; j < string; ++j) {
			dropItem(Items.string, 1);
		}
		if (flag && (rand.nextInt(3) == 0 || rand.nextInt(1 + i) > 0)) {
			dropItem(Items.spider_eye, 1);
		}
	}

	@Override
	public boolean canDropRares() {
		return false;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		int i = getSpiderScale();
		return 2 + i + rand.nextInt(i + 2);
	}

	@Override
	public boolean isOnLadder() {
		return isSpiderClimbing();
	}

	@Override
	public void setInWeb() {
	}

	public void setInQuag() {
		super.setInWeb();
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect effect) {
		if (getSpiderType() == VENOM_SLOWNESS && effect.getPotionID() == Potion.moveSlowdown.id) {
			return false;
		}
		if (getSpiderType() == VENOM_POISON && effect.getPotionID() == Potion.poison.id) {
			return false;
		}
		return super.isPotionApplicable(effect);
	}

	@Override
	public boolean allowLeashing() {
		return isNPCTamed();
	}

	@Override
	public boolean canReEquipHired(int slot, ItemStack itemstack) {
		return false;
	}
}
