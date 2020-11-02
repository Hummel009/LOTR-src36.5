package lotr.common.entity.npc;

import lotr.common.LOTRMod;
import lotr.common.entity.ai.LOTREntityAIRangedAttack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityDorwinionElfArcher extends LOTREntityDorwinionElfWarrior {
	public LOTREntityDorwinionElfArcher(World world) {
		super(world);
	}

	@Override
	protected EntityAIBase createElfRangedAttackAI() {
		return new LOTREntityAIRangedAttack(this, 1.25, 30, 50, 24.0f);
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
		data = super.onSpawnWithEgg(data);
		npcItemsInv.setRangedWeapon(new ItemStack(LOTRMod.dorwinionElfBow));
		npcItemsInv.setIdleItem(npcItemsInv.getRangedWeapon());
		return data;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			tasks.removeTask(meleeAttackAI);
			tasks.removeTask(rangedAttackAI);
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			tasks.removeTask(meleeAttackAI);
			tasks.removeTask(rangedAttackAI);
			tasks.addTask(2, rangedAttackAI);
			setCurrentItemOrArmor(0, npcItemsInv.getRangedWeapon());
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float f) {
		npcArrowAttack(target, f);
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
	}
}
