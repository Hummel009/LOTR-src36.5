package lotr.client;

import java.util.*;

import lotr.common.*;
import lotr.common.fac.LOTRFaction;
import net.minecraft.entity.player.EntityPlayer;

public class LOTRAlignmentTicker {
	private static Map<LOTRFaction, LOTRAlignmentTicker> allFactionTickers = new HashMap<>();
	private final LOTRFaction theFac;
	private float oldAlign;
	private float newAlign;
	private int moveTick = 0;
	private int prevMoveTick = 0;
	public int flashTick;
	public int numericalTick;

	public static LOTRAlignmentTicker forFaction(LOTRFaction fac) {
		LOTRAlignmentTicker ticker = allFactionTickers.get(fac);
		if (ticker == null) {
			ticker = new LOTRAlignmentTicker(fac);
			allFactionTickers.put(fac, ticker);
		}
		return ticker;
	}

	public static void updateAll(EntityPlayer entityplayer, boolean forceInstant) {
		for (LOTRDimension dim : LOTRDimension.values()) {
			for (LOTRFaction fac : dim.factionList) {
				LOTRAlignmentTicker.forFaction(fac).update(entityplayer, forceInstant);
			}
		}
	}

	public LOTRAlignmentTicker(LOTRFaction f) {
		theFac = f;
	}

	public void update(EntityPlayer entityplayer, boolean forceInstant) {
		float curAlign = LOTRLevelData.getData(entityplayer).getAlignment(theFac);
		if (forceInstant) {
			oldAlign = newAlign = curAlign;
			moveTick = 0;
			prevMoveTick = 0;
			flashTick = 0;
			numericalTick = 0;
		} else {
			if (newAlign != curAlign) {
				oldAlign = newAlign;
				newAlign = curAlign;
				moveTick = 20;
				prevMoveTick = 20;
				flashTick = 30;
				numericalTick = 200;
			}
			prevMoveTick = moveTick;
			if (moveTick > 0) {
				--moveTick;
				if (moveTick <= 0) {
					oldAlign = newAlign;
				}
			}
			if (flashTick > 0) {
				--flashTick;
			}
			if (numericalTick > 0) {
				--numericalTick;
			}
		}
	}

	public float getInterpolatedAlignment(float f) {
		if (moveTick == 0) {
			return oldAlign;
		}
		float tickF = prevMoveTick + (moveTick - prevMoveTick) * f;
		tickF /= 20.0f;
		tickF = 1.0f - tickF;
		float align = oldAlign + (newAlign - oldAlign) * tickF;
		return align;
	}
}
