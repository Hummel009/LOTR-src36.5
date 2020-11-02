package lotr.common.entity.npc;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.*;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.*;
import lotr.common.*;
import lotr.common.entity.*;
import lotr.common.entity.ai.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.entity.item.*;
import lotr.common.entity.projectile.*;
import lotr.common.fac.LOTRFaction;
import lotr.common.inventory.*;
import lotr.common.item.*;
import lotr.common.network.*;
import lotr.common.quest.*;
import lotr.common.world.LOTRUtumnoLevel;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeGenBase;

public abstract class LOTREntityNPC extends EntityCreature implements IRangedAttackMob, LOTRRandomSkinEntity {
	public static IAttribute npcAttackDamage = new RangedAttribute("lotr.npcAttackDamage", 2.0, 0.0, Double.MAX_VALUE).setDescription("LOTR NPC Attack Damage");
	public static IAttribute npcAttackDamageExtra = new RangedAttribute("lotr.npcAttackDamageExtra", 0.0, 0.0, Double.MAX_VALUE).setDescription("LOTR NPC Extra Attack Damage");
	public static IAttribute npcAttackDamageDrunk = new RangedAttribute("lotr.npcAttackDamageDrunk", 4.0, 0.0, Double.MAX_VALUE).setDescription("LOTR NPC Drunken Attack Damage");
	public static IAttribute npcRangedAccuracy = new RangedAttribute("lotr.npcRangedAccuracy", 1.0, 0.0, Double.MAX_VALUE).setDescription("LOTR NPC Ranged Accuracy");
	public static IAttribute horseAttackSpeed = new RangedAttribute("lotr.horseAttackSpeed", 1.7, 0.0, Double.MAX_VALUE).setDescription("LOTR Horse Attack Speed");
	public static float MOUNT_RANGE_BONUS = 1.5f;
	protected float npcWidth = -1.0f;
	protected float npcHeight;
	private boolean loadingFromNBT = false;
	public boolean isPassive = false;
	public boolean isImmuneToFrost = false;
	protected boolean isChilly = false;
	protected boolean spawnsInDarkness = false;
	public boolean isNPCPersistent = false;
	public boolean liftSpawnRestrictions = false;
	private boolean isConquestSpawning = false;
	public boolean liftBannerRestrictions = false;
	private boolean addedBurningPanic = false;
	public List<LOTRFaction> killBonusFactions = new ArrayList<>();
	private UUID invasionID = null;
	private boolean isTargetSeeker = false;
	public String npcLocationName;
	private boolean hasSpecificLocationName;
	public boolean spawnRidingHorse;
	protected boolean canBannerBearerSpawnRiding = false;
	private boolean ridingMount;
	public LOTRFamilyInfo familyInfo;
	public LOTREntityQuestInfo questInfo;
	public LOTRHiredNPCInfo hiredNPCInfo;
	public LOTRTraderNPCInfo traderNPCInfo;
	public LOTRTravellingTraderInfo travellingTraderInfo;
	public boolean isTraderEscort = false;
	public LOTRBossInfo bossInfo;
	private boolean setInitialHome = false;
	private int initHomeX;
	private int initHomeY;
	private int initHomeZ;
	private int initHomeRange = -1;
	private AttackMode currentAttackMode = AttackMode.IDLE;
	private boolean firstUpdatedAttackMode = false;
	private UUID prevAttackTarget;
	private int combatCooldown;
	private boolean combatStance;
	private boolean prevCombatStance;
	public boolean clientCombatStance;
	public boolean clientIsEating;
	private int nearbyBannerFactor;
	public LOTRInventoryNPCItems npcItemsInv;
	public LOTRInventoryHiredReplacedItems hiredReplacedInv;
	private ItemStack[] festiveItems = new ItemStack[5];
	private Random festiveRand = new Random();
	private boolean initFestiveItems = false;
	public LOTRShields npcShield;
	public ResourceLocation npcCape;
	public int npcTalkTick = 0;
	private boolean hurtOnlyByPlates = true;
	private List<ItemStack> enpouchedDrops = new ArrayList<>();
	private boolean enpouchNPCDrops = false;

	public LOTREntityNPC(World world) {
		super(world);
		if (this instanceof LOTRBoss || this instanceof LOTRCharacter) {
			isNPCPersistent = true;
		} else if (this instanceof LOTRTradeable) {
			isNPCPersistent = this instanceof LOTRTravellingTrader ? true : ((LOTRTradeable) this).shouldTraderRespawn();
		} else if (this instanceof LOTRUnitTradeable) {
			isNPCPersistent = ((LOTRUnitTradeable) this).shouldTraderRespawn();
		}
	}

	public final boolean isTrader() {
		return this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable || this instanceof LOTRMercenary;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		familyInfo = new LOTRFamilyInfo(this);
		questInfo = new LOTREntityQuestInfo(this);
		hiredNPCInfo = new LOTRHiredNPCInfo(this);
		traderNPCInfo = new LOTRTraderNPCInfo(this);
		if (this instanceof LOTRTravellingTrader) {
			travellingTraderInfo = new LOTRTravellingTraderInfo((LOTRTravellingTrader) this);
		}
		if (this instanceof LOTRBoss) {
			bossInfo = new LOTRBossInfo((LOTRBoss) this);
		}
		setupNPCGender();
		setupNPCName();
		npcItemsInv = new LOTRInventoryNPCItems(this);
		hiredReplacedInv = new LOTRInventoryHiredReplacedItems(this);
	}

	public void setupNPCGender() {
	}

	public void setupNPCName() {
	}

	public void startTraderVisiting(EntityPlayer entityplayer) {
		travellingTraderInfo.startVisiting(entityplayer);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getAttributeMap().registerAttribute(npcAttackDamage);
		getAttributeMap().registerAttribute(npcAttackDamageExtra);
		getAttributeMap().registerAttribute(npcAttackDamageDrunk);
		getAttributeMap().registerAttribute(npcRangedAccuracy);
		getAttributeMap().registerAttribute(horseAttackSpeed);
	}

	@Override
	public void setUniqueID(UUID uuid) {
		entityUniqueID = uuid;
	}

	public int addTargetTasks(boolean seekTargets) {
		return this.addTargetTasks(seekTargets, LOTREntityAINearestAttackableTargetBasic.class);
	}

	public int addTargetTasks(boolean seekTargets, Class<? extends LOTREntityAINearestAttackableTargetBasic> c) {
		targetTasks.taskEntries.clear();
		targetTasks.addTask(1, new LOTREntityAIHiringPlayerHurtByTarget(this));
		targetTasks.addTask(2, new LOTREntityAIHiringPlayerHurtTarget(this));
		targetTasks.addTask(3, new LOTREntityAINPCHurtByTarget(this, false));
		isTargetSeeker = seekTargets;
		if (seekTargets) {
			return LOTREntityNPC.addTargetTasks(this, 4, c);
		}
		return 3;
	}

