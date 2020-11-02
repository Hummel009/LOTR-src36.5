package lotr.client.gui;

import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.network.*;
import net.minecraft.client.gui.GuiButton;

public class LOTRGuiMercenaryInteract extends LOTRGuiUnitTradeInteract {
	public LOTRGuiMercenaryInteract(LOTREntityNPC entity) {
		super(entity);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			LOTRPacketMercenaryInteract packet = new LOTRPacketMercenaryInteract(theEntity.getEntityId(), button.id);
			LOTRPacketHandler.networkWrapper.sendToServer(packet);
		}
	}
}
