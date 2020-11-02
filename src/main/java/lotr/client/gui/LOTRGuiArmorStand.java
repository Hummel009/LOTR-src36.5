package lotr.client.gui;

import org.lwjgl.opengl.GL11;

import lotr.common.inventory.LOTRContainerArmorStand;
import lotr.common.tileentity.LOTRTileEntityArmorStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.*;

public class LOTRGuiArmorStand extends GuiContainer {
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/armor_stand.png");
	private LOTRTileEntityArmorStand theArmorStand;

	public LOTRGuiArmorStand(InventoryPlayer inv, LOTRTileEntityArmorStand armorStand) {
		super(new LOTRContainerArmorStand(inv, armorStand));
		theArmorStand = armorStand;
		ySize = 189;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		String s = theArmorStand.getInventoryName();
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 95, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(guiTexture);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
