package lotr.common.entity.npc;

import lotr.common.*;
import lotr.common.fac.LOTRFaction;
import net.minecraft.world.World;

public class LOTREntityMirkwoodSpider extends LOTREntitySpiderBase {
	public LOTREntityMirkwoodSpider(World world) {
		super(world);
	}

	@Override
	protected int getRandomSpiderScale() {
		return rand.nextInt(3);
	}

	@Override
	protected int getRandomSpiderType() {
		return rand.nextBoolean() ? 0 : 1 + rand.nextInt(2);
	}

	@Override
	public LOTRFaction getFaction() {
		return LOTRFaction.DOL_GULDUR;
	}

	@Override
	public float getAlignmentBonus() {
		return 1.0f;
	}

	@Override
	protected void dropFewItems(boolean flag, int i) {
		super.dropFewItems(flag, i);
		if (flag && rand.nextInt(4) == 0) {
			dropItem(LOTRMod.mysteryWeb, 1);
		}
	}

	@Override
	protected LOTRAchievement getKillAchievement() {
		return LOTRAchievement.killMirkwoodSpider;
	}
}
