package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIRangedAttack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBlackrootArcher extends LOTREntityBlackrootSoldier {
	public LOTREntityBlackrootArcher(World world) {
		super(world);
		spawnRidingHorse = false;
	}

	@Override
	public EntityAIBase createGondorAttackAI() {
		return new LOTREntityAIRangedAttack(this, 1.45, 30, 40, 24.0f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(24.0);
		getEntityAttribute(npcRangedAccuracy).setBaseValue(0.5);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setRangedWeapon(new ItemStack(LOTRMod.blackrootBow));
		npcItemsInv.setIdleItem(npcItemsInv.getRangedWeapon());
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getRangedWeapon());
		}
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		dropNPCArrows(i);
	}
}
