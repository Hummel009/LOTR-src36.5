package lotr.common.fellowship;

import java.util.*;

import lotr.common.LOTRTitle;
import net.minecraft.item.ItemStack;

public class LOTRFellowshipClient {
	private UUID fellowshipID;
	private String fellowshipName;
	private ItemStack fellowshipIcon;
	private boolean isOwned;
	private boolean isAdminned;
	private String ownerName;
	private List<String> allPlayerNames = new ArrayList<>();
	private List<String> memberNames = new ArrayList<>();
	private Map<String, LOTRTitle.PlayerTitle> titleMap = new HashMap<>();
	private Set<String> adminNames = new HashSet<>();
	private boolean preventPVP;
	private boolean preventHiredFF;
	private boolean showMapLocations;

	public LOTRFellowshipClient(UUID id, String name, boolean owned, boolean admin, List<String> members) {
		fellowshipID = id;
		fellowshipName = name;
		isOwned = owned;
		isAdminned = admin;
		ownerName = members.get(0);
		allPlayerNames = members;
		memberNames = new ArrayList<>(allPlayerNames);
		memberNames.remove(ownerName);
	}

	public void setTitles(Map<String, LOTRTitle.PlayerTitle> titles) {
		titleMap = titles;
	}

	public void setAdmins(Set<String> admins) {
		adminNames = admins;
	}

	public void setName(String name) {
		fellowshipName = name;
	}

	public void setIcon(ItemStack itemstack) {
		fellowshipIcon = itemstack;
	}

	public void setPreventPVP(boolean flag) {
		preventPVP = flag;
	}

	public void setPreventHiredFriendlyFire(boolean flag) {
		preventHiredFF = flag;
	}

	public void setShowMapLocations(boolean flag) {
		showMapLocations = flag;
	}

	public UUID getFellowshipID() {
		return fellowshipID;
	}

	public String getName() {
		return fellowshipName;
	}

	public boolean isOwned() {
		return isOwned;
	}

	public boolean isAdminned() {
		return isAdminned;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public List<String> getMemberNames() {
		return memberNames;
	}

	public List<String> getAllPlayerNames() {
		return allPlayerNames;
	}

	public boolean isPlayerIn(String name) {
		return allPlayerNames.contains(name);
	}

	public int getMemberCount() {
		return allPlayerNames.size();
	}

	public LOTRTitle.PlayerTitle getTitleFor(String name) {
		return titleMap.get(name);
	}

	public boolean isAdmin(String name) {
		return adminNames.contains(name);
	}

	public ItemStack getIcon() {
		return fellowshipIcon;
	}

	public boolean getPreventPVP() {
		return preventPVP;
	}

	public boolean getPreventHiredFriendlyFire() {
		return preventHiredFF;
	}

	public boolean getShowMapLocations() {
		return showMapLocations;
	}

	public void updateDataFrom(LOTRFellowshipClient other) {
		fellowshipName = other.fellowshipName;
		fellowshipIcon = other.fellowshipIcon;
		isOwned = other.isOwned;
		isAdminned = other.isAdminned;
		ownerName = other.ownerName;
		allPlayerNames = other.allPlayerNames;
		memberNames = other.memberNames;
		titleMap = other.titleMap;
		adminNames = other.adminNames;
		preventPVP = other.preventPVP;
		preventHiredFF = other.preventHiredFF;
		showMapLocations = other.showMapLocations;
	}
}
