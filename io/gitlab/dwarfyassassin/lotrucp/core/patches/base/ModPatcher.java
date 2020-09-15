/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.Loader
 */
package io.gitlab.dwarfyassassin.lotrucp.core.patches.base;

import cpw.mods.fml.common.Loader;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;

public abstract class ModPatcher
extends Patcher {
    private String modID;

    public ModPatcher(String name, String modID) {
        super(name);
        this.modID = modID;
    }

    @Override
    public Patcher.LoadingPhase getLoadPhase() {
        return Patcher.LoadingPhase.FORGE_MOD_LOADING;
    }

    @Override
    public boolean shouldInit() {
        return Loader.isModLoaded((String)this.modID);
    }
}

