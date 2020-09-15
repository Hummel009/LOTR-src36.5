/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.FMLLaunchHandler
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  net.minecraft.launchwrapper.IClassTransformer
 *  net.minecraft.launchwrapper.LaunchClassLoader
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class UCPClassTransformer
implements IClassTransformer {
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        boolean ran = false;
        for (Patcher patcher : UCPCoreMod.activePatches) {
            if (!patcher.canRun(name)) continue;
            ran = true;
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classBytes);
            classReader.accept((ClassVisitor)classNode, 0);
            UCPCoreMod.log.info("Running patcher " + patcher.getName() + " for " + name);
            patcher.run(name, classNode);
            ClassWriter writer = new ClassWriter(1);
            classNode.accept((ClassVisitor)writer);
            classBytes = writer.toByteArray();
        }
        if (ran) {
            HashSet<Patcher> removes = new HashSet<Patcher>();
            for (Patcher patcher : UCPCoreMod.activePatches) {
                if (!patcher.isDone()) continue;
                removes.add(patcher);
            }
            UCPCoreMod.activePatches.removeAll(removes);
            if (UCPCoreMod.activePatches.isEmpty()) {
                UCPCoreMod.log.info("Ran all active patches.");
            }
        }
        return classBytes;
    }

    static {
        FMLLaunchHandler launchHandler = (FMLLaunchHandler)ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, null, (String[])new String[]{"INSTANCE"});
        LaunchClassLoader launchClassLoader = (LaunchClassLoader)ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, launchHandler, (String[])new String[]{"classLoader"});
    }
}

