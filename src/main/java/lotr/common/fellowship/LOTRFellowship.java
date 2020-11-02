package lotr.common.fellowship;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import lotr.common.LOTRLevelData;
import lotr.common.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.event.ClickEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraftforge.common.ForgeHooks;

public class LOTRFellowship {
	private boolean needsSave = false;
	private UUID fellowshipUUID;
	private String fellowshipName;
	private boolean disbanded = false;
	private ItemStack fellowshipIcon;
	private UUID ownerUUID;
	private List<UUID> memberUUIDs = new ArrayList<>();
	private Set<UUID> adminUUIDs = new HashSet<>();
	private boolean preventPVP = true;
	private boolean preventHiredFF = true;
	private boolean showMapLocations = true;

	public LOTRFellowship() {
		fellowshipUUID = UUID.randomUUID();
	}

	public LOTRFellowship(UUID fsID) {
		fellowshipUUID = fsID;
	}

	public LOTRFellowship(UUID owner, String name) {
		this();
		ownerUUID = owner;
		fellowshipName = name;
	}

	public void createAndRegister() {
		LOTRFellowshipData.addFellowship(this);
		LOTRLevelData.getData(ownerUUID).addFellowship(this);
		updateForAllMembers(FellowshipUpdateType.FULL);
		markDirty();
	}

	public void validate() {
		if (fellowshipUUID == null) {
			fellowshipUUID = UUID.randomUUID();
		}
		if (ownerUUID == null) {
			ownerUUID = UUID.randomUUID();
		}
	}

	public void markDirty() {
		needsSave = true;
	}

	public boolean needsSave() {
		return needsSave;
	}

