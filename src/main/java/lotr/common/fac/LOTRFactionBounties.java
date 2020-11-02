package lotr.common.fac;

import java.io.File;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLLog;
import lotr.common.LOTRLevelData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.UsernameCache;

public class LOTRFactionBounties {
	public static Map<LOTRFaction, LOTRFactionBounties> factionBountyMap = new HashMap<>();
	public static boolean needsLoad = true;
	public static final int KILL_RECORD_TIME = 3456000;
	public static final int BOUNTY_KILLED_TIME = 864000;
	public final LOTRFaction theFaction;
	private Map<UUID, PlayerData> playerList = new HashMap<>();
	private boolean needsSave = false;

	public static LOTRFactionBounties forFaction(LOTRFaction fac) {
		LOTRFactionBounties bounties = factionBountyMap.get(fac);
		if (bounties == null) {
			bounties = LOTRFactionBounties.loadFaction(fac);
			if (bounties == null) {
				bounties = new LOTRFactionBounties(fac);
			}
			factionBountyMap.put(fac, bounties);
		}
		return bounties;
	}

	public static void updateAll() {
		for (LOTRFactionBounties fb : factionBountyMap.values()) {
			fb.update();
		}
	}

	public LOTRFactionBounties(LOTRFaction f) {
		theFaction = f;
	}

	public PlayerData forPlayer(EntityPlayer entityplayer) {
		return this.forPlayer(entityplayer.getUniqueID());
	}

	public PlayerData forPlayer(UUID id) {
		PlayerData pd = playerList.get(id);
		if (pd == null) {
			pd = new PlayerData(this, id);
			playerList.put(id, pd);
		}
		return pd;
	}

	public void update() {
		for (PlayerData pd : playerList.values()) {
			pd.update();
		}
	}

	public List<PlayerData> findBountyTargets(int killAmount) {
		ArrayList<PlayerData> players = new ArrayList<>();
		for (PlayerData pd : playerList.values()) {
			if (pd.recentlyBountyKilled() || pd.getNumKills() < killAmount) {
				continue;
			}
			players.add(pd);
		}
		return players;
	}

	public void markDirty() {
		needsSave = true;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList playerTags = new NBTTagList();
		for (Map.Entry<UUID, PlayerData> e : playerList.entrySet()) {
			UUID id = e.getKey();
			PlayerData pd = e.getValue();
			if (!pd.shouldSave()) {
				continue;
			}
			NBTTagCompound playerData = new NBTTagCompound();
			playerData.setString("UUID", id.toString());
			pd.writeToNBT(playerData);
			playerTags.appendTag(playerData);
		}
		nbt.setTag("PlayerList", playerTags);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		playerList.clear();
		if (nbt.hasKey("PlayerList")) {
			NBTTagList playerTags = nbt.getTagList("PlayerList", 10);
			for (int i = 0; i < playerTags.tagCount(); ++i) {
				NBTTagCompound playerData = playerTags.getCompoundTagAt(i);
				UUID id = UUID.fromString(playerData.getString("UUID"));
				if (id == null) {
					continue;
				}
				PlayerData pd = new PlayerData(this, id);
				pd.readFromNBT(playerData);
				playerList.put(id, pd);
			}
		}
	}

	public static boolean anyDataNeedsSave() {
		for (LOTRFactionBounties fb : factionBountyMap.values()) {
			if (!fb.needsSave) {
				continue;
			}
			return true;
		}
		return false;
	}

