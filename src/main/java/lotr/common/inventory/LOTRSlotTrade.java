package lotr.common.inventory;

import lotr.common.entity.npc.*;
import lotr.common.item.LOTRItemCoin;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LOTRSlotTrade extends LOTRSlotProtected {
	private LOTRContainerTrade theContainer;
	private LOTREntityNPC theEntity;
	private LOTRTradeEntries.TradeType tradeType;

	public LOTRSlotTrade(LOTRContainerTrade container, IInventory inv, int i, int j, int k, LOTREntityNPC entity, LOTRTradeEntries.TradeType type) {
		super(inv, i, j, k);
		theContainer = container;
		theEntity = entity;
		tradeType = type;
	}

	public int cost() {
		LOTRTradeEntry trade = getTrade();
		return trade == null ? 0 : trade.getCost();
	}

	public LOTRTradeEntry getTrade() {
		LOTRTradeEntry[] trades = null;
		if (tradeType == LOTRTradeEntries.TradeType.BUY) {
			trades = theEntity.traderNPCInfo.getBuyTrades();
		} else if (tradeType == LOTRTradeEntries.TradeType.SELL) {
			trades = theEntity.traderNPCInfo.getSellTrades();
		}
		if (trades == null) {
			return null;
		}
		int i = getSlotIndex();
		if (i >= 0 && i < trades.length) {
			return trades[i];
		}
		return null;
	}

	@Override
	public boolean canTakeStack(EntityPlayer entityplayer) {
		if (tradeType == LOTRTradeEntries.TradeType.BUY) {
			if (getTrade() != null && !getTrade().isAvailable()) {
				return false;
			}
			int coins = LOTRItemCoin.getInventoryValue(entityplayer, false);
			if (coins < cost()) {
				return false;
			}
		}
		if (tradeType == LOTRTradeEntries.TradeType.SELL) {
			return false;
		}
		return super.canTakeStack(entityplayer);
	}

	@Override
	public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack) {
		if (tradeType == LOTRTradeEntries.TradeType.BUY && !entityplayer.worldObj.isRemote) {
			LOTRItemCoin.takeCoins(cost(), entityplayer);
		}
		super.onPickupFromSlot(entityplayer, itemstack);
		if (tradeType == LOTRTradeEntries.TradeType.BUY) {
			LOTRTradeEntry trade = getTrade();
			if (!entityplayer.worldObj.isRemote && trade != null) {
				putStack(trade.createTradeItem());
				((EntityPlayerMP) entityplayer).sendContainerToPlayer(theContainer);
				theEntity.traderNPCInfo.onTrade(entityplayer, trade, LOTRTradeEntries.TradeType.BUY, cost());
				theEntity.playTradeSound();
			}
		}
	}
}
