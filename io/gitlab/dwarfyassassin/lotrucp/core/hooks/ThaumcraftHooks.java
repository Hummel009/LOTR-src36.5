/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.world.World
 *  net.minecraftforge.common.util.FakePlayer
 *  org.apache.logging.log4j.Logger
 */
package io.gitlab.dwarfyassassin.lotrucp.core.hooks;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.server.util.PlayerUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import lotr.common.LOTRBannerProtection;
import lotr.common.LOTRReflection;
import lotr.common.entity.item.LOTREntityBanner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import org.apache.logging.log4j.Logger;

public class ThaumcraftHooks {
    private static boolean doneReflection = false;
    private static Class class_golem;
    private static Method method_getOwnerName;

    public static LOTRBannerProtection.ProtectType thaumcraftGolemBannerProtection(EntityPlayer player, LOTREntityBanner banner) {
        GameProfile profile;
        FakePlayer fakePlayer;
        World world = player.worldObj;
        if (player instanceof FakePlayer && (profile = (fakePlayer = (FakePlayer)player).getGameProfile()).getName().equals("FakeThaumcraftGolem")) {
            if (!doneReflection) {
                try {
                    class_golem = Class.forName("thaumcraft.common.entities.golems.EntityGolemBase");
                    method_getOwnerName = class_golem.getDeclaredMethod("getOwnerName", new Class[0]);
                }
                catch (ClassNotFoundException e) {
                    UCPCoreMod.log.error("Was unable to find Thaumcraft EntityGolemBase class");
                    e.printStackTrace();
                }
                catch (NoSuchMethodException e) {
                    UCPCoreMod.log.error("Was unable to find Thaumcraft EntityGolemBase.getOwnerName method");
                    e.printStackTrace();
                }
                doneReflection = true;
            }
            if (class_golem == null || method_getOwnerName == null) {
                UCPCoreMod.log.error("Failed to reflectively locate Thaumcraft EntityGolemBase class or getOwnerName method.Found class = %s, found method = %s", new Object[]{class_golem, method_getOwnerName});
                return null;
            }
            List<Entity> foundGolems = world.getEntitiesWithinAABB(class_golem, player.boundingBox.expand(1.0, 1.0, 1.0));
            Entity closestGolem = null;
            double foundDistance = Double.MAX_VALUE;
            for (Entity golem : foundGolems) {
                double distance = player.getDistanceSqToEntity(golem);
                if (!(distance < foundDistance)) continue;
                closestGolem = golem;
            }
            if (closestGolem == null) {
                return null;
            }
            UUID uuid = null;
            try {
                String golemOwner = (String)method_getOwnerName.invoke((Object)closestGolem, new Object[0]);
                uuid = PlayerUtils.getLastKownUUIDFromUsername(golemOwner);
            }
            catch (IllegalAccessException e) {
                UCPCoreMod.log.error("Was unable to invoke Thaumcraft EntityGolemBase.getOwnerName method");
                e.printStackTrace();
            }
            catch (IllegalArgumentException e) {
                UCPCoreMod.log.error("Was unable to invoke Thaumcraft EntityGolemBase.getOwnerName method");
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                UCPCoreMod.log.error("Was unable to invoke Thaumcraft EntityGolemBase.getOwnerName method");
                e.printStackTrace();
            }
            if (uuid == null) {
                UCPCoreMod.log.error("Was unable to find the player UUID from Thaumcraft EntityGolemBase.getOwnerName - UUID is %s", new Object[]{uuid});
                return null;
            }
            try {
                LOTRReflection.setFinalField(GameProfile.class, profile, uuid, new String[]{"id"});
                ReflectionHelper.setPrivateValue(Entity.class, fakePlayer, (Object)uuid, (String[])new String[]{"entityUniqueID", "field_96093_i"});
            }
            catch (Exception e) {
                UCPCoreMod.log.error("Was unable to set a FakeThaumcraftGolem player uuid to " + uuid.toString());
                e.printStackTrace();
            }
        }
        return null;
    }
}

