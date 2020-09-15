/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.event.HoverEvent
 *  net.minecraft.event.HoverEvent$Action
 *  net.minecraftforge.common.util.EnumHelper
 */
package lotr.common;

import net.minecraft.event.HoverEvent;
import net.minecraftforge.common.util.EnumHelper;

public class LOTRChatEvents {
    private static Class[][] hoverParams = new Class[][]{{HoverEvent.Action.class, String.class, Boolean.TYPE}};
    public static HoverEvent.Action SHOW_LOTR_ACHIEVEMENT;

    public static void createEvents() {
        SHOW_LOTR_ACHIEVEMENT = (HoverEvent.Action)EnumHelper.addEnum((Class[][])hoverParams, HoverEvent.Action.class, (String)"SHOW_LOTR_ACHIEVEMENT", (Object[])new Object[]{"show_lotr_achievement", true});
        LOTRReflection.getHoverEventMappings().put(SHOW_LOTR_ACHIEVEMENT.getCanonicalName(), SHOW_LOTR_ACHIEVEMENT);
    }
}

