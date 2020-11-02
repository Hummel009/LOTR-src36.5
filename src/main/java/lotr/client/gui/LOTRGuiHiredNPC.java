package lotr.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import lotr.common.entity.npc.*;
import lotr.common.fac.*;
import lotr.common.network.*;
import net.minecraft.util.*;

public abstract class LOTRGuiHiredNPC extends LOTRGuiScreenBase {
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/npc/hired.png");
	public int xSize = 200;
	public int ySize = 220;
	public int guiLeft;
	public int guiTop;
	public LOTREntityNPC theNPC;
	public int page;

	public LOTRGuiHiredNPC(LOTREntityNPC npc) {
		theNPC = npc;
	}

	@Override
	public void initGui() {
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(guiTexture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		String s = theNPC.getNPCName();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 11, 3618615);
		s = theNPC.getEntityClassName();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 26, 3618615);
		if (page == 0 && theNPC.hiredNPCInfo.hasHiringRequirements()) {
			int x = guiLeft + 6;
			int y = guiTop + 170;
			s = StatCollector.translateToLocal("lotr.hiredNPC.commandReq");
			fontRendererObj.drawString(s, x, y, 3618615);
			y += fontRendererObj.FONT_HEIGHT;
			x += 4;
			ArrayList requirementLines = new ArrayList();
			int maxWidth = xSize - 12 - 4;
			LOTRFaction fac = theNPC.getHiringFaction();
			String alignS = LOTRAlignmentValues.formatAlignForDisplay(theNPC.hiredNPCInfo.alignmentRequiredToCommand);
			String alignReq = StatCollector.translateToLocalFormatted("lotr.hiredNPC.commandReq.align", alignS, fac.factionName());
			requirementLines.addAll(fontRendererObj.listFormattedStringToWidth(alignReq, maxWidth));
			LOTRUnitTradeEntry.PledgeType pledge = theNPC.hiredNPCInfo.pledgeType;
			String pledgeReq = pledge.getCommandReqText(fac);
			if (pledgeReq != null) {
				requirementLines.addAll(fontRendererObj.listFormattedStringToWidth(pledgeReq, maxWidth));
			}
			for (Object obj : requirementLines) {
				String line = (String) obj;
				fontRendererObj.drawString(line, x, y, 3618615);
				y += fontRendererObj.FONT_HEIGHT;
			}
		}
		super.drawScreen(i, j, f);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (!theNPC.isEntityAlive() || theNPC.hiredNPCInfo.getHiringPlayer() != mc.thePlayer || theNPC.getDistanceSqToEntity(mc.thePlayer) > 64.0) {
			mc.thePlayer.closeScreen();
		}
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		this.sendActionPacket(-1);
	}

	public void sendActionPacket(int action) {
		this.sendActionPacket(action, 0);
	}

	public void sendActionPacket(int action, int value) {
		LOTRPacketHiredUnitCommand packet = new LOTRPacketHiredUnitCommand(theNPC.getEntityId(), page, action, value);
		LOTRPacketHandler.networkWrapper.sendToServer(packet);
	}
}
