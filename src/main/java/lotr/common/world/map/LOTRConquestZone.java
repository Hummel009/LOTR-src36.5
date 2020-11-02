package lotr.common.world.map;

import java.util.*;

import lotr.common.fac.LOTRFaction;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class LOTRConquestZone {
	public final int gridX;
	public final int gridZ;
	public boolean isDummyZone = false;
	private float[] conquestStrengths;
	public static List<LOTRFaction> allPlayableFacs = null;
	private long lastChangeTime;
	private long isEmptyKey = 0L;
	private boolean isLoaded = true;
	private boolean clientSide = false;

	public LOTRConquestZone(int i, int k) {
		gridX = i;
		gridZ = k;
		if (allPlayableFacs == null && (allPlayableFacs = LOTRFaction.getPlayableAlignmentFactions()).size() >= 62) {
			throw new RuntimeException("Too many factions! Need to upgrade LOTRConquestZone data format.");
		}
		conquestStrengths = new float[allPlayableFacs.size()];
	}

	public LOTRConquestZone setClientSide() {
		clientSide = true;
		return this;
	}

	public LOTRConquestZone setDummyZone() {
		isDummyZone = true;
		return this;
	}

	public float getConquestStrength(LOTRFaction fac, World world) {
		return this.getConquestStrength(fac, world.getTotalWorldTime());
	}

	public float getConquestStrength(LOTRFaction fac, long worldTime) {
		float str = getConquestStrengthRaw(fac);
		str -= calcTimeStrReduction(worldTime);
		str = Math.max(str, 0.0f);
		return str;
	}

	public float getConquestStrengthRaw(LOTRFaction fac) {
		if (!fac.isPlayableAlignmentFaction()) {
			return 0.0f;
		}
		int index = allPlayableFacs.indexOf(fac);
		float str = conquestStrengths[index];
		return str;
	}

	public void setConquestStrengthRaw(LOTRFaction fac, float str) {
		if (!fac.isPlayableAlignmentFaction()) {
			return;
		}
		if (str < 0.0f) {
			str = 0.0f;
		}
		int index = allPlayableFacs.indexOf(fac);
		conquestStrengths[index] = str;
		isEmptyKey = str == 0.0f ? (isEmptyKey &= 1L << index ^ 0xFFFFFFFFFFFFFFFFL) : (isEmptyKey |= 1L << index);
		markDirty();
	}

	public void setConquestStrength(LOTRFaction fac, float str, World world) {
		setConquestStrengthRaw(fac, str);
		updateAllOtherFactions(fac, world);
		lastChangeTime = world.getTotalWorldTime();
		markDirty();
	}

	public void addConquestStrength(LOTRFaction fac, float add, World world) {
		float str = this.getConquestStrength(fac, world);
		setConquestStrength(fac, str += add, world);
	}

	private void updateAllOtherFactions(LOTRFaction fac, World world) {
		for (int i = 0; i < conquestStrengths.length; ++i) {
			LOTRFaction otherFac = allPlayableFacs.get(i);
			if (otherFac == fac || conquestStrengths[i] <= 0.0f) {
				continue;
			}
			float otherStr = this.getConquestStrength(otherFac, world);
			setConquestStrengthRaw(otherFac, otherStr);
		}
	}

	public void checkForEmptiness(World world) {
		boolean emptyCheck = true;
		for (LOTRFaction fac : allPlayableFacs) {
			float str = this.getConquestStrength(fac, world);
			if (str == 0.0f) {
				continue;
			}
			emptyCheck = false;
			break;
		}
		if (emptyCheck) {
			conquestStrengths = new float[allPlayableFacs.size()];
			isEmptyKey = 0L;
			markDirty();
		}
	}

	public void clearAllFactions(World world) {
		for (LOTRFaction fac : allPlayableFacs) {
			setConquestStrengthRaw(fac, 0.0f);
		}
		lastChangeTime = world.getTotalWorldTime();
		markDirty();
	}

	public long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(long l) {
		lastChangeTime = l;
		markDirty();
	}

	private float calcTimeStrReduction(long worldTime) {
		int dl = (int) (worldTime - lastChangeTime);
		float s = dl / 20.0f;
		float graceCap = 3600.0f;
		if (s > graceCap) {
			float decayRate = 3600.0f;
			return (s -= graceCap) / decayRate;
		}
		return 0.0f;
	}

	public boolean isEmpty() {
		return isEmptyKey == 0L;
	}

	public void markDirty() {
		if (isLoaded && !clientSide) {
			LOTRConquestGrid.markZoneDirty(this);
		}
	}

	@Override
	public String toString() {
		return "LOTRConquestZone: " + gridX + ", " + gridZ;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("X", (short) gridX);
		nbt.setShort("Z", (short) gridZ);
		nbt.setLong("Time", lastChangeTime);
		for (int i = 0; i < conquestStrengths.length; ++i) {
			LOTRFaction fac = allPlayableFacs.get(i);
			String facKey = fac.codeName() + "_str";
			float str = conquestStrengths[i];
			if (str == 0.0f) {
				continue;
			}
			nbt.setFloat(facKey, str);
		}
	}

	public static LOTRConquestZone readFromNBT(NBTTagCompound nbt) {
		short x = nbt.getShort("X");
		short z = nbt.getShort("Z");
		long time = nbt.getLong("Time");
		LOTRConquestZone zone = new LOTRConquestZone(x, z);
		zone.isLoaded = false;
		zone.lastChangeTime = time;
		block0: for (LOTRFaction fac : allPlayableFacs) {
			ArrayList<String> nameAndAliases = new ArrayList<>();
			nameAndAliases.add(fac.codeName());
			nameAndAliases.addAll(fac.listAliases());
			for (String alias : nameAndAliases) {
				String facKey = alias + "_str";
				if (!nbt.hasKey(facKey)) {
					continue;
				}
				float str = nbt.getFloat(facKey);
				zone.setConquestStrengthRaw(fac, str);
				continue block0;
			}
		}
		zone.isLoaded = true;
		return zone;
	}
}
