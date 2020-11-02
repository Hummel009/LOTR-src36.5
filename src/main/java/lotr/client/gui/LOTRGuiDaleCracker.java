package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import lotr.common.inventory.LOTRContainerDaleCracker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public class LOTRGuiDaleCracker extends GuiContainer {
	private static ResourceLocation texture = new ResourceLocation("lotr:gui/daleCracker.png");
	private LOTRContainerDaleCracker theCracker;
	private GuiButton buttonSeal;

	public LOTRGuiDaleCracker(EntityPlayer entityplayer) {
		super(new LOTRContainerDaleCracker(entityplayer));
		theCracker = (LOTRContainerDaleCracker) inventorySlots;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonSeal = new GuiButton(0, guiLeft + xSize / 2 - 40, guiTop + 48, 80, 20, StatCollector.translateToLocal("lotr.gui.daleCracker.seal"));
		buttonList.add(buttonSeal);
		buttonSeal.enabled = false;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String s = StatCollector.translateToLocal("lotr.gui.daleCracker");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		buttonSeal.enabled = !theCracker.isCrackerInvEmpty();
	}

	@Override
	protected boolean checkHotbarKeys(int i) {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled && button == buttonSeal && !theCracker.isCrackerInvEmpty()) {
			theCracker.sendSealingPacket(mc.thePlayer);
			mc.displayGuiScreen(null);
		}
	}
}
