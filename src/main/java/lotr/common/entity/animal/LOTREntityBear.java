package lotr.common.entity.animal;

import java.util.List;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.world.LOTRWorldChunkManager;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.biome.variant.LOTRBiomeVariant;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;

public class LOTREntityBear extends EntityAnimal implements LOTRAnimalSpawnConditions {
	private EntityAIBase attackAI = new LOTREntityAIAttackOnCollide(this, 1.7, false);
	private EntityAIBase panicAI = new EntityAIPanic(this, 1.5);
	private EntityAIBase targetNearAI = new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true);
	private int hostileTick = 0;
	private boolean prevIsChild = true;

	public LOTREntityBear(World world) {
		super(world);
		setSize(1.6f, 1.8f);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, panicAI);
		tasks.addTask(3, new EntityAIMate(this, 1.0));
		tasks.addTask(4, new EntityAITempt(this, 1.4, Items.fish, false));
		tasks.addTask(5, new EntityAIFollowParent(this, 1.4));
		tasks.addTask(6, new EntityAIWander(this, 1.0));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(1, targetNearAI);
	}

	@Override
	public void entityInit() {
		super.entityInit();
		dataWatcher.addObject(18, (byte) 0);
		dataWatcher.addObject(20, (byte) 0);
		setBearType(BearType.forID(rand.nextInt(BearType.values().length)));
	}

	public BearType getBearType() {
		byte i = dataWatcher.getWatchableObjectByte(18);
		return BearType.forID(i);
	}

	public void setBearType(BearType t) {
		dataWatcher.updateObject(18, (byte) t.bearID);
	}

	public boolean isHostile() {
		return dataWatcher.getWatchableObjectByte(20) == 1;
	}

	public void setHostile(boolean flag) {
		dataWatcher.updateObject(20, flag ? (byte) 1 : 0);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		if ((data = super.onSpawnWithEgg(data)) == null) {
			data = new BearGroupSpawnData();
			((BearGroupSpawnData) data).numSpawned = 1;
		} else if (data instanceof BearGroupSpawnData) {
			BearGroupSpawnData bgsd = (BearGroupSpawnData) data;
			if (bgsd.numSpawned >= 1 && rand.nextBoolean()) {
				setGrowingAge(-24000);
			}
			++bgsd.numSpawned;
		}
		if (rand.nextInt(10000) == 0) {
			setCustomNameTag("Wojtek");
		}
		return data;
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		boolean isChild;
		EntityLivingBase entity;
		if (!worldObj.isRemote && (isChild = isChild()) != prevIsChild) {
			if (isChild) {
				tasks.removeTask(attackAI);
				tasks.addTask(2, panicAI);
				targetTasks.removeTask(targetNearAI);
			} else {
				tasks.removeTask(panicAI);
				if (hostileTick > 0) {
					tasks.addTask(1, attackAI);
					targetTasks.addTask(1, targetNearAI);
				} else {
					tasks.removeTask(attackAI);
					targetTasks.removeTask(targetNearAI);
				}
			}
		}
		super.onLivingUpdate();
		if (!worldObj.isRemote && getAttackTarget() != null && (!(entity = getAttackTarget()).isEntityAlive() || entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
			setAttackTarget(null);
		}
		if (!worldObj.isRemote) {
			if (hostileTick > 0 && getAttackTarget() == null) {
				--hostileTick;
			}
			setHostile(hostileTick > 0);
			if (isHostile()) {
				resetInLove();
			}
		}
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int furs = 1 + rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < furs; ++l) {
			dropItem(LOTRMod.fur, 1);
		}
		if (flag) {
			int rugChance = 30 - i * 5;
			if (rand.nextInt(rugChance = Math.max(rugChance, 1)) == 0) {
				entityDropItem(new ItemStack(LOTRMod.bearRug, 1, getBearType().bearID), 0.0f);
			}
		}
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 2 + worldObj.rand.nextInt(3);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		float f = (float) getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		return entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entity) {
		LOTREntityBear mate = (LOTREntityBear) entity;
		LOTREntityBear child = new LOTREntityBear(worldObj);
		if (rand.nextBoolean()) {
			child.setBearType(getBearType());
		} else {
			child.setBearType(mate.getBearType());
		}
		return child;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		Entity attacker;
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && (attacker = damagesource.getEntity()) instanceof EntityLivingBase) {
			if (isChild()) {
				double range = 12.0;
				List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(range, range, range));
				for (Object obj : list) {
					LOTREntityBear bear;
					Entity entity = (Entity) obj;
					if (!(entity instanceof LOTREntityBear) || (bear = (LOTREntityBear) entity).isChild()) {
						continue;
					}
					bear.becomeAngryAt((EntityLivingBase) attacker);
				}
			} else {
				becomeAngryAt((EntityLivingBase) attacker);
			}
		}
		return flag;
	}

	private void becomeAngryAt(EntityLivingBase entity) {
		setAttackTarget(entity);
		hostileTick = 200;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("BearType", (byte) getBearType().bearID);
		nbt.setInteger("Angry", hostileTick);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		if (nbt.hasKey("BearType")) {
			setBearType(BearType.forID(nbt.getByte("BearType")));
		}
		hostileTick = nbt.getInteger("Angry");
	}

	@Override
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack.getItem() == Items.fish;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (isHostile()) {
			return false;
		}
		return super.interact(entityplayer);
	}

	@Override
	public boolean canWorldGenSpawnAt(int i, int j, int k, LOTRBiome biome, LOTRBiomeVariant variant) {
		int trees = biome.decorator.getVariantTreesPerChunk(variant);
		return trees >= 1;
	}

	@Override
	public boolean getCanSpawnHere() {
		WorldChunkManager worldChunkMgr = worldObj.getWorldChunkManager();
		if (worldChunkMgr instanceof LOTRWorldChunkManager) {
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			LOTRBiome biome = (LOTRBiome) worldObj.getBiomeGenForCoords(i, k);
			LOTRBiomeVariant variant = ((LOTRWorldChunkManager) worldChunkMgr).getBiomeVariantAt(i, k);
			return super.getCanSpawnHere() && canWorldGenSpawnAt(i, j, k, biome, variant);
		}
		return super.getCanSpawnHere();
	}

	@Override
	protected String getLivingSound() {
		return "lotr:bear.say";
	}

	@Override
	protected String getHurtSound() {
		return "lotr:bear.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "lotr:bear.death";
	}

	@Override
	public int getTalkInterval() {
		return 200;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
	}

	public enum BearType {
		LIGHT(0), DARK(1), BLACK(2);

		public final int bearID;

		private BearType(int i) {
			bearID = i;
		}

		public String textureName() {
			return name().toLowerCase();
		}

		public static BearType forID(int ID) {
			for (BearType t : BearType.values()) {
				if (t.bearID != ID) {
					continue;
				}
				return t;
			}
			return LIGHT;
		}

		public static String[] bearTypeNames() {
			String[] names = new String[BearType.values().length];
			for (int i = 0; i < names.length; ++i) {
				names[i] = BearType.values()[i].textureName();
			}
			return names;
		}
	}

	private static class BearGroupSpawnData implements IEntityLivingData {
		public int numSpawned = 0;

		private BearGroupSpawnData() {
		}
	}

}
