/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.FieldInsnNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodInsnNode
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.VarInsnNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;
import io.gitlab.dwarfyassassin.lotrucp.core.utils.ASMUtils;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class FMLPatcher
extends Patcher {
    public FMLPatcher() {
        super("FML");
        this.classes.put("cpw.mods.fml.common.LoadController", new Patcher.ConsumerImplBecauseNoLambdas<ClassNode>(){

            @Override
            public void accept(ClassNode node) {
                FMLPatcher.this.patchLoadController(node);
            }
        });
    }

    private void patchLoadController(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "buildModList", "(Lcpw/mods/fml/common/event/FMLLoadEvent;)V");
        if (method == null) {
            return;
        }
        method.instructions.insert((AbstractInsnNode)new MethodInsnNode(184, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/PreMCHooks", "postFMLLoad", "()V", false));
        UCPCoreMod.log.info("Patched the FML load controller.");
    }

    private void patchModContainer(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "bindMetadata", "(Lcpw/mods/fml/common/MetadataCollection;)V");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (!(node instanceof FieldInsnNode)) continue;
            FieldInsnNode fieldNode = (FieldInsnNode)node;
            if (!fieldNode.name.equals("dependants")) continue;
            InsnList insList = new InsnList();
            insList.add((AbstractInsnNode)new VarInsnNode(25, 0));
            insList.add((AbstractInsnNode)new FieldInsnNode(180, "cpw/mods/fml/common/FMLModContainer", "modMetadata", "Lcpw/mods/fml/common/ModMetadata;"));
            insList.add((AbstractInsnNode)new MethodInsnNode(184, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/PreMCHooks", "forgeLoadOrderHook", "(Lcpw/mods/fml/common/ModMetadata;)V", false));
            method.instructions.insert((AbstractInsnNode)fieldNode, insList);
            break;
        }
        UCPCoreMod.log.info("Patched the FML dependency loader.");
    }

    private void patchLoader(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "loadMods", "()V");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (!(node instanceof MethodInsnNode) || node.getOpcode() != 184) continue;
            MethodInsnNode methodNode = (MethodInsnNode)node;
            if (!methodNode.name.equals("copyOf") || !methodNode.owner.equals("com/google/common/collect/ImmutableList")) continue;
            MethodInsnNode insertNode = new MethodInsnNode(184, "io/gitlab/dwarfyassassin/lotrucp/core/hooks/PreMCHooks", "postFMLLoad", "()V", false);
            method.instructions.insert(methodNode.getNext(), (AbstractInsnNode)insertNode);
            break;
        }
        UCPCoreMod.log.info("Patched the FML loader.");
    }

}

