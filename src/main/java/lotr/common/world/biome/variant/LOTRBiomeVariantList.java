package lotr.common.world.biome.variant;

import java.util.*;

public class LOTRBiomeVariantList {
	private float totalWeight;
	private List<VariantBucket> variantList = new ArrayList<>();

	public void add(LOTRBiomeVariant v, float f) {
		variantList.add(new VariantBucket(v, totalWeight, totalWeight + f));
		totalWeight += f;
	}

	public void clear() {
		totalWeight = 0.0f;
		variantList.clear();
	}

	public LOTRBiomeVariant get(float index) {
		if (index < 0.0f) {
			index = 0.0f;
		}
		if (index >= 1.0f) {
			index = 0.9999f;
		}
		float f = index * totalWeight;
		for (VariantBucket bucket : variantList) {
			if (f < bucket.min || f >= bucket.max) {
				continue;
			}
			return bucket.variant;
		}
		return null;
	}

	public boolean isEmpty() {
		return totalWeight == 0.0f;
	}

	private static class VariantBucket {
		public final LOTRBiomeVariant variant;
		public final float min;
		public final float max;

		private VariantBucket(LOTRBiomeVariant v, float f0, float f1) {
			variant = v;
			min = f0;
			max = f1;
		}
	}

}
