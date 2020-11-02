package lotr.common.entity.item;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemLionRug;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTREntityLionRug extends LOTREntityRugBase {
	public LOTREntityLionRug(World world) {
		super(world);
		setSize(1.8f, 0.3f);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(18, (byte) 0);
	}

	public LOTRItemLionRug.LionRugType getRugType() {
		byte i = dataWatcher.getWatchableObjectByte(18);
		return LOTRItemLionRug.LionRugType.forID(i);
	}

	public void setRugType(LOTRItemLionRug.LionRugType t) {
		dataWatcher.updateObject(18, (byte) t.lionID);
	}

	@Override
	protected String getRugNoise() {
		return "lotr:lion.say";
	}

	@Override
	protected ItemStack getRugItem() {
		return new ItemStack(LOTRMod.lionRug, 1, getRugType().lionID);
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setByte("RugType", (byte) getRugType().lionID);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		setRugType(LOTRItemLionRug.LionRugType.forID(nbt.getByte("RugType")));
	}
}
