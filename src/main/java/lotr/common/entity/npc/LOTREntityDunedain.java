package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.*;
import lotr.common.entity.animal.LOTREntityHorse;
import lotr.common.fac.LOTRFaction;
import lotr.common.quest.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class LOTREntityDunedain extends LOTREntityMan {
	private static ItemStack[] weapons = new ItemStack[] { new ItemStack(LOTRMod.daggerBarrow), new ItemStack(LOTRMod.daggerIron), new ItemStack(LOTRMod.daggerBronze), new ItemStack(Items.iron_axe), new ItemStack(LOTRMod.axeBronze), new ItemStack(Items.stone_axe) };

	public LOTREntityDunedain(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		getNavigator().setAvoidsWater(true);
		getNavigator().setBreakDoors(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new LOTREntityAIHiredRemainStill(this));
		tasks.addTask(2, createDunedainAttackAI());
		tasks.addTask(3, new LOTREntityAIFollowHiringPlayer(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIWander(this, 1.0));
		tasks.addTask(6, new LOTREntityAIEat(this, getDunedainFoods(), 8000));
		tasks.addTask(6, new LOTREntityAIDrink(this, getDunedainDrinks(), 8000));
		tasks.addTask(7, new EntityAIWatchClosest2(this, EntityPlayer.class, 8.0f, 0.02f));
		tasks.addTask(7, new EntityAIWatchClosest2(this, LOTREntityNPC.class, 5.0f, 0.02f));
		tasks.addTask(8, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f, 0.02f));
		tasks.addTask(9, new EntityAILookIdle(this));
		this.addTargetTasks(true);
	}

	protected EntityAIBase createDunedainAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.4, false);
	}

	protected LOTRFoods getDunedainFoods() {
		return LOTRFoods.RANGER;
	}

	protected LOTRFoods getDunedainDrinks() {
		return LOTRFoods.RANGER_DRINK;
	}

	@Override
	public LOTRNPCMount createMountToRide() {
		LOTREntityHorse horse = (LOTREntityHorse) super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorIron));
		return horse;
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(rand.nextBoolean());
	}

	@Override
	public void setupNPCName() {
		familyInfo.setName(LOTRNames.getGondorName(rand, familyInfo.isMale()));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(weapons.length);
		npcItemsInv.setMeleeWeapon(weapons[i].copy());
		npcItemsInv.setIdleItem(null);
		return data;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.RANGER_NORTH;
	}

	@Override
	public String getNPCName() {
		return familyInfo.getName();
	}

	@Override
	public String getNPCFormattedName(String npcName, String entityName) {
		if (this.getClass() == LOTREntityDunedain.class) {
			return StatCollector.translateToLocalFormatted("entity.lotr.Dunedain.entityName", npcName);
		}
		return super.getNPCFormattedName(npcName, entityName);
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
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int bones = rand.nextInt(2) + rand.nextInt(i + 1);
		for (int l = 0; l < bones; ++l) {
			dropItem(Items.bone, 1);
		}
		dropDunedainItems(flag, i);
	}

	protected void dropDunedainItems(boolean flag, int i) {
		if (rand.nextInt(6) == 0) {
			dropChestContents(LOTRChestContents.RANGER_HOUSE, 1, 2 + i);
		}
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killDunedain;
	}

	@Override
	public float getAlignmentBonus() {
		return 1.0f;
	}

	@Override
	public boolean getCanSpawnHere() {
		if (super.getCanSpawnHere()) {
			if (liftSpawnRestrictions) {
				return true;
			}
			int i = MathHelper.floor_double(posX);
			int j = MathHelper.floor_double(boundingBox.minY);
			int k = MathHelper.floor_double(posZ);
			Block block = worldObj.getBlock(i, j - 1, k);
			BiomeGenBase biome = worldObj.getBiomeGenForCoords(i, k);
			if (j > 62 && (block == biome.topBlock || block == Blocks.grass || block == Blocks.sand)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public float getBlockPathWeight(int i, int j, int k) {
		float f = 0.0f;
		worldObj.getBiomeGenForCoords(i, k);
		return f += 20.0f;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isDrunkard()) {
			return "rangerNorth/drunkard/neutral";
		}
		if (isFriendly(entityplayer)) {
			return "rangerNorth/man/friendly";
		}
		return "rangerNorth/man/hostile";
	}

	@Override
	public LOTRMiniQuest createMiniQuest() {
		if (rand.nextInt(8) == 0) {
			return LOTRMiniQuestFactory.RANGER_NORTH_ARNOR_RELIC.createQuest(this);
		}
		return LOTRMiniQuestFactory.RANGER_NORTH.createQuest(this);
	}

	@Override
	public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
		return LOTRMiniQuestFactory.RANGER_NORTH;
	}
}
