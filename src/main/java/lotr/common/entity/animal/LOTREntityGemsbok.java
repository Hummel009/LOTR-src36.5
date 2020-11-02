package lotr.common.entity.animal;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class LOTREntityGemsbok extends EntityAnimal {
	public LOTREntityGemsbok(World world) {
		super(world);
		setSize(0.9f, 1.4f);
		getNavigator().setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIPanic(this, 1.3));
		tasks.addTask(2, new EntityAIMate(this, 1.0));
		tasks.addTask(3, new EntityAITempt(this, 1.2, Items.wheat, false));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
		tasks.addTask(5, new EntityAIWander(this, 1.0));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
		tasks.addTask(7, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(22.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
	}

	@Override
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public boolean isBreedingItem(ItemStack itemstack) {
		return itemstack.getItem() == Items.wheat;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		int j = 1 + rand.nextInt(4) + rand.nextInt(1 + i);
		for (int k = 0; k < j; ++k) {
			dropItem(LOTRMod.gemsbokHide, 1);
		}
		if (rand.nextBoolean()) {
			dropItem(LOTRMod.gemsbokHorn, 1);
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable entity) {
		return new LOTREntityGemsbok(worldObj);
	}

	@Override
	protected String getLivingSound() {
		return "lotr:deer.say";
	}

	@Override
	protected String getHurtSound() {
		return "lotr:deer.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "lotr:deer.death";
	}

	@Override
	protected float getSoundPitch() {
		return super.getSoundPitch() * getGemsbokSoundPitch();
	}

	protected float getGemsbokSoundPitch() {
		return 0.8f;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target) {
		return new ItemStack(LOTRMod.spawnEgg, 1, LOTREntities.getEntityID(this));
	}
}
