package lotr.common.world.genlayer;

import java.util.*;

import net.minecraft.world.World;

public class LOTRIntCache {
	private static LOTRIntCache SERVER = new LOTRIntCache();
	private static LOTRIntCache CLIENT = new LOTRIntCache();
	private int intCacheSize = 256;
	private List<int[]> freeSmallArrays = new ArrayList<>();
	private List<int[]> inUseSmallArrays = new ArrayList<>();
	private List<int[]> freeLargeArrays = new ArrayList<>();
	private List<int[]> inUseLargeArrays = new ArrayList<>();

	public static LOTRIntCache get(World world) {
		if (!world.isRemote) {
			return SERVER;
		}
		return CLIENT;
	}

	public int[] getIntArray(int size) {
		if (size <= 256) {
			if (freeSmallArrays.isEmpty()) {
				int[] ints = new int[256];
				inUseSmallArrays.add(ints);
				return ints;
			}
			int[] ints = freeSmallArrays.remove(freeSmallArrays.size() - 1);
			inUseSmallArrays.add(ints);
			return ints;
		}
		if (size > intCacheSize) {
			intCacheSize = size;
			freeLargeArrays.clear();
			inUseLargeArrays.clear();
			int[] ints = new int[intCacheSize];
			inUseLargeArrays.add(ints);
			return ints;
		}
		if (freeLargeArrays.isEmpty()) {
			int[] ints = new int[intCacheSize];
			inUseLargeArrays.add(ints);
			return ints;
		}
		int[] ints = freeLargeArrays.remove(freeLargeArrays.size() - 1);
		inUseLargeArrays.add(ints);
		return ints;
	}

	public void resetIntCache() {
		if (!freeLargeArrays.isEmpty()) {
			freeLargeArrays.remove(freeLargeArrays.size() - 1);
		}
		if (!freeSmallArrays.isEmpty()) {
			freeSmallArrays.remove(freeSmallArrays.size() - 1);
		}
		freeLargeArrays.addAll(inUseLargeArrays);
		freeSmallArrays.addAll(inUseSmallArrays);
		inUseLargeArrays.clear();
		inUseSmallArrays.clear();
	}

	public String getCacheSizes() {
		return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
	}
}
