package lotr.common.entity.npc;

import lotr.common.entity.ai.LOTREntityAIAttackOnCollide;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntitySkeletalWraith extends LOTREntityNPC {
	public LOTREntitySkeletalWraith(World world) {
		super(world);
		setSize(0.6f, 1.8f);
		isImmuneToFire = true;
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIRestrictSun(this));
		tasks.addTask(2, new EntityAIFleeSun(this, 1.0));
		tasks.addTask(3, new LOTREntityAIAttackOnCollide(this, 1.2, false));
		tasks.addTask(4, new EntityAIWander(this, 1.0));
		tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f, 0.02f));
		tasks.addTask(6, new EntityAILookIdle(this));
		this.addTargetTasks(true);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(24.0);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.HOSTILE;
	}

	@Override
	protected void onAttackModeChange(LOTREntityNPC.AttackMode mode, boolean mounted) {
		if (mode == LOTREntityNPC.AttackMode.IDLE) {
			setCurrentItemOrArmor(0, npcItemsInv.getIdleItem());
		} else {
			setCurrentItemOrArmor(0, npcItemsInv.getMeleeWeapon());
		}
	}

	@Override
	public void onLivingUpdate() {
		float f;
		if (worldObj.isDaytime() && !worldObj.isRemote && (f = getBrightness(1.0f)) > 0.5f && rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ))) {
			boolean flag = true;
			ItemStack itemstack = getEquipmentInSlot(4);
			if (itemstack != null) {
				if (itemstack.isItemStackDamageable()) {
					itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + rand.nextInt(2));
					if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage()) {
						renderBrokenItemStack(itemstack);
						setCurrentItemOrArmor(4, null);
					}
				}
				flag = false;
			}
			if (flag) {
				setFire(8);
			}
		}
		super.onLivingUpdate();
		if (rand.nextBoolean()) {
			worldObj.spawnParticle("smoke", posX + (rand.nextDouble() - 0.5) * width, posY + rand.nextDouble() * height, posZ + (rand.nextDouble() - 0.5) * width, 0.0, 0.0, 0.0);
		}
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		int bones = rand.nextInt(3) + rand.nextInt(i + 1);
		for (int l = 0; l < bones; ++l) {
			dropItem(Items.bone, 1);
		}
	}

	@Override
	public boolean canDropRares() {
		return false;
	}

	@Override
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	@Override
	protected String getHurtSound() {
		return "mob.skeleton.hurt";
	}

	@Override
	protected String getDeathSound() {
		return "mob.skeleton.death";
	}

	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}
}
