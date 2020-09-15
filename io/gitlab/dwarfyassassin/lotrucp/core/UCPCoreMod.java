/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.Logger
 */
package io.gitlab.dwarfyassassin.lotrucp.core;

import io.gitlab.dwarfyassassin.lotrucp.core.UCPClassTransformer;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreSetup;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.base.Patcher;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class UCPCoreMod {
    public static Logger log;
    public static List<Patcher> activePatches;
    private static List<Patcher> modPatches;

    public String[] getASMTransformerClass() {
        return new String[]{UCPClassTransformer.class.getName()};
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return UCPCoreSetup.class.getName();
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }

    public static void registerPatcher(Patcher patcher) {
        if (patcher.getLoadPhase() == Patcher.LoadingPhase.CORE_MOD_LOADING && patcher.shouldInit()) {
            activePatches.add(patcher);
        } else if (patcher.getLoadPhase() == Patcher.LoadingPhase.FORGE_MOD_LOADING) {
            modPatches.add(patcher);
        }
    }

    public static void loadModPatches() {
        int i = 0;
        for (Patcher patcher : modPatches) {
            if (!patcher.shouldInit()) continue;
            activePatches.add(patcher);
            ++i;
        }
        log.info("Loaded " + i + " mod patches.");
        modPatches.clear();
    }

    static {
        activePatches = new ArrayList<Patcher>();
        modPatches = new ArrayList<Patcher>();
        System.out.println("LOTR-UCP: Found core mod.");
    }
}

