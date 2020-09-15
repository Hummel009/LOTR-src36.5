/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.tree.ClassNode
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches.base;

import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.tree.ClassNode;

public abstract class Patcher {
    protected Map<String, ConsumerImplBecauseNoLambdas<ClassNode>> classes = new HashMap<String, ConsumerImplBecauseNoLambdas<ClassNode>>();
    private String patcherName;

    public Patcher(String name) {
        this.patcherName = name;
    }

    public LoadingPhase getLoadPhase() {
        return LoadingPhase.CORE_MOD_LOADING;
    }

    public boolean shouldInit() {
        return true;
    }

    public boolean isDone() {
        return this.classes.isEmpty();
    }

    public boolean canRun(String className) {
        return this.classes.containsKey(className);
    }

    public void run(String className, ClassNode classNode) {
        this.classes.get(className).accept(classNode);
        this.classes.remove(className);
    }

    public String getName() {
        return this.patcherName;
    }

    public static enum LoadingPhase {
        CORE_MOD_LOADING,
        FORGE_MOD_LOADING;

    }

    public static interface ConsumerImplBecauseNoLambdas<T> {
        public void accept(T var1);
    }

}

