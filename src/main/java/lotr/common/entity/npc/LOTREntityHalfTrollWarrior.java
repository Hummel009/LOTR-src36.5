package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.animal.LOTREntityRhino;
import net.minecraft.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityHalfTrollWarrior extends LOTREntityHalfTroll {
	public LOTREntityHalfTrollWarrior(World world) {
		super(world);
		npcShield = LOTRShields.ALIGNMENT_HALF_TROLL;
		spawnRidingHorse = rand.nextInt(12) == 0;
	}

	@Override
	public LOTRNPCMount createMountToRide() {
		LOTREntityRhino rhino = new LOTREntityRhino(worldObj);
		if (rand.nextBoolean()) {
			rhino.setMountArmor(new ItemStack(LOTRMod.rhinoArmorHalfTroll));
		}
		return rhino;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.24);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(7);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.battleaxeHalfTroll));
		} else if (i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.hammerHalfTroll));
		} else if (i == 2) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.maceHalfTroll));
		} else if (i == 3) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.scimitarHalfTroll));
		} else if (i == 4) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerHalfTroll));
		} else if (i == 5) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.daggerHalfTrollPoisoned));
		} else if (i == 6) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeHalfTroll));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsHalfTroll));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsHalfTroll));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyHalfTroll));
		if (rand.nextInt(4) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetHalfTroll));
		}
		return data;
	}

	@Override
	public float getAlignmentBonus() {
		return 2.0f;
	}
}
