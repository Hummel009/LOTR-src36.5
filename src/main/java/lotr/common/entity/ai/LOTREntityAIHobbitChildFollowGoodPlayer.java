package lotr.common.entity.ai;

import java.util.List;

import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityHobbit;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class LOTREntityAIHobbitChildFollowGoodPlayer extends EntityAIBase {
	private LOTREntityHobbit theHobbit;
	private EntityPlayer playerToFollow;
	private float range;
	private double speed;
	private int followDelay;

	public LOTREntityAIHobbitChildFollowGoodPlayer(LOTREntityHobbit hobbit, float f, double d) {
		theHobbit = hobbit;
		range = f;
		speed = d;
	}

	@Override
	public boolean shouldExecute() {
		if (theHobbit.familyInfo.getAge() >= 0) {
			return false;
		}
		List<EntityPlayer> list = theHobbit.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theHobbit.boundingBox.expand(range, 3.0, range));
		EntityPlayer entityplayer = null;
		double distanceSq = Double.MAX_VALUE;
		for (EntityPlayer playerCandidate : list) {
			double d;
			if ((LOTRLevelData.getData(playerCandidate).getAlignment(theHobbit.getFaction()) < 200.0f) || ((d = theHobbit.getDistanceSqToEntity(playerCandidate)) > distanceSq)) {
				continue;
			}
			distanceSq = d;
			entityplayer = playerCandidate;
		}
		if (entityplayer == null) {
			return false;
		}
		if (distanceSq < 9.0) {
			return false;
		}
		playerToFollow = entityplayer;
		return true;
	}

	@Override
	public boolean continueExecuting() {
		if (!playerToFollow.isEntityAlive() || theHobbit.familyInfo.getAge() >= 0) {
			return false;
		}
		double distanceSq = theHobbit.getDistanceSqToEntity(playerToFollow);
		return distanceSq >= 9.0 && distanceSq <= 256.0;
	}

	@Override
	public void startExecuting() {
		followDelay = 0;
	}

	@Override
	public void resetTask() {
		playerToFollow = null;
	}

	@Override
	public void updateTask() {
		if (--followDelay <= 0) {
			followDelay = 10;
			theHobbit.getNavigator().tryMoveToEntityLiving(playerToFollow, speed);
		}
	}
}
