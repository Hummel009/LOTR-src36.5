package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityPinnathGelinSoldier extends LOTREntityGondorSoldier {
	public LOTREntityPinnathGelinSoldier(World world) {
		super(world);
		spawnRidingHorse = rand.nextInt(8) == 0;
		npcShield = LOTRShields.ALIGNMENT_PINNATH_GELIN;
		npcCape = LOTRCapes.PINNATH_GELIN;
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIAttackOnCollide(this, 1.45, false);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		int i = rand.nextInt(2);
		if (i == 0) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.swordGondor));
		} else if (i == 1) {
			npcItemsInv.setMeleeWeapon(new ItemStack(LOTRMod.pikeGondor));
		}
		npcItemsInv.setIdleItem(npcItemsInv.getMeleeWeapon());
		setCurrentItemOrArmor(1, new ItemStack(LOTRMod.bootsPinnathGelin));
		setCurrentItemOrArmor(2, new ItemStack(LOTRMod.legsPinnathGelin));
		setCurrentItemOrArmor(3, new ItemStack(LOTRMod.bodyPinnathGelin));
		if (rand.nextInt(10) != 0) {
			setCurrentItemOrArmor(4, new ItemStack(LOTRMod.helmetPinnathGelin));
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
