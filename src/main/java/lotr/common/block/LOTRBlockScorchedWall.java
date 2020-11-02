package lotr.common.block;

import cpw.mods.fml.relauncher.*;
import lotr.common.LOTRMod;
import net.minecraft.util.IIcon;

public class LOTRBlockScorchedWall extends LOTRBlockWallBase {
	public LOTRBlockScorchedWall() {
		super(LOTRMod.scorchedStone, 1);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public IIcon getIcon(int i, int j) {
		return LOTRMod.scorchedStone.getIcon(i, j);
	}
}