	private static File getBountiesDir() {
		File dir = new File(LOTRLevelData.getOrCreateLOTRDir(), "factionbounties");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private static File getFactionFile(LOTRFaction f, boolean findLegacy) {
		File defaultFile = new File(LOTRFactionBounties.getBountiesDir(), f.codeName() + ".dat");
		if (!findLegacy) {
			return defaultFile;
		}
		if (defaultFile.exists()) {
			return defaultFile;
		}
		for (String alias : f.listAliases()) {
			File aliasFile = new File(LOTRFactionBounties.getBountiesDir(), alias + ".dat");
			if (!aliasFile.exists()) {
				continue;
			}
			return aliasFile;
		}
		return defaultFile;
	}

	public static void saveAll() {
		try {
			for (LOTRFactionBounties fb : factionBountyMap.values()) {
				if (!fb.needsSave) {
					continue;
				}
				LOTRFactionBounties.saveFaction(fb);
				fb.needsSave = false;
			}
		} catch (Exception e) {
			FMLLog.severe("Error saving LOTR faction bounty data", new Object[0]);
			e.printStackTrace();
		}
	}

	public static void loadAll() {
		try {
			factionBountyMap.clear();
			needsLoad = false;
			LOTRFactionBounties.saveAll();
		} catch (Exception e) {
			FMLLog.severe("Error loading LOTR faction bounty data", new Object[0]);
			e.printStackTrace();
		}
	}

	private static LOTRFactionBounties loadFaction(LOTRFaction fac) {
		File file = LOTRFactionBounties.getFactionFile(fac, true);
		try {
			NBTTagCompound nbt = LOTRLevelData.loadNBTFromFile(file);
			if (nbt.hasNoTags()) {
				return null;
			}
			LOTRFactionBounties fb = new LOTRFactionBounties(fac);
			fb.readFromNBT(nbt);
			return fb;
		} catch (Exception e) {
			FMLLog.severe("Error loading LOTR faction bounty data for %s", fac.codeName());
			e.printStackTrace();
			return null;
		}
	}

	private static void saveFaction(LOTRFactionBounties fb) {
		try {
			NBTTagCompound nbt = new NBTTagCompound();
			fb.writeToNBT(nbt);
			LOTRLevelData.saveNBTToFile(LOTRFactionBounties.getFactionFile(fb.theFaction, false), nbt);
		} catch (Exception e) {
			FMLLog.severe("Error saving LOTR faction bounty data for %s", fb.theFaction.codeName());
			e.printStackTrace();
		}
	}

	public static class PlayerData {
		public final LOTRFactionBounties bountyList;
		public final UUID playerID;
		private String username;
		private List<KillRecord> killRecords = new ArrayList<>();
		private int recentBountyKilled;

		public PlayerData(LOTRFactionBounties b, UUID id) {
			bountyList = b;
			playerID = id;
		}

		public void recordNewKill() {
			killRecords.add(new KillRecord());
			markDirty();
		}

		public int getNumKills() {
			return killRecords.size();
		}

		public void recordBountyKilled() {
			recentBountyKilled = 864000;
			markDirty();
		}

		public boolean recentlyBountyKilled() {
			return recentBountyKilled > 0;
		}

		public void update() {
			boolean minorChanges = false;
			if (recentBountyKilled > 0) {
				--recentBountyKilled;
				minorChanges = true;
			}
			ArrayList<KillRecord> toRemove = new ArrayList<>();
			for (KillRecord kr : killRecords) {
				kr.timeElapsed--;
				if (kr.timeElapsed <= 0) {
					toRemove.add(kr);
				}
				minorChanges = true;
			}
			if (!toRemove.isEmpty()) {
				killRecords.removeAll(toRemove);
				minorChanges = true;
			}
			if (minorChanges && MinecraftServer.getServer().getTickCounter() % 600 == 0) {
				markDirty();
			}
		}

		private void markDirty() {
			bountyList.markDirty();
		}

		public boolean shouldSave() {
			return !killRecords.isEmpty() || recentBountyKilled > 0;
		}

		public void writeToNBT(NBTTagCompound nbt) {
			NBTTagList recordTags = new NBTTagList();
			for (KillRecord kr : killRecords) {
				NBTTagCompound killData = new NBTTagCompound();
				kr.writeToNBT(killData);
				recordTags.appendTag(killData);
			}
			nbt.setTag("KillRecords", recordTags);
			nbt.setInteger("RecentBountyKilled", recentBountyKilled);
		}

		public void readFromNBT(NBTTagCompound nbt) {
			killRecords.clear();
			if (nbt.hasKey("KillRecords")) {
				NBTTagList recordTags = nbt.getTagList("KillRecords", 10);
				for (int i = 0; i < recordTags.tagCount(); ++i) {
					NBTTagCompound killData = recordTags.getCompoundTagAt(i);
					KillRecord kr = new KillRecord();
					kr.readFromNBT(killData);
					killRecords.add(kr);
				}
			}
			recentBountyKilled = nbt.getInteger("RecentBountyKilled");
		}

		public String findUsername() {
			if (username == null) {
				GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(playerID);
				if (profile == null || StringUtils.isBlank(profile.getName())) {
					String name = UsernameCache.getLastKnownUsername(playerID);
					if (name != null) {
						profile = new GameProfile(playerID, name);
					} else {
						profile = new GameProfile(playerID, "");
						MinecraftServer.getServer().func_147130_as().fillProfileProperties(profile, true);
					}
				}
				username = profile.getName();
			}
			return username;
		}

		private static class KillRecord {
			private int timeElapsed = 3456000;

			public void writeToNBT(NBTTagCompound nbt) {
				nbt.setInteger("Time", timeElapsed);
			}

			public void readFromNBT(NBTTagCompound nbt) {
				timeElapsed = nbt.getInteger("Time");
			}
		}

	}

}
