/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.classloading.FMLForgePlugin
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.ModPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;
import io.gitlab.dwarfyassassin.lotrucp.core.utils.ASMUtils;
import java.util.Map;
import net.minecraftforge.classloading.FMLForgePlugin;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ScreenshotEnhancedPatcher
extends ModPatcher {
    public ScreenshotEnhancedPatcher() {
        super("Screenshots Enhanced", "screenshots");
        this.classes.put("lotr.client.render.entity.LOTRRenderScrapTrader", new Patcher.ConsumerImplBecauseNoLambdas<ClassNode>(){

            @Override
            public void accept(ClassNode node) {
                ScreenshotEnhancedPatcher.this.patchScrapTraderRender(node);
            }
        });
    }

    private void patchScrapTraderRender(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "doRender", "func_76986_a", "(Lnet/minecraft/entity/EntityLiving;DDDFF)V");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (node.getOpcode() != 182) continue;
            MethodInsnNode methodNode = (MethodInsnNode)node;
            if (!methodNode.name.equals(FMLForgePlugin.RUNTIME_DEOBF ? "func_151463_i" : "getKeyCode") || !methodNode.desc.equals("()I")) continue;
            ASMUtils.removePreviousNodes(method.instructions, (AbstractInsnNode)methodNode, 3);
            FieldInsnNode keyCodeField = new FieldInsnNode(178, "net/undoredo/screenshots/KeyScreenshotListener", "screenshotKeyBinding", "Lnet/minecraft/client/settings/KeyBinding;");
            method.instructions.insertBefore((AbstractInsnNode)methodNode, (AbstractInsnNode)keyCodeField);
            break;
        }
        UCPCoreMod.log.info("Patched the Oddment Collector render to be compatible with Screenshots Enhanced.");
    }

}

