package lotr.common.entity.npc;

import java.util.*;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import lotr.common.LOTRLevelData;
import lotr.common.inventory.LOTRContainerTrade;
import lotr.common.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.*;
import net.minecraft.util.MathHelper;

public class LOTRTraderNPCInfo {
	private LOTREntityNPC theEntity;
	private LOTRTradeEntry[] buyTrades;
	private LOTRTradeEntry[] sellTrades;
	private int timeUntilAdvertisement;
	private int timeSinceTrade;
	private boolean shouldLockTrades = true;
	private int lockTradeAtValue = 200;
	private int lockValueDecayTicks = 60;
	private boolean shouldRefresh = true;
	private int valueSinceRefresh;
	private int refreshAtValue = 5000;
	private int lockTicksAfterRefresh = 6000;

	public LOTRTraderNPCInfo(LOTREntityNPC npc) {
		theEntity = npc;
		if (theEntity instanceof LOTRTradeable && !theEntity.worldObj.isRemote) {
			refreshTrades();
		}
	}

	public LOTRTradeEntry[] getBuyTrades() {
		return buyTrades;
	}

	public LOTRTradeEntry[] getSellTrades() {
		return sellTrades;
	}

	private void setBuyTrades(LOTRTradeEntry[] trades) {
		for (LOTRTradeEntry trade : trades) {
			trade.setOwningTrader(this);
		}
		buyTrades = trades;
	}

	private void setSellTrades(LOTRTradeEntry[] trades) {
		for (LOTRTradeEntry trade : trades) {
			trade.setOwningTrader(this);
		}
		sellTrades = trades;
	}

	protected boolean shouldLockTrades() {
		return shouldLockTrades;
	}

	protected int getLockTradeAtValue() {
		return lockTradeAtValue;
	}

	protected int getValueDecayTicks() {
		return lockValueDecayTicks;
	}

	public void onTrade(EntityPlayer entityplayer, LOTRTradeEntry trade, LOTRTradeEntries.TradeType type, int value) {
		((LOTRTradeable) theEntity).onPlayerTrade(entityplayer, type, trade.createTradeItem());
		LOTRLevelData.getData(entityplayer).getFactionData(theEntity.getFaction()).addTrade();
		trade.doTransaction(value);
		timeSinceTrade = 0;
		valueSinceRefresh += value;
		sendClientPacket(entityplayer);
	}

	private void refreshTrades() {
		LOTRTradeable theTrader = (LOTRTradeable) theEntity;
		Random rand = theEntity.getRNG();
		setBuyTrades(theTrader.getBuyPool().getRandomTrades(rand));
		setSellTrades(theTrader.getSellPool().getRandomTrades(rand));
		valueSinceRefresh = 0;
		for (Object element : theEntity.worldObj.playerEntities) {
			EntityPlayer entityplayer = (EntityPlayer) element;
			Container container = entityplayer.openContainer;
			if (!(container instanceof LOTRContainerTrade) || ((LOTRContainerTrade) container).theTraderNPC != theEntity) {
				continue;
			}
			((LOTRContainerTrade) container).updateAllTradeSlots();
		}
	}

	private void setAllTradesDelayed() {
		int delay = lockTicksAfterRefresh;
		for (LOTRTradeEntry trade : buyTrades) {
			if (trade == null) {
				continue;
			}
			trade.setLockedForTicks(delay);
		}
		for (LOTRTradeEntry trade : sellTrades) {
			if (trade == null) {
				continue;
			}
			trade.setLockedForTicks(delay);
		}
	}

	public void onUpdate() {
		if (!theEntity.worldObj.isRemote) {
			if (timeUntilAdvertisement > 0) {
				--timeUntilAdvertisement;
			}
			++timeSinceTrade;
			int ticksExisted = theEntity.ticksExisted;
			boolean sendUpdate = false;
			for (LOTRTradeEntry trade : buyTrades) {
				if (trade == null || !trade.updateAvailability(ticksExisted)) {
					continue;
				}
				sendUpdate = true;
			}
			for (LOTRTradeEntry trade : sellTrades) {
				if (trade == null || !trade.updateAvailability(ticksExisted)) {
					continue;
				}
				sendUpdate = true;
			}
			if (shouldRefresh && valueSinceRefresh >= refreshAtValue) {
				refreshTrades();
				setAllTradesDelayed();
				sendUpdate = true;
			}
			if (sendUpdate) {
				for (Object element : theEntity.worldObj.playerEntities) {
					EntityPlayer entityplayer = (EntityPlayer) element;
					Container container = entityplayer.openContainer;
					if (!(container instanceof LOTRContainerTrade) || ((LOTRContainerTrade) container).theTraderNPC != theEntity) {
						continue;
					}
					sendClientPacket(entityplayer);
				}
			}
			if (theEntity.isEntityAlive() && theEntity.getAttackTarget() == null && timeUntilAdvertisement == 0 && timeSinceTrade > 600) {
				double range = 10.0;
				List players = theEntity.worldObj.getEntitiesWithinAABB(EntityPlayer.class, theEntity.boundingBox.expand(range, range, range));
				for (Object obj : players) {
					String speechBank;
					EntityPlayer entityplayer = (EntityPlayer) obj;
					if (!entityplayer.isEntityAlive() || entityplayer.capabilities.isCreativeMode || entityplayer.openContainer != null && entityplayer.openContainer != entityplayer.inventoryContainer || (speechBank = theEntity.getSpeechBank(entityplayer)) == null || theEntity.getRNG().nextInt(3) != 0) {
						continue;
					}
					theEntity.sendSpeechBank(entityplayer, speechBank);
				}
				timeUntilAdvertisement = 20 * MathHelper.getRandomIntegerInRange(theEntity.getRNG(), 5, 20);
			}
		}
	}

