package lotr.common.entity.npc;

import java.util.UUID;

import lotr.common.LOTRMod;
import lotr.common.entity.LOTRMountFunctions;
import lotr.common.item.LOTRItemMountArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class LOTREntityNPCRideable extends LOTREntityNPC implements LOTRNPCMount {
	private UUID tamingPlayer;
	private int npcTemper;

	public LOTREntityNPCRideable(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(17, (byte) 0);
	}

	public boolean isNPCTamed() {
		return dataWatcher.getWatchableObjectByte(17) == 1;
	}

	public void setNPCTamed(boolean flag) {
		dataWatcher.updateObject(17, (byte) (flag ? 1 : 0));
	}

	@Override
	public boolean isMountArmorValid(ItemStack itemstack) {
		if (itemstack != null && itemstack.getItem() instanceof LOTRItemMountArmor) {
			LOTRItemMountArmor armor = (LOTRItemMountArmor) itemstack.getItem();
			return armor.isValid(this);
		}
		return false;
	}

	public IInventory getMountInventory() {
		return null;
	}

	public void openGUI(EntityPlayer entityplayer) {
		IInventory inv = getMountInventory();
		if (inv != null && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == entityplayer) && isNPCTamed()) {
			entityplayer.openGui(LOTRMod.instance, 29, worldObj, getEntityId(), inv.getSizeInventory(), 0);
		}
	}

	public void tameNPC(EntityPlayer entityplayer) {
		setNPCTamed(true);
		tamingPlayer = entityplayer.getUniqueID();
	}

	public EntityPlayer getTamingPlayer() {
		return worldObj.func_152378_a(tamingPlayer);
	}

	@Override
	public boolean canDespawn() {
		return super.canDespawn() && !isNPCTamed();
	}

	@Override
	public boolean canRenameNPC() {
		return isNPCTamed() ? true : super.canRenameNPC();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		LOTRMountFunctions.update(this);
	}

	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
		LOTRMountFunctions.move(this, strafe, forward);
	}

	@Override
	public void super_moveEntityWithHeading(float strafe, float forward) {
		super.moveEntityWithHeading(strafe, forward);
	}

	@Override
	public final double getMountedYOffset() {
		double d = getBaseMountedYOffset();
		if (riddenByEntity != null) {
			d += riddenByEntity.yOffset - riddenByEntity.getYOffset();
		}
		return d;
	}

	protected double getBaseMountedYOffset() {
		return height * 0.5;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("NPCTamed", isNPCTamed());
		if (tamingPlayer != null) {
			nbt.setString("NPCTamer", tamingPlayer.toString());
		}
		nbt.setInteger("NPCTemper", npcTemper);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setNPCTamed(nbt.getBoolean("NPCTamed"));
		if (nbt.hasKey("NPCTamer")) {
			tamingPlayer = UUID.fromString(nbt.getString("NPCTamer"));
		}
		npcTemper = nbt.getInteger("NPCTemper");
	}

	public int getMaxNPCTemper() {
		return 100;
	}

	public int getNPCTemper() {
		return npcTemper;
	}

	public void setNPCTemper(int i) {
		npcTemper = i;
	}

	public int increaseNPCTemper(int i) {
		int temper = MathHelper.clamp_int(getNPCTemper() + i, 0, getMaxNPCTemper());
		setNPCTemper(temper);
		return getNPCTemper();
	}

	public void angerNPC() {
		playSound(getHurtSound(), getSoundVolume(), getSoundPitch() * 1.5f);
	}
}
