package lotr.common.coremod;

import java.util.*;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import io.gitlab.dwarfyassassin.lotrucp.core.UCPCoreMod;

@IFMLLoadingPlugin.TransformerExclusions(value = { "lotr.common.coremod", "io.gitlab.dwarfyassassin.lotrucp.core" })
@IFMLLoadingPlugin.SortingIndex(value = 1001)
@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
public class LOTRLoadingPlugin implements IFMLLoadingPlugin {
	private final UCPCoreMod dwarfyAssassinCompatibilityCoremod = new UCPCoreMod();

	@Override
	public String[] getASMTransformerClass() {
		ArrayList<String> classes = new ArrayList<>();
		classes.add(LOTRClassTransformer.class.getName());
		classes.addAll(Arrays.asList(dwarfyAssassinCompatibilityCoremod.getASMTransformerClass()));
		return classes.toArray(new String[0]);
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return dwarfyAssassinCompatibilityCoremod.getSetupClass();
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
