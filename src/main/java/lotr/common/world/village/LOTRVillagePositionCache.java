package lotr.common.world.village;

import java.util.*;

import net.minecraft.world.ChunkCoordIntPair;

public class LOTRVillagePositionCache {
	private Map<ChunkCoordIntPair, LocationInfo> cacheMap = new HashMap<>();

	public LocationInfo markResult(int chunkX, int chunkZ, LocationInfo result) {
		if (cacheMap.size() >= 20000) {
			clearCache();
		}
		cacheMap.put(getChunkKey(chunkX, chunkZ), result);
		return result;
	}

	public LocationInfo getLocationAt(int chunkX, int chunkZ) {
		LocationInfo loc = cacheMap.get(getChunkKey(chunkX, chunkZ));
		return loc;
	}

	private ChunkCoordIntPair getChunkKey(int chunkX, int chunkZ) {
		return new ChunkCoordIntPair(chunkX, chunkZ);
	}

	public void clearCache() {
		cacheMap.clear();
	}
}
