/*
 * Decompiled with CFR 0.148.
 */
package io.gitlab.dwarfyassassin.lotrucp.core.hooks;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;

public class PreMCHooks {
    public static void postFMLLoad() {
        UCPCoreMod.loadModPatches();
    }
}

