package lotr.common.entity.animal;

import java.util.*;

import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockBerryBush;
import lotr.common.entity.*;
import lotr.common.inventory.LOTREntityInventory;
import lotr.common.item.LOTRValuableItems;
import lotr.common.world.biome.LOTRBiomeGenFarHarad;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.ForgeDirection;

public class LOTREntityBird extends EntityLiving implements LOTRAmbientCreature, LOTRRandomSkinEntity {
	private ChunkCoordinates currentFlightTarget;
	private int flightTargetTime = 0;
	public int flapTime = 0;
	private LOTREntityInventory birdInv = new LOTREntityInventory("BirdItems", this, 9);
	private EntityItem stealTargetItem;
	private EntityPlayer stealTargetPlayer;
	private int stolenTime = 0;
	private boolean stealingCrops = false;

	public LOTREntityBird(World world) {
		super(world);
		setSize(0.5f, 0.5f);
		tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 12.0f, 0.05f));
		tasks.addTask(1, new EntityAIWatchClosest(this, EntityLiving.class, 12.0f, 0.1f));
		tasks.addTask(2, new EntityAILookIdle(this));
	}

	@Override
	public void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, (byte) 0);
		dataWatcher.addObject(17, (byte) 1);
	}

	public BirdType getBirdType() {
		byte i = dataWatcher.getWatchableObjectByte(16);
		if (i < 0 || i >= BirdType.values().length) {
			i = 0;
		}
		return BirdType.values()[i];
	}

	public void setBirdType(BirdType type) {
		this.setBirdType(type.ordinal());
	}

	public void setBirdType(int i) {
		dataWatcher.updateObject(16, (byte) i);
	}

	public boolean isBirdStill() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	public void setBirdStill(boolean flag) {
		dataWatcher.updateObject(17, flag ? (byte) 1 : 0);
	}

	public String getBirdTextureDir() {
		return getBirdType().textureDir;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(MathHelper.getRandomDoubleInRange(rand, 0.08, 0.13));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = MathHelper.floor_double(posX);
		MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
		if (biome instanceof LOTRBiomeGenFarHarad) {
			if (rand.nextInt(8) == 0) {
				this.setBirdType(BirdType.CROW);
			} else {
				this.setBirdType(BirdType.FAR_HARAD);
			}
		} else if (rand.nextInt(6) == 0) {
			this.setBirdType(BirdType.CROW);
		} else if (rand.nextInt(10) == 0) {
			this.setBirdType(BirdType.MAGPIE);
		} else {
			this.setBirdType(BirdType.COMMON);
		}
		return data;
	}

	@Override
	public void setUniqueID(UUID uuid) {
		entityUniqueID = uuid;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	protected void collideWithEntity(Entity entity) {
	}

	@Override
	protected void collideWithNearbyEntities() {
	}

	@Override
	protected boolean isAIEnabled() {
		return true;
	}

	protected boolean canStealItems() {
		return getBirdType().canSteal;
	}

	protected boolean isStealable(ItemStack itemstack) {
		BirdType type = getBirdType();
		Item item = itemstack.getItem();
		if (type == BirdType.COMMON) {
			return item instanceof IPlantable && ((IPlantable) item).getPlantType(worldObj, -1, -1, -1) == EnumPlantType.Crop;
		}
		if (type == BirdType.CROW) {
			return item instanceof ItemFood || LOTRMod.isOreNameEqual(itemstack, "bone");
		}
		if (type == BirdType.MAGPIE) {
			return LOTRValuableItems.canMagpieSteal(itemstack);
		}
		return false;
	}

	public ItemStack getStolenItem() {
		return getEquipmentInSlot(4);
	}

	public void setStolenItem(ItemStack itemstack) {
		setCurrentItemOrArmor(4, itemstack);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (isBirdStill()) {
			motionZ = 0.0;
			motionY = 0.0;
			motionX = 0.0;
			posY = MathHelper.floor_double(posY);
			if (worldObj.isRemote) {
				if (rand.nextInt(200) == 0) {
					flapTime = 40;
				}
				if (flapTime > 0) {
					--flapTime;
				}
			}
		} else {
			motionY *= 0.6;
			if (worldObj.isRemote) {
				flapTime = 0;
			}
		}
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
		if (getStolenItem() != null) {
			++stolenTime;
			if (stolenTime >= 200) {
				setStolenItem(null);
				stolenTime = 0;
			}
		}
		if (isBirdStill()) {
			if (!canBirdSit() || (rand.nextInt(400) == 0 || worldObj.getClosestPlayerToEntity(this, 6.0) != null)) {
				setBirdStill(false);
			}
		} else {
			if (canStealItems() && !stealingCrops && stealTargetItem == null && stealTargetPlayer == null && !birdInv.isFull() && rand.nextInt(100) == 0) {
				double range = 16.0;
				List players = worldObj.selectEntitiesWithinAABB(EntityPlayer.class, boundingBox.expand(range, range, range), new IEntitySelector() {

					@Override
					public boolean isEntityApplicable(Entity e) {
						EntityPlayer entityplayer;
						if (e instanceof EntityPlayer && LOTREntityBird.this.canStealPlayer(entityplayer = (EntityPlayer) e)) {
							ChunkCoordinates coords = LOTREntityBird.this.getPlayerFlightTarget(entityplayer);
							return LOTREntityBird.this.isValidFlightTarget(coords);
						}
						return false;
					}
				});
				if (!players.isEmpty()) {
					stealTargetPlayer = (EntityPlayer) players.get(rand.nextInt(players.size()));
					currentFlightTarget = getPlayerFlightTarget(stealTargetPlayer);
					newFlight();
				} else {
					List entityItems = worldObj.selectEntitiesWithinAABB(EntityItem.class, boundingBox.expand(range, range, range), new IEntitySelector() {

						@Override
						public boolean isEntityApplicable(Entity e) {
							EntityItem eItem;
							if (e instanceof EntityItem && LOTREntityBird.this.canStealItem(eItem = (EntityItem) e)) {
								ChunkCoordinates coords = LOTREntityBird.this.getItemFlightTarget(eItem);
								return LOTREntityBird.this.isValidFlightTarget(coords);
							}
							return false;
						}
					});
					if (!entityItems.isEmpty()) {
						stealTargetItem = (EntityItem) entityItems.get(rand.nextInt(entityItems.size()));
						currentFlightTarget = getItemFlightTarget(stealTargetItem);
						newFlight();
					}
				}
			}
			if (stealTargetItem != null || stealTargetPlayer != null) {
				if (birdInv.isFull() || currentFlightTarget == null || !isValidFlightTarget(currentFlightTarget)) {
					cancelFlight();
				} else if ((stealTargetItem != null && !canStealItem(stealTargetItem)) || (stealTargetPlayer != null && !canStealPlayer(stealTargetPlayer))) {
					cancelFlight();
				} else {
					if (stealTargetItem != null) {
						currentFlightTarget = getItemFlightTarget(stealTargetItem);
					} else if (stealTargetPlayer != null) {
						currentFlightTarget = getPlayerFlightTarget(stealTargetPlayer);
					}
					if (getDistanceSqToFlightTarget() < 1.0) {
						ItemStack stolenItem = null;
						if (stealTargetItem != null) {
							ItemStack itemstack = stealTargetItem.getEntityItem();
							ItemStack stealCopy = itemstack.copy();
							stealCopy.stackSize = MathHelper.getRandomIntegerInRange(rand, 1, Math.min(stealCopy.stackSize, 4));
							ItemStack safeCopy = stealCopy.copy();
							if (birdInv.addItemToInventory(stealCopy)) {
								itemstack.stackSize -= safeCopy.stackSize - stealCopy.stackSize;
								if (itemstack.stackSize <= 0) {
									stealTargetItem.setDead();
								}
								stolenItem = safeCopy;
							}
						} else if (stealTargetPlayer != null) {
							List<Integer> slots = getStealablePlayerSlots(stealTargetPlayer);
							int randSlot = slots.get(rand.nextInt(slots.size()));
							ItemStack itemstack = stealTargetPlayer.inventory.getStackInSlot(randSlot);
							ItemStack stealCopy = itemstack.copy();
							stealCopy.stackSize = MathHelper.getRandomIntegerInRange(rand, 1, Math.min(stealCopy.stackSize, 4));
							ItemStack safeCopy = stealCopy.copy();
							if (birdInv.addItemToInventory(stealCopy)) {
								itemstack.stackSize -= safeCopy.stackSize - stealCopy.stackSize;
								if (itemstack.stackSize <= 0) {
									itemstack = null;
								}
								stealTargetPlayer.inventory.setInventorySlotContents(randSlot, itemstack);
								stolenItem = safeCopy;
							}
						}
						if (stolenItem != null) {
							stolenTime = 0;
							setStolenItem(stolenItem);
							playSound("random.pop", 0.5f, ((rand.nextFloat() - rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
						}
						cancelFlight();
					}
				}
			} else if (stealingCrops) {
				if (!LOTRMod.canGrief(worldObj)) {
					stealingCrops = false;
				} else if (currentFlightTarget == null || !isValidFlightTarget(currentFlightTarget)) {
					cancelFlight();
				} else {
					int i = currentFlightTarget.posX;
					int j = currentFlightTarget.posY;
					int k = currentFlightTarget.posZ;
					if (getDistanceSqToFlightTarget() < 1.0) {
						if (canStealCrops(i, j, k)) {
							eatCropBlock(i, j, k);
							playSound("random.eat", 1.0f, (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f + 1.0f);
						}
						cancelFlight();
					} else if (!canStealCrops(i, j, k) || (flightTargetTime % 100 == 0 && LOTRScarecrows.anyScarecrowsNearby(worldObj, i, j, k))) {
						cancelFlight();
					}
				}
			} else {
				int j;
				if (LOTRMod.canGrief(worldObj) && !stealingCrops && rand.nextInt(100) == 0) {
					int i = MathHelper.floor_double(posX);
					j = MathHelper.floor_double(posY);
					int k = MathHelper.floor_double(posZ);
					int range = 16;
					int yRange = 8;
					int attempts = 32;
					for (int l = 0; l < attempts; ++l) {
						int j1;
						int k1;
						int i1 = i + MathHelper.getRandomIntegerInRange(rand, -range, range);
						if (!canStealCrops(i1, j1 = j + MathHelper.getRandomIntegerInRange(rand, -yRange, yRange), k1 = k + MathHelper.getRandomIntegerInRange(rand, -range, range)) || LOTRScarecrows.anyScarecrowsNearby(worldObj, i1, j1, k1)) {
							continue;
						}
						stealingCrops = true;
						currentFlightTarget = new ChunkCoordinates(i1, j1, k1);
						newFlight();
						break;
					}
				}
				if (!stealingCrops) {
					if (currentFlightTarget != null && !isValidFlightTarget(currentFlightTarget)) {
						cancelFlight();
					}
					if (currentFlightTarget == null || rand.nextInt(50) == 0 || getDistanceSqToFlightTarget() < 4.0) {
						int i = MathHelper.floor_double(posX);
						j = MathHelper.floor_double(posY);
						int k = MathHelper.floor_double(posZ);
						currentFlightTarget = new ChunkCoordinates(i += rand.nextInt(16) - rand.nextInt(16), j += MathHelper.getRandomIntegerInRange(rand, -2, 3), k += rand.nextInt(16) - rand.nextInt(16));
						newFlight();
					}
				}
			}
			if (currentFlightTarget != null) {
				double speed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
				double d0 = currentFlightTarget.posX + 0.5 - posX;
				double d1 = currentFlightTarget.posY + 0.5 - posY;
				double d2 = currentFlightTarget.posZ + 0.5 - posZ;
				motionX += (Math.signum(d0) * 0.5 - motionX) * speed;
				motionY += (Math.signum(d1) * 0.8 - motionY) * speed;
				motionZ += (Math.signum(d2) * 0.5 - motionZ) * speed;
				float f = (float) (Math.atan2(motionZ, motionX) * 180.0 / 3.141592653589793) - 90.0f;
				float f1 = MathHelper.wrapAngleTo180_float(f - rotationYaw);
				moveForward = 0.5f;
				rotationYaw += f1;
				++flightTargetTime;
				if (flightTargetTime >= 400) {
					cancelFlight();
				}
			}
			if (rand.nextInt(200) == 0 && canBirdSit()) {
				setBirdStill(true);
				cancelFlight();
			}
		}
	}

	private boolean canBirdSit() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);
		Block block = worldObj.getBlock(i, j, k);
		Block below = worldObj.getBlock(i, j - 1, k);
		return block.getBlocksMovement(worldObj, i, j, k) && below.isSideSolid(worldObj, i, j - 1, k, ForgeDirection.UP);
	}

	private boolean isValidFlightTarget(ChunkCoordinates coords) {
		int i = coords.posX;
		int j = coords.posY;
		int k = coords.posZ;
		if (j >= 1) {
			Block block = worldObj.getBlock(i, j, k);
			return block.getBlocksMovement(worldObj, i, j, k);
		}
		return false;
	}

	private double getDistanceSqToFlightTarget() {
		double d = currentFlightTarget.posX + 0.5;
		double d1 = currentFlightTarget.posY + 0.5;
		double d2 = currentFlightTarget.posZ + 0.5;
		return getDistanceSq(d, d1, d2);
	}

	private void cancelFlight() {
		currentFlightTarget = null;
		flightTargetTime = 0;
		stealTargetItem = null;
		stealTargetPlayer = null;
		stealingCrops = false;
	}

	private void newFlight() {
		flightTargetTime = 0;
	}

	private boolean canStealItem(EntityItem entity) {
		return entity.isEntityAlive() && isStealable(entity.getEntityItem());
	}

	private boolean canStealPlayer(EntityPlayer entityplayer) {
		if (entityplayer.capabilities.isCreativeMode || !entityplayer.isEntityAlive()) {
			return false;
		}
		List<Integer> slots = getStealablePlayerSlots(entityplayer);
		return !slots.isEmpty();
	}

	private List<Integer> getStealablePlayerSlots(EntityPlayer entityplayer) {
		ArrayList<Integer> slots = new ArrayList<>();
		for (int i = 0; i <= 8; ++i) {
			ItemStack itemstack;
			if (i != entityplayer.inventory.currentItem || (itemstack = entityplayer.inventory.getStackInSlot(i)) == null || !isStealable(itemstack)) {
				continue;
			}
			slots.add(i);
		}
		return slots;
	}

	private ChunkCoordinates getItemFlightTarget(EntityItem entity) {
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.boundingBox.minY);
		int k = MathHelper.floor_double(entity.posZ);
		return new ChunkCoordinates(i, j, k);
	}

	private ChunkCoordinates getPlayerFlightTarget(EntityPlayer entityplayer) {
		int i = MathHelper.floor_double(entityplayer.posX);
		int j = MathHelper.floor_double(entityplayer.boundingBox.minY + 1.0);
		int k = MathHelper.floor_double(entityplayer.posZ);
		return new ChunkCoordinates(i, j, k);
	}

	private boolean canStealCrops(int i, int j, int k) {
		Block block = worldObj.getBlock(i, j, k);
		if (block instanceof BlockCrops) {
			return true;
		}
		if (block instanceof LOTRBlockBerryBush) {
			int meta = worldObj.getBlockMetadata(i, j, k);
			return LOTRBlockBerryBush.hasBerries(meta);
		}
		return false;
	}

	private void eatCropBlock(int i, int j, int k) {
		Block block = worldObj.getBlock(i, j, k);
		if (block instanceof LOTRBlockBerryBush) {
			int meta = worldObj.getBlockMetadata(i, j, k);
			meta = LOTRBlockBerryBush.setHasBerries(meta, false);
			worldObj.setBlockMetadataWithNotify(i, j, k, meta, 3);
		} else {
			worldObj.setBlockToAir(i, j, k);
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void fall(float f) {
	}

	@Override
	protected void updateFallState(double d, boolean flag) {
	}

	@Override
	public boolean doesEntityNotTriggerPressurePlate() {
		return true;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		boolean flag = super.attackEntityFrom(damagesource, f);
		if (flag && !worldObj.isRemote && isBirdStill()) {
			setBirdStill(false);
		}
		return flag;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int feathers = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < feathers; ++l) {
			dropItem(Items.feather, 1);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if (!worldObj.isRemote) {
			birdInv.dropAllItems();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		this.setBirdType(nbt.getInteger("BirdType"));
		setBirdStill(nbt.getBoolean("BirdStill"));
		birdInv.writeToNBT(nbt);
		nbt.setShort("StealTime", (short) stolenTime);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("BirdType", getBirdType().ordinal());
		nbt.setBoolean("BirdStill", isBirdStill());
		birdInv.readFromNBT(nbt);
		stolenTime = nbt.getShort("StealTime");
	}

	@Override
	protected boolean canDespawn() {
		return super.canDespawn();
	}

	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			return canBirdSpawnHere();
		}
		return false;
	}

	protected boolean canBirdSpawnHere() {
		return LOTRAmbientSpawnChecks.canSpawn(this, 8, 12, 40, 4, Material.leaves);
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	protected boolean interact(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public int getTalkInterval() {
		return 60;
	}

	@Override
	public void playLivingSound() {
		boolean sound = true;
		if (!worldObj.isDaytime()) {
			sound = rand.nextInt(20) == 0;
		}
		if (sound) {
			super.playLivingSound();
		}
	}

	@Override
	protected float getSoundVolume() {
		return 1.0f;
	}

	@Override
	protected String getLivingSound() {
		BirdType type = getBirdType();
		if (type == BirdType.CROW) {
			return "lotr:bird.crow.say";
		}
		return "lotr:bird.say";
	}

	@Override
	protected String getHurtSound() {
		BirdType type = getBirdType();
		if (type == BirdType.CROW) {
			return "lotr:bird.crow.hurt";
		}
		return "lotr:bird.hurt";
	}

	@Override
	protected String getDeathSound() {
		BirdType type = getBirdType();
		if (type == BirdType.CROW) {
			return "lotr:bird.crow.hurt";
		}
		return "lotr:bird.hurt";
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
	}

	public enum BirdType {
		COMMON("common", true), CROW("crow", true), MAGPIE("magpie", true), FAR_HARAD("farHarad", true);

		public final String textureDir;
		public final boolean canSteal;

		private BirdType(String s, boolean flag) {
			textureDir = s;
			canSteal = flag;
		}
	}

}
