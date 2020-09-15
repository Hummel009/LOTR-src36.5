/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  org.apache.logging.log4j.Logger
 */
package lotr.common.quest;

import java.util.UUID;

import lotr.common.util.LOTRLog;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IPickpocketable {
    public boolean canPickpocket();

    public ItemStack createPickpocketItem();

    public static class Helper {
        public static void setPickpocketData(ItemStack itemstack, String ownerName, String wanterName, UUID wantedID) {
            NBTTagCompound data = new NBTTagCompound();
            data.setString("Owner", ownerName);
            data.setString("Wanter", wanterName);
            data.setString("WanterID", wantedID.toString());
            itemstack.setTagInfo("LOTRPickpocket", (NBTBase)data);
        }

        public static boolean isPickpocketed(ItemStack itemstack) {
            return itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("LOTRPickpocket");
        }

        public static String getOwner(ItemStack itemstack) {
            if (itemstack.hasTagCompound()) {
                return itemstack.getTagCompound().getCompoundTag("LOTRPickpocket").getString("Owner");
            }
            return null;
        }

        public static String getWanter(ItemStack itemstack) {
            if (itemstack.hasTagCompound()) {
                return itemstack.getTagCompound().getCompoundTag("LOTRPickpocket").getString("Wanter");
            }
            return null;
        }

        public static UUID getWanterID(ItemStack itemstack) {
            if (itemstack.hasTagCompound()) {
                String id = itemstack.getTagCompound().getCompoundTag("LOTRPickpocket").getString("WanterID");
                try {
                    return UUID.fromString(id);
                }
                catch (IllegalArgumentException e) {
                    LOTRLog.logger.warn("Item %s has invalid pickpocketed wanter ID %s", new Object[]{itemstack.getDisplayName(), id});
                }
            }
            return null;
        }
    }

}

