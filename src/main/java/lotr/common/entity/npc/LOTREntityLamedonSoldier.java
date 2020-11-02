package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.entity.animal.LOTREntityHorse;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityLamedonSoldier extends LOTREntityGondorSoldier {
	public LOTREntityLamedonSoldier(World world) {
		super(world);
		spawnRidingHorse = rand.nextInt(6) == 0;
		npcShield = LOTRShields.ALIGNMENT_LAMEDON;
		npcCape = LOTRCapes.LAMEDON;
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.5, false);
	}

	@Override
	public LOTRNPCMount createMountToRide() {
		LOTREntityHorse horse = (LOTREntityHorse) super.createMountToRide();
		horse.setMountArmor(new ItemStack(LOTRMod.horseArmorLamedon));
		return horse;
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(3);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordGondor));
		} else if (i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerGondor));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeGondor));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsLamedon));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsLamedon));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyLamedon));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetLamedon));
		} else {
			setCurrentItemOrArmor(4, null);
		}
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}
}
