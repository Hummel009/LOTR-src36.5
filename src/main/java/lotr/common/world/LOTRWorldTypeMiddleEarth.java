package lotr.common.world;

import cpw.mods.fml.relauncher.*;
import net.minecraft.world.WorldType;

public class LOTRWorldTypeMiddleEarth extends WorldType {
	public LOTRWorldTypeMiddleEarth(String name) {
		super(name);
	}

	@Override
	@SideOnly(value = Side.CLIENT)
	public boolean showWorldInfoNotice() {
		return true;
	}
}
