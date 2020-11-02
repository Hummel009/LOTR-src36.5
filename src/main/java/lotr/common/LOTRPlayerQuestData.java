package lotr.common;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

public class LOTRPlayerQuestData {
	private LOTRPlayerData playerData;
	private boolean givenFirstPouches = false;

	public LOTRPlayerQuestData(LOTRPlayerData pd) {
		playerData = pd;
	}

	public void save(NBTTagCompound questData) {
		questData.setBoolean("Pouches", givenFirstPouches);
	}

	public void load(NBTTagCompound questData) {
		givenFirstPouches = questData.getBoolean("Pouches");
	}

	public void onUpdate(EntityPlayerMP entityplayer, WorldServer world) {
	}

	private void markDirty() {
		playerData.markDirty();
	}

	public boolean getGivenFirstPouches() {
		return givenFirstPouches;
	}

	public void setGivenFirstPouches(boolean flag) {
		givenFirstPouches = flag;
		markDirty();
	}
}
