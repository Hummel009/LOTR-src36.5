/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.StringUtils
 */
package lotr.common;

import lotr.common.entity.npc.LOTREntityNPC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;

public class LOTRSquadrons {
    public static int SQUADRON_LENGTH_MAX = 200;

    public static String checkAcceptableLength(String squadron) {
        if (squadron != null && squadron.length() > SQUADRON_LENGTH_MAX) {
            squadron = squadron.substring(0, SQUADRON_LENGTH_MAX);
        }
        return squadron;
    }

    public static boolean areSquadronsCompatible(LOTREntityNPC npc, ItemStack itemstack) {
        String npcSquadron = npc.hiredNPCInfo.getSquadron();
        String itemSquadron = LOTRSquadrons.getSquadron(itemstack);
        if (StringUtils.isNullOrEmpty((String)npcSquadron)) {
            return StringUtils.isNullOrEmpty((String)itemSquadron);
        }
        return npcSquadron.equalsIgnoreCase(itemSquadron);
    }

    public static String getSquadron(ItemStack itemstack) {
        if (itemstack.getItem() instanceof SquadronItem && itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("LOTRSquadron")) {
            String squadron = itemstack.getTagCompound().getString("LOTRSquadron");
            return squadron;
        }
        return null;
    }

    public static void setSquadron(ItemStack itemstack, String squadron) {
        if (itemstack.getItem() instanceof SquadronItem) {
            if (itemstack.getTagCompound() == null) {
                itemstack.setTagCompound(new NBTTagCompound());
            }
            itemstack.getTagCompound().setString("LOTRSquadron", squadron);
        }
    }

    public static interface SquadronItem {
    }

}

