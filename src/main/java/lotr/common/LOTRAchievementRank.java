package lotr.common;

import lotr.common.fac.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

public class LOTRAchievementRank extends LOTRAchievement {
	private LOTRFactionRank theRank;
	private LOTRFaction theFac;

	public LOTRAchievementRank(LOTRFactionRank rank) {
		super(rank.fac.getAchieveCategory(), rank.fac.getAchieveCategory().getNextRankAchID(), LOTRMod.goldRing, "alignment_" + rank.fac.codeName() + "_" + rank.alignment);
		theRank = rank;
		theFac = theRank.fac;
		setRequiresAlly(theFac);
		setSpecial();
	}

	@Override
	public String getUntranslatedTitle(EntityPlayer entityplayer) {
		return theRank.getCodeFullNameWithGender(LOTRLevelData.getData(entityplayer));
	}

	@Override
	public String getDescription(EntityPlayer entityplayer) {
		String suffix = requiresPledge() ? "achieveRankPledge" : "achieveRank";
		return StatCollector.translateToLocalFormatted("lotr.faction." + theFac.codeName() + "." + suffix, LOTRAlignmentValues.formatAlignForDisplay(theRank.alignment));
	}

	private boolean requiresPledge() {
		return theRank.isAbovePledgeRank();
	}

	@Override
	public boolean canPlayerEarn(EntityPlayer entityplayer) {
		LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
		float align = pd.getAlignment(theFac);
		if (align < 0.0f) {
			return false;
		}
		return !requiresPledge() || pd.isPledgedTo(theFac);
	}

	public boolean isPlayerRequiredRank(EntityPlayer entityplayer) {
		LOTRPlayerData pd = LOTRLevelData.getData(entityplayer);
		float align = pd.getAlignment(theFac);
		float rankAlign = theRank.alignment;
		if (requiresPledge() && !pd.isPledgedTo(theFac)) {
			return false;
		}
		return align >= rankAlign;
	}
}
