package lotr.common.entity.ai;

import lotr.common.entity.npc.*;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAIBossJumpAttack extends EntityAIBase {
	private LOTREntityNPC theBoss;
	private double jumpSpeed;
	private float jumpChance;

	public LOTREntityAIBossJumpAttack(LOTREntityNPC boss, double d, float f) {
		theBoss = boss;
		jumpSpeed = d;
		jumpChance = f;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (theBoss.getAttackTarget() == null) {
			return false;
		}
		if (theBoss.bossInfo.jumpAttack) {
			return false;
		}
		if (theBoss.getRNG().nextInt(20) == 0) {
			float f = ((LOTRBoss) theBoss).getBaseChanceModifier();
			f *= jumpChance;
			int enemies = theBoss.bossInfo.getNearbyEnemies().size();
			if (enemies > 1.0f) {
				f *= enemies * 4.0f;
			}
			float distance = theBoss.getDistanceToEntity(theBoss.getAttackTarget());
			distance = 8.0f - distance;
			if ((distance /= 2.0f) > 1.0f) {
				f *= distance;
			}
			return theBoss.getRNG().nextFloat() < f;
		}
		return false;
	}

	@Override
	public void startExecuting() {
		theBoss.bossInfo.doJumpAttack(jumpSpeed);
	}
}
