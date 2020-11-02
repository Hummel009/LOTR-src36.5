package lotr.common.fac;

import lotr.common.LOTRPlayerData;
import net.minecraft.nbt.NBTTagCompound;

public class LOTRFactionData {
	private LOTRPlayerData playerData;
	private LOTRFaction theFaction;
	private int npcsKilled;
	private int enemiesKilled;
	private int tradeCount;
	private int hireCount;
	private int miniQuestsCompleted;
	private float conquestEarned;
	private boolean hasConquestHorn;

	public LOTRFactionData(LOTRPlayerData data, LOTRFaction faction) {
		playerData = data;
		theFaction = faction;
	}

	public void save(NBTTagCompound nbt) {
		nbt.setInteger("NPCKill", npcsKilled);
		nbt.setInteger("EnemyKill", enemiesKilled);
		nbt.setInteger("Trades", tradeCount);
		nbt.setInteger("Hired", hireCount);
		nbt.setInteger("MiniQuests", miniQuestsCompleted);
		if (conquestEarned != 0.0f) {
			nbt.setFloat("Conquest", conquestEarned);
		}
		nbt.setBoolean("ConquestHorn", hasConquestHorn);
	}

	public void load(NBTTagCompound nbt) {
		npcsKilled = nbt.getInteger("NPCKill");
		enemiesKilled = nbt.getInteger("EnemyKill");
		tradeCount = nbt.getInteger("Trades");
		hireCount = nbt.getInteger("Hired");
		miniQuestsCompleted = nbt.getInteger("MiniQuests");
		conquestEarned = nbt.getFloat("Conquest");
		hasConquestHorn = nbt.getBoolean("ConquestHorn");
	}

	private void updateFactionData() {
		playerData.updateFactionData(theFaction, this);
	}

	public int getNPCsKilled() {
		return npcsKilled;
	}

	public void addNPCKill() {
		++npcsKilled;
		updateFactionData();
	}

	public int getEnemiesKilled() {
		return enemiesKilled;
	}

	public void addEnemyKill() {
		++enemiesKilled;
		updateFactionData();
	}

	public int getTradeCount() {
		return tradeCount;
	}

	public void addTrade() {
		++tradeCount;
		updateFactionData();
	}

	public int getHireCount() {
		return hireCount;
	}

	public void addHire() {
		++hireCount;
		updateFactionData();
	}

	public int getMiniQuestsCompleted() {
		return miniQuestsCompleted;
	}

	public void completeMiniQuest() {
		++miniQuestsCompleted;
		updateFactionData();
	}

	public float getConquestEarned() {
		return conquestEarned;
	}

	public void addConquest(float f) {
		conquestEarned += f;
		updateFactionData();
	}

	public boolean hasConquestHorn() {
		return hasConquestHorn;
	}

	public void takeConquestHorn() {
		hasConquestHorn = true;
		updateFactionData();
	}
}