	public static int addTargetTasks(EntityCreature entity, int index, Class<? extends LOTREntityAINearestAttackableTargetBasic> c) {
		try {
			Constructor<? extends LOTREntityAINearestAttackableTargetBasic> constructor = c.getConstructor(EntityCreature.class, Class.class, Integer.TYPE, Boolean.TYPE, IEntitySelector.class);
			entity.targetTasks.addTask(index, constructor.newInstance(entity, EntityPlayer.class, 0, true, null));
			entity.targetTasks.addTask(index, constructor.newInstance(entity, EntityLiving.class, 0, true, new LOTRNPCTargetSelector(entity)));
		} catch (Exception e) {
			FMLLog.severe("Error adding LOTR target tasks to entity " + entity.toString(), new Object[0]);
			e.printStackTrace();
		}
		return index;
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public boolean isInRangeToRenderDist(double dist) {
		EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
		if (entityplayer != null && !LOTRLevelData.getData(entityplayer).getMiniQuestsForEntity(this, true).isEmpty()) {
			return true;
		}
		return super.isInRangeToRenderDist(dist);
	}

	protected boolean shouldBurningPanic() {
		return true;
	}

	public IEntityLivingData initCreatureForHire(IEntityLivingData data) {
		spawnRidingHorse = false;
		return onSpawnWithEgg(data);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		if (!worldObj.isRemote) {
			if (spawnRidingHorse && (!(this instanceof LOTRBannerBearer) || canBannerBearerSpawnRiding)) {
				LOTRNPCMount mount = createMountToRide();
				EntityCreature livingMount = (EntityCreature) mount;
				livingMount.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
				if (worldObj.func_147461_a(livingMount.boundingBox).isEmpty()) {
					livingMount.onSpawnWithEgg(null);
					worldObj.spawnEntityInWorld(livingMount);
					mountEntity(livingMount);
					if (!(mount instanceof LOTREntityNPC)) {
						setRidingHorse(true);
						mount.setBelongsToNPC(true);
						LOTRMountFunctions.setNavigatorRangeFromNPC(mount, this);
					}
				}
			}
			if (traderNPCInfo.getBuyTrades() != null && rand.nextInt(10000) == 0) {
				for (LOTRTradeEntry trade : traderNPCInfo.getBuyTrades()) {
					trade.setCost(trade.getCost() * 100);
				}
				familyInfo.setName("Friendly Capitalist");
			}
		}
		return data;
	}

	public LOTRNPCMount createMountToRide() {
		return new LOTREntityHorse(worldObj);
	}

	public void setRidingHorse(boolean flag) {
		ridingMount = flag;
		double d = getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		d = flag ? (d *= 1.5) : (d /= 1.5);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(d);
	}

	public void onPlayerStartTracking(EntityPlayerMP entityplayer) {
		hiredNPCInfo.sendBasicData(entityplayer);
		familyInfo.sendData(entityplayer);
		questInfo.sendData(entityplayer);
		sendCombatStance(entityplayer);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public final String getCommandSenderName() {
		if (hasCustomNameTag()) {
			return super.getCommandSenderName();
		}
		String entityName = getEntityClassName();
		String npcName = getNPCName();
		if (LOTRMod.isAprilFools()) {
			npcName = "Gandalf";
		}
		return getNPCFormattedName(npcName, entityName);
	}

	public String getNPCFormattedName(String npcName, String entityName) {
		if (npcName.equals(entityName)) {
			return entityName;
		}
		return StatCollector.translateToLocalFormatted("entity.lotr.generic.entityName", npcName, entityName);
	}

	public String getEntityClassName() {
		return super.getCommandSenderName();
	}

	public String getNPCName() {
		return super.getCommandSenderName();
	}

	public LOTRFaction getFaction() {
		return LOTRFaction.UNALIGNED;
	}

	public LOTRFaction getHiringFaction() {
		return getFaction();
	}

	public LOTRFaction getInfluenceZoneFaction() {
		return getFaction();
	}

	public boolean isCivilianNPC() {
		return !isTargetSeeker && !(this instanceof LOTRUnitTradeable) && !(this instanceof LOTRMercenary) && !(this instanceof LOTRBoss);
	}

	public boolean generatesControlZone() {
		return true;
	}

	public boolean canBeFreelyTargetedBy(EntityLiving attacker) {
		return true;
	}

	public int getNPCTalkInterval() {
		return 40;
	}

	public boolean canNPCTalk() {
		return isEntityAlive() && npcTalkTick >= getNPCTalkInterval();
	}

	public void markNPCSpoken() {
		npcTalkTick = 0;
	}

	@Override
	public final void setAttackTarget(EntityLivingBase target) {
		boolean speak = target != null && getEntitySenses().canSee(target) && rand.nextInt(3) == 0;
		this.setAttackTarget(target, speak);
	}

	public void setAttackTarget(EntityLivingBase target, boolean speak) {
		EntityLivingBase prevEntityTarget = getAttackTarget();
		super.setAttackTarget(target);
		hiredNPCInfo.onSetTarget(target, prevEntityTarget);
		if (target != null && !target.getUniqueID().equals(prevAttackTarget)) {
			prevAttackTarget = target.getUniqueID();
			if (!worldObj.isRemote) {
				EntityPlayer entityplayer;
				String speechBank;
				if (getAttackSound() != null) {
					worldObj.playSoundAtEntity(this, getAttackSound(), getSoundVolume(), getSoundPitch());
				}
				if (target instanceof EntityPlayer && speak && (speechBank = getSpeechBank(entityplayer = (EntityPlayer) target)) != null) {
					IEntitySelector selectorAttackingNPCs = new IEntitySelector() {

						@Override
						public boolean isEntityApplicable(Entity entity) {
							if (entity instanceof LOTREntityNPC) {
								LOTREntityNPC npc = (LOTREntityNPC) entity;
								return npc.isAIEnabled() && npc.isEntityAlive() && npc.getAttackTarget() == entityplayer;
							}
							return false;
						}
					};
					double range = 16.0;
					List nearbyMobs = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(range, range, range), selectorAttackingNPCs);
					if (nearbyMobs.size() <= 5) {
						this.sendSpeechBank(entityplayer, speechBank);
					}
				}
			}
		}
	}

	public String getAttackSound() {
		return null;
	}

	@Override
	public int getTalkInterval() {
		return 200;
	}

	@Override
	public boolean isChild() {
		return familyInfo.getAge() < 0;
	}

	public void changeNPCNameForMarriage(LOTREntityNPC spouse) {
	}

	public void createNPCChildName(LOTREntityNPC maleParent, LOTREntityNPC femaleParent) {
	}

	@Override
	public boolean canDespawn() {
		return !isNPCPersistent && !hiredNPCInfo.isActive && !questInfo.anyActiveQuestPlayers();
	}

	@Override
	protected final void setSize(float f, float f1) {
		boolean flag = npcWidth > 0.0f;
		npcWidth = f;
		npcHeight = f1;
		if (!flag) {
			rescaleNPC(1.0f);
		}
	}

	protected void rescaleNPC(float f) {
		super.setSize(npcWidth * f, npcHeight * f);
	}

	protected float getNPCScale() {
		return isChild() ? 0.5f : 1.0f;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		rescaleNPC(getNPCScale());
		updateCombat();
		if (ticksExisted % 10 == 0) {
			updateNearbyBanners();
		}
		familyInfo.onUpdate();
		questInfo.onUpdate();
		hiredNPCInfo.onUpdate();
		if (this instanceof LOTRTradeable) {
			traderNPCInfo.onUpdate();
		}
		if (travellingTraderInfo != null) {
			travellingTraderInfo.onUpdate();
		}
		if ((this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable) && !worldObj.isRemote) {
			int preventKidnap;
			if (!setInitialHome) {
				if (hasHome()) {
					initHomeX = getHomePosition().posX;
					initHomeY = getHomePosition().posY;
					initHomeZ = getHomePosition().posZ;
					initHomeRange = (int) func_110174_bM();
				}
				setInitialHome = true;
			}
			if ((preventKidnap = LOTRConfig.preventTraderKidnap) > 0 && setInitialHome && initHomeRange > 0 && getDistanceSq(initHomeX + 0.5, initHomeY + 0.5, initHomeZ + 0.5) > preventKidnap * preventKidnap) {
				if (ridingEntity != null) {
					mountEntity(null);
				}
				worldObj.getChunkFromBlockCoords(initHomeX, initHomeZ);
				setLocationAndAngles(initHomeX + 0.5, initHomeY, initHomeZ + 0.5, rotationYaw, rotationPitch);
			}
		}
		if (bossInfo != null) {
			bossInfo.onUpdate();
		}
		if (!worldObj.isRemote && !addedBurningPanic) {
			LOTREntityUtils.removeAITask(this, LOTREntityAIBurningPanic.class);
			if (shouldBurningPanic()) {
				tasks.addTask(0, new LOTREntityAIBurningPanic(this, 1.5));
			}
			addedBurningPanic = true;
		}
		if (!worldObj.isRemote && isEntityAlive() && (isTrader() || hiredNPCInfo.isActive) && getAttackTarget() == null) {
			float healAmount = 0.0f;
			if (ticksExisted % 40 == 0) {
				healAmount += 1.0f;
			}
			if (hiredNPCInfo.isActive && nearbyBannerFactor > 0 && ticksExisted % (240 - nearbyBannerFactor * 40) == 0) {
				healAmount += 1.0f;
			}
			if (healAmount > 0.0f) {
				heal(healAmount);
				if (ridingEntity instanceof EntityLivingBase && !(ridingEntity instanceof LOTREntityNPC)) {
					((EntityLivingBase) ridingEntity).heal(healAmount);
				}
			}
		}
		if (!worldObj.isRemote && isEntityAlive() && getAttackTarget() == null) {
			boolean guiOpen = false;
			if (this instanceof LOTRTradeable || this instanceof LOTRUnitTradeable || this instanceof LOTRMercenary) {
				for (Object element : worldObj.playerEntities) {
					EntityPlayer entityplayer = (EntityPlayer) element;
					Container container = entityplayer.openContainer;
					if (container instanceof LOTRContainerTrade && ((LOTRContainerTrade) container).theTraderNPC == this) {
						guiOpen = true;
						break;
					}
					if (container instanceof LOTRContainerUnitTrade && ((LOTRContainerUnitTrade) container).theLivingTrader == this) {
						guiOpen = true;
						break;
					}
					if (container instanceof LOTRContainerCoinExchange && ((LOTRContainerCoinExchange) container).theTraderNPC == this) {
						guiOpen = true;
						break;
					}
					if (!(container instanceof LOTRContainerAnvil) || ((LOTRContainerAnvil) container).theNPC != this) {
						continue;
					}
					guiOpen = true;
					break;
				}
			}
			if (hiredNPCInfo.isActive && hiredNPCInfo.isGuiOpen) {
				guiOpen = true;
			}
			if (questInfo.anyOpenOfferPlayers()) {
				guiOpen = true;
			}
			if (guiOpen) {
				getNavigator().clearPathEntity();
				if (ridingEntity instanceof LOTRNPCMount) {
					((EntityLiving) ridingEntity).getNavigator().clearPathEntity();
				}
			}
		}
		updateArmSwingProgress();
		if (npcTalkTick < getNPCTalkInterval()) {
			++npcTalkTick;
		}
		if (!worldObj.isRemote && hasHome() && !isWithinHomeDistanceCurrentPosition()) {
			int homeX = getHomePosition().posX;
			int homeY = getHomePosition().posY;
			int homeZ = getHomePosition().posZ;
			int homeRange = (int) func_110174_bM();
			double maxDist = homeRange + 128.0;
			double distToHome = getDistance(homeX + 0.5, homeY + 0.5, homeZ + 0.5);
			if (distToHome > maxDist) {
				detachHome();
			} else if (getAttackTarget() == null && getNavigator().noPath()) {
				detachHome();
				boolean goDirectlyHome = false;
				if (worldObj.blockExists(homeX, homeY, homeZ) && hiredNPCInfo.isGuardMode()) {
					hiredNPCInfo.getGuardRange();
					goDirectlyHome = distToHome < 16.0;
				}
				double homeSpeed = 1.3;
				if (goDirectlyHome) {
					getNavigator().tryMoveToXYZ(homeX + 0.5, homeY + 0.5, homeZ + 0.5, homeSpeed);
				} else {
					Vec3 path = null;
					for (int l = 0; l < 16 && path == null; ++l) {
						path = RandomPositionGenerator.findRandomTargetBlockTowards(this, 8, 7, Vec3.createVectorHelper(homeX, homeY, homeZ));
					}
					if (path != null) {
						getNavigator().tryMoveToXYZ(path.xCoord, path.yCoord, path.zCoord, homeSpeed);
					}
				}
				setHomeArea(homeX, homeY, homeZ, homeRange);
			}
		}
		if (isChilly && motionX * motionX + motionY * motionY + motionZ * motionZ >= 0.01) {
			double d = posX + MathHelper.randomFloatClamp(rand, -0.3f, 0.3f) * width;
			double d1 = boundingBox.minY + MathHelper.randomFloatClamp(rand, 0.2f, 0.7f) * height;
			double d2 = posZ + MathHelper.randomFloatClamp(rand, -0.3f, 0.3f) * width;
			LOTRMod.proxy.spawnParticle("chill", d, d1, d2, -motionX * 0.5, 0.0, -motionZ * 0.5);
		}
	}

	private void updateCombat() {
		EntityLivingBase entity;
		if (!worldObj.isRemote && getAttackTarget() != null && (!(entity = getAttackTarget()).isEntityAlive() || entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
			this.setAttackTarget(null);
		}
		boolean changedMounted = false;
		boolean changedAttackMode = false;
		if (!worldObj.isRemote) {
			boolean isRidingMountNow;
			isRidingMountNow = ridingEntity instanceof EntityLiving && ridingEntity.isEntityAlive() && !(ridingEntity instanceof LOTREntityNPC);
			if (ridingMount != isRidingMountNow) {
				setRidingHorse(isRidingMountNow);
				changedMounted = true;
			}
		}
		if (!worldObj.isRemote && !isChild()) {
			boolean carryingSpearWithBackup;
			ItemStack weapon = getEquipmentInSlot(0);
			carryingSpearWithBackup = weapon != null && weapon.getItem() instanceof LOTRItemSpear && npcItemsInv.getSpearBackup() != null;
			if (getAttackTarget() != null) {
				double d = getDistanceSqToEntity(getAttackTarget());
				if (d < getMeleeRangeSq() || carryingSpearWithBackup) {
					if (currentAttackMode != AttackMode.MELEE) {
						currentAttackMode = AttackMode.MELEE;
						changedAttackMode = true;
					}
				} else if (d < getMaxCombatRangeSq() && currentAttackMode != AttackMode.RANGED) {
					currentAttackMode = AttackMode.RANGED;
					changedAttackMode = true;
				}
			} else if (currentAttackMode != AttackMode.IDLE) {
				currentAttackMode = AttackMode.IDLE;
				changedAttackMode = true;
			}
			if (!firstUpdatedAttackMode) {
				firstUpdatedAttackMode = true;
				changedAttackMode = true;
			}
		}
		if (changedAttackMode || changedMounted) {
			onAttackModeChange(currentAttackMode, ridingMount);
		}
		if (!worldObj.isRemote) {
			prevCombatStance = combatCooldown > 0;
			if (getAttackTarget() != null) {
				combatCooldown = 40;
			} else if (combatCooldown > 0) {
				--combatCooldown;
			}
			combatStance = combatCooldown > 0;
			if (combatStance != prevCombatStance) {
				int x = MathHelper.floor_double(posX) >> 4;
				int z = MathHelper.floor_double(posZ) >> 4;
				PlayerManager playermanager = ((WorldServer) worldObj).getPlayerManager();
				List players = worldObj.playerEntities;
				for (Object obj : players) {
					EntityPlayerMP entityplayer = (EntityPlayerMP) obj;
					if (!playermanager.isPlayerWatchingChunk(entityplayer, x, z)) {
						continue;
					}
					sendCombatStance(entityplayer);
				}
			}
		}
	}

	protected void onAttackModeChange(AttackMode mode, boolean mounted) {
	}

	public void refreshCurrentAttackMode() {
		onAttackModeChange(currentAttackMode, ridingMount);
	}

	protected AttackMode getCurrentAttackMode() {
		return currentAttackMode;
	}

	public double getMeleeRange() {
		double d = 4.0 + width * width;
		if (ridingMount) {
			d *= MOUNT_RANGE_BONUS;
		}
		return d;
	}

	public final double getMeleeRangeSq() {
		double d = getMeleeRange();
		return d * d;
	}

	public final double getMaxCombatRange() {
		double d = getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		return d * 0.95;
	}

	public final double getMaxCombatRangeSq() {
		double d = getMaxCombatRange();
		return d * d;
	}

	public final boolean isAimingRanged() {
		Item item;
		ItemStack itemstack = getHeldItem();
		if (itemstack != null && !((item = itemstack.getItem()) instanceof LOTRItemSpear) && !(item instanceof LOTRItemTrident) && itemstack.getItemUseAction() == EnumAction.bow) {
			EntityLivingBase target = getAttackTarget();
			return target != null && getDistanceSqToEntity(target) < getMaxCombatRangeSq();
		}
		return false;
	}

	private void sendCombatStance(EntityPlayerMP entityplayer) {
		LOTRPacketNPCCombatStance packet = new LOTRPacketNPCCombatStance(getEntityId(), combatStance);
		LOTRPacketHandler.networkWrapper.sendTo(packet, entityplayer);
	}

	public void sendIsEatingToWatchers() {
		int x = MathHelper.floor_double(posX) >> 4;
		int z = MathHelper.floor_double(posZ) >> 4;
		PlayerManager playermanager = ((WorldServer) worldObj).getPlayerManager();
		List players = worldObj.playerEntities;
		for (Object obj : players) {
			EntityPlayerMP entityplayer = (EntityPlayerMP) obj;
			if (!playermanager.isPlayerWatchingChunk(entityplayer, x, z)) {
				continue;
			}
			sendIsEatingPacket(entityplayer);
		}
	}

	private void sendIsEatingPacket(EntityPlayerMP entityplayer) {
		LOTRPacketNPCIsEating packet = new LOTRPacketNPCIsEating(getEntityId(), npcItemsInv.getIsEating());
		LOTRPacketHandler.networkWrapper.sendTo(packet, entityplayer);
	}

	@Override
	protected void fall(float f) {
		if (bossInfo != null) {
			f = bossInfo.onFall(f);
		}
		super.fall(f);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		familyInfo.writeToNBT(nbt);
		questInfo.writeToNBT(nbt);
		hiredNPCInfo.writeToNBT(nbt);
		traderNPCInfo.writeToNBT(nbt);
		if (travellingTraderInfo != null) {
			travellingTraderInfo.writeToNBT(nbt);
		}
		if (bossInfo != null) {
			bossInfo.writeToNBT(nbt);
		}
		npcItemsInv.writeToNBT(nbt);
		hiredReplacedInv.writeToNBT(nbt);
		nbt.setInteger("NPCHomeX", getHomePosition().posX);
		nbt.setInteger("NPCHomeY", getHomePosition().posY);
		nbt.setInteger("NPCHomeZ", getHomePosition().posZ);
		nbt.setInteger("NPCHomeRadius", (int) func_110174_bM());
		nbt.setBoolean("NPCPersistent", isNPCPersistent);
		if (npcLocationName != null) {
			nbt.setString("NPCLocationName", npcLocationName);
		}
		nbt.setBoolean("SpecificLocationName", hasSpecificLocationName);
		nbt.setBoolean("HurtOnlyByPlates", hurtOnlyByPlates);
		nbt.setBoolean("RidingHorse", ridingMount);
		nbt.setBoolean("NPCPassive", isPassive);
		nbt.setBoolean("TraderEscort", isTraderEscort);
		if (!killBonusFactions.isEmpty()) {
			NBTTagList bonusTags = new NBTTagList();
			for (LOTRFaction f : killBonusFactions) {
				String fName = f.codeName();
				bonusTags.appendTag(new NBTTagString(fName));
			}
			nbt.setTag("BonusFactions", bonusTags);
		}
		if (invasionID != null) {
			nbt.setString("InvasionID", invasionID.toString());
		}
		nbt.setBoolean("SetInitHome", setInitialHome);
		nbt.setInteger("InitHomeX", initHomeX);
		nbt.setInteger("InitHomeY", initHomeY);
		nbt.setInteger("InitHomeZ", initHomeZ);
		nbt.setInteger("InitHomeR", initHomeRange);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		loadingFromNBT = true;
		super.readEntityFromNBT(nbt);
		familyInfo.readFromNBT(nbt);
		questInfo.readFromNBT(nbt);
		hiredNPCInfo.readFromNBT(nbt);
		traderNPCInfo.readFromNBT(nbt);
		if (travellingTraderInfo != null) {
			travellingTraderInfo.readFromNBT(nbt);
		}
		if (bossInfo != null) {
			bossInfo.readFromNBT(nbt);
		}
		npcItemsInv.readFromNBT(nbt);
		hiredReplacedInv.readFromNBT(nbt);
		if (nbt.hasKey("NPCHomeRadius")) {
			int x = nbt.getInteger("NPCHomeX");
			int y = nbt.getInteger("NPCHomeY");
			int z = nbt.getInteger("NPCHomeZ");
			int r = nbt.getInteger("NPCHomeRadius");
			setHomeArea(x, y, z, r);
		}
		if (nbt.hasKey("NPCPersistent")) {
			isNPCPersistent = nbt.getBoolean("NPCPersistent");
		}
		if (nbt.hasKey("NPCLocationName")) {
			npcLocationName = nbt.getString("NPCLocationName");
		}
		hasSpecificLocationName = nbt.getBoolean("SpecificLocationName");
		hurtOnlyByPlates = nbt.getBoolean("HurtOnlyByPlates");
		ridingMount = nbt.getBoolean("RidingHorse");
		isPassive = nbt.getBoolean("NPCPassive");
		isTraderEscort = nbt.getBoolean("TraderEscort");
		if (nbt.hasKey("BonusFactions")) {
			NBTTagList bonusTags = nbt.getTagList("BonusFactions", 8);
			for (int i = 0; i < bonusTags.tagCount(); ++i) {
				String fName = bonusTags.getStringTagAt(i);
				LOTRFaction f = LOTRFaction.forName(fName);
				if (f == null) {
					continue;
				}
				killBonusFactions.add(f);
			}
		}
		if (nbt.hasKey("InvasionID")) {
			String invID = nbt.getString("InvasionID");
			try {
				invasionID = UUID.fromString(invID);
			} catch (IllegalArgumentException e) {
				FMLLog.warning("LOTR: Error loading NPC - %s is not a valid invasion UUID", invID);
				e.printStackTrace();
			}
		}
		setInitialHome = nbt.getBoolean("SetInitHome");
		initHomeX = nbt.getInteger("InitHomeX");
		initHomeY = nbt.getInteger("InitHomeY");
		initHomeZ = nbt.getInteger("InitHomeZ");
		initHomeRange = nbt.getInteger("InitHomeR");
		loadingFromNBT = false;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		int id = LOTREntities.getEntityID(this);
		if (LOTREntities.spawnEggs.containsKey(id)) {
			return new ItemStack(LOTRMod.spawnEgg, 1, id);
		}
		return null;
	}

	@Override
	public boolean attackEntityAsMob(Entity entity) {
		boolean flag;
		float damage = (float) getEntityAttribute(npcAttackDamage).getAttributeValue();
		float weaponDamage = 0.0f;
		ItemStack weapon = getEquipmentInSlot(0);
		if (weapon != null) {
			weaponDamage = LOTRWeaponStats.getMeleeDamageBonus(weapon) * 0.75f;
		}
		if (weaponDamage > 0.0f) {
			damage = weaponDamage;
		}
		damage += (float) getEntityAttribute(npcAttackDamageExtra).getAttributeValue();
		if (isDrunkard()) {
			damage += (float) getEntityAttribute(npcAttackDamageDrunk).getAttributeValue();
		}
		damage += nearbyBannerFactor * 0.5f;
		int knockbackModifier = 0;
		if (entity instanceof EntityLivingBase) {
			damage += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase) entity);
			knockbackModifier += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase) entity);
		}
		if (flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), damage)) {
			int fireAspectModifier;
			if (weapon != null && entity instanceof EntityLivingBase) {
				int weaponItemDamage = weapon.getItemDamage();
				weapon.getItem().hitEntity(weapon, (EntityLivingBase) entity, (EntityLivingBase) this);
				weapon.setItemDamage(weaponItemDamage);
			}
			if (knockbackModifier > 0) {
				entity.addVelocity(-MathHelper.sin(rotationYaw * 3.1415927f / 180.0f) * knockbackModifier * 0.5f, 0.1, MathHelper.cos(rotationYaw * 3.1415927f / 180.0f) * knockbackModifier * 0.5f);
				motionX *= 0.6;
				motionZ *= 0.6;
			}
			if ((fireAspectModifier = EnchantmentHelper.getFireAspectModifier(this)) > 0) {
				entity.setFire(fireAspectModifier * 4);
			}
			if (entity instanceof EntityLivingBase) {
				EnchantmentHelper.func_151384_a((EntityLivingBase) entity, this);
			}
			EnchantmentHelper.func_151385_b(this, entity);
		}
		return flag;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float f) {
		npcArrowAttack(target, f);
	}

	protected void npcArrowAttack(EntityLivingBase target, float f) {
		ItemStack heldItem = getHeldItem();
		float str = 1.3f + getDistanceToEntity(target) / 80.0f;
		float accuracy = (float) getEntityAttribute(npcRangedAccuracy).getAttributeValue();
		float poisonChance = getPoisonedArrowChance();
		EntityArrow arrow = rand.nextFloat() < poisonChance ? new LOTREntityArrowPoisoned(worldObj, this, target, str, accuracy) : new EntityArrow(worldObj, this, target, str *= LOTRItemBow.getLaunchSpeedFactor(heldItem), accuracy);
		if (heldItem != null) {
			LOTRItemBow.applyBowModifiers(arrow, heldItem);
		}
		playSound("random.bow", 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.8f));
		worldObj.spawnEntityInWorld(arrow);
	}

	protected void npcCrossbowAttack(EntityLivingBase target, float f) {
		ItemStack heldItem = getHeldItem();
		float str = 1.0f + getDistanceToEntity(target) / 16.0f * 0.015f;
		boolean poison = rand.nextFloat() < getPoisonedArrowChance();
		ItemStack boltItem = poison ? new ItemStack(LOTRMod.crossbowBoltPoisoned) : new ItemStack(LOTRMod.crossbowBolt);
		LOTREntityCrossbowBolt bolt = new LOTREntityCrossbowBolt(worldObj, this, target, boltItem, str *= LOTRItemCrossbow.getCrossbowLaunchSpeedFactor(heldItem), 1.0f);
		if (heldItem != null) {
			LOTRItemCrossbow.applyCrossbowModifiers(bolt, heldItem);
		}
		playSound("lotr:item.crossbow", 1.0f, 1.0f / (rand.nextFloat() * 0.4f + 0.8f));
		worldObj.spawnEntityInWorld(bolt);
	}

	protected float getPoisonedArrowChance() {
		return 0.0f;
	}

	@Override
	public void onKillEntity(EntityLivingBase entity) {
		super.onKillEntity(entity);
		hiredNPCInfo.onKillEntity(entity);
		if (lootsExtraCoins() && !worldObj.isRemote && entity instanceof LOTREntityNPC && ((LOTREntityNPC) entity).canDropRares() && rand.nextInt(2) == 0) {
			int coins = getRandomCoinDropAmount();
			if ((coins = (int) (coins * MathHelper.randomFloatClamp(rand, 1.0f, 3.0f))) > 0) {
				entity.dropItem(LOTRMod.silverCoin, coins);
			}
		}
	}

	public boolean lootsExtraCoins() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource damagesource, float f) {
		boolean flag;
		if (riddenByEntity != null && damagesource.getEntity() == riddenByEntity) {
			return false;
		}
		if (nearbyBannerFactor > 0) {
			int i = 12 - nearbyBannerFactor;
			float f1 = f * i;
			f = f1 / 12.0f;
		}
		if ((flag = super.attackEntityFrom(damagesource, f)) && damagesource.getEntity() instanceof LOTREntityNPC) {
			LOTREntityNPC attacker = (LOTREntityNPC) damagesource.getEntity();
			if (attacker.hiredNPCInfo.isActive && attacker.hiredNPCInfo.getHiringPlayer() != null) {
				recentlyHit = 100;
				attackingPlayer = null;
			}
		}
		if (flag && !worldObj.isRemote && hurtOnlyByPlates) {
			hurtOnlyByPlates = damagesource.getSourceOfDamage() instanceof LOTREntityPlate;
		}
		if (flag && !worldObj.isRemote && isInvasionSpawned() && damagesource.getEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
			LOTREntityInvasionSpawner invasion = LOTREntityInvasionSpawner.locateInvasionNearby(this, invasionID);
			if (invasion != null) {
				invasion.setWatchingInvasion((EntityPlayerMP) entityplayer, true);
			}
		}
		return flag;
	}

	@Override
	protected void damageEntity(DamageSource damagesource, float f) {
		super.damageEntity(damagesource, f);
		if (bossInfo != null) {
			bossInfo.onHurt(damagesource, f);
		}
	}

	@Override
	public final boolean canPickUpLoot() {
		return false;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		hiredReplacedInv.dropAllReplacedItems();
		dropNPCEquipment(flag, i);
		if (flag && canDropRares()) {
			int coinChance = 8 - i * 2;
			if (rand.nextInt(coinChance = Math.max(coinChance, 1)) == 0) {
				int coins = getRandomCoinDropAmount();
				dropItem(LOTRMod.silverCoin, coins *= MathHelper.getRandomIntegerInRange(rand, 1, i + 1));
			}
			int rareChance = 50 - i * 5;
			if (rand.nextInt(rareChance = Math.max(rareChance, 1)) == 0) {
				dropChestContents(LOTRChestContents.RARE_DROPS, 1, 1);
			}
		}
		if (flag && canDropRares()) {
			int modChance = 60;
			modChance -= i * 5;
			if (rand.nextInt(modChance = Math.max(modChance, 1)) == 0) {
				ItemStack modItem = LOTRItemModifierTemplate.getRandomCommonTemplate(rand);
				entityDropItem(modItem, 0.0f);
			}
		}
		if (getFaction() == LOTRFaction.UTUMNO && LOTRDimension.getCurrentDimensionWithFallback(worldObj) == LOTRDimension.UTUMNO) {
			LOTRUtumnoLevel level = LOTRUtumnoLevel.forY((int) posY);
			if (rand.nextInt(12) == 0) {
				int l;
				if (level == LOTRUtumnoLevel.ICE) {
					ItemStack keypart = new ItemStack(LOTRMod.utumnoKey);
					l = rand.nextInt(3);
					if (l == 0) {
						keypart.setItemDamage(2);
					} else if (l == 1) {
						keypart.setItemDamage(3);
					} else if (l == 2) {
						keypart.setItemDamage(4);
					}
					entityDropItem(keypart, 0.0f);
				} else if (level == LOTRUtumnoLevel.OBSIDIAN) {
					ItemStack keypart = new ItemStack(LOTRMod.utumnoKey);
					l = rand.nextInt(3);
					if (l == 0) {
						keypart.setItemDamage(5);
					} else if (l == 1) {
						keypart.setItemDamage(6);
					} else if (l == 2) {
						keypart.setItemDamage(7);
					}
					entityDropItem(keypart, 0.0f);
				}
			}
			if (level == LOTRUtumnoLevel.ICE && isChilly) {
				int chillChance = 30;
				chillChance -= i * 3;
				if (rand.nextInt(chillChance = Math.max(chillChance, 1)) == 0) {
					int chills = 1;
					if (i > 0) {
						float x = MathHelper.randomFloatClamp(rand, 0.0f, i * 0.667f);
						while (x > 1.0f) {
							x -= 1.0f;
							++chills;
						}
						if (rand.nextFloat() < x) {
							++chills;
						}
					}
					for (int l = 0; l < chills; ++l) {
						dropItem(LOTRMod.chilling, 1);
					}
				}
			}
			if (level == LOTRUtumnoLevel.FIRE && canDropRares()) {
				int pickChance = 100;
				pickChance -= i * 20;
				if (rand.nextInt(pickChance = Math.max(pickChance, 1)) == 0) {
					entityDropItem(new ItemStack(LOTRMod.utumnoPickaxe), 0.0f);
				}
			}
			if (rand.nextInt(20) == 0) {
				entityDropItem(new ItemStack(LOTRMod.mithrilNugget), 0.0f);
			}
		}
	}

	protected int getRandomCoinDropAmount() {
		return 1 + (int) Math.round(Math.pow(1.0 + Math.abs(rand.nextGaussian()), 3.0) * 0.25);
	}

	public void dropNPCEquipment(boolean flag, int i) {
		if (flag) {
			int j;
			int equipmentCount = 0;
			for (j = 0; j < 5; ++j) {
				if (getEquipmentInSlot(j) == null) {
					continue;
				}
				++equipmentCount;
			}
			if (equipmentCount > 0) {
				for (j = 0; j < 5; ++j) {
					boolean dropGuaranteed;
					ItemStack equipmentDrop = getEquipmentInSlot(j);
					if (equipmentDrop == null) {
						continue;
					}
					dropGuaranteed = equipmentDropChances[j] >= 1.0f;
					if (!dropGuaranteed) {
						int chance = 20 * equipmentCount - i * 4 * equipmentCount;
						if (rand.nextInt(chance = Math.max(chance, 1)) != 0) {
							continue;
						}
					}
					if (!dropGuaranteed) {
						int dropDamage = MathHelper.floor_double(equipmentDrop.getItem().getMaxDamage() * (0.5f + rand.nextFloat() * 0.25f));
						equipmentDrop.setItemDamage(dropDamage);
					}
					entityDropItem(equipmentDrop, 0.0f);
					setCurrentItemOrArmor(j, null);
				}
			}
		}
	}

	protected void dropChestContents(LOTRChestContents itemPool, int min, int max) {
		InventoryBasic drops = new InventoryBasic("drops", false, max * 5);
		LOTRChestContents.fillInventory(drops, rand, itemPool, MathHelper.getRandomIntegerInRange(rand, min, max), true);
		for (int i = 0; i < drops.getSizeInventory(); ++i) {
			ItemStack item = drops.getStackInSlot(i);
			if (item == null) {
				continue;
			}
			entityDropItem(item, 0.0f);
		}
	}

	protected void dropNPCArrows(int i) {
		dropNPCAmmo(Items.arrow, i);
	}

	protected void dropNPCCrossbowBolts(int i) {
		dropNPCAmmo(LOTRMod.crossbowBolt, i);
	}

	protected void dropNPCAmmo(Item item, int i) {
		int ammo = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < ammo; ++l) {
			dropItem(item, 1);
		}
	}

	@Override
	public final void dropEquipment(boolean flag, int i) {
	}

	@Override
	public final EntityItem entityDropItem(ItemStack item, float offset) {
		return npcDropItem(item, offset, true, true);
	}

	public final EntityItem npcDropItem(ItemStack item, float offset, boolean enpouch, boolean applyOwnership) {
		if (applyOwnership && item != null && item.getItem() != null && item.getMaxStackSize() == 1) {
			LOTRItemOwnership.addPreviousOwner(item, getCommandSenderName());
		}
		if (enpouch && enpouchNPCDrops && item != null) {
			enpouchedDrops.add(item);
			return null;
		}
		return super.entityDropItem(item, offset);
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		EntityPlayer entityplayer;
		LOTREntityInvasionSpawner invasion;
		enpouchNPCDrops = true;
		hiredNPCInfo.onDeath(damagesource);
		if (travellingTraderInfo != null) {
			travellingTraderInfo.onDeath();
		}
		if (bossInfo != null) {
			bossInfo.onDeath(damagesource);
		}
		super.onDeath(damagesource);
		if (!worldObj.isRemote && recentlyHit > 0 && canDropRares() && LOTRMod.canDropLoot(worldObj) && rand.nextInt(60) == 0) {
			ItemStack pouch = createNPCPouchDrop();
			fillPouchFromListAndRetainUnfilled(pouch, enpouchedDrops);
			enpouchNPCDrops = false;
			entityDropItem(pouch, 0.0f);
		}
		enpouchNPCDrops = false;
		this.dropItemList(enpouchedDrops, false);
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer) {
			entityplayer = (EntityPlayer) damagesource.getEntity();
			if (hurtOnlyByPlates && damagesource.getSourceOfDamage() instanceof LOTREntityPlate) {
				if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) < 0.0f) {
					// empty if block
				}
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killUsingOnlyPlates);
			}
			if (damagesource.getSourceOfDamage() instanceof LOTREntityPebble && ((LOTREntityPebble) damagesource.getSourceOfDamage()).isSling() && width * width * height > 5.0f && LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) < 0.0f) {
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killLargeMobWithSlingshot);
			}
			if (this instanceof LOTREntityOrc) {
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killOrc);
			}
			if (this instanceof LOTREntityWarg) {
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killWarg);
			}
			if (getKillAchievement() != null) {
				LOTRLevelData.getData(entityplayer).addAchievement(getKillAchievement());
			}
		}
		if (!worldObj.isRemote) {
			boolean flag = false;
			if (this instanceof LOTRTradeable) {
				flag = ((LOTRTradeable) this).shouldTraderRespawn();
			} else if (this instanceof LOTRUnitTradeable) {
				flag = ((LOTRUnitTradeable) this).shouldTraderRespawn();
			}
			if (flag) {
				LOTREntityTraderRespawn entity = new LOTREntityTraderRespawn(worldObj);
				entity.setLocationAndAngles(posX, boundingBox.minY + height / 2.0f, posZ, 0.0f, 0.0f);
				entity.copyTraderDataFrom(this);
				worldObj.spawnEntityInWorld(entity);
				entity.onSpawn();
			}
		}
		questInfo.onDeath();
		if (!worldObj.isRemote && isInvasionSpawned() && (entityplayer = LOTRMod.getDamagingPlayerIncludingUnits(damagesource)) != null && (invasion = LOTREntityInvasionSpawner.locateInvasionNearby(this, invasionID)) != null) {
			invasion.addPlayerKill(entityplayer);
			if (damagesource.getEntity() == entityplayer) {
				invasion.setWatchingInvasion((EntityPlayerMP) entityplayer, true);
			}
		}
	}

	public ItemStack createNPCPouchDrop() {
		LOTRFaction faction;
		ItemStack pouch = new ItemStack(LOTRMod.pouch, 1, LOTRItemPouch.getRandomPouchSize(rand));
		if (rand.nextBoolean() && (faction = getFaction()) != null) {
			LOTRItemPouch.setPouchColor(pouch, faction.getFactionColor());
		}
		return pouch;
	}

	public void fillPouchFromListAndRetainUnfilled(ItemStack pouch, List<ItemStack> items) {
		ArrayList<ItemStack> pouchContents = new ArrayList<>();
		while (!items.isEmpty()) {
			pouchContents.add(items.remove(0));
			if (pouchContents.size() < LOTRItemPouch.getCapacity(pouch)) {
				continue;
			}
		}
		for (ItemStack itemstack : pouchContents) {
			if (LOTRItemPouch.tryAddItemToPouch(pouch, itemstack, false)) {
				continue;
			}
			items.add(itemstack);
		}
	}

	public void dropItemList(List<ItemStack> items) {
		this.dropItemList(items, true);
	}

	public void dropItemList(List<ItemStack> items, boolean applyOwnership) {
		if (!items.isEmpty()) {
			for (ItemStack item : items) {
				npcDropItem(item, 0.0f, true, applyOwnership);
			}
		}
	}

	protected LOTRAchievement getKillAchievement() {
		return null;
	}

	@Override
	public void setDead() {
		super.setDead();
		if (deathTime == 0 && ridingEntity != null) {
			ridingEntity.setDead();
		}
	}

	public boolean canDropRares() {
		return !hiredNPCInfo.isActive;
	}

	public float getAlignmentBonus() {
		return 0.0f;
	}

	@Override
	protected int getExperiencePoints(EntityPlayer entityplayer) {
		return 4 + rand.nextInt(3);
	}

	@Override
	public float getBlockPathWeight(int i, int j, int k) {
		if (liftSpawnRestrictions) {
			return 1.0f;
		}
		if (!isConquestSpawning || !conquestSpawnIgnoresDarkness()) {
			BiomeGenBase biome;
			if (spawnsInDarkness && (biome = worldObj.getBiomeGenForCoords(i, k)) instanceof LOTRBiome && ((LOTRBiome) biome).canSpawnHostilesInDay()) {
				return 1.0f;
			}
			if (spawnsInDarkness) {
				return 0.5f - worldObj.getLightBrightness(i, j, k);
			}
		}
		return 0.0f;
	}

	private boolean isValidLightLevelForDarkSpawn() {
		BiomeGenBase biome;
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(boundingBox.minY);
		int k = MathHelper.floor_double(posZ);
		if (spawnsInDarkness && (biome = worldObj.getBiomeGenForCoords(i, k)) instanceof LOTRBiome && ((LOTRBiome) biome).canSpawnHostilesInDay()) {
			return true;
		}
		if (worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > rand.nextInt(32)) {
			return false;
		}
		int l = worldObj.getBlockLightValue(i, j, k);
		if (worldObj.isThundering()) {
			int i1 = worldObj.skylightSubtracted;
			worldObj.skylightSubtracted = 10;
			l = worldObj.getBlockLightValue(i, j, k);
			worldObj.skylightSubtracted = i1;
		}
		return l <= rand.nextInt(8);
	}

	public void setConquestSpawning(boolean flag) {
		isConquestSpawning = flag;
	}

	protected boolean conquestSpawnIgnoresDarkness() {
		return true;
	}

	@Override
	public boolean getCanSpawnHere() {
		if ((!spawnsInDarkness || liftSpawnRestrictions || isConquestSpawning && conquestSpawnIgnoresDarkness() || isValidLightLevelForDarkSpawn()) && super.getCanSpawnHere()) {
			if (!liftBannerRestrictions) {
				if (LOTRBannerProtection.isProtected(worldObj, this, LOTRBannerProtection.forNPC(this), false)) {
					return false;
				}
				if (!isConquestSpawning && LOTREntityNPCRespawner.isSpawnBlocked(this)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public final int getSpawnCountValue() {
		if (isNPCPersistent || hiredNPCInfo.isActive) {
			return 0;
		}
		int multiplier = 1;
		BiomeGenBase biome = worldObj.getBiomeGenForCoords(MathHelper.floor_double(posX), MathHelper.floor_double(posZ));
		if (biome instanceof LOTRBiome) {
			multiplier = ((LOTRBiome) biome).spawnCountMultiplier();
		}
		return multiplier;
	}

	@Override
	public boolean interact(EntityPlayer entityplayer) {
		if (!worldObj.isRemote && canNPCTalk()) {
			if (questInfo.interact(entityplayer)) {
				return true;
			}
			if (getAttackTarget() == null && speakTo(entityplayer)) {
				return true;
			}
		}
		return super.interact(entityplayer);
	}

	public void sendSpeechBank(EntityPlayer entityplayer, String speechBank) {
		this.sendSpeechBank(entityplayer, speechBank, null);
	}

	public void sendSpeechBank(EntityPlayer entityplayer, String speechBank, LOTRMiniQuest miniquest) {
		String location = null;
		String objective = null;
		if (npcLocationName != null) {
			location = !hasSpecificLocationName ? StatCollector.translateToLocalFormatted(npcLocationName, getNPCName()) : npcLocationName;
		}
		if (miniquest != null) {
			objective = miniquest.getProgressedObjectiveInSpeech();
		}
		this.sendSpeechBank(entityplayer, speechBank, location, objective);
	}

	public void sendSpeechBank(EntityPlayer entityplayer, String speechBank, String location, String objective) {
		LOTRSpeech.sendSpeech(entityplayer, this, LOTRSpeech.getRandomSpeechForPlayer(this, speechBank, entityplayer, location, objective));
		markNPCSpoken();
	}

	public void sendSpeechBankLine(EntityPlayer entityplayer, String speechBank, int i) {
		LOTRSpeech.sendSpeech(entityplayer, this, LOTRSpeech.getSpeechLineForPlayer(this, speechBank, i, entityplayer, null, null));
		markNPCSpoken();
	}

	public boolean isFriendly(EntityPlayer entityplayer) {
		return LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 0.0f && getAttackTarget() != entityplayer && attackingPlayer != entityplayer;
	}

	public String getSpeechBank(EntityPlayer entityplayer) {
		return null;
	}

	public boolean speakTo(EntityPlayer entityplayer) {
		String speechBank = getSpeechBank(entityplayer);
		if (rand.nextInt(8) == 0) {
			if (LOTRMod.isChristmas()) {
				speechBank = "special/christmas";
			} else if (LOTRMod.isNewYearsDay()) {
				speechBank = "special/newYear";
			} else if (LOTRMod.isAprilFools()) {
				speechBank = "special/aprilFool";
			} else if (LOTRMod.isHalloween()) {
				speechBank = "special/halloween";
			}
		}
		if (rand.nextInt(10000) == 0) {
			speechBank = "special/smilebc";
		}
		if (speechBank != null) {
			this.sendSpeechBank(entityplayer, speechBank);
			if (getTalkAchievement() != null) {
				LOTRLevelData.getData(entityplayer).addAchievement(getTalkAchievement());
			}
			return true;
		}
		return false;
	}

	protected LOTRAchievement getTalkAchievement() {
		return null;
	}

	public LOTRMiniQuest createMiniQuest() {
		return null;
	}

	public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
		return null;
	}

	public int getMiniquestColor() {
		return getFaction().getFactionColor();
	}

	public void onArtificalSpawn() {
	}

	public boolean isDrunkard() {
		return familyInfo.isDrunk();
	}

	public boolean canGetDrunk() {
		if (isChild()) {
			return false;
		}
		return !isTrader() && !isTraderEscort && !hiredNPCInfo.isActive;
	}

	public float getDrunkenSpeechFactor() {
		if (rand.nextInt(3) == 0) {
			return MathHelper.randomFloatClamp(rand, 0.0f, 0.3f);
		}
		return 0.0f;
	}

	public boolean shouldRenderNPCHair() {
		return true;
	}

	public boolean shouldRenderNPCChest() {
		return !familyInfo.isMale() && !isChild() && getEquipmentInSlot(3) == null;
	}

	public boolean canReEquipHired(int slot, ItemStack itemstack) {
		return true;
	}

	public void setSpecificLocationName(String name) {
		npcLocationName = name;
		hasSpecificLocationName = true;
	}

	public boolean getHasSpecificLocationName() {
		return hasSpecificLocationName;
	}

	private void updateNearbyBanners() {
		if (getFaction() == LOTRFaction.UNALIGNED) {
			nearbyBannerFactor = 0;
		} else {
			double range = 16.0;
			List bannerBearers = worldObj.selectEntitiesWithinAABB(LOTRBannerBearer.class, boundingBox.expand(range, range, range), new IEntitySelector() {

				@Override
				public boolean isEntityApplicable(Entity entity) {
					EntityLivingBase living = (EntityLivingBase) entity;
					return living != LOTREntityNPC.this && living.isEntityAlive() && LOTRMod.getNPCFaction(living) == LOTREntityNPC.this.getFaction();
				}
			});
			nearbyBannerFactor = Math.min(bannerBearers.size(), 5);
		}
	}

	@Override
	public final ItemStack getEquipmentInSlot(int i) {
		if (worldObj.isRemote) {
			if (!initFestiveItems) {
				festiveRand.setSeed(getEntityId() * 341873128712L);
				if (LOTRMod.isHalloween()) {
					if (festiveRand.nextInt(3) == 0) {
						festiveItems[4] = festiveRand.nextInt(10) == 0 ? new ItemStack(Blocks.lit_pumpkin) : new ItemStack(Blocks.pumpkin);
					}
				} else if (LOTRMod.isChristmas() && festiveRand.nextInt(3) == 0) {
					ItemStack hat;
					if (rand.nextBoolean()) {
						hat = new ItemStack(LOTRMod.leatherHat);
						LOTRItemLeatherHat.setHatColor(hat, 13378587);
						LOTRItemLeatherHat.setFeatherColor(hat, 16777215);
						festiveItems[4] = hat;
					} else {
						hat = new ItemStack(LOTRMod.partyHat);
						float hue = rand.nextFloat();
						LOTRItemPartyHat.setHatColor(hat, Color.HSBtoRGB(hue, 1.0f, 1.0f));
					}
					festiveItems[4] = hat;
				}
				initFestiveItems = true;
			}
			if (festiveItems[i] != null) {
				return festiveItems[i];
			}
		}
		return super.getEquipmentInSlot(i);
	}

	@Override
	public final ItemStack func_130225_q(int i) {
		return getEquipmentInSlot(i + 1);
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	public void setCustomNameTag(String name) {
		if (canRenameNPC() || loadingFromNBT) {
			super.setCustomNameTag(name);
		}
	}

	public boolean canRenameNPC() {
		return false;
	}

	@Override
	public void func_110163_bv() {
	}

	@Override
	public boolean shouldDismountInWater(Entity rider) {
		return false;
	}

	public void spawnHearts() {
		if (!worldObj.isRemote) {
			LOTRPacketNPCFX packet = new LOTRPacketNPCFX(getEntityId(), LOTRPacketNPCFX.FXType.HEARTS);
			LOTRPacketHandler.networkWrapper.sendToAllAround(packet, LOTRPacketHandler.nearEntity(this, 32.0));
		} else {
			for (int i = 0; i < 8; ++i) {
				double d = rand.nextGaussian() * 0.02;
				double d1 = rand.nextGaussian() * 0.02;
				double d2 = rand.nextGaussian() * 0.02;
				worldObj.spawnParticle("heart", posX + rand.nextFloat() * width * 2.0f - width, posY + 0.5 + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0f - width, d, d1, d2);
			}
		}
	}

	public void spawnSmokes() {
		if (!worldObj.isRemote) {
			LOTRPacketNPCFX packet = new LOTRPacketNPCFX(getEntityId(), LOTRPacketNPCFX.FXType.SMOKE);
			LOTRPacketHandler.networkWrapper.sendToAllAround(packet, LOTRPacketHandler.nearEntity(this, 32.0));
		} else {
			for (int i = 0; i < 8; ++i) {
				double d = rand.nextGaussian() * 0.02;
				double d1 = rand.nextGaussian() * 0.02;
				double d2 = rand.nextGaussian() * 0.02;
				worldObj.spawnParticle("smoke", posX + rand.nextFloat() * width * 2.0f - width, posY + 0.5 + rand.nextFloat() * height, posZ + rand.nextFloat() * width * 2.0f - width, d, d1, d2);
			}
		}
	}

	public void spawnFoodParticles() {
		if (getHeldItem() == null) {
			return;
		}
		if (!worldObj.isRemote) {
			LOTRPacketNPCFX packet = new LOTRPacketNPCFX(getEntityId(), LOTRPacketNPCFX.FXType.EATING);
			LOTRPacketHandler.networkWrapper.sendToAllAround(packet, LOTRPacketHandler.nearEntity(this, 32.0));
		} else {
			for (int i = 0; i < 5; ++i) {
				Vec3 vec1 = Vec3.createVectorHelper((rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
				vec1.rotateAroundX(-rotationPitch * 3.1415927f / 180.0f);
				vec1.rotateAroundY(-rotationYaw * 3.1415927f / 180.0f);
				Vec3 vec2 = Vec3.createVectorHelper((rand.nextFloat() - 0.5) * 0.3, -rand.nextFloat() * 0.6 - 0.3, 0.6);
				vec2.rotateAroundX(-rotationPitch * 3.1415927f / 180.0f);
				vec2.rotateAroundY(-rotationYaw * 3.1415927f / 180.0f);
				vec2 = vec2.addVector(posX, posY + getEyeHeight(), posZ);
				worldObj.spawnParticle("iconcrack_" + Item.getIdFromItem(getHeldItem().getItem()), vec2.xCoord, vec2.yCoord, vec2.zCoord, vec1.xCoord, vec1.yCoord + 0.05, vec1.zCoord);
			}
		}
	}

	public ItemStack getHeldItemLeft() {
		if (this instanceof LOTRBannerBearer) {
			LOTRBannerBearer bannerBearer = (LOTRBannerBearer) this;
			return new ItemStack(LOTRMod.banner, 1, bannerBearer.getBannerType().bannerID);
		}
		if (isTrader()) {
			boolean showCoin = false;
			if (npcShield == null || !clientCombatStance && hiredNPCInfo.getHiringPlayerUUID() == null) {
				showCoin = true;
			}
			if (showCoin) {
				return new ItemStack(LOTRMod.silverCoin);
			}
		}
		return null;
	}

	public void playTradeSound() {
		playSound("lotr:event.trade", 0.5f, 1.0f + (rand.nextFloat() - rand.nextFloat()) * 0.1f);
	}

	public boolean isInvasionSpawned() {
		return getInvasionID() != null;
	}

	public UUID getInvasionID() {
		return invasionID;
	}

	public void setInvasionID(UUID id) {
		invasionID = id;
	}

	protected enum AttackMode {
		MELEE, RANGED, IDLE;

	}

}
