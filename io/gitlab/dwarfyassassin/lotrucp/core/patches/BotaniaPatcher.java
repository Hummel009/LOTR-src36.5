/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 *  org.objectweb.asm.tree.AbstractInsnNode
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodNode
 *  org.objectweb.asm.tree.TypeInsnNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.ModPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;
import io.gitlab.dwarfyassassin.lotrucp.core.utils.ASMUtils;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class BotaniaPatcher
extends ModPatcher {
    public BotaniaPatcher() {
        super("Botania", "Botania");
        this.classes.put("vazkii.botania.common.block.subtile.generating.SubTileKekimurus", new Patcher.ConsumerImplBecauseNoLambdas<ClassNode>(){

            @Override
            public void accept(ClassNode node) {
                BotaniaPatcher.this.patchKekimurus(node);
            }
        });
    }

    private void patchKekimurus(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "onUpdate", "()V");
        if (method == null) {
            return;
        }
        for (AbstractInsnNode node : method.instructions.toArray()) {
            if (!(node instanceof TypeInsnNode)) continue;
            TypeInsnNode typeNode = (TypeInsnNode)node;
            if (!typeNode.desc.equals("net/minecraft/block/BlockCake")) continue;
            typeNode.desc = "lotr/common/block/LOTRBlockPlaceableFood";
            break;
        }
        UCPCoreMod.log.info("Patched the Kekimurus to eat all LOTR cakes.");
    }

}

