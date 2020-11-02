package lotr.common.network;

import java.io.IOException;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.*;
import io.netty.buffer.ByteBuf;
import lotr.common.*;
import lotr.common.fellowship.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.UsernameCache;

public class LOTRPacketFellowship implements IMessage {
	private UUID fellowshipID;
	private boolean isInvite;
	private String fellowshipName;
	private ItemStack fellowshipIcon;
	private boolean isOwned;
	private boolean isAdminned;
	private List<String> playerNames = new ArrayList<>();
	private Map<String, LOTRTitle.PlayerTitle> titleMap = new HashMap<>();
	private Set<String> adminNames = new HashSet<>();
	private boolean preventPVP;
	private boolean preventHiredFF;
	private boolean showMapLocations;

	public LOTRPacketFellowship() {
	}

	public LOTRPacketFellowship(LOTRPlayerData playerData, LOTRFellowship fs, boolean invite) {
		fellowshipID = fs.getFellowshipID();
		isInvite = invite;
		fellowshipName = fs.getName();
		fellowshipIcon = fs.getIcon();
		UUID thisPlayer = playerData.getPlayerUUID();
		isOwned = fs.isOwner(thisPlayer);
		isAdminned = fs.isAdmin(thisPlayer);
		List<UUID> playerIDs = fs.getAllPlayerUUIDs();
		for (UUID player : playerIDs) {
			String username = LOTRPacketFellowship.getPlayerUsername(player);
			playerNames.add(username);
			LOTRPlayerData data = LOTRLevelData.getData(player);
			LOTRTitle.PlayerTitle title = data.getPlayerTitle();
			if (title != null) {
				titleMap.put(username, title);
			}
			if (!fs.isAdmin(player)) {
				continue;
			}
			adminNames.add(username);
		}
		preventPVP = fs.getPreventPVP();
		preventHiredFF = fs.getPreventHiredFriendlyFire();
		showMapLocations = fs.getShowMapLocations();
	}

	public static String getPlayerUsername(UUID player) {
		GameProfile profile = MinecraftServer.getServer().func_152358_ax().func_152652_a(player);
		if (profile == null || StringUtils.isBlank(profile.getName())) {
			String name = UsernameCache.getLastKnownUsername(player);
			if (name != null) {
				profile = new GameProfile(player, name);
			} else {
				profile = new GameProfile(player, "");
				MinecraftServer.getServer().func_147130_as().fillProfileProperties(profile, true);
			}
		}
		String username = profile.getName();
		return username;
	}

	@Override
	public void toBytes(ByteBuf data) {
		data.writeLong(fellowshipID.getMostSignificantBits());
		data.writeLong(fellowshipID.getLeastSignificantBits());
		data.writeBoolean(isInvite);
		byte[] fsNameBytes = fellowshipName.getBytes(Charsets.UTF_8);
		data.writeByte(fsNameBytes.length);
		data.writeBytes(fsNameBytes);
		NBTTagCompound iconData = new NBTTagCompound();
		if (fellowshipIcon != null) {
			fellowshipIcon.writeToNBT(iconData);
		}
		try {
			new PacketBuffer(data).writeNBTTagCompoundToBuffer(iconData);
		} catch (IOException e) {
			FMLLog.severe("LOTR: Error writing fellowship icon data", new Object[0]);
			e.printStackTrace();
		}
		data.writeBoolean(isOwned);
		data.writeBoolean(isAdminned);
		for (String username : playerNames) {
			byte[] usernameBytes = username.getBytes(Charsets.UTF_8);
			data.writeByte(usernameBytes.length);
			data.writeBytes(usernameBytes);
			LOTRTitle.PlayerTitle title = titleMap.get(username);
			if (title != null) {
				data.writeShort(title.getTitle().titleID);
				data.writeByte(title.getColor().getFormattingCode());
			} else {
				data.writeShort(-1);
			}
			boolean admin = adminNames.contains(username);
			data.writeBoolean(admin);
		}
		data.writeByte(-1);
		data.writeBoolean(preventPVP);
		data.writeBoolean(preventHiredFF);
		data.writeBoolean(showMapLocations);
	}

	@Override
	public void fromBytes(ByteBuf data) {
		fellowshipID = new UUID(data.readLong(), data.readLong());
		isInvite = data.readBoolean();
		byte fsNameLength = data.readByte();
		ByteBuf fsNameBytes = data.readBytes(fsNameLength);
		fellowshipName = fsNameBytes.toString(Charsets.UTF_8);
		NBTTagCompound iconData = new NBTTagCompound();
		try {
			iconData = new PacketBuffer(data).readNBTTagCompoundFromBuffer();
		} catch (IOException e) {
			FMLLog.severe("LOTR: Error reading fellowship icon data", new Object[0]);
			e.printStackTrace();
		}
		fellowshipIcon = ItemStack.loadItemStackFromNBT(iconData);
		isOwned = data.readBoolean();
		isAdminned = data.readBoolean();
		byte usernameLength = 0;
		while ((usernameLength = data.readByte()) >= 0) {
			ByteBuf usernameBytes = data.readBytes(usernameLength);
			String username = usernameBytes.toString(Charsets.UTF_8);
			playerNames.add(username);
			short titleID = data.readShort();
			if (titleID >= 0) {
				byte colorID = data.readByte();
				LOTRTitle title = LOTRTitle.forID(titleID);
				EnumChatFormatting color = LOTRTitle.PlayerTitle.colorForID(colorID);
				if (title != null && color != null) {
					LOTRTitle.PlayerTitle playerTitle = new LOTRTitle.PlayerTitle(title, color);
					titleMap.put(username, playerTitle);
				}
			}
			if (!data.readBoolean()) {
				continue;
			}
			adminNames.add(username);
		}
		preventPVP = data.readBoolean();
		preventHiredFF = data.readBoolean();
		showMapLocations = data.readBoolean();
	}

	public static class Handler implements IMessageHandler<LOTRPacketFellowship, IMessage> {
		@Override
		public IMessage onMessage(LOTRPacketFellowship packet, MessageContext context) {
			LOTRFellowshipClient fellowship = new LOTRFellowshipClient(packet.fellowshipID, packet.fellowshipName, packet.isOwned, packet.isAdminned, packet.playerNames);
			fellowship.setTitles(packet.titleMap);
			fellowship.setAdmins(packet.adminNames);
			fellowship.setIcon(packet.fellowshipIcon);
			fellowship.setPreventPVP(packet.preventPVP);
			fellowship.setPreventHiredFriendlyFire(packet.preventHiredFF);
			fellowship.setShowMapLocations(packet.showMapLocations);
			EntityPlayer entityplayer = LOTRMod.proxy.getClientPlayer();
			if (packet.isInvite) {
				LOTRLevelData.getData(entityplayer).addOrUpdateClientFellowshipInvite(fellowship);
			} else {
				LOTRLevelData.getData(entityplayer).addOrUpdateClientFellowship(fellowship);
			}
			return null;
		}
	}

}
