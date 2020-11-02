package lotr.client.gui;

import java.util.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.math.IntMath;

import lotr.common.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.*;

public class LOTRGuiAchievements extends LOTRGuiMenuBase {
	public static ResourceLocation pageTexture = new ResourceLocation("lotr:gui/achievements/page.png");
	public static ResourceLocation iconsTexture = new ResourceLocation("lotr:gui/achievements/icons.png");
	private static LOTRDimension currentDimension;
	private static LOTRDimension prevDimension;
	private static LOTRAchievement.Category currentCategory;
	private ArrayList currentCategoryTakenAchievements = new ArrayList();
	private ArrayList currentCategoryUntakenAchievements = new ArrayList();
	private int currentCategoryTakenCount;
	private int currentCategoryUntakenCount;
	private LOTRGuiButtonAchievements buttonCategoryPrev;
	private LOTRGuiButtonAchievements buttonCategoryNext;
	private int totalTakenCount;
	private int totalAvailableCount;
	private float currentScroll = 0.0f;
	private boolean isScrolling = false;
	private boolean wasMouseDown;
	private int catScrollAreaX0;
	private int catScrollAreaX1;
	private int catScrollAreaY0;
	private int catScrollAreaY1;
	private boolean wasInCategoryScrollBar;
	private int prevMouseX;
	private int mouseX;
	private int mouseY;

	@Override
	public void initGui() {
		xSize = 220;
		super.initGui();
		buttonCategoryPrev = new LOTRGuiButtonAchievements(0, true, guiLeft + 14, guiTop + 13);
		buttonList.add(buttonCategoryPrev);
		buttonCategoryNext = new LOTRGuiButtonAchievements(1, false, guiLeft + 191, guiTop + 13);
		buttonList.add(buttonCategoryNext);
		updateAchievementLists();
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateAchievementLists();
		prevMouseX = mouseX;
		wasInCategoryScrollBar = isMouseInCategoryScrollBar();
	}

