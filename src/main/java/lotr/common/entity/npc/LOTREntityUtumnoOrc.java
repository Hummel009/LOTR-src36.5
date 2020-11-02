package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityUtumnoOrc extends LOTREntityOrc {
	public LOTREntityUtumnoOrc(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		isWeakOrc = false;
	}

	@Override
	public EntityAIBase createOrcAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, true);
	}

	@Override
	public void setupNPCName() {
		if (rand.nextInt(5) == 0) {
			familyInfo.setName(LOTRNames.getSindarinOrQuenyaName(rand, rand.nextBoolean()));
		} else {
			familyInfo.setName(LOTRNames.getOrcName(rand));
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24.0);
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(6);
		if (i == 0 || i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordUtumno));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeUtumno));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerUtumno));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerUtumnoPoisoned));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerUtumno));
		}
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearUtumno));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsUtumno));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsUtumno));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyUtumno));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetUtumno));
		}
		return data;
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.UTUMNO;
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killUtumnoOrc;
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * 0.65f;
	}

	@Override
	public boolean canOrcSkirmish() {
		return false;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		return "utumno/orc/hostile";
	}
}
