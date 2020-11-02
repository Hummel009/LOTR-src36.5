package lotr.common.fac;

import lotr.common.*;
import net.minecraft.util.StatCollector;

public class LOTRFactionRank implements Comparable<LOTRFactionRank> {
	public static final LOTRFactionRank RANK_NEUTRAL = new Dummy("lotr.faction.rank.neutral");
	public static final LOTRFactionRank RANK_ENEMY = new Dummy("lotr.faction.rank.enemy");
	public final LOTRFaction fac;
	public final float alignment;
	public final String name;
	private LOTRAchievementRank rankAchievement;
	private boolean isGendered;
	private LOTRTitle rankTitle;
	private LOTRTitle rankTitleMasc;
	private LOTRTitle rankTitleFem;

	public LOTRFactionRank(LOTRFaction f, float al, String s, boolean gend) {
		fac = f;
		alignment = al;
		name = s;
		isGendered = gend;
	}

	public String getCodeName() {
		return "lotr.faction." + fac.codeName() + ".rank." + name;
	}

	public String getCodeNameFem() {
		return getCodeName() + "_fm";
	}

	public String getCodeFullName() {
		return getCodeName() + ".f";
	}

	public String getCodeFullNameFem() {
		return getCodeNameFem() + ".f";
	}

	public String getCodeFullNameWithGender(LOTRPlayerData pd) {
		if (isGendered() && pd.useFeminineRanks()) {
			return getCodeFullNameFem();
		}
		return getCodeFullName();
	}

	public String getDisplayName() {
		return StatCollector.translateToLocal(getCodeName());
	}

	public String getDisplayNameFem() {
		return StatCollector.translateToLocal(getCodeNameFem());
	}

	public String getDisplayFullName() {
		return StatCollector.translateToLocal(getCodeFullName());
	}

	public String getDisplayFullNameFem() {
		return StatCollector.translateToLocal(getCodeFullNameFem());
	}

	public String getShortNameWithGender(LOTRPlayerData pd) {
		if (isGendered() && pd.useFeminineRanks()) {
			return getDisplayNameFem();
		}
		return getDisplayName();
	}

	public String getFullNameWithGender(LOTRPlayerData pd) {
		if (isGendered() && pd.useFeminineRanks()) {
			return getDisplayFullNameFem();
		}
		return getDisplayFullName();
	}

	public boolean isGendered() {
		return isGendered;
	}

	public boolean isDummyRank() {
		return false;
	}

	public LOTRFactionRank makeTitle() {
		if (isGendered) {
			rankTitleMasc = new LOTRTitle(this, false);
			rankTitleFem = new LOTRTitle(this, true);
			return this;
		}
		rankTitle = new LOTRTitle(this, false);
		return this;
	}

	public LOTRFactionRank makeAchievement() {
		rankAchievement = new LOTRAchievementRank(this);
		return this;
	}

	public LOTRAchievementRank getRankAchievement() {
		return rankAchievement;
	}

	public LOTRFactionRank setPledgeRank() {
		fac.setPledgeRank(this);
		return this;
	}

	public boolean isAbovePledgeRank() {
		return alignment > fac.getPledgeAlignment();
	}

	@Override
	public int compareTo(LOTRFactionRank other) {
		if (fac != other.fac) {
			throw new IllegalArgumentException("Cannot compare two ranks from different factions!");
		}
		float al1 = alignment;
		float al2 = other.alignment;
		if (al1 == al2) {
			throw new IllegalArgumentException("Two ranks cannot have the same alignment value!");
		}
		return -Float.valueOf(al1).compareTo(al2);
	}

	public static final class Dummy extends LOTRFactionRank {
		public Dummy(String s) {
			super(null, 0.0f, s, false);
		}

		@Override
		public String getCodeName() {
			return name;
		}

		@Override
		public String getDisplayName() {
			return StatCollector.translateToLocal(getCodeName());
		}

		@Override
		public String getDisplayFullName() {
			return getDisplayName();
		}

		@Override
		public boolean isDummyRank() {
			return true;
		}
	}

}
