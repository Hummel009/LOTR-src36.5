/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.InsnNode
 *  org.objectweb.asm.tree.JumpInsnNode
 *  org.objectweb.asm.tree.LabelNode
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.TypeInsnNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.ModPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.utils.ASMUtils;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ThaumcraftPatcher
extends ModPatcher {
    public ThaumcraftPatcher() {
        super("Thaumcraft", "Thaumcraft");
    }

    private void patchGolemInventory(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "getSizeInventory", "func_70302_i_", "()I");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (node.getOpcode() != 172) continue;
            ASMUtils.removePreviousNodes(method.instructions, node, 2);
            break;
        }
        UCPCoreMod.log.info("Patched the Golem inventory to be compatible with the coin conter (This might lead to issues with other mods).");
    }

    private void patchGolemBannerProtection(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "protects", "(Llotr/common/entity/item/LOTREntityBanner;)Llotr/common/LOTRBannerProtection$ProtectType;");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (node.getOpcode() != 25) continue;
            InsnList insList = new InsnList();
            LabelNode endLabel = new LabelNode();
            LabelNode popLabel = new LabelNode();
            insList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insList.add((AbstractInsnNode)new FieldInsnNode(180, "lotr/common/LOTRBannerProtection$2", "val$entityplayer", "Lnet/minecraft/entity/player/EntityPlayer;"));
            insList.add((AbstractInsnNode)new TypeInsnNode(193, "net/minecraftforge/common/util/FakePlayer"));
            insList.add((AbstractInsnNode)new JumpInsnNode(153, endLabel));
            insList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insList.add((AbstractInsnNode)new FieldInsnNode(180, "lotr/common/LOTRBannerProtection$2", "val$entityplayer", "Lnet/minecraft/entity/player/EntityPlayer;"));
            insList.add((AbstractInsnNode)new VarInsnNode(25, 1));
            insList.add((AbstractInsnNode)new MethodInsnNode(184, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/ThaumcraftHooks", "thaumcraftGolemBannerProtection", "(Lnet/minecraft/entity/player/EntityPlayer;Llotr/common/entity/item/LOTREntityBanner;)Llotr/common/LOTRBannerProtection$ProtectType;", false));
            insList.add((AbstractInsnNode)new InsnNode(89));
            insList.add((AbstractInsnNode)new JumpInsnNode(198, popLabel));
            insList.add((AbstractInsnNode)new InsnNode(176));
            insList.add((AbstractInsnNode)popLabel);
            insList.add((AbstractInsnNode)new InsnNode(87));
            insList.add((AbstractInsnNode)endLabel);
            method.instructions.insertBefore(node, insList);
            break;
        }
        UCPCoreMod.log.info("Patched the banner protection to user the golem owner instead of the golem itself and prevented sending warning messages to fake players.");
    }
}

