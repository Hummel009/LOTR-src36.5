package lotr.client.gui;

import lotr.common.entity.npc.*;
import lotr.common.network.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.StatCollector;

public class LOTRGuiTradeInteract extends LOTRGuiNPCInteract {
	private GuiButton buttonTalk;
	private GuiButton buttonTrade;
	private GuiButton buttonExchange;
	private GuiButton buttonSmith;

	public LOTRGuiTradeInteract(LOTREntityNPC entity) {
		super(entity);
	}

	@Override
	public void initGui() {
		buttonTalk = new GuiButton(0, width / 2 - 65, height / 5 * 3, 60, 20, StatCollector.translateToLocal("lotr.gui.npc.talk"));
		buttonTrade = new GuiButton(1, width / 2 + 5, height / 5 * 3, 60, 20, StatCollector.translateToLocal("lotr.gui.npc.trade"));
		buttonExchange = new GuiButton(2, width / 2 - 65, height / 5 * 3 + 25, 130, 20, StatCollector.translateToLocal("lotr.gui.npc.exchange"));
		buttonList.add(buttonTalk);
		buttonList.add(buttonTrade);
		buttonList.add(buttonExchange);
		if (theEntity instanceof LOTRTradeable.Smith) {
			buttonTalk.xPosition -= 35;
			buttonTrade.xPosition -= 35;
			buttonSmith = new GuiButton(3, width / 2 + 40, height / 5 * 3, 60, 20, StatCollector.translateToLocal("lotr.gui.npc.smith"));
			buttonList.add(buttonSmith);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			LOTRPacketTraderInteract packet = new LOTRPacketTraderInteract(theEntity.getEntityId(), button.id);
			LOTRPacketHandler.networkWrapper.sendToServer(packet);
		}
	}
}
