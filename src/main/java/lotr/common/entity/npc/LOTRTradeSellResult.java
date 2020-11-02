package lotr.common.entity.npc;

import net.minecraft.item.ItemStack;

public class LOTRTradeSellResult {
	public final int tradeIndex;
	public final int tradeValue;
	public final int tradeStackSize;
	public final int tradesMade;
	public final int itemsSold;
	public final int totalSellValue;

	public LOTRTradeSellResult(int index, LOTRTradeEntry trade, ItemStack sellItem) {
		ItemStack tradeItem = trade.createTradeItem();
		tradeIndex = index;
		tradeValue = trade.getCost();
		tradeStackSize = tradeItem.stackSize;
		tradesMade = !trade.isAvailable() ? 0 : sellItem.stackSize / tradeStackSize;
		itemsSold = tradesMade * tradeItem.stackSize;
		totalSellValue = tradesMade * tradeValue;
	}
}
