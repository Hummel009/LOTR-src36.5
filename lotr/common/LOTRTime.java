/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.FMLLog
 *  net.minecraft.nbt.CompressedStreamTools
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldProvider
 *  net.minecraft.world.WorldServer
 *  net.minecraft.world.storage.WorldInfo
 */
package lotr.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import cpw.mods.fml.common.FMLLog;
import lotr.common.world.LOTRWorldInfo;
import lotr.common.world.LOTRWorldProvider;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LOTRTime {
    public static int DAY_LENGTH = 48000;
    private static long totalTime;
    private static long worldTime;
    public static boolean needsLoad;

    private static File getTimeDat() {
        return new File(LOTRLevelData.getOrCreateLOTRDir(), "LOTRTime.dat");
    }

    public static void save() {
        try {
            File time_dat = LOTRTime.getTimeDat();
            if (!time_dat.exists()) {
                CompressedStreamTools.writeCompressed((NBTTagCompound)new NBTTagCompound(), (OutputStream)new FileOutputStream(time_dat));
            }
            NBTTagCompound timeData = new NBTTagCompound();
            timeData.setLong("LOTRTotalTime", totalTime);
            timeData.setLong("LOTRWorldTime", worldTime);
            LOTRLevelData.saveNBTToFile(time_dat, timeData);
        }
        catch (Exception e) {
            FMLLog.severe((String)"Error saving LOTR time data", (Object[])new Object[0]);
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            NBTTagCompound timeData = LOTRLevelData.loadNBTFromFile(LOTRTime.getTimeDat());
            totalTime = timeData.getLong("LOTRTotalTime");
            worldTime = timeData.getLong("LOTRWorldTime");
            needsLoad = false;
            LOTRTime.save();
        }
        catch (Exception e) {
            FMLLog.severe((String)"Error loading LOTR time data", (Object[])new Object[0]);
            e.printStackTrace();
        }
    }

    public static void setWorldTime(long time) {
        worldTime = time;
    }

    public static void addWorldTime(long time) {
        worldTime += time;
    }

    public static void advanceToMorning() {
        long l = worldTime + (long)DAY_LENGTH;
        LOTRTime.setWorldTime(l - l % (long)DAY_LENGTH);
    }

    public static void update() {
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer overworld = server.worldServerForDimension(0);
        if (LOTRMod.doDayCycle((World)overworld)) {
            ++worldTime;
        }
        ++totalTime;
        for (WorldServer world : server.worldServers) {
            if (!(world.provider instanceof LOTRWorldProvider)) continue;
            LOTRWorldInfo worldinfo = (LOTRWorldInfo)world.getWorldInfo();
            worldinfo.lotr_setTotalTime(totalTime);
            worldinfo.lotr_setWorldTime(worldTime);
        }
    }

    static {
        needsLoad = true;
    }
}

