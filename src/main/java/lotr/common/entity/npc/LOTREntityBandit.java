package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.*;
import lotr.common.fac.LOTRFaction;
import lotr.common.inventory.LOTRInventoryNPC;
import lotr.common.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityBandit extends LOTREntityMan implements IBandit {
	private static ItemStack[] weapons = new ItemStack[] { new ItemStack(LOTRMod.daggerBronze), new ItemStack(LOTRMod.daggerIron) };
	private LOTRInventoryNPC banditInventory = IBandit.Helper.createInv(this);

	public LOTREntityBandit(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIAttackOnCollide(this, 1.5, false));
		tasks.addTask(2, new LOTREntityAIBanditSteal(this, 1.8));
		tasks.addTask(3, new LOTREntityAIBanditFlee(this, 1.5));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIWander(this, 1.5));
		tasks.addTask(6, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0f, 0.1f));
		tasks.addTask(6, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5.0f, 0.05f));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f, 0.02f));
		tasks.addTask(8, new EntityAILookIdle(this));
		this.addTargetTasks(true, LOTREntityAINearestAttackableTargetBandit.class);
	}

	@Override
	public LOTREntityNPC getBanditAsNPC() {
		return this;
	}

	@Override
	public int getMaxThefts() {
		return 3;
	}

	@Override
	public LOTRInventoryNPC getBanditInventory() {
		return banditInventory;
	}

	@Override
	public boolean canTargetPlayerForTheft(EntityPlayer player) {
		return true;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(weapons.length);
		npcItemsInv.setMeleeWeapon(weapons[i].copy());
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		if (rand.nextInt(3) == 0) {
			ItemStack hat = new ItemStack(LOTRMod.leatherHat);
			LOTRItemLeatherHat.setHatColor(hat, 0);
			LOTRItemLeatherHat.setFeatherColor(hat, 16777215);
			setCurrentItemOrArmor(4, hat);
		}
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}

	@Override
	public int getTotalArmorValue() {
		return 10;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.HOSTILE;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		banditInventory.writeToNBT(nbt);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		banditInventory.readFromNBT(nbt);
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int bones = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int l = 0; l < bones; ++l) {
			dropItem(Items.bone, 1);
		}
		int coins = 10 + rand.nextInt(10) + rand.nextInt((i + 1) * 10);
		for (int l = 0; l < coins; ++l) {
			dropItem(LOTRMod.silverCoin, 1);
		}
		if (rand.nextInt(5) == 0) {
			entityDropItem(LOTRItemMug.Vessel.SKULL.getEmptyVessel(), 0.0f);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer && !banditInventory.isEmpty()) {
			EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
			LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.killThievingBandit);
		}
		if (!worldObj.isRemote) {
			banditInventory.dropAllItems();
		}
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		return "misc/bandit/hostile";
	}

	@Override
	public String getTheftSpeechBank(EntityPlayer player) {
		return getSpeechBank(player);
	}

	@Override
	public IChatComponent getTheftChatMsg(EntityPlayer player) {
		return new ChatComponentTranslation("chat.lotr.banditSteal", new Object[0]);
	}
}
