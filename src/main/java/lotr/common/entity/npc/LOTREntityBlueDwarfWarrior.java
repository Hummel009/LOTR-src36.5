package lotr.common.entity.npc;

import lotr.common.*;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlueDwarfWarrior extends LOTREntityBlueDwarf {
	public LOTREntityBlueDwarfWarrior(World world) {
		super(world);
		npcShield = LOTRShields.ALIGNMENT_BLUE_MOUNTAINS;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(7);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordBlueDwarven));
		} else if (i == 1 || i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeBlueDwarven));
		} else if (i == 3 || i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerBlueDwarven));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.mattockBlueDwarven));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeBlueDwarven));
		}
		if (rand.nextInt(6) == 0) {
			npcItemsInv.setSpearBackup(npcItemsInv.getMeleeWeapon());
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.spearBlueDwarven));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsBlueDwarven));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsBlueDwarven));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyBlueDwarven));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetBlueDwarven));
		}
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}
}