	public void writeToNBT(NBTTagCompound data) {
		NBTTagCompound nbt;
		if (buyTrades != null) {
			NBTTagList buyTradesTags = new NBTTagList();
			for (LOTRTradeEntry trade : buyTrades) {
				if (trade == null) {
					continue;
				}
				nbt = new NBTTagCompound();
				trade.writeToNBT(nbt);
				buyTradesTags.appendTag(nbt);
			}
			NBTTagCompound buyTradesData = new NBTTagCompound();
			buyTradesData.setTag("Trades", buyTradesTags);
			data.setTag("LOTRBuyTrades", buyTradesData);
		}
		if (sellTrades != null) {
			NBTTagList sellTradesTags = new NBTTagList();
			for (LOTRTradeEntry trade : sellTrades) {
				if (trade == null) {
					continue;
				}
				nbt = new NBTTagCompound();
				trade.writeToNBT(nbt);
				sellTradesTags.appendTag(nbt);
			}
			NBTTagCompound sellTradesData = new NBTTagCompound();
			sellTradesData.setTag("Trades", sellTradesTags);
			data.setTag("LOTRSellTrades", sellTradesData);
		}
		data.setInteger("TimeSinceTrade", timeSinceTrade);
		data.setBoolean("ShouldLockTrades", shouldLockTrades);
		data.setInteger("LockTradeAtValue", lockTradeAtValue);
		data.setInteger("LockValueDecayTicks", lockValueDecayTicks);
		data.setBoolean("ShouldRefresh", shouldRefresh);
		data.setInteger("RefreshAtValue", refreshAtValue);
		data.setInteger("LockTicksAfterRefresh", lockTicksAfterRefresh);
		data.setInteger("ValueSinceRefresh", valueSinceRefresh);
	}

	public void readFromNBT(NBTTagCompound data) {
		int i;
		NBTTagCompound nbt;
		NBTTagCompound sellTradesData;
		NBTTagCompound buyTradesData;
		LOTRTradeEntry trade;
		if (data.hasKey("LOTRBuyTrades") && (buyTradesData = data.getCompoundTag("LOTRBuyTrades")).hasKey("Trades")) {
			NBTTagList buyTradesTags = buyTradesData.getTagList("Trades", 10);
			buyTrades = new LOTRTradeEntry[buyTradesTags.tagCount()];
			for (i = 0; i < buyTradesTags.tagCount(); ++i) {
				nbt = buyTradesTags.getCompoundTagAt(i);
				trade = LOTRTradeEntry.readFromNBT(nbt);
				trade.setOwningTrader(this);
				buyTrades[i] = trade;
			}
		}
		if (data.hasKey("LOTRSellTrades") && (sellTradesData = data.getCompoundTag("LOTRSellTrades")).hasKey("Trades")) {
			NBTTagList sellTradesTags = sellTradesData.getTagList("Trades", 10);
			sellTrades = new LOTRTradeEntry[sellTradesTags.tagCount()];
			for (i = 0; i < sellTradesTags.tagCount(); ++i) {
				nbt = sellTradesTags.getCompoundTagAt(i);
				trade = LOTRTradeEntry.readFromNBT(nbt);
				trade.setOwningTrader(this);
				sellTrades[i] = trade;
			}
		}
		timeSinceTrade = data.getInteger("TimeSinceTrade");
		if (data.hasKey("ShouldLockTrades")) {
			shouldLockTrades = data.getBoolean("ShouldLockTrades");
		}
		if (data.hasKey("LockTradeAtValue")) {
			lockTradeAtValue = data.getInteger("LockTradeAtValue");
		}
		if (data.hasKey("LockValueDecayTicks")) {
			lockValueDecayTicks = data.getInteger("LockValueDecayTicks");
		}
		if (data.hasKey("ShouldRefresh")) {
			shouldRefresh = data.getBoolean("ShouldRefresh");
		}
		if (data.hasKey("RefreshAtValue")) {
			refreshAtValue = data.getInteger("RefreshAtValue");
		}
		if (data.hasKey("LockTicksAfterRefresh")) {
			lockTicksAfterRefresh = data.getInteger("LockTicksAfterRefresh");
		}
		valueSinceRefresh = data.getInteger("ValueSinceRefresh");
	}

	public void sendClientPacket(EntityPlayer entityplayer) {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		LOTRPacketTraderInfo packet = new LOTRPacketTraderInfo(nbt);
		LOTRPacketHandler.networkWrapper.sendTo((IMessage) packet, (EntityPlayerMP) entityplayer);
	}

	public void receiveClientPacket(LOTRPacketTraderInfo packet) {
		NBTTagCompound nbt = packet.traderData;
		readFromNBT(nbt);
	}
}
