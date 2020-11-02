package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.network.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class LOTRGuiHiredInteract extends LOTRGuiNPCInteract {
	public LOTRGuiHiredInteract(LOTREntityNPC entity) {
		super(entity);
	}

	@Override
	public void initGui() {
		buttonList.add(new GuiButton(0, width / 2 - 65, height / 5 * 3, 60, 20, StatCollector.translateToLocal("lotr.gui.npc.talk")));
		buttonList.add(new GuiButton(1, width / 2 + 5, height / 5 * 3, 60, 20, StatCollector.translateToLocal("lotr.gui.npc.command")));
		buttonList.add(new GuiButton(2, width / 2 - 65, height / 5 * 3 + 25, 130, 20, StatCollector.translateToLocal("lotr.gui.npc.dismiss")));
		((GuiButton) buttonList.get(0)).enabled = theEntity.getSpeechBank(mc.thePlayer) != null;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button.id == 2) {
				mc.displayGuiScreen(new LOTRGuiHiredDismiss(theEntity));
				return;
			}
			LOTRPacketHiredUnitInteract packet = new LOTRPacketHiredUnitInteract(theEntity.getEntityId(), button.id);
			LOTRPacketHandler.networkWrapper.sendToServer(packet);
		}
	}
}
