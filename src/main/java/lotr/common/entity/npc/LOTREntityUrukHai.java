package lotr.common.entity.npc;

import java.util.List;

import lotr.common.*;
import lotr.common.entity.LOTREntityNPCRespawner;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import lotr.common.quest.*;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class LOTREntityUrukHai extends LOTREntityOrc {
	public LOTREntityUrukHai(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		isWeakOrc = false;
		npcShield = LOTRShields.ALIGNMENT_URUK_HAI;
	}

	@Override
	public EntityAIBase createOrcAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.4, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(8);
		if (i == 0 || i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.scimitarUruk));
		} else if (i == 2 || i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeUruk));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeUruk));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerUruk));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerUrukPoisoned));
		} else if (i == 7) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerUruk));
		}
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearUruk));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsUruk));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsUruk));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyUruk));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetUruk));
		}
		return data;
	}

	@Override
	protected void dropOrcItems(boolean flag, int i) {
		if (rand.nextInt(6) == 0) {
			dropChestContents(LOTRChestContents.URUK_TENT, 1, 2 + i);
		}
	}

	@Override
	public void onDeath(DamageSource damagesource) {
		super.onDeath(damagesource);
		if (!worldObj.isRemote && damagesource.getEntity() instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
			double range = 12.0;
			List nearbySpawners = worldObj.getEntitiesWithinAABB(LOTREntityNPCRespawner.class, boundingBox.expand(range, range, range));
			for (Object obj : nearbySpawners) {
				LOTREntityNPCRespawner spawner = (LOTREntityNPCRespawner) obj;
				if (spawner.spawnClass1 == null || !LOTREntityUrukHai.class.isAssignableFrom(spawner.spawnClass1)) {
					continue;
				}
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.raidUrukCamp);
				break;
			}
		}
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.ISENGARD;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killUrukHai;
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.75f;
	}

	@Override
	public boolean canOrcSkirmish() {
		return false;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "isengard/orc/hired";
			}
			if (LOTRLevelData.getData(entityplayer).getAlignment(getFaction()) >= 100.0f) {
				return "isengard/orc/friendly";
			}
			return "isengard/orc/neutral";
		}
		return "isengard/orc/hostile";
	}

	@Override
	public LOTRMiniQuest createMiniQuest() {
		return LOTRMiniQuestFactory.ISENGARD.createQuest(this);
	}

	@Override
	public LOTRMiniQuestFactory getBountyHelpSpeechDir() {
		return LOTRMiniQuestFactory.ISENGARD;
	}
}
