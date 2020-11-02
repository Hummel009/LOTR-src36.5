package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;

public class LOTRGuiSlider extends GuiButton {
	private String baseDisplayString;
	private String overrideStateString;
	private boolean isTime = false;
	private boolean isFloat = false;
	private boolean valueOnly = false;
	private int numberDigits = 0;
	private int minValue;
	private int maxValue;
	private float minValueF;
	private float maxValueF;
	private float sliderValue = 1.0f;
	public boolean dragging = false;

	public LOTRGuiSlider(int id, int x, int y, int width, int height, String s) {
		super(id, x, y, width, height, s);
		baseDisplayString = s;
	}

	public void setFloat() {
		isFloat = true;
	}

	public void setMinutesSecondsTime() {
		isTime = true;
	}

	public void setValueOnly() {
		valueOnly = true;
	}

	public void setNumberDigits(int i) {
		numberDigits = i;
	}

	@Override
	public int getHoverState(boolean flag) {
		return 0;
	}

	public void setMinMaxValues(int min, int max) {
		minValue = min;
		maxValue = max;
	}

	public int getSliderValue() {
		return minValue + Math.round(sliderValue * (maxValue - minValue));
	}

	public void setSliderValue(int value) {
		value = MathHelper.clamp_int(value, minValue, maxValue);
		sliderValue = (float) (value - minValue) / (float) (maxValue - minValue);
	}

	public void setMinMaxValues_F(float min, float max) {
		minValueF = min;
		maxValueF = max;
	}

	public float getSliderValue_F() {
		return minValueF + sliderValue * (maxValueF - minValueF);
	}

	public void setSliderValue_F(float value) {
		value = MathHelper.clamp_float(value, minValueF, maxValueF);
		sliderValue = (value - minValueF) / (maxValueF - minValueF);
	}

	public void setOverrideStateString(String s) {
		overrideStateString = s;
	}

	@Override
	public void drawButton(Minecraft mc, int i, int j) {
		if (overrideStateString != null) {
			displayString = overrideStateString;
		} else if (isTime) {
			int value = getSliderValue();
			int seconds = value % 60;
			int minutes = value / 60;
			displayString = String.format("%d:%02d", minutes, seconds);
		} else if (isFloat) {
			displayString = String.format("%.2f", Float.valueOf(getSliderValue_F()));
		} else {
			int value = getSliderValue();
			displayString = String.valueOf(value);
			if (numberDigits > 0) {
				displayString = String.format("%0" + numberDigits + "d", value);
			}
		}
		if (!valueOnly) {
			displayString = baseDisplayString + ": " + displayString;
		}
		super.drawButton(mc, i, j);
	}

	@Override
	protected void mouseDragged(Minecraft mc, int i, int j) {
		if (visible && enabled) {
			if (dragging) {
				sliderValue = (float) (i - (xPosition + 4)) / (float) (width - 8);
				if (sliderValue < 0.0f) {
					sliderValue = 0.0f;
				}
				if (sliderValue > 1.0f) {
					sliderValue = 1.0f;
				}
			}
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)), yPosition, 0, 66, 4, 20);
			drawTexturedModalRect(xPosition + (int) (sliderValue * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
		}
	}

	@Override
	public boolean mousePressed(Minecraft mc, int i, int j) {
		if (super.mousePressed(mc, i, j)) {
			sliderValue = (float) (i - (xPosition + 4)) / (float) (width - 8);
			if (sliderValue < 0.0f) {
				sliderValue = 0.0f;
			}
			if (sliderValue > 1.0f) {
				sliderValue = 1.0f;
			}
			dragging = true;
			return true;
		}
		return false;
	}

	@Override
	public void mouseReleased(int i, int j) {
		dragging = false;
	}
}
