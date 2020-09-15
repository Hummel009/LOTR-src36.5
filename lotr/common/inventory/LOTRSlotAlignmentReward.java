/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.inventory;

import lotr.common.LOTRLevelData;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.entity.npc.LOTRHireableBase;
import lotr.common.fac.LOTRFaction;
import lotr.common.item.LOTRItemCoin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class LOTRSlotAlignmentReward
extends LOTRSlotProtected {
    public static int REWARD_COST = 2000;
    private LOTRContainerUnitTrade theContainer;
    private LOTRHireableBase theTrader;
    private LOTREntityNPC theLivingTrader;
    private ItemStack alignmentReward;

    public LOTRSlotAlignmentReward(LOTRContainerUnitTrade container, IInventory inv, int i, int j, int k, LOTRHireableBase entity, ItemStack item) {
        super(inv, i, j, k);
        this.theContainer = container;
        this.theTrader = entity;
        this.theLivingTrader = (LOTREntityNPC)((Object)this.theTrader);
        this.alignmentReward = item.copy();
    }

    public boolean canTakeStack(EntityPlayer entityplayer) {
        if (LOTRLevelData.getData(entityplayer).getAlignment(this.theTrader.getFaction()) < 1500.0f) {
            return false;
        }
        int coins = LOTRItemCoin.getInventoryValue(entityplayer, false);
        if (coins < REWARD_COST) {
            return false;
        }
        return super.canTakeStack(entityplayer);
    }

    public void onPickupFromSlot(EntityPlayer entityplayer, ItemStack itemstack) {
        LOTRFaction faction = this.theLivingTrader.getFaction();
        if (!entityplayer.worldObj.isRemote) {
            LOTRItemCoin.takeCoins(REWARD_COST, entityplayer);
            LOTRLevelData.getData(entityplayer).getFactionData(faction).takeConquestHorn();
            this.theLivingTrader.playTradeSound();
        }
        super.onPickupFromSlot(entityplayer, itemstack);
        if (!entityplayer.worldObj.isRemote) {
            ItemStack reward = this.alignmentReward.copy();
            this.putStack(reward);
            ((EntityPlayerMP)entityplayer).sendContainerToPlayer((Container)this.theContainer);
        }
    }
}

