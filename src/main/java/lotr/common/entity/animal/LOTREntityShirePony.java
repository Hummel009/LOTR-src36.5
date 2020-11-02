package lotr.common.entity.animal;

import lotr.common.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class LOTREntityShirePony extends LOTREntityHorse {
	public static float PONY_SCALE = 0.8f;
	public boolean breedingFlag = false;

	public LOTREntityShirePony(World world) {
		super(world);
		setSize(width * PONY_SCALE, height * PONY_SCALE);
	}

	@Override
	public int getHorseType() {
		if (breedingFlag) {
			return 0;
		}
		return worldObj.isRemote ? 0 : 1;
	}

	@Override
	public boolean func_110259_cr() {
		return false;
	}

	@Override
	protected void onLOTRHorseSpawn() {
		double maxHealth = getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth *= 0.75);
		double jumpStrength = getEntityAttribute(LOTRReflection.getHorseJumpStrength()).getAttributeValue();
		getEntityAttribute(LOTRReflection.getHorseJumpStrength()).setBaseValue(jumpStrength *= 0.5);
		double moveSpeed = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed *= 0.8);
	}

	@Override
	protected double clampChildHealth(double health) {
		return MathHelper.clamp_double(health, 10.0, 28.0);
	}

	@Override
	protected double clampChildJump(double jump) {
		return MathHelper.clamp_double(jump, 0.2, 1.0);
	}

	@Override
	protected double clampChildSpeed(double speed) {
		return MathHelper.clamp_double(speed, 0.08, 0.3);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable other) {
		LOTREntityShirePony otherPony = (LOTREntityShirePony) other;
		breedingFlag = true;
		otherPony.breedingFlag = true;
		EntityAgeable child = super.createChild(otherPony);
		breedingFlag = false;
		otherPony.breedingFlag = false;
		return child;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote && riddenByEntity != null && riddenByEntity instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) riddenByEntity;
			if (isHorseSaddled() && isChested()) {
				LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.rideShirePony);
			}
		}
	}

	@Override
	protected String getLivingSound() {
		return "mob.horse.idle";
	}

	@Override
	protected String getHurtSound() {
		return "mob.horse.hit";
	}

	@Override
	protected String getDeathSound() {
		return "mob.horse.death";
	}

	@Override
	protected String getAngrySoundName() {
		return "mob.horse.angry";
	}
}
