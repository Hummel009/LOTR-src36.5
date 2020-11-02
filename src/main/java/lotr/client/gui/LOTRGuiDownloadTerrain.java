package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import lotr.common.LOTRDimension;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.StatCollector;

public class LOTRGuiDownloadTerrain extends GuiDownloadTerrain {
	private LOTRGuiMap mapGui = new LOTRGuiMap();
	private LOTRGuiRendererMap mapRenderer = new LOTRGuiRendererMap();
	private int tickCounter;

	public LOTRGuiDownloadTerrain(NetHandlerPlayClient handler) {
		super(handler);
		mapRenderer.setSepia(true);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int i, int j) {
		super.setWorldAndResolution(mc, i, j);
		mapGui.setWorldAndResolution(mc, i, j);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		++tickCounter;
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		int dimension = mc.thePlayer.dimension;
		if (dimension == LOTRDimension.MIDDLE_EARTH.dimensionID) {
			drawBackground(0);
			GL11.glEnable(3008);
			GL11.glEnable(3042);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			mapRenderer.prevMapX = mapRenderer.mapX = LOTRWaypoint.worldToMapX(mc.thePlayer.posX);
			mapRenderer.prevMapY = mapRenderer.mapY = LOTRWaypoint.worldToMapZ(mc.thePlayer.posZ);
			mapRenderer.zoomExp = -0.3f;
			mapRenderer.zoomStable = (float) Math.pow(2.0, -0.30000001192092896);
			int x0 = 0;
			int x1 = width;
			int y0 = 40;
			int y1 = height - 40;
			mapRenderer.renderMap(this, mapGui, f, x0, y0, x1, y1);
			mapRenderer.renderVignettes(this, zLevel, 1, x0, y0, x1, y1);
			GL11.glDisable(3042);
			String s = StatCollector.translateToLocal("lotr.loading.enterME");
			drawCenteredString(fontRendererObj, s, width / 2, height / 2 - 50, 16777215);
		} else if (dimension == LOTRDimension.UTUMNO.dimensionID) {
			Gui.drawRect(0, 0, width, height, -16777216);
			GL11.glEnable(3042);
			float alpha = 1.0f - tickCounter / 120.0f;
			int alphaI = (int) (alpha * 255.0f);
			if (alphaI > 4) {
				String s = StatCollector.translateToLocal("lotr.loading.enterUtumno");
				drawCenteredString(fontRendererObj, s, width / 2, height / 2 - 50, alphaI << 24 | 0xFFFFFF);
			}
			GL11.glDisable(3042);
		} else {
			super.drawScreen(i, j, f);
		}
	}
}
