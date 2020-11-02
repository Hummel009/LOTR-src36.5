package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import lotr.common.world.structure.LOTRChestContents;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlackUruk extends LOTREntityMordorOrc {
	public LOTREntityBlackUruk(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		isWeakOrc = false;
		npcShield = LOTRShields.ALIGNMENT_BLACK_URUK;
	}

	@Override
	public EntityAIBase createOrcAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0);
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.5);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(7);
		if (i == 0 || i == 1 || i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.scimitarBlackUruk));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeBlackUruk));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerBlackUruk));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerBlackUrukPoisoned));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerBlackUruk));
		}
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearBlackUruk));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsBlackUruk));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsBlackUruk));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyBlackUruk));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetBlackUruk));
		return data;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.MORDOR;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killBlackUruk;
	}

	@Override
	protected void dropOrcItems(boolean flag, int i) {
		if (rand.nextInt(6) == 0) {
			dropChestContents(LOTRChestContents.BLACK_URUK_FORT, 1, 2 + i);
		}
		if (flag) {
			int shinyShirtChance = 6000;
			shinyShirtChance -= i * 500;
			if (rand.nextInt(shinyShirtChance = Math.max(shinyShirtChance, 1)) == 0) {
				dropItem(LOTRMod.bodyMithril, 1);
			}
		}
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.75f;
	}

	@Override
	public boolean canOrcSkirmish() {
		return false;
	}
}
