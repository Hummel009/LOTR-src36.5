package lotr.common.entity.ai;

import java.util.Random;

import lotr.common.LOTRMod;
import lotr.common.entity.animal.LOTREntityRabbit;
import net.minecraft.block.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class LOTREntityAIRabbitEatCrops extends EntityAIBase {
	private LOTREntityRabbit theRabbit;
	private double xPos;
	private double yPos;
	private double zPos;
	private double moveSpeed;
	private World theWorld;
	private int pathingTick;
	private int eatingTick;
	private int rePathDelay;

	public LOTREntityAIRabbitEatCrops(LOTREntityRabbit rabbit, double d) {
		theRabbit = rabbit;
		moveSpeed = d;
		theWorld = rabbit.worldObj;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		Vec3 vec3;
		if (!LOTRMod.canGrief(theWorld)) {
			return false;
		}
		if (theRabbit.getRNG().nextInt(20) == 0 && (vec3 = findCropsLocation()) != null) {
			xPos = vec3.xCoord;
			yPos = vec3.yCoord;
			zPos = vec3.zCoord;
			return true;
		}
		return false;
	}

	@Override
	public boolean continueExecuting() {
		if (!LOTRMod.canGrief(theWorld)) {
			return false;
		}
		if (pathingTick < 200 && eatingTick < 60) {
			int i = MathHelper.floor_double(xPos);
			int j = MathHelper.floor_double(yPos);
			int k = MathHelper.floor_double(zPos);
			return canEatBlock(i, j, k);
		}
		return false;
	}

	@Override
	public void resetTask() {
		pathingTick = 0;
		eatingTick = 0;
		rePathDelay = 0;
		theRabbit.setRabbitEating(false);
	}

	@Override
	public void updateTask() {
		if (theRabbit.getDistanceSq(xPos, yPos, zPos) > 1.0) {
			theRabbit.setRabbitEating(false);
			theRabbit.getLookHelper().setLookPosition(xPos, yPos - 0.5, zPos, 10.0f, theRabbit.getVerticalFaceSpeed());
			--rePathDelay;
			if (rePathDelay <= 0) {
				rePathDelay = 10;
				theRabbit.getNavigator().tryMoveToXYZ(xPos, yPos, zPos, moveSpeed);
			}
			++pathingTick;
		} else {
			theRabbit.setRabbitEating(true);
			++eatingTick;
			if (eatingTick % 6 == 0) {
				theRabbit.playSound("random.eat", 1.0f, (theWorld.rand.nextFloat() - theWorld.rand.nextFloat()) * 0.2f + 1.0f);
			}
			if (eatingTick >= 60) {
				theWorld.setBlockToAir(MathHelper.floor_double(xPos), MathHelper.floor_double(yPos), MathHelper.floor_double(zPos));
			}
		}
	}

	private Vec3 findCropsLocation() {
		Random random = theRabbit.getRNG();
		for (int l = 0; l < 32; ++l) {
			int k;
			int j;
			int i = MathHelper.floor_double(theRabbit.posX) + MathHelper.getRandomIntegerInRange(random, -16, 16);
			if (!canEatBlock(i, j = MathHelper.floor_double(theRabbit.boundingBox.minY) + MathHelper.getRandomIntegerInRange(random, -8, 8), k = MathHelper.floor_double(theRabbit.posZ) + MathHelper.getRandomIntegerInRange(random, -16, 16))) {
				continue;
			}
			return Vec3.createVectorHelper(i + 0.5, j, k + 0.5);
		}
		return null;
	}

	private boolean canEatBlock(int i, int j, int k) {
		Block block = theWorld.getBlock(i, j, k);
		return block instanceof BlockCrops && !theRabbit.anyFarmhandsNearby(i, j, k);
	}
}