	private boolean isMouseInCategoryScrollBar() {
		return Mouse.isButtonDown(0) && mouseX >= catScrollAreaX0 && mouseX < catScrollAreaX1 && mouseY >= catScrollAreaY0 && mouseY < catScrollAreaY1;
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		mouseX = i;
		mouseY = j;
		if (wasInCategoryScrollBar) {
			int diff = mouseX - prevMouseX;
			boolean changed = false;
			if (diff >= 4) {
				prevCategory();
				changed = true;
			} else if (diff <= -4) {
				nextCategory();
				changed = true;
			}
			if (changed) {
				mouseX = prevMouseX;
				wasInCategoryScrollBar = false;
			}
		}
		boolean isMouseDown = Mouse.isButtonDown(0);
		int scrollBarX0 = guiLeft + 201;
		int scrollBarX1 = scrollBarX0 + 12;
		int scrollBarY0 = guiTop + 48;
		int scrollBarY1 = scrollBarY0 + 200;
		if (!wasMouseDown && isMouseDown && i >= scrollBarX0 && i < scrollBarX1 && j >= scrollBarY0 && j < scrollBarY1) {
			isScrolling = hasScrollBar();
		}
		if (!isMouseDown) {
			isScrolling = false;
		}
		wasMouseDown = isMouseDown;
		if (isScrolling) {
			currentScroll = (j - scrollBarY0 - 8.5f) / (scrollBarY1 - scrollBarY0 - 17.0f);
			currentScroll = Math.max(currentScroll, 0.0f);
			currentScroll = Math.min(currentScroll, 1.0f);
		}
		drawDefaultBackground();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(pageTexture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		String title = StatCollector.translateToLocalFormatted("lotr.gui.achievements.title", currentDimension.getDimensionName(), totalTakenCount, totalAvailableCount);
		this.drawCenteredString(title, guiLeft + xSize / 2, guiTop - 30, 16777215);
		String categoryName = currentCategory.getDisplayName();
		categoryName = StatCollector.translateToLocalFormatted("lotr.gui.achievements.category", categoryName, currentCategoryTakenCount, currentCategoryTakenCount + currentCategoryUntakenCount);
		this.drawCenteredString(categoryName, guiLeft + xSize / 2, guiTop + 28, 8019267);
		buttonCategoryPrev.buttonCategory = getCategoryAtRelativeIndex(-1);
		buttonCategoryNext.buttonCategory = getCategoryAtRelativeIndex(1);
		super.drawScreen(i, j, f);
		int catScrollCentre = guiLeft + xSize / 2;
		int catScrollX = catScrollCentre - 76;
		int catScrollY = guiTop + 13;
		mc.getTextureManager().bindTexture(iconsTexture);
		this.drawTexturedModalRect(catScrollX, catScrollY, 0, 100, 152, 10);
		catScrollAreaX0 = catScrollX;
		catScrollAreaX1 = catScrollX + 152;
		catScrollAreaY0 = catScrollY;
		catScrollAreaY1 = catScrollY + 10;
		int catWidth = 16;
		int catCentreWidth = 50;
		int catsEitherSide = (catScrollAreaX1 - catScrollAreaX0) / catWidth + 1;
		for (int l = -catsEitherSide; l <= catsEitherSide; ++l) {
			int thisCatWidth = l == 0 ? catCentreWidth : catWidth;
			int catX = catScrollCentre;
			if (l != 0) {
				int signum = Integer.signum(l);
				catX += (catCentreWidth + catWidth) / 2 * signum;
				catX += (Math.abs(l) - 1) * signum * catWidth;
			}
			int catX0 = catX - thisCatWidth / 2;
			int catX1 = catX + thisCatWidth;
			if (catX0 < catScrollAreaX0) {
				catX0 = catScrollAreaX0;
			}
			if (catX1 > catScrollAreaX1) {
				catX1 = catScrollAreaX1;
			}
			int catY0 = catScrollAreaY0;
			int catY1 = catScrollAreaY1;
			LOTRAchievement.Category thisCategory = getCategoryAtRelativeIndex(l);
			float[] catColors = thisCategory.getCategoryRGB();
			mc.getTextureManager().bindTexture(iconsTexture);
			GL11.glColor4f(catColors[0], catColors[1], catColors[2], 1.0f);
			this.drawTexturedModalRect(catX0, catY0, catX0 - catScrollAreaX0 + 0, 100, catX1 - catX0, catY1 - catY0);
		}
		mc.getTextureManager().bindTexture(iconsTexture);
		this.drawTexturedModalRect(catScrollX, catScrollY, 0, 110, 152, 10);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(iconsTexture);
		if (hasScrollBar()) {
			int offset = (int) (currentScroll * 181.0f);
			this.drawTexturedModalRect(scrollBarX0, scrollBarY0 + offset, 190, 0, 10, 17);
		} else {
			this.drawTexturedModalRect(scrollBarX0, scrollBarY0, 200, 0, 10, 17);
		}
		drawAchievements(i, j);
	}

	private void drawAchievements(int mouseX, int mouseY) {
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glDisable(2896);
		GL11.glEnable(32826);
		GL11.glEnable(2903);
		int size = currentCategoryTakenCount + currentCategoryUntakenCount;
		int min = 0 + Math.round(currentScroll * (size - 4));
		int max = 3 + Math.round(currentScroll * (size - 4));
		if (max > size - 1) {
			max = size - 1;
		}
		for (int i = min; i <= max; ++i) {
			LOTRAchievement achievement;
			boolean hasAchievement;
			if (i < currentCategoryTakenCount) {
				achievement = (LOTRAchievement) currentCategoryTakenAchievements.get(i);
				hasAchievement = true;
			} else {
				achievement = (LOTRAchievement) currentCategoryUntakenAchievements.get(i - currentCategoryTakenCount);
				hasAchievement = false;
			}
			int offset = 47 + 50 * (i - min);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			mc.getTextureManager().bindTexture(iconsTexture);
			this.drawTexturedModalRect(guiLeft + 9, guiTop + offset, 0, hasAchievement ? 0 : 50, 190, 50);
			int iconLeft = guiLeft + 12;
			int iconTop = guiTop + offset + 3;
			GL11.glEnable(2896);
			GL11.glEnable(2884);
			renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), achievement.icon, iconLeft, iconTop);
			GL11.glDisable(2896);
			if (!hasAchievement) {
				GL11.glPushMatrix();
				GL11.glTranslatef(0.0f, 0.0f, 300.0f);
				Gui.drawRect(iconLeft, iconTop, iconLeft + 16, iconTop + 16, -2013265920);
				GL11.glPopMatrix();
			}
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			int textColour = hasAchievement ? 8019267 : 5652783;
			mc.fontRenderer.drawString(achievement.getTitle(mc.thePlayer), guiLeft + 33, guiTop + offset + 5, textColour);
			mc.fontRenderer.drawSplitString(achievement.getDescription(mc.thePlayer), guiLeft + 12, guiTop + offset + 24, 184, textColour);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			if (!hasAchievement) {
				continue;
			}
			mc.getTextureManager().bindTexture(iconsTexture);
			this.drawTexturedModalRect(guiLeft + 179, guiTop + offset + 2, 190, 17, 16, 16);
		}
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == buttonCategoryPrev) {
				prevCategory();
			} else if (button == buttonCategoryNext) {
				nextCategory();
			} else {
				super.actionPerformed(button);
			}
		}
	}

	private LOTRAchievement.Category getCategoryAtRelativeIndex(int i) {
		List<LOTRAchievement.Category> categories = LOTRGuiAchievements.currentDimension.achievementCategories;
		int index = categories.indexOf(currentCategory);
		index += i;
		index = IntMath.mod(index, LOTRGuiAchievements.currentDimension.achievementCategories.size());
		return LOTRGuiAchievements.currentDimension.achievementCategories.get(index);
	}

	private void prevCategory() {
		currentCategory = getCategoryAtRelativeIndex(-1);
		currentScroll = 0.0f;
	}

	private void nextCategory() {
		currentCategory = getCategoryAtRelativeIndex(1);
		currentScroll = 0.0f;
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0 && hasScrollBar()) {
			int j = currentCategoryTakenCount + currentCategoryUntakenCount - 4;
			if (i > 0) {
				i = 1;
			}
			if (i < 0) {
				i = -1;
			}
			currentScroll = (float) (currentScroll - (double) i / (double) j);
			if (currentScroll < 0.0f) {
				currentScroll = 0.0f;
			}
			if (currentScroll > 1.0f) {
				currentScroll = 1.0f;
			}
		}
	}

	private boolean hasScrollBar() {
		return currentCategoryTakenCount + currentCategoryUntakenCount > 4;
	}

	private void updateAchievementLists() {
		currentDimension = LOTRDimension.getCurrentDimensionWithFallback(mc.theWorld);
		if (currentDimension != prevDimension) {
			currentCategory = LOTRGuiAchievements.currentDimension.achievementCategories.get(0);
		}
		prevDimension = currentDimension;
		currentCategoryTakenAchievements.clear();
		currentCategoryUntakenAchievements.clear();
		for (LOTRAchievement achievement : LOTRGuiAchievements.currentCategory.list) {
			if (!achievement.canPlayerEarn(mc.thePlayer)) {
				continue;
			}
			if (LOTRLevelData.getData(mc.thePlayer).hasAchievement(achievement)) {
				currentCategoryTakenAchievements.add(achievement);
				continue;
			}
			currentCategoryUntakenAchievements.add(achievement);
		}
		currentCategoryTakenCount = currentCategoryTakenAchievements.size();
		currentCategoryUntakenCount = currentCategoryUntakenAchievements.size();
		totalTakenCount = LOTRLevelData.getData(mc.thePlayer).getEarnedAchievements(currentDimension).size();
		totalAvailableCount = 0;
		for (LOTRAchievement achievement : LOTRGuiAchievements.currentDimension.allAchievements) {
			if (!achievement.canPlayerEarn(mc.thePlayer)) {
				continue;
			}
			++totalAvailableCount;
		}
		Comparator<LOTRAchievement> sorter = LOTRAchievement.sortForDisplay(mc.thePlayer);
		Collections.sort(currentCategoryTakenAchievements, sorter);
		Collections.sort(currentCategoryUntakenAchievements, sorter);
	}
}
