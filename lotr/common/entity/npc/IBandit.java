/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.IChatComponent
 */
package lotr.common.entity.npc;

import lotr.common.inventory.LOTRInventoryNPC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;

public interface IBandit {
    public LOTREntityNPC getBanditAsNPC();

    public int getMaxThefts();

    public LOTRInventoryNPC getBanditInventory();

    public boolean canTargetPlayerForTheft(EntityPlayer var1);

    public String getTheftSpeechBank(EntityPlayer var1);

    public IChatComponent getTheftChatMsg(EntityPlayer var1);

    public static class Helper {
        public static LOTRInventoryNPC createInv(IBandit bandit) {
            return new LOTRInventoryNPC("BanditInventory", bandit.getBanditAsNPC(), bandit.getMaxThefts());
        }

        public static boolean canStealFromPlayerInv(IBandit bandit, EntityPlayer entityplayer) {
            for (int slot = 0; slot < entityplayer.inventory.mainInventory.length; ++slot) {
                if (slot == entityplayer.inventory.currentItem || entityplayer.inventory.getStackInSlot(slot) == null) continue;
                return true;
            }
            return false;
        }
    }

}

