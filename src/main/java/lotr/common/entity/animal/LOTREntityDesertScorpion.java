package lotr.common.entity.animal;

import lotr.common.world.biome.LOTRBiomeGenNearHarad;
import net.minecraft.world.World;

public class LOTREntityDesertScorpion extends LOTREntityScorpion implements LOTRBiomeGenNearHarad.ImmuneToHeat {
	public boolean pyramidSpawned = false;

	public LOTREntityDesertScorpion(World world) {
		super(world);
		isImmuneToFire = true;
	}

	@Override
	protected int getRandomScorpionScale() {
		return rand.nextInt(2);
	}

	@Override
	public boolean getCanSpawnHere() {
		return super.getCanSpawnHere() && (spawningFromSpawner || pyramidSpawned || posY < 60.0 || rand.nextInt(500) == 0);
	}

	@Override
	public boolean isValidLightLevel() {
		return spawningFromSpawner || pyramidSpawned || super.isValidLightLevel();
	}
}
