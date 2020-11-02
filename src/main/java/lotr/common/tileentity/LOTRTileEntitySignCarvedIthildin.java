package lotr.common.tileentity;

import cpw.mods.fml.relauncher.*;

public class LOTRTileEntitySignCarvedIthildin extends LOTRTileEntitySignCarved {
	private LOTRDwarvenGlowLogic glowLogic = new LOTRDwarvenGlowLogic().setPlayerRange(8);

	@Override
	public void updateEntity() {
		super.updateEntity();
		glowLogic.update(worldObj, xCoord, yCoord, zCoord);
	}

	public float getGlowBrightness(float f) {
		if (isFakeGuiSign) {
			return 1.0f;
		}
		return glowLogic.getGlowBrightness(worldObj, xCoord, yCoord, zCoord, f);
	}

	@SideOnly(value = Side.CLIENT)
	@Override
	public double getMaxRenderDistanceSquared() {
		return 1024.0;
	}
}
