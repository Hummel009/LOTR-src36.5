package lotr.common.world.structure2;

import lotr.common.LOTRMod;
import lotr.common.entity.npc.*;
import net.minecraft.world.World;

public class LOTRWorldGenRivendellForge extends LOTRWorldGenHighElvenForge {
	public LOTRWorldGenRivendellForge(boolean flag) {
		super(flag);
		roofBlock = LOTRMod.clayTileDyed;
		roofMeta = 9;
		roofStairBlock = LOTRMod.stairsClayTileDyedCyan;
		tableBlock = LOTRMod.rivendellTable;
	}

	@Override
	protected LOTREntityElf getElf(World world) {
		return new LOTREntityRivendellSmith(world);
	}
}