	public void save(NBTTagCompound fsData) {
		if (ownerUUID != null) {
			fsData.setString("Owner", ownerUUID.toString());
		}
		NBTTagList memberTags = new NBTTagList();
		for (UUID member : memberUUIDs) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("Member", member.toString());
			if (adminUUIDs.contains(member)) {
				nbt.setBoolean("Admin", true);
			}
			memberTags.appendTag(nbt);
		}
		fsData.setTag("Members", memberTags);
		if (fellowshipName != null) {
			fsData.setString("Name", fellowshipName);
		}
		if (fellowshipIcon != null) {
			NBTTagCompound itemData = new NBTTagCompound();
			fellowshipIcon.writeToNBT(itemData);
			fsData.setTag("Icon", itemData);
		}
		fsData.setBoolean("PreventPVP", preventPVP);
		fsData.setBoolean("PreventHiredFF", preventHiredFF);
		fsData.setBoolean("ShowMap", showMapLocations);
		needsSave = false;
	}

	public void load(NBTTagCompound fsData) {
		if (fsData.hasKey("Owner")) {
			ownerUUID = UUID.fromString(fsData.getString("Owner"));
		}
		memberUUIDs.clear();
		adminUUIDs.clear();
		NBTTagList memberTags = fsData.getTagList("Members", 10);
		for (int i = 0; i < memberTags.tagCount(); ++i) {
			NBTTagCompound nbt = memberTags.getCompoundTagAt(i);
			UUID member = UUID.fromString(nbt.getString("Member"));
			if (member == null) {
				continue;
			}
			memberUUIDs.add(member);
			if (!nbt.hasKey("Admin") || !nbt.getBoolean("Admin")) {
				continue;
			}
			adminUUIDs.add(member);
		}
		if (fsData.hasKey("Name")) {
			fellowshipName = fsData.getString("Name");
		}
		if (fsData.hasKey("Icon")) {
			NBTTagCompound itemData = fsData.getCompoundTag("Icon");
			fellowshipIcon = ItemStack.loadItemStackFromNBT(itemData);
		}
		if (fsData.hasKey("PreventPVP")) {
			preventPVP = fsData.getBoolean("PreventPVP");
		}
		if (fsData.hasKey("PreventPVP")) {
			preventHiredFF = fsData.getBoolean("PreventHiredFF");
		}
		if (fsData.hasKey("ShowMap")) {
			showMapLocations = fsData.getBoolean("ShowMap");
		}
		validate();
	}

	public UUID getFellowshipID() {
		return fellowshipUUID;
	}

	public UUID getOwner() {
		return ownerUUID;
	}

	public boolean isOwner(UUID player) {
		return ownerUUID.equals(player);
	}

	public void setOwner(UUID owner) {
		UUID prevOwner = ownerUUID;
		if (prevOwner != null && !memberUUIDs.contains(prevOwner)) {
			memberUUIDs.add(0, prevOwner);
		}
		ownerUUID = owner;
		if (memberUUIDs.contains(owner)) {
			memberUUIDs.remove(owner);
		}
		if (adminUUIDs.contains(owner)) {
			adminUUIDs.remove(owner);
		}
		LOTRLevelData.getData(ownerUUID).addFellowship(this);
		updateForAllMembers(FellowshipUpdateType.FULL);
		markDirty();
	}

	public String getName() {
		return fellowshipName;
	}

	public void setName(String name) {
		fellowshipName = name;
		updateForAllMembers(FellowshipUpdateType.RENAME);
		markDirty();
	}

	public boolean containsPlayer(UUID player) {
		return isOwner(player) || hasMember(player);
	}

	public boolean hasMember(UUID player) {
		return memberUUIDs.contains(player);
	}

	public void addMember(UUID player) {
		if (!isOwner(player) && !memberUUIDs.contains(player)) {
			memberUUIDs.add(player);
			LOTRLevelData.getData(player).addFellowship(this);
			updateForAllMembers(FellowshipUpdateType.FULL);
			markDirty();
		}
	}

	public void removeMember(UUID player) {
		if (memberUUIDs.contains(player)) {
			memberUUIDs.remove(player);
			if (adminUUIDs.contains(player)) {
				adminUUIDs.remove(player);
			}
			LOTRLevelData.getData(player).removeFellowship(this);
			updateForAllMembers(FellowshipUpdateType.FULL);
			markDirty();
		}
	}

	public List<UUID> getMemberUUIDs() {
		return memberUUIDs;
	}

	public List<UUID> getAllPlayerUUIDs() {
		ArrayList<UUID> list = new ArrayList<>();
		list.add(ownerUUID);
		list.addAll(memberUUIDs);
		return list;
	}

	public boolean isAdmin(UUID player) {
		return hasMember(player) && adminUUIDs.contains(player);
	}

	public void setAdmin(UUID player, boolean flag) {
		if (memberUUIDs.contains(player)) {
			if (flag && !adminUUIDs.contains(player)) {
				adminUUIDs.add(player);
				updateForAllMembers(FellowshipUpdateType.FULL);
				markDirty();
			} else if (!flag && adminUUIDs.contains(player)) {
				adminUUIDs.remove(player);
				updateForAllMembers(FellowshipUpdateType.FULL);
				markDirty();
			}
		}
	}

	public ItemStack getIcon() {
		return fellowshipIcon;
	}

	public void setIcon(ItemStack itemstack) {
		fellowshipIcon = itemstack;
		updateForAllMembers(FellowshipUpdateType.CHANGE_ICON);
		markDirty();
	}

	public boolean getPreventPVP() {
		return preventPVP;
	}

	public void setPreventPVP(boolean flag) {
		preventPVP = flag;
		updateForAllMembers(FellowshipUpdateType.TOGGLE_PVP);
		markDirty();
	}

	public boolean getPreventHiredFriendlyFire() {
		return preventHiredFF;
	}

	public void setPreventHiredFriendlyFire(boolean flag) {
		preventHiredFF = flag;
		updateForAllMembers(FellowshipUpdateType.TOGGLE_HIRED_FRIENDLY_FIRE);
		markDirty();
	}

	public boolean getShowMapLocations() {
		return showMapLocations;
	}

	public void setShowMapLocations(boolean flag) {
		showMapLocations = flag;
		updateForAllMembers(FellowshipUpdateType.TOGGLE_SHOW_MAP_LOCATIONS);
		markDirty();
	}

	public void updateForAllMembers(FellowshipUpdateType updateType) {
		for (UUID player : getAllPlayerUUIDs()) {
			LOTRLevelData.getData(player).updateFellowship(this, updateType);
		}
	}

	public void disband() {
		disbanded = true;
		ArrayList<UUID> copyMemberIDs = new ArrayList<>(memberUUIDs);
		for (UUID player : copyMemberIDs) {
			removeMember(player);
		}
	}

	public boolean isDisbanded() {
		return disbanded;
	}

	public void sendFellowshipMessage(EntityPlayerMP sender, String message) {
		if (sender.func_147096_v() == EntityPlayer.EnumChatVisibility.HIDDEN) {
			ChatComponentTranslation msgCannotSend = new ChatComponentTranslation("chat.cannotSend", new Object[0]);
			msgCannotSend.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.playerNetServerHandler.sendPacket(new S02PacketChat(msgCannotSend));
		} else {
			sender.func_143004_u();
			message = StringUtils.normalizeSpace(message);
			if (StringUtils.isBlank(message)) {
				return;
			}
			for (int i = 0; i < message.length(); ++i) {
				if (ChatAllowedCharacters.isAllowedCharacter(message.charAt(i))) {
					continue;
				}
				sender.playerNetServerHandler.kickPlayerFromServer("Illegal characters in chat");
				return;
			}
			ClickEvent fMsgClickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/fmsg \"" + getName() + "\" ");
			IChatComponent msgComponent = ForgeHooks.newChatWithLinks(message);
			msgComponent.getChatStyle().setColor(EnumChatFormatting.YELLOW);
			IChatComponent senderComponent = sender.func_145748_c_();
			senderComponent.getChatStyle().setChatClickEvent(fMsgClickEvent);
			ChatComponentTranslation chatComponent = new ChatComponentTranslation("chat.type.text", new Object[] { senderComponent, "" });
			chatComponent = ForgeHooks.onServerChatEvent(sender.playerNetServerHandler, message, chatComponent);
			if (chatComponent == null) {
				return;
			}
			chatComponent.appendSibling(msgComponent);
			ChatComponentTranslation fsComponent = new ChatComponentTranslation("commands.lotr.fmsg.fsPrefix", new Object[] { getName() });
			fsComponent.getChatStyle().setColor(EnumChatFormatting.YELLOW);
			fsComponent.getChatStyle().setChatClickEvent(fMsgClickEvent);
			ChatComponentTranslation fullChatComponent = new ChatComponentTranslation("%s %s", new Object[] { fsComponent, chatComponent });
			MinecraftServer server = MinecraftServer.getServer();
			server.addChatMessage(fullChatComponent);
			S02PacketChat packetChat = new S02PacketChat(fullChatComponent, false);
			for (Object player : server.getConfigurationManager().playerEntityList) {
				EntityPlayerMP entityplayer = (EntityPlayerMP) player;
				if (!containsPlayer(entityplayer.getUniqueID())) {
					continue;
				}
				entityplayer.playerNetServerHandler.sendPacket(packetChat);
			}
		}
	}

	public void sendNotification(EntityPlayer entityplayer, String key, Object... args) {
		ChatComponentTranslation message = new ChatComponentTranslation(key, args);
		message.getChatStyle().setColor(EnumChatFormatting.YELLOW);
		entityplayer.addChatMessage(message);
		LOTRPacketFellowshipNotification packet = new LOTRPacketFellowshipNotification(message);
		LOTRPacketHandler.networkWrapper.sendTo((IMessage) packet, (EntityPlayerMP) entityplayer);
	}
}
