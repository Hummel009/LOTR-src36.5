package lotr.common.world;

import net.minecraft.world.storage.*;

public class LOTRWorldInfo extends DerivedWorldInfo {
	private long lotrTotalTime;
	private long lotrWorldTime;

	public LOTRWorldInfo(WorldInfo worldinfo) {
		super(worldinfo);
	}

	@Override
	public long getWorldTotalTime() {
		return lotrTotalTime;
	}

	@Override
	public long getWorldTime() {
		return lotrWorldTime;
	}

	@Override
	public void incrementTotalWorldTime(long time) {
	}

	@Override
	public void setWorldTime(long time) {
	}

	public void lotr_setTotalTime(long time) {
		lotrTotalTime = time;
	}

	public void lotr_setWorldTime(long time) {
		lotrWorldTime = time;
	}
}
