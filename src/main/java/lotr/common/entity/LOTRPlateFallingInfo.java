package lotr.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class LOTRPlateFallingInfo implements IExtendedEntityProperties {
	private static final String propID = "lotr_plateFall";
	private Entity theEntity;
	private int updateTick;
	private float[] posXTicksAgo = new float[65];
	private boolean[] isFalling = new boolean[65];
	private float[] fallerPos = new float[65];
	private float[] prevFallerPos = new float[65];
	private float[] fallerSpeed = new float[65];

	public LOTRPlateFallingInfo(Entity entity) {
		theEntity = entity;
	}

	public void update() {
		int l;
		float curPos = (float) theEntity.posY;
		if (!theEntity.onGround && theEntity.motionY > 0.0) {
			for (l = 0; l < posXTicksAgo.length; ++l) {
				posXTicksAgo[l] = Math.max(posXTicksAgo[l], curPos);
			}
		}
		if (updateTick % 1 == 0) {
			for (l = posXTicksAgo.length - 1; l > 0; --l) {
				posXTicksAgo[l] = posXTicksAgo[l - 1];
			}
			posXTicksAgo[0] = curPos;
		}
		++updateTick;
		for (l = 0; l < fallerPos.length; ++l) {
			prevFallerPos[l] = fallerPos[l];
			float pos = fallerPos[l];
			float speed = fallerSpeed[l];
			boolean fall = isFalling[l];
			if (!fall && pos > posXTicksAgo[l]) {
				fall = true;
			}
			isFalling[l] = fall;
			if (fall) {
				speed = (float) (speed + 0.08);
				pos -= speed;
				speed = (float) (speed * 0.98);
			} else {
				speed = 0.0f;
			}
			if (pos < curPos) {
				pos = curPos;
				speed = 0.0f;
				isFalling[l] = false;
			}
			fallerPos[l] = pos;
			fallerSpeed[l] = speed;
		}
	}

	public float getPlateOffsetY(float f) {
		return getOffsetY(0, f);
	}

	public float getFoodOffsetY(int food, float f) {
		return getOffsetY(food - 1, f);
	}

	private float getOffsetY(int index, float f) {
		index = MathHelper.clamp_int(index, 0, fallerPos.length - 1);
		float pos = prevFallerPos[index] + (fallerPos[index] - prevFallerPos[index]) * f;
		float offset = pos - (float) (theEntity.prevPosY + (theEntity.posY - theEntity.prevPosY) * f);
		offset = Math.max(offset, 0.0f);
		return offset;
	}

	public static LOTRPlateFallingInfo getOrCreateFor(Entity entity, boolean create) {
		LOTRPlateFallingInfo props = (LOTRPlateFallingInfo) entity.getExtendedProperties(propID);
		if (props == null && create) {
			props = new LOTRPlateFallingInfo(entity);
			entity.registerExtendedProperties(propID, props);
		}
		return props;
	}

	@Override
	public void saveNBTData(NBTTagCompound nbt) {
	}

	@Override
	public void loadNBTData(NBTTagCompound nbt) {
	}

	@Override
	public void init(Entity entity, World world) {
	}
}
