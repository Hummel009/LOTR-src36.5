package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityTauredainWarrior extends LOTREntityTauredain {
	public LOTREntityTauredainWarrior(World world) {
		super(world);
		this.addTargetTasks(true);
		npcShield = LOTRShields.ALIGNMENT_TAUREDAIN;
	}

	@Override
	protected EntityAIBase createHaradrimAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, true);
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(8);
		if (i == 0 || i == 1 || i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordTauredain));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerTauredain));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerTauredainPoisoned));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerTauredain));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeTauredain));
		} else if (i == 7) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeTauredain));
		}
		if (rand.nextInt(5) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearTauredain));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsTauredain));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsTauredain));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyTauredain));
		if (rand.nextInt(5) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetTauredain));
		}
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public String getSpeechBank(EntityPlayer entityplayer) {
		if (isFriendly(entityplayer)) {
			if (hiredNPCInfo.getHiringPlayer() == entityplayer) {
				return "tauredain/warrior/hired";
			}
			return "tauredain/warrior/friendly";
		}
		return "tauredain/warrior/hostile";
	}
}
