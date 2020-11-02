package lotr.common.entity.ai;

import java.util.List;

import lotr.common.entity.npc.*;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;

public class LOTREntityAIBanditFlee extends EntityAIBase {
	private IBandit theBandit;
	private LOTREntityNPC theBanditAsNPC;
	private double speed;
	private double range;
	private EntityPlayer targetPlayer;

	public LOTREntityAIBanditFlee(IBandit bandit, double d) {
		theBandit = bandit;
		theBanditAsNPC = theBandit.getBanditAsNPC();
		speed = d;
		range = theBanditAsNPC.getEntityAttribute(SharedMonsterAttributes.followRange).getAttributeValue();
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (theBanditAsNPC.getAttackTarget() != null) {
			return false;
		}
		if (theBandit.getBanditInventory().isEmpty()) {
			return false;
		}
		targetPlayer = findNearestPlayer();
		return targetPlayer != null;
	}

	private EntityPlayer findNearestPlayer() {
		List players = theBanditAsNPC.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theBanditAsNPC.boundingBox.expand(range, range, range));
		double distance = range;
		EntityPlayer ret = null;
		for (Object player : players) {
			double d;
			EntityPlayer entityplayer = (EntityPlayer) player;
			if (entityplayer.capabilities.isCreativeMode || ((d = theBanditAsNPC.getDistanceToEntity(entityplayer)) >= distance)) {
				continue;
			}
			distance = d;
			ret = entityplayer;
		}
		return ret;
	}

	@Override
	public void updateTask() {
		if (theBanditAsNPC.getNavigator().noPath()) {
			Vec3 away = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theBanditAsNPC, (int) range, 10, Vec3.createVectorHelper(targetPlayer.posX, targetPlayer.posY, targetPlayer.posZ));
			if (away != null) {
				theBanditAsNPC.getNavigator().tryMoveToXYZ(away.xCoord, away.yCoord, away.zCoord, speed);
			}
			targetPlayer = findNearestPlayer();
		}
	}

	@Override
	public boolean continueExecuting() {
		if (targetPlayer == null || !targetPlayer.isEntityAlive() || targetPlayer.capabilities.isCreativeMode) {
			return false;
		}
		return theBanditAsNPC.getAttackTarget() == null && theBanditAsNPC.getDistanceSqToEntity(targetPlayer) < range * range;
	}

	@Override
	public void resetTask() {
		targetPlayer = null;
	}
}
