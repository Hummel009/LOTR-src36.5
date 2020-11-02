package lotr.common.entity.animal;

import java.util.UUID;

import lotr.common.LOTRMod;
import lotr.common.entity.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityFish extends EntityWaterMob implements LOTRRandomSkinEntity {
	private ChunkCoordinates currentSwimTarget;
	private int swimTargetTime = 0;

	public LOTREntityFish(World world) {
		super(world);
		setSize(0.5f, 0.5f);
	}

	@Override
	public void setUniqueID(UUID uuid) {
		entityUniqueID = uuid;
	}

	@Override
	public void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, (byte) 0);
	}

	public FishType getFishType() {
		byte i = dataWatcher.getWatchableObjectByte(16);
		if (i < 0 || i >= FishType.values().length) {
			i = 0;
		}
		return FishType.values()[i];
	}

	public void setFishType(FishType type) {
		this.setFishType(type.ordinal());
	}

	public void setFishType(int i) {
		dataWatcher.updateObject(16, (byte) i);
	}

	public String getFishTextureDir() {
		return getFishType().textureDir;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MathHelper.getRandomDoubleInRange(rand, 0.04, 0.08));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = MathHelper.floor_double(posX);
		MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);
		worldObj.getBiomeGenForCoords(i, k);
		if (rand.nextInt(30) == 0) {
			this.setFishType(FishType.CLOWNFISH);
		} else if (rand.nextInt(8) == 0) {
			this.setFishType(FishType.SALMON);
		} else {
			this.setFishType(FishType.COMMON);
		}
		return data;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.setFishType(nbt.getInteger("FishType"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("FishType", getFishType().ordinal());
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int drops = rand.nextInt(2 + i);
		for (int l = 0; l < drops; ++l) {
			if (getFishType() == FishType.SALMON) {
				entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.func_150976_a()), 0.0f);
				continue;
			}
			if (getFishType() == FishType.CLOWNFISH) {
				entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.func_150976_a()), 0.0f);
				continue;
			}
			entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.func_150976_a()), 0.0f);
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!isInWater() && !worldObj.isRemote) {
			motionX = 0.0;
			motionY -= 0.08;
			motionY *= 0.98;
			motionZ = 0.0;
		}
	}

	@Override
	public boolean isInWater() {
		double d = 0.5;
		return worldObj.isMaterialInBB(boundingBox.expand(d, d, d), Material.water);
	}

	@Override
	protected void updateEntityActionState() {
		++entityAge;
		if (currentSwimTarget != null && !isValidSwimTarget(currentSwimTarget.posX, currentSwimTarget.posY, currentSwimTarget.posZ)) {
			currentSwimTarget = null;
			swimTargetTime = 0;
		}
		if (currentSwimTarget == null || rand.nextInt(200) == 0 || getDistanceSqToSwimTarget() < 4.0) {
			for (int l = 0; l < 16; ++l) {
				int i = MathHelper.floor_double(posX);
				int j = MathHelper.floor_double(posY);
				int k = MathHelper.floor_double(posZ);
				if (!isValidSwimTarget(i += rand.nextInt(16) - rand.nextInt(16), j += MathHelper.getRandomIntegerInRange(rand, -2, 4), k += rand.nextInt(16) - rand.nextInt(16))) {
					continue;
				}
				currentSwimTarget = new ChunkCoordinates(i, j, k);
				swimTargetTime = 0;
				break;
			}
		}
		if (currentSwimTarget != null) {
			double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
			double d0 = currentSwimTarget.posX + 0.5 - posX;
			double d1 = currentSwimTarget.posY + 0.5 - posY;
			double d2 = currentSwimTarget.posZ + 0.5 - posZ;
			motionX += (Math.signum(d0) * 0.5 - motionX) * speed;
			motionY += (Math.signum(d1) * 0.5 - motionY) * speed;
			motionZ += (Math.signum(d2) * 0.5 - motionZ) * speed;
			float f = (float) (Math.atan2(motionZ, motionX) * 180.0 / 3.141592653589793) - 90.0f;
			float f1 = MathHelper.wrapAngleTo180_float(f - rotationYaw);
			moveForward = 0.5f;
			rotationYaw += f1;
			++swimTargetTime;
			if (swimTargetTime >= 200) {
				currentSwimTarget = null;
				swimTargetTime = 0;
			}
		}
		despawnEntity();
	}

	private boolean isValidSwimTarget(int i, int j, int k) {
		return worldObj.getBlock(i, j, k).getMaterial() == Material.water;
	}

	private double getDistanceSqToSwimTarget() {
		double d = currentSwimTarget.posX + 0.5;
		double d1 = currentSwimTarget.posY + 0.5;
		double d2 = currentSwimTarget.posZ + 0.5;
		return getDistanceSq(d, d1, d2);
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
	}

	public enum FishType {
		COMMON("common"), SALMON("salmon"), CLOWNFISH("clownfish");

		public final String textureDir;

		private FishType(String s) {
			textureDir = s;
		}
	}

}
