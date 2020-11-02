package lotr.common.entity.ai;

import lotr.common.entity.npc.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;

public class LOTREntityAIHiredHorseRemainStill extends EntityAIBase {
	private LOTRNPCMount theHorse;
	private EntityCreature livingHorse;

	public LOTREntityAIHiredHorseRemainStill(LOTRNPCMount entity) {
		theHorse = entity;
		livingHorse = (EntityCreature) theHorse;
		setMutexBits(5);
	}

	@Override
	public boolean shouldExecute() {
		if (!theHorse.getBelongsToNPC()) {
			return false;
		}
		Entity rider = livingHorse.riddenByEntity;
		if (rider == null || !rider.isEntityAlive() || !(rider instanceof LOTREntityNPC)) {
			return false;
		}
		LOTREntityNPC ridingNPC = (LOTREntityNPC) rider;
		if (!ridingNPC.hiredNPCInfo.isActive) {
			return false;
		}
		if (livingHorse.isInWater()) {
			return false;
		}
		if (!livingHorse.onGround) {
			return false;
		}
		return ridingNPC.hiredNPCInfo.isHalted() && (ridingNPC.getAttackTarget() == null || !ridingNPC.getAttackTarget().isEntityAlive());
	}

	@Override
	public void startExecuting() {
		livingHorse.getNavigator().clearPathEntity();
	}
}
