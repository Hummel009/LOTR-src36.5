package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDunlendingWarrior extends LOTREntityDunlending {
	public LOTREntityDunlendingWarrior(World world) {
		super(world);
		npcShield = LOTRShields.ALIGNMENT_DUNLAND;
	}

	@Override
	public EntityAIBase getDunlendingAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.6, false);
	}

	@Override
	public void setupNPCGender() {
		familyInfo.setMale(true);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(7);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(Items.iron_sword));
		} else if (i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordBronze));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerIron));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerBronze));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeIron));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeBronze));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeIron));
		}
		if (rand.nextInt(5) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			if (rand.nextBoolean()) {
				npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearIron));
			} else {
				npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearBronze));
			}
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsDunlending));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsDunlending));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyDunlending));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetDunlending));
		}
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}
}
