package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.quest.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityGondorSoldier extends LOTREntityGondorLevyman {
	public LOTREntityGondorSoldier(World world) {
		super(world);
		spawnRidingHorse = rand.nextInt(6) == 0;
		npcShield = LOTRShields.ALIGNMENT_GONDOR;
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.45, false);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.75);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(6);
		if (i == 0 || i == 1 || i == 2 || i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerGondor));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordGondor));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeGondor));
		}
		if (rand.nextInt(3) == 0) {
			npcItemsInv.setMeleeWeaponMounted(new ItemStack(LOTRMod.lanceGondor));
		} else {
			npcItemsInv.setMeleeWeaponMounted(npcItemsInv.getMeleeWeapon());
		}
		if (rand.nextInt(5) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearGondor));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		npcItemsInv.setIdleItemMounted(npcItemsInv.getMeleeWeaponMounted());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsGondor));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsGondor));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyGondor));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetGondor));
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			if (mounted) {
				setCurrentItemOrArmor(0, npcItemsInv.getIdleItemMounted());
			} else {
				setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
			}
		} else if (mounted) {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeaponMounted());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}

	@Override
	public LOTRMiniQuest createMiniQuest() {
		if (rand.nextInt(8) == 0) {
			return LOTRMiniQuestFactory.GONDOR_KILL_RENEGADE.createQuest(this);
		}
		return super.createMiniQuest();
	}
}
