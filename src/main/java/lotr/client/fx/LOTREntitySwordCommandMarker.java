package lotr.client.fx;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntitySwordCommandMarker extends Entity {
	private int particleAge;
	private int particleMaxAge;

	public LOTREntitySwordCommandMarker(World world, double d, double d1, double d2) {
		super(world);
		setSize(0.5f, 0.5f);
		yOffset = height / 2.0f;
		setPosition(d, d1, d2);
		particleAge = 0;
		particleMaxAge = 30;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		posY -= 0.35;
		++particleAge;
		if (particleAge >= particleMaxAge) {
			setDead();
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}

	@Override
	public boolean canBePushed() {
		return false;
	}
}
