/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.IFMLCallHook
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package io.gitlab.dwarfyassassin.lotrucp.core;

import cpw.mods.fml.relauncher.IFMLCallHook;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.BotaniaPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.FMLPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.ScreenshotEnhancedPatcher;
import io.gitlab.dwarfyassassin.lotrucp.core.patches.ThaumcraftPatcher;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UCPCoreSetup
implements IFMLCallHook {
    public Void call() throws Exception {
        UCPCoreMod.log = LogManager.getLogger((String)"LOTR-UCP");
        UCPCoreMod.registerPatcher(new FMLPatcher());
        UCPCoreMod.registerPatcher(new BotaniaPatcher());
        UCPCoreMod.registerPatcher(new ScreenshotEnhancedPatcher());
        UCPCoreMod.registerPatcher(new ThaumcraftPatcher());
        return null;
    }

    public void injectData(Map<String, Object> data) {
    }
}

