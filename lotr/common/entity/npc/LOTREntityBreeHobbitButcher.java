/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.IEntityLivingData
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package lotr.common.entity.npc;

import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LOTREntityBreeHobbitButcher
extends LOTREntityBreeHobbitTrader {
    public LOTREntityBreeHobbitButcher(World world) {
        super(world);
    }

    @Override
    public LOTRTradeEntries getBuyPool() {
        return LOTRTradeEntries.BREE_BUTCHER_BUY;
    }

    @Override
    public LOTRTradeEntries getSellPool() {
        return LOTRTradeEntries.BREE_BUTCHER_SELL;
    }

    @Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
        data = super.onSpawnWithEgg(data);
        this.npcItemsInv.setIdleItem(new ItemStack(Items.chicken));
        return data;
    }
}

