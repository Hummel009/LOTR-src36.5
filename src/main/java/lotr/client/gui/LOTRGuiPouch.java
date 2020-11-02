package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import lotr.common.inventory.LOTRContainerPouch;
import lotr.common.network.*;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public class LOTRGuiPouch extends GuiContainer {
	public static ResourceLocation texture = new ResourceLocation("lotr:gui/pouch.png");
	private LOTRContainerPouch thePouch;
	private int pouchRows;
	private GuiTextField theGuiTextField;

	public LOTRGuiPouch(EntityPlayer entityplayer, int slot) {
		super(new LOTRContainerPouch(entityplayer, slot));
		thePouch = (LOTRContainerPouch) inventorySlots;
		pouchRows = thePouch.capacity / 9;
		ySize = 180;
	}

	@Override
	public void initGui() {
		super.initGui();
		theGuiTextField = new GuiTextField(fontRendererObj, guiLeft + xSize / 2 - 80, guiTop + 7, 160, 20);
		theGuiTextField.setText(thePouch.getDisplayName());
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		for (int l = 0; l < pouchRows; ++l) {
			drawTexturedModalRect(guiLeft + 7, guiTop + 29 + l * 18, 0, 180, 162, 18);
		}
		GL11.glDisable(2896);
		theGuiTextField.drawTextBox();
		GL11.glEnable(2896);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		theGuiTextField.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (theGuiTextField.textboxKeyTyped(c, i)) {
			renamePouch();
		} else {
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		theGuiTextField.mouseClicked(i, j, k);
	}

	private void renamePouch() {
		String name = theGuiTextField.getText();
		thePouch.renamePouch(name);
		LOTRPacketRenamePouch packet = new LOTRPacketRenamePouch(name);
		LOTRPacketHandler.networkWrapper.sendToServer(packet);
	}
}
