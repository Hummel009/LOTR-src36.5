package lotr.client.sound;

import java.io.InputStream;
import java.util.*;

import lotr.common.world.biome.LOTRMusicRegion;
import net.minecraft.client.audio.*;
import net.minecraft.util.ResourceLocation;

public class LOTRMusicTrack extends PositionedSound {
	private final String filename;
	private String title;
	private Map<LOTRMusicRegion, LOTRTrackRegionInfo> regions = new HashMap<>();
	private List<String> authors = new ArrayList<>();

	public LOTRMusicTrack(String s) {
		super(LOTRMusicTrack.getMusicResource(s));
		volume = 1.0f;
		field_147663_c = 1.0f;
		xPosF = 0.0f;
		yPosF = 0.0f;
		zPosF = 0.0f;
		repeat = false;
		field_147665_h = 0;
		field_147666_i = ISound.AttenuationType.NONE;
		filename = s;
	}

	private static ResourceLocation getMusicResource(String s) {
		ResourceLocation res = new ResourceLocation("lotrmusic", s);
		return res;
	}

	public void loadTrack(InputStream in) {
		loadSoundResource();
		LOTRMusic.addTrackToRegions(this);
	}

	private void loadSoundResource() {
		SoundEventAccessorComposite soundAccessorComp;
		ResourceLocation resource = getPositionedSoundLocation();
		SoundList soundList = new SoundList();
		soundList.setReplaceExisting(true);
		soundList.setSoundCategory(SoundCategory.MUSIC);
		SoundList.SoundEntry soundEntry = new SoundList.SoundEntry();
		soundEntry.setSoundEntryName(filename);
		soundEntry.setSoundEntryVolume(getVolume());
		soundEntry.setSoundEntryPitch(getPitch());
		soundEntry.setSoundEntryWeight(1);
		soundEntry.setSoundEntryType(SoundList.SoundEntry.Type.SOUND_EVENT);
		soundEntry.setStreaming(true);
		soundList.getSoundList().add(soundEntry);
		SoundRegistry sndRegistry = LOTRMusic.Reflect.getSoundRegistry();
		if (sndRegistry.containsKey(resource) && !soundList.canReplaceExisting()) {
			soundAccessorComp = (SoundEventAccessorComposite) sndRegistry.getObject(resource);
		} else {
			soundAccessorComp = new SoundEventAccessorComposite(resource, 1.0, 1.0, soundList.getSoundCategory());
			sndRegistry.registerSound(soundAccessorComp);
		}
		SoundPoolEntry soundPoolEntry = new SoundPoolEntry(resource, soundEntry.getSoundEntryPitch(), soundEntry.getSoundEntryVolume(), soundEntry.isStreaming());
		TrackSoundAccessor soundAccessor = new TrackSoundAccessor(soundPoolEntry, soundEntry.getSoundEntryWeight());
		soundAccessorComp.addSoundToEventPool(soundAccessor);
	}

	public String getFilename() {
		return filename;
	}

	public String getTitle() {
		if (title != null) {
			return title;
		}
		return filename;
	}

	public void setTitle(String s) {
		title = s;
	}

	public Set<LOTRMusicRegion> getAllRegions() {
		return regions.keySet();
	}

	public LOTRTrackRegionInfo createRegionInfo(LOTRMusicRegion reg) {
		LOTRTrackRegionInfo info = regions.get(reg);
		if (info == null) {
			info = new LOTRTrackRegionInfo(reg);
			regions.put(reg, info);
		}
		return info;
	}

	public LOTRTrackRegionInfo getRegionInfo(LOTRMusicRegion reg) {
		if (regions.containsKey(reg)) {
			return regions.get(reg);
		}
		return null;
	}

	public void addAuthor(String s) {
		authors.add(s);
	}

	public List<String> getAuthors() {
		return authors;
	}

	public String[] getTrackInfo() {
		ArrayList<String> list = new ArrayList<>();
		list.add("Title: " + getTitle());
		list.add("Filename: " + getFilename());
		list.add("Regions:");
		for (LOTRMusicRegion reg : getAllRegions()) {
			List<LOTRMusicCategory> categories;
			LOTRTrackRegionInfo info = getRegionInfo(reg);
			list.add(">" + reg.regionName);
			list.add(">Weight: " + info.getWeight());
			List<String> subs = info.getSubregions();
			if (!subs.isEmpty()) {
				list.add(">Subregions:");
				for (String s2 : subs) {
					list.add(">>" + s2);
				}
			}
			if ((categories = info.getCategories()).isEmpty()) {
				continue;
			}
			list.add(">Categories:");
			for (LOTRMusicCategory cat : categories) {
				list.add(">>" + cat.categoryName);
			}
		}
		list.add("Authors:");
		for (String auth : getAuthors()) {
			list.add(">" + auth);
		}
		return list.toArray(new String[0]);
	}

	private static class TrackSoundAccessor implements ISoundEventAccessor {
		private final SoundPoolEntry soundEntry;
		private final int weight;

		private TrackSoundAccessor(SoundPoolEntry e, int i) {
			soundEntry = e;
			weight = i;
		}

		@Override
		public int func_148721_a() {
			return weight;
		}

		@Override
		public SoundPoolEntry func_148720_g() {
			return new SoundPoolEntry(soundEntry);
		}
	}

}
