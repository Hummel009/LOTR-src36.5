package lotr.client.gui;

import lotr.common.entity.npc.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredFarmer extends LOTRGuiHiredNPC {
	private LOTRGuiButtonOptions buttonGuardMode;
	private LOTRGuiSlider sliderGuardRange;

	public LOTRGuiHiredFarmer(LOTREntityNPC npc) {
		super(npc);
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonGuardMode = new LOTRGuiButtonOptions(0, guiLeft + xSize / 2 - 80, guiTop + 70, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.mode"));
		buttonList.add(buttonGuardMode);
		buttonGuardMode.setState(theNPC.hiredNPCInfo.isGuardMode());
		sliderGuardRange = new LOTRGuiSlider(1, guiLeft + xSize / 2 - 80, guiTop + 94, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.range"));
		buttonList.add(sliderGuardRange);
		sliderGuardRange.setMinMaxValues(LOTRHiredNPCInfo.GUARD_RANGE_MIN, LOTRHiredNPCInfo.GUARD_RANGE_MAX);
		sliderGuardRange.setSliderValue(theNPC.hiredNPCInfo.getGuardRange());
		sliderGuardRange.visible = theNPC.hiredNPCInfo.isGuardMode();
		buttonList.add(new LOTRGuiButtonOptions(2, guiLeft + xSize / 2 - 80, guiTop + 142, 160, 20, StatCollector.translateToLocal("lotr.gui.farmer.openInv")));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button instanceof LOTRGuiSlider) {
			return;
		}
		if (button.enabled) {
			this.sendActionPacket(button.id);
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		super.drawScreen(i, j, f);
		String s = theNPC.hiredNPCInfo.getStatusString();
		fontRendererObj.drawString(s, guiLeft + xSize / 2 - fontRendererObj.getStringWidth(s) / 2, guiTop + 50, 4210752);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		buttonGuardMode.setState(theNPC.hiredNPCInfo.isGuardMode());
		sliderGuardRange.visible = theNPC.hiredNPCInfo.isGuardMode();
		if (sliderGuardRange.dragging) {
			int i = sliderGuardRange.getSliderValue();
			theNPC.hiredNPCInfo.setGuardRange(i);
			this.sendActionPacket(sliderGuardRange.id, i);
		}
	}
}
