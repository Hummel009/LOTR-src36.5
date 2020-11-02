package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityEasterlingWarrior extends LOTREntityEasterlingLevyman {
	public LOTREntityEasterlingWarrior(World world) {
		super(world);
		this.addTargetTasks(true);
		spawnRidingHorse = rand.nextInt(6) == 0;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.75);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(10);
		if (i == 0 || i == 1 || i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordRhun));
		} else if (i == 3 || i == 4 || i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeRhun));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.polearmRhun));
		} else if (i == 7) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerRhun));
		} else if (i == 8) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerRhunPoisoned));
		} else if (i == 9) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeRhun));
		}
		if (rand.nextInt(5) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearRhun));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsRhun));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsRhun));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyRhun));
		setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetRhun));
		return data;
	}
}
