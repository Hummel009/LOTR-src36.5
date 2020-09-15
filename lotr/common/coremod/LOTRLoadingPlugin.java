/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.IFMLLoadingPlugin
 *  cpw.mods.fml.relauncher.IFMLLoadingPlugin$MCVersion
 *  cpw.mods.fml.relauncher.IFMLLoadingPlugin$SortingIndex
 *  cpw.mods.fml.relauncher.IFMLLoadingPlugin$TransformerExclusions
 */
package lotr.common.coremod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;

@IFMLLoadingPlugin.TransformerExclusions(value={"lotr.common.coremod", "io.gitlab.dwarfyassassin.lotrucp.core"})
@IFMLLoadingPlugin.SortingIndex(value=1001)
@IFMLLoadingPlugin.MCVersion(value="1.7.10")
public class LOTRLoadingPlugin
implements IFMLLoadingPlugin {
    private final UCPCoreMod dwarfyAssassinCompatibilityCoremod = new UCPCoreMod();

    public String[] getASMTransformerClass() {
        ArrayList<String> classes = new ArrayList<String>();
        classes.add(LOTRClassTransformer.class.getName());
        classes.addAll(Arrays.asList(this.dwarfyAssassinCompatibilityCoremod.getASMTransformerClass()));
        return classes.toArray(new String[0]);
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return this.dwarfyAssassinCompatibilityCoremod.getSetupClass();
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

