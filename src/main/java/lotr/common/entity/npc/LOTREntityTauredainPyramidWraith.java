package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityTauredainPyramidWraith extends LOTREntitySkeletalWraith {
	public LOTREntityTauredainPyramidWraith(World world) {
		super(world);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(6);
		if (i == 0 || i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordTauredain));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerTauredain));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerTauredainPoisoned));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerTauredain));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeTauredain));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsTauredain));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsTauredain));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyTauredain));
		return data;
	}
}
