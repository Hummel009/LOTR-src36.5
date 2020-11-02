package lotr.client.gui;

import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

public class LOTRGuiScrollPane {
	private final int scrollWidgetWidth;
	private final int scrollWidgetHeight;
	private int barColor;
	private int widgetColor;
	public int scrollBarX0;
	public int paneX0;
	public int paneY0;
	public int paneY1;
	public boolean hasScrollBar;
	public float currentScroll;
	public boolean isScrolling;
	public boolean mouseOver;
	private boolean wasMouseDown;

	public LOTRGuiScrollPane(int ww, int wh) {
		scrollWidgetWidth = ww;
		scrollWidgetHeight = wh;
		barColor = -1711276033;
		widgetColor = -1426063361;
	}

	public LOTRGuiScrollPane setColors(int c1, int c2) {
		int alphaMask = -16777216;
		if ((c1 & alphaMask) == 0) {
			c1 |= alphaMask;
		}
		if ((c2 & alphaMask) == 0) {
			c2 |= alphaMask;
		}
		barColor = c1;
		widgetColor = c2;
		return this;
	}

	public int[] getMinMaxIndices(List list, int displayed) {
		int size = list.size();
		int min = 0 + Math.round(currentScroll * (size - displayed));
		int max = displayed - 1 + Math.round(currentScroll * (size - displayed));
		min = Math.max(min, 0);
		max = Math.min(max, size - 1);
		return new int[] { min, max };
	}

	public void drawScrollBar() {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		int x0 = scrollBarX0 + scrollWidgetWidth / 2;
		int y0 = paneY0;
		int y1 = paneY1;
		Gui.drawRect(x0, y0, x0 + 1, y1, barColor);
		int scroll = (int) (currentScroll * (y1 - y0 - scrollWidgetHeight));
		x0 = scrollBarX0;
		int x1 = x0 + scrollWidgetWidth;
		y1 = (y0 += scroll) + scrollWidgetHeight;
		Gui.drawRect(x0, y0, x1, y1, widgetColor);
	}

	public void mouseDragScroll(int i, int j) {
		boolean isMouseDown = Mouse.isButtonDown(0);
		if (!hasScrollBar) {
			resetScroll();
		} else {
			boolean mouseOverScroll;
			int x0 = paneX0;
			int x1 = scrollBarX0 + scrollWidgetWidth;
			int y0 = paneY0;
			int y1 = paneY1;
			mouseOver = i >= x0 && j >= y0 && i < x1 && j < y1;
			x0 = scrollBarX0;
			mouseOverScroll = i >= x0 && j >= y0 && i < x1 && j < y1;
			if (!wasMouseDown && isMouseDown && mouseOverScroll) {
				isScrolling = true;
			}
			if (!isMouseDown) {
				isScrolling = false;
			}
			if (isScrolling) {
				currentScroll = (j - y0 - scrollWidgetHeight / 2.0f) / ((float) (y1 - y0) - (float) scrollWidgetHeight);
				currentScroll = MathHelper.clamp_float(currentScroll, 0.0f, 1.0f);
			}
		}
		wasMouseDown = isMouseDown;
	}

	public void resetScroll() {
		currentScroll = 0.0f;
		isScrolling = false;
	}

	public void mouseWheelScroll(int delta, int size) {
		currentScroll -= (float) delta / (float) size;
		currentScroll = MathHelper.clamp_float(currentScroll, 0.0f, 1.0f);
	}
}
