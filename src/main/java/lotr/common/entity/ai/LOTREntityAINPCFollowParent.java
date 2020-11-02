package lotr.common.entity.ai;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAINPCFollowParent extends EntityAIBase {
	private LOTREntityNPC theNPC;
	private LOTREntityNPC parentNPC;
	private double moveSpeed;
	private int followTick;

	public LOTREntityAINPCFollowParent(LOTREntityNPC npc, double d) {
		theNPC = npc;
		moveSpeed = d;
	}

	@Override
	public boolean shouldExecute() {
		if (theNPC.familyInfo.getAge() >= 0) {
			return false;
		}
		LOTREntityNPC parent = theNPC.familyInfo.getParentToFollow();
		if (parent == null) {
			return false;
		}
		if (theNPC.getDistanceSqToEntity(parent) < 9.0 || theNPC.getDistanceSqToEntity(parent) >= 256.0) {
			return false;
		}
		parentNPC = parent;
		return true;
	}

	@Override
	public boolean continueExecuting() {
		if (!parentNPC.isEntityAlive()) {
			return false;
		}
		double d = theNPC.getDistanceSqToEntity(parentNPC);
		return d >= 9.0 && d <= 256.0;
	}

	@Override
	public void startExecuting() {
		followTick = 0;
	}

	@Override
	public void resetTask() {
		parentNPC = null;
	}

	@Override
	public void updateTask() {
		if (followTick-- <= 0) {
			followTick = 10;
			theNPC.getNavigator().tryMoveToEntityLiving(parentNPC, moveSpeed);
		}
	}
}
