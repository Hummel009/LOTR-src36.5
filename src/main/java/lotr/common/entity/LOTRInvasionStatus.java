package lotr.common.entity;

public class LOTRInvasionStatus {
	private LOTREntityInvasionSpawner watchedInvasion;
	private int ticksSinceRelevance;

	public String getTitle() {
		return watchedInvasion.getInvasionType().invasionName();
	}

	public float[] getRGB() {
		return watchedInvasion.getInvasionType().invasionFaction.getFactionRGB_MinBrightness(0.45f);
	}

	public float getHealth() {
		return watchedInvasion.getInvasionHealthStatus();
	}

	public boolean isActive() {
		return watchedInvasion != null;
	}

	public void setWatchedInvasion(LOTREntityInvasionSpawner invasion) {
		watchedInvasion = invasion;
		ticksSinceRelevance = 0;
	}

	public void tick() {
		if (watchedInvasion != null) {
			if (watchedInvasion.isDead) {
				clear();
			} else {
				++ticksSinceRelevance;
				if (ticksSinceRelevance >= 600) {
					ticksSinceRelevance = 0;
					clear();
				}
			}
		}
	}

	public void clear() {
		watchedInvasion = null;
		ticksSinceRelevance = 0;
	}
}
