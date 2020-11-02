package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import lotr.common.quest.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDolGuldurOrc extends LOTREntityOrc {
	public LOTREntityDolGuldurOrc(World world) {
		super(world);
	}

	@Override
	public EntityAIBase createOrcAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.4, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(8);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeDolGuldur));
		} else if (i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerDolGuldur));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerDolGuldurPoisoned));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordDolGuldur));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerDolGuldur));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pickaxeDolGuldur));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.axeDolGuldur));
		} else if (i == 7) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeDolGuldur));
		}
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearDolGuldur));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDolGuldur));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDolGuldur));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDolGuldur));
		if (rand.nextInt(5) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDolGuldur));
		}
		if (!worldObj.isRemote && spawnRidingHorse && !(this instanceof LOTRBannerBearer)) {
			LOTREntityMirkwoodSpider spider = new LOTREntityMirkwoodSpider(worldObj);
			spider.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0f);
			if (worldObj.func_147461_a(spider.boundingBox).isEmpty()) {
				spider.onSpawnWithEgg(null);
				spider.isNPCPersistent = isNPCPersistent;
				worldObj.spawnEntityInWorld(spider);
				mountEntity(spider);
			}
		}
		return data;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.DOL_GULDUR;
	}

	@Override
	public float getAlignmentBonus() {
		return 1.0f;
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killDolGuldurOrc;
	}

	@Override
	protected void dropOrcItems(boolean flag, int i) {
		if (rand.nextInt(6) == 0) {
			dropChestContents(LOTRChestContents.DOL_GULDUR_TENT, 1, 2 + i);
		}
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "dolGuldur/orc/hired";
			}
			if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 100.0f) {
				return "dolGuldur/orc/friendly";
			}
			return "dolGuldur/orc/neutral";
		}
		return "dolGuldur/orc/hostile";
	}

	@Override
	protected String getOrcSkirmishSpeech() {
		return "dolGuldur/orc/skirmish";
	}

	@Override
	public LOTRMiniQuest createMiniQuest() {
		return LOTRMiniQuestFactory.DOL_GULDUR.createQuest(this);
	}

	@Override
	public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
		return LOTRMiniQuestFactory.DOL_GULDUR;
	}
}
