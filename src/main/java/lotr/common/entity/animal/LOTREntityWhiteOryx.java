package lotr.common.entity.animal;

import java.util.UUID;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTRRandomSkinEntity;
import net.minecraft.entity.*;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class LOTREntityWhiteOryx extends LOTREntityGemsbok implements LOTRRandomSkinEntity {
	public static final float ORYX_SCALE = 0.9f;

	public LOTREntityWhiteOryx(World world) {
		super(world);
		setSize(width * 0.9f, height * 0.9f);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0);
	}

	@Override
	public void setUniqueID(UUID uuid) {
		entityUniqueID = uuid;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entity) {
		return new LOTREntityWhiteOryx(worldObj);
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int hide = rand.nextInt(3) + rand.nextInt(1 + i);
		for (int l = 0; l < hide; ++l) {
			dropItem(Items.leather, 1);
		}
		int meat = rand.nextInt(3) + rand.nextInt(1 + i);
		for (int l = 0; l < meat; ++l) {
			if (isBurning()) {
				dropItem(LOTRMod.deerCooked, 1);
				continue;
			}
			dropItem(LOTRMod.deerRaw, 1);
		}
	}

	@Override
	protected float getGemsbokSoundPitch() {
		return 0.9f;
	}
}
