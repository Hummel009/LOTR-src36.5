/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.classloading.FMLForgePlugin
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.utils;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import java.util.List;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtils {
    public static MethodNode findMethod(ClassNode classNode, String targetMethodName, String obfTargetMethodName, String targetMethodDesc) {
        return ASMUtils.findMethod(classNode, FMLForgePlugin.RUNTIME_DEOBF ? obfTargetMethodName : targetMethodName, targetMethodDesc);
    }

    public static MethodNode findMethod(ClassNode classNode, String targetMethodName, String targetMethodDesc) {
        for (MethodNode method : classNode.methods) {
            if (!method.name.equals(targetMethodName) || !method.desc.equals(targetMethodDesc)) continue;
            return method;
        }
        UCPCoreMod.log.error("Couldn't find method " + targetMethodName + " with desc " + targetMethodDesc + " in " + classNode.name);
        return null;
    }

    public static void removePreviousNodes(InsnList list, AbstractInsnNode start, int amount) {
        for (int i = 0; i < amount; ++i) {
            AbstractInsnNode prevNode = start.getPrevious();
            list.remove(prevNode);
        }
    }
}

