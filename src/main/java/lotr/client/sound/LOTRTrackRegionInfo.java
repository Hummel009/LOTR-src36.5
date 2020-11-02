package lotr.client.sound;

import java.util.*;

import lotr.common.world.biome.LOTRMusicRegion;

public class LOTRTrackRegionInfo {
	private LOTRMusicRegion region;
	private List<String> subregions = new ArrayList<>();
	private double weight;
	private List<LOTRMusicCategory> categories = new ArrayList<>();

	public LOTRTrackRegionInfo(LOTRMusicRegion r) {
		region = r;
		weight = 1.0;
	}

	public List<String> getSubregions() {
		return subregions;
	}

	public void addSubregion(String sub) {
		if (!subregions.contains(sub)) {
			subregions.add(sub);
		}
	}

	public void addAllSubregions() {
		List<String> allSubs = region.getAllSubregions();
		if (!allSubs.isEmpty()) {
			for (String sub : allSubs) {
				addSubregion(sub);
			}
		}
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double d) {
		weight = d;
	}

	public List<LOTRMusicCategory> getCategories() {
		return categories;
	}

	public void addCategory(LOTRMusicCategory cat) {
		if (!categories.contains(cat)) {
			categories.add(cat);
		}
	}

	public void addAllCategories() {
		for (LOTRMusicCategory cat : LOTRMusicCategory.values()) {
			addCategory(cat);
		}
	}
}
