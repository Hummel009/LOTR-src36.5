package lotr.client.gui;

import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import lotr.common.*;
import lotr.common.fellowship.LOTRFellowshipClient;
import lotr.common.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;

public class LOTRGuiFellowships extends LOTRGuiMenuBase {
	public static final ResourceLocation iconsTextures = new ResourceLocation("lotr:gui/fellowships.png");
	private Page page = Page.LIST;
	private List<LOTRFellowshipClient> allFellowshipsLeading = new ArrayList<>();
	private List<LOTRFellowshipClient> allFellowshipsOther = new ArrayList<>();
	private List<LOTRFellowshipClient> allFellowshipInvites = new ArrayList<>();
	private LOTRFellowshipClient mouseOverFellowship;
	private LOTRFellowshipClient viewingFellowship;
	private String mouseOverPlayer;
	private boolean mouseOverPlayerRemove;
	private boolean mouseOverPlayerOp;
	private boolean mouseOverPlayerDeop;
	private boolean mouseOverPlayerTransfer;
	private String removingPlayer;
	private String oppingPlayer;
	private String deoppingPlayer;
	private String transferringPlayer;
	private boolean mouseOverInviteAccept;
	private boolean mouseOverInviteReject;
	private GuiButton buttonCreate;
	private GuiButton buttonCreateThis;
	private LOTRGuiButtonFsOption buttonInvitePlayer;
	private GuiButton buttonInviteThis;
	private LOTRGuiButtonFsOption buttonDisband;
	private GuiButton buttonDisbandThis;
	private GuiButton buttonLeave;
	private GuiButton buttonLeaveThis;
	private LOTRGuiButtonFsOption buttonSetIcon;
	private GuiButton buttonRemove;
	private GuiButton buttonTransfer;
	private LOTRGuiButtonFsOption buttonRename;
	private GuiButton buttonRenameThis;
	private GuiButton buttonBack;
	private GuiButton buttonInvites;
	private LOTRGuiButtonFsOption buttonPVP;
	private LOTRGuiButtonFsOption buttonHiredFF;
	private LOTRGuiButtonFsOption buttonMapShow;
	private GuiButton buttonOp;
	private GuiButton buttonDeop;
	private List<LOTRGuiButtonFsOption> orderedFsOptionButtons = new ArrayList<>();
	private GuiTextField textFieldName;
	private GuiTextField textFieldPlayer;
	private GuiTextField textFieldRename;
	public static final int entrySplit = 5;
	public static final int entryBorder = 10;
	public static final int selectBorder = 2;
	private int scrollWidgetWidth;
	private int scrollWidgetHeight;
	private int scrollBarX;
	private LOTRGuiScrollPane scrollPaneLeading;
	private LOTRGuiScrollPane scrollPaneOther;
	private LOTRGuiScrollPane scrollPaneMembers;
	private LOTRGuiScrollPane scrollPaneInvites;
	private int displayedFellowshipsLeading;
	private int displayedFellowshipsOther;
	private int displayedMembers;
	private int displayedInvites;

	public LOTRGuiFellowships() {
		xSize = 256;
		scrollWidgetWidth = 9;
		scrollWidgetHeight = 8;
		scrollBarX = xSize + 2 + 1;
		scrollPaneLeading = new LOTRGuiScrollPane(scrollWidgetWidth, scrollWidgetHeight);
		scrollPaneOther = new LOTRGuiScrollPane(scrollWidgetWidth, scrollWidgetHeight);
		scrollPaneMembers = new LOTRGuiScrollPane(scrollWidgetWidth, scrollWidgetHeight);
		scrollPaneInvites = new LOTRGuiScrollPane(scrollWidgetWidth, scrollWidgetHeight);
	}

	@Override
	public void initGui() {
		super.initGui();
		if (mc.thePlayer != null) {
			refreshFellowshipList();
		}
		int midX = guiLeft + xSize / 2;
		buttonCreate = new GuiButton(0, midX - 100, guiTop + 230, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.create"));
		buttonList.add(buttonCreate);
		buttonCreateThis = new GuiButton(1, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.createThis"));
		buttonList.add(buttonCreateThis);
		buttonInvitePlayer = new LOTRGuiButtonFsOption(2, midX, guiTop + 232, 0, 48, StatCollector.translateToLocal("lotr.gui.fellowships.invite"));
		buttonList.add(buttonInvitePlayer);
		buttonInviteThis = new GuiButton(3, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.inviteThis"));
		buttonList.add(buttonInviteThis);
		buttonDisband = new LOTRGuiButtonFsOption(4, midX, guiTop + 232, 16, 48, StatCollector.translateToLocal("lotr.gui.fellowships.disband"));
		buttonList.add(buttonDisband);
		buttonDisbandThis = new GuiButton(5, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.disbandThis"));
		buttonList.add(buttonDisbandThis);
		buttonLeave = new GuiButton(6, midX - 60, guiTop + 230, 120, 20, StatCollector.translateToLocal("lotr.gui.fellowships.leave"));
		buttonList.add(buttonLeave);
		buttonLeaveThis = new GuiButton(7, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.leaveThis"));
		buttonList.add(buttonLeaveThis);
		buttonSetIcon = new LOTRGuiButtonFsOption(8, midX, guiTop + 232, 48, 48, StatCollector.translateToLocal("lotr.gui.fellowships.setIcon"));
		buttonList.add(buttonSetIcon);
		buttonRemove = new GuiButton(9, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.remove"));
		buttonList.add(buttonRemove);
		buttonTransfer = new GuiButton(10, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.transfer"));
		buttonList.add(buttonTransfer);
		buttonRename = new LOTRGuiButtonFsOption(11, midX, guiTop + 232, 32, 48, StatCollector.translateToLocal("lotr.gui.fellowships.rename"));
		buttonList.add(buttonRename);
		buttonRenameThis = new GuiButton(12, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.renameThis"));
		buttonList.add(buttonRenameThis);
		buttonBack = new GuiButton(13, guiLeft - 10, guiTop, 20, 20, "<");
		buttonList.add(buttonBack);
		buttonInvites = new LOTRGuiButtonFsInvites(14, guiLeft + xSize - 16, guiTop, "");
		buttonList.add(buttonInvites);
		buttonPVP = new LOTRGuiButtonFsOption(15, midX, guiTop + 232, 64, 48, StatCollector.translateToLocal("lotr.gui.fellowships.togglePVP"));
		buttonList.add(buttonPVP);
		buttonHiredFF = new LOTRGuiButtonFsOption(16, midX, guiTop + 232, 80, 48, StatCollector.translateToLocal("lotr.gui.fellowships.toggleHiredFF"));
		buttonList.add(buttonHiredFF);
		buttonMapShow = new LOTRGuiButtonFsOption(17, midX, guiTop + 232, 96, 48, StatCollector.translateToLocal("lotr.gui.fellowships.toggleMapShow"));
		buttonList.add(buttonMapShow);
		buttonOp = new GuiButton(18, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.op"));
		buttonList.add(buttonOp);
		buttonDeop = new GuiButton(19, midX - 100, guiTop + 170, 200, 20, StatCollector.translateToLocal("lotr.gui.fellowships.deop"));
		buttonList.add(buttonDeop);
		orderedFsOptionButtons.clear();
		orderedFsOptionButtons.add(buttonInvitePlayer);
		orderedFsOptionButtons.add(buttonDisband);
		orderedFsOptionButtons.add(buttonRename);
		orderedFsOptionButtons.add(buttonSetIcon);
		orderedFsOptionButtons.add(buttonMapShow);
		orderedFsOptionButtons.add(buttonPVP);
		orderedFsOptionButtons.add(buttonHiredFF);
		textFieldName = new GuiTextField(fontRendererObj, midX - 80, guiTop + 40, 160, 20);
		textFieldName.setMaxStringLength(40);
		textFieldPlayer = new GuiTextField(fontRendererObj, midX - 80, guiTop + 40, 160, 20);
		textFieldRename = new GuiTextField(fontRendererObj, midX - 80, guiTop + 40, 160, 20);
		textFieldRename.setMaxStringLength(40);
	}

	private void refreshFellowshipList() {
		allFellowshipsLeading.clear();
		allFellowshipsOther.clear();
		ArrayList<LOTRFellowshipClient> fellowships = new ArrayList<>(LOTRLevelData.getData(mc.thePlayer).getClientFellowships());
		for (LOTRFellowshipClient fs : fellowships) {
			if (fs.isOwned()) {
				allFellowshipsLeading.add(fs);
				continue;
			}
			allFellowshipsOther.add(fs);
		}
		allFellowshipInvites.clear();
		allFellowshipInvites.addAll(LOTRLevelData.getData(mc.thePlayer).getClientFellowshipInvites());
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		refreshFellowshipList();
		textFieldName.updateCursorCounter();
		if (page != Page.CREATE) {
			textFieldName.setText("");
		}
		textFieldPlayer.updateCursorCounter();
		if (page != Page.INVITE) {
			textFieldPlayer.setText("");
		}
		textFieldRename.updateCursorCounter();
		if (page != Page.RENAME) {
			textFieldRename.setText("");
		}
	}

	private void alignOptionButtons() {
		ArrayList<GuiButton> activeOptionButtons = new ArrayList<>();
		for (GuiButton button : orderedFsOptionButtons) {
			if (!button.visible) {
				continue;
			}
			activeOptionButtons.add(button);
		}
		if (buttonLeave.visible) {
			activeOptionButtons.add(buttonLeave);
		}
		int midX = guiLeft + xSize / 2;
		int numActive = activeOptionButtons.size();
		if (numActive > 0) {
			int gap = 8;
			int allWidth = 0;
			for (GuiButton button : activeOptionButtons) {
				if (allWidth > 0) {
					allWidth += gap;
				}
				allWidth += button.width;
			}
			int x = midX - allWidth / 2;
			for (GuiButton activeOptionButton : activeOptionButtons) {
				GuiButton button = activeOptionButton;
				button.xPosition = x;
				x += button.width;
				x += gap;
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		LOTRPlayerData playerData = LOTRLevelData.getData(mc.thePlayer);
		boolean viewingOwned = viewingFellowship != null && viewingFellowship.isOwned();
		boolean viewingAdminned = viewingFellowship != null && viewingFellowship.isAdminned();
		mouseOverFellowship = null;
		mouseOverPlayer = null;
		mouseOverPlayerRemove = false;
		mouseOverPlayerOp = false;
		mouseOverPlayerDeop = false;
		mouseOverPlayerTransfer = false;
		if (page != Page.REMOVE) {
			removingPlayer = null;
		}
		if (page != Page.OP) {
			oppingPlayer = null;
		}
		if (page != Page.DEOP) {
			deoppingPlayer = null;
		}
		if (page != Page.TRANSFER) {
			transferringPlayer = null;
		}
		mouseOverInviteAccept = false;
		mouseOverInviteReject = false;
		boolean creationEnabled = LOTRConfig.isFellowshipCreationEnabled(mc.theWorld);
		boolean canPlayerCreateNew = playerData.canCreateFellowships(true);
		buttonCreate.visible = page == Page.LIST;
		buttonCreate.enabled = buttonCreate.visible && creationEnabled && canPlayerCreateNew;
		buttonCreateThis.visible = page == Page.CREATE;
		String checkValidName = checkValidFellowshipName(textFieldName.getText());
		buttonCreateThis.enabled = buttonCreateThis.visible && checkValidName == null;
		buttonInvitePlayer.enabled = page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
		buttonInvitePlayer.visible = buttonInvitePlayer.enabled;
		buttonInviteThis.visible = page == Page.INVITE;
		String checkValidPlayer = checkValidPlayerName(textFieldPlayer.getText());
		buttonInviteThis.enabled = buttonInviteThis.visible && checkValidPlayer == null;
		buttonDisband.enabled = page == Page.FELLOWSHIP && viewingOwned;
		buttonDisband.visible = buttonDisband.enabled;
		buttonDisbandThis.enabled = page == Page.DISBAND;
		buttonDisbandThis.visible = buttonDisbandThis.enabled;
		buttonLeave.enabled = page == Page.FELLOWSHIP && !viewingOwned;
		buttonLeave.visible = buttonLeave.enabled;
		buttonLeaveThis.enabled = page == Page.LEAVE;
		buttonLeaveThis.visible = buttonLeaveThis.enabled;
		buttonSetIcon.enabled = page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
		buttonSetIcon.visible = buttonSetIcon.enabled;
		buttonRemove.enabled = page == Page.REMOVE;
		buttonRemove.visible = buttonRemove.enabled;
		buttonTransfer.enabled = page == Page.TRANSFER;
		buttonTransfer.visible = buttonTransfer.enabled;
		buttonRename.enabled = page == Page.FELLOWSHIP && viewingOwned;
		buttonRename.visible = buttonRename.enabled;
		buttonRenameThis.visible = page == Page.RENAME;
		String checkValidRename = checkValidFellowshipName(textFieldRename.getText());
		buttonRenameThis.enabled = buttonRenameThis.visible && checkValidRename == null;
		buttonBack.enabled = page != Page.LIST;
		buttonBack.visible = buttonBack.enabled;
		buttonInvites.enabled = page == Page.LIST;
		buttonInvites.visible = buttonInvites.enabled;
		buttonPVP.enabled = page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
		buttonPVP.visible = buttonPVP.enabled;
		if (buttonPVP.enabled) {
			buttonPVP.setIconUV(64, viewingFellowship.getPreventPVP() ? 80 : 48);
		}
		buttonHiredFF.enabled = page == Page.FELLOWSHIP && (viewingOwned || viewingAdminned);
		buttonHiredFF.visible = buttonHiredFF.enabled;
		if (buttonHiredFF.enabled) {
			buttonHiredFF.setIconUV(80, viewingFellowship.getPreventHiredFriendlyFire() ? 80 : 48);
		}
		buttonMapShow.enabled = page == Page.FELLOWSHIP && viewingOwned;
		buttonMapShow.visible = buttonMapShow.enabled;
		if (buttonMapShow.enabled) {
			buttonMapShow.setIconUV(96, viewingFellowship.getShowMapLocations() ? 48 : 80);
		}
		buttonOp.enabled = page == Page.OP;
		buttonOp.visible = buttonOp.enabled;
		buttonDeop.enabled = page == Page.DEOP;
		buttonDeop.visible = buttonDeop.enabled;
		alignOptionButtons();
		setupScrollBars(i, j);
		drawDefaultBackground();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		super.drawScreen(i, j, f);
		String s = StatCollector.translateToLocal("lotr.gui.fellowships.title");
		this.drawCenteredString(s, guiLeft + xSize / 2, guiTop - 30, 16777215);
		if (page == Page.LIST) {
			int x = guiLeft;
			int y = scrollPaneLeading.paneY0;
			s = StatCollector.translateToLocal("lotr.gui.fellowships.leading");
			this.drawCenteredString(s, guiLeft + xSize / 2, y, 16777215);
			y += fontRendererObj.FONT_HEIGHT + 10;
			List<LOTRFellowshipClient> sortedLeading = sortFellowshipsForDisplay(allFellowshipsLeading);
			int[] leadingMinMax = scrollPaneLeading.getMinMaxIndices(sortedLeading, displayedFellowshipsLeading);
			for (int index = leadingMinMax[0]; index <= leadingMinMax[1]; ++index) {
				LOTRFellowshipClient fs = sortedLeading.get(index);
				this.drawFellowshipEntry(fs, x, y, i, j, false);
				y += fontRendererObj.FONT_HEIGHT + 5;
			}
			y = scrollPaneOther.paneY0;
			s = StatCollector.translateToLocal("lotr.gui.fellowships.member");
			this.drawCenteredString(s, guiLeft + xSize / 2, y, 16777215);
			y += fontRendererObj.FONT_HEIGHT + 10;
			List<LOTRFellowshipClient> sortedOther = sortFellowshipsForDisplay(allFellowshipsOther);
			int[] otherMinMax = scrollPaneOther.getMinMaxIndices(sortedOther, displayedFellowshipsOther);
			for (int index = otherMinMax[0]; index <= otherMinMax[1]; ++index) {
				LOTRFellowshipClient fs = sortedOther.get(index);
				this.drawFellowshipEntry(fs, x, y, i, j, false);
				y += fontRendererObj.FONT_HEIGHT + 5;
			}
			String invites = String.valueOf(playerData.getClientFellowshipInvites().size());
			int invitesX = buttonInvites.xPosition - 2 - fontRendererObj.getStringWidth(invites);
			int invitesY = buttonInvites.yPosition + buttonInvites.height / 2 - fontRendererObj.FONT_HEIGHT / 2;
			fontRendererObj.drawString(invites, invitesX, invitesY, 16777215);
			if (buttonInvites.func_146115_a()) {
				renderIconTooltip(i, j, StatCollector.translateToLocal("lotr.gui.fellowships.invitesTooltip"));
			}
			if (buttonCreate.func_146115_a()) {
				if (!creationEnabled) {
					s = StatCollector.translateToLocal("lotr.gui.fellowships.creationDisabled");
					this.drawCenteredString(s, guiLeft + xSize / 2, buttonCreate.yPosition + buttonCreate.height + 4, 16777215);
				} else if (!canPlayerCreateNew) {
					s = StatCollector.translateToLocal("lotr.gui.fellowships.createLimit");
					this.drawCenteredString(s, guiLeft + xSize / 2, buttonCreate.yPosition + buttonCreate.height + 4, 16777215);
				}
			}
			if (scrollPaneLeading.hasScrollBar) {
				scrollPaneLeading.drawScrollBar();
			}
			if (scrollPaneOther.hasScrollBar) {
				scrollPaneOther.drawScrollBar();
			}
		} else if (page == Page.CREATE) {
			s = StatCollector.translateToLocal("lotr.gui.fellowships.createName");
			this.drawCenteredString(s, guiLeft + xSize / 2, textFieldName.yPosition - 4 - fontRendererObj.FONT_HEIGHT, 16777215);
			textFieldName.drawTextBox();
			if (checkValidName != null) {
				this.drawCenteredString(checkValidName, guiLeft + xSize / 2, textFieldName.yPosition + textFieldName.height + fontRendererObj.FONT_HEIGHT, 16711680);
			}
		} else if (page == Page.FELLOWSHIP) {
			int x = guiLeft;
			int y = guiTop + 10;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.nameAndPlayers", viewingFellowship.getName(), viewingFellowship.getMemberCount());
			this.drawCenteredString(s, guiLeft + xSize / 2, y, 16777215);
			y += fontRendererObj.FONT_HEIGHT;
			y += 5;
			if (viewingFellowship.getIcon() != null) {
				drawFellowshipIcon(viewingFellowship, guiLeft + xSize / 2 - 8, y, 1.0f);
			}
			boolean preventPVP = viewingFellowship.getPreventPVP();
			boolean preventHiredFF = viewingFellowship.getPreventHiredFriendlyFire();
			boolean mapShow = viewingFellowship.getShowMapLocations();
			int iconPVPX = guiLeft + xSize - 36;
			int iconHFFX = guiLeft + xSize - 16;
			int iconMapX = guiLeft + xSize - 56;
			int iconY = y;
			int iconSize = 16;
			mc.getTextureManager().bindTexture(iconsTextures);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(iconPVPX, iconY, 64, preventPVP ? 80 : 48, iconSize, iconSize);
			this.drawTexturedModalRect(iconHFFX, iconY, 80, preventHiredFF ? 80 : 48, iconSize, iconSize);
			this.drawTexturedModalRect(iconMapX, iconY, 96, mapShow ? 48 : 80, iconSize, iconSize);
			if (i >= iconPVPX && i < iconPVPX + iconSize && j >= iconY && j < iconY + iconSize) {
				renderIconTooltip(i, j, StatCollector.translateToLocal(preventPVP ? "lotr.gui.fellowships.pvp.prevent" : "lotr.gui.fellowships.pvp.allow"));
			}
			if (i >= iconHFFX && i < iconHFFX + iconSize && j >= iconY && j < iconY + iconSize) {
				renderIconTooltip(i, j, StatCollector.translateToLocal(preventHiredFF ? "lotr.gui.fellowships.hiredFF.prevent" : "lotr.gui.fellowships.hiredFF.allow"));
			}
			if (i >= iconMapX && i < iconMapX + iconSize && j >= iconY && j < iconY + iconSize) {
				renderIconTooltip(i, j, StatCollector.translateToLocal(mapShow ? "lotr.gui.fellowships.mapShow.on" : "lotr.gui.fellowships.mapShow.off"));
			}
			y += iconSize;
			y += 10;
			int titleOffset = 0;
			for (String name : viewingFellowship.getAllPlayerNames()) {
				LOTRTitle.PlayerTitle title = viewingFellowship.getTitleFor(name);
				if (title == null) {
					continue;
				}
				String titleName = title.getFormattedTitle(mc.thePlayer);
				int thisTitleWidth = fontRendererObj.getStringWidth(titleName + " ");
				titleOffset = Math.max(titleOffset, thisTitleWidth);
			}
			drawPlayerEntry(viewingFellowship.getOwnerName(), x, y, titleOffset, i, j);
			y += fontRendererObj.FONT_HEIGHT + 10;
			List<String> membersSorted = sortMemberNamesForDisplay(viewingFellowship);
			int[] membersMinMax = scrollPaneMembers.getMinMaxIndices(membersSorted, displayedMembers);
			for (int index = membersMinMax[0]; index <= membersMinMax[1]; ++index) {
				String name = membersSorted.get(index);
				drawPlayerEntry(name, x, y, titleOffset, i, j);
				y += fontRendererObj.FONT_HEIGHT + 5;
			}
			for (Object bObj : buttonList) {
				GuiButton button = (GuiButton) bObj;
				if (!(button instanceof LOTRGuiButtonFsOption) || !button.visible || !button.func_146115_a()) {
					continue;
				}
				s = button.displayString;
				this.drawCenteredString(s, guiLeft + xSize / 2, button.yPosition + button.height + 4, 16777215);
			}
			if (scrollPaneMembers.hasScrollBar) {
				scrollPaneMembers.drawScrollBar();
			}
		} else if (page == Page.INVITE) {
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.inviteName", viewingFellowship.getName());
			this.drawCenteredString(s, guiLeft + xSize / 2, textFieldPlayer.yPosition - 4 - fontRendererObj.FONT_HEIGHT, 16777215);
			textFieldPlayer.drawTextBox();
			if (checkValidPlayer != null) {
				this.drawCenteredString(checkValidPlayer, guiLeft + xSize / 2, textFieldPlayer.yPosition + textFieldPlayer.height + fontRendererObj.FONT_HEIGHT, 16711680);
			}
		} else if (page == Page.DISBAND) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.disbandCheck1", viewingFellowship.getName());
			this.drawCenteredString(s, x, y, 16777215);
			s = StatCollector.translateToLocal("lotr.gui.fellowships.disbandCheck2");
			this.drawCenteredString(s, x, y += fontRendererObj.FONT_HEIGHT, 16777215);
			s = StatCollector.translateToLocal("lotr.gui.fellowships.disbandCheck3");
			this.drawCenteredString(s, x, y += fontRendererObj.FONT_HEIGHT * 2, 16777215);
			y += fontRendererObj.FONT_HEIGHT;
		} else if (page == Page.LEAVE) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.leaveCheck1", viewingFellowship.getName());
			this.drawCenteredString(s, x, y, 16777215);
			s = StatCollector.translateToLocal("lotr.gui.fellowships.leaveCheck2");
			this.drawCenteredString(s, x, y += fontRendererObj.FONT_HEIGHT, 16777215);
			y += fontRendererObj.FONT_HEIGHT * 2;
		} else if (page == Page.REMOVE) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.removeCheck", viewingFellowship.getName(), removingPlayer);
			List<String> lines = fontRendererObj.listFormattedStringToWidth(s, xSize);
			for (String line : lines) {
				this.drawCenteredString(line, x, y, 16777215);
				y += fontRendererObj.FONT_HEIGHT;
			}
		} else if (page == Page.OP) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.opCheck1", viewingFellowship.getName(), oppingPlayer);
			List<String> lines = fontRendererObj.listFormattedStringToWidth(s, xSize);
			for (String line : lines) {
				this.drawCenteredString(line, x, y, 16777215);
				y += fontRendererObj.FONT_HEIGHT;
			}
			y += fontRendererObj.FONT_HEIGHT;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.opCheck2", viewingFellowship.getName(), oppingPlayer);
			lines = fontRendererObj.listFormattedStringToWidth(s, xSize);
			for (String line : lines) {
				this.drawCenteredString(line, x, y, 16777215);
				y += fontRendererObj.FONT_HEIGHT;
			}
		} else if (page == Page.DEOP) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.deopCheck", viewingFellowship.getName(), deoppingPlayer);
			List<String> lines = fontRendererObj.listFormattedStringToWidth(s, xSize);
			for (String line : lines) {
				this.drawCenteredString(line, x, y, 16777215);
				y += fontRendererObj.FONT_HEIGHT;
			}
		} else if (page == Page.TRANSFER) {
			int x = guiLeft + xSize / 2;
			int y = guiTop + 30;
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.transferCheck1", viewingFellowship.getName(), transferringPlayer);
			List<String> lines = fontRendererObj.listFormattedStringToWidth(s, xSize);
			for (String line : lines) {
				this.drawCenteredString(line, x, y, 16777215);
				y += fontRendererObj.FONT_HEIGHT;
			}
			s = StatCollector.translateToLocal("lotr.gui.fellowships.transferCheck2");
			this.drawCenteredString(s, x, y += fontRendererObj.FONT_HEIGHT, 16777215);
			y += fontRendererObj.FONT_HEIGHT;
		} else if (page == Page.RENAME) {
			s = StatCollector.translateToLocalFormatted("lotr.gui.fellowships.renameName", viewingFellowship.getName());
			this.drawCenteredString(s, guiLeft + xSize / 2, textFieldRename.yPosition - 4 - fontRendererObj.FONT_HEIGHT, 16777215);
			textFieldRename.drawTextBox();
			if (checkValidRename != null) {
				this.drawCenteredString(checkValidRename, guiLeft + xSize / 2, textFieldRename.yPosition + textFieldRename.height + fontRendererObj.FONT_HEIGHT, 16711680);
			}
		} else if (page == Page.INVITATIONS) {
			int x = guiLeft;
			int y = guiTop + 10;
			s = StatCollector.translateToLocal("lotr.gui.fellowships.invites");
			this.drawCenteredString(s, guiLeft + xSize / 2, y, 16777215);
			y += fontRendererObj.FONT_HEIGHT + 10;
			if (allFellowshipInvites.isEmpty()) {
				s = StatCollector.translateToLocal("lotr.gui.fellowships.invitesNone");
				this.drawCenteredString(s, guiLeft + xSize / 2, y += fontRendererObj.FONT_HEIGHT, 16777215);
			} else {
				int[] invitesMinMax = scrollPaneInvites.getMinMaxIndices(allFellowshipInvites, displayedInvites);
				for (int index = invitesMinMax[0]; index <= invitesMinMax[1]; ++index) {
					LOTRFellowshipClient fs = allFellowshipInvites.get(index);
					this.drawFellowshipEntry(fs, x, y, i, j, true);
					y += fontRendererObj.FONT_HEIGHT + 5;
				}
			}
			if (scrollPaneInvites.hasScrollBar) {
				scrollPaneInvites.drawScrollBar();
			}
		}
	}

	private void drawFellowshipEntry(LOTRFellowshipClient fs, int x, int y, int mouseX, int mouseY, boolean isInvite) {
		this.drawFellowshipEntry(fs, x, y, mouseX, mouseY, isInvite, xSize);
	}

	public void drawFellowshipEntry(LOTRFellowshipClient fs, int x, int y, int mouseX, int mouseY, boolean isInvite, int selectWidth) {
		int selectX0 = x - 2;
		int selectX1 = x + selectWidth + 2;
		int selectY0 = y - 2;
		int selectY1 = y + fontRendererObj.FONT_HEIGHT + 2;
		if (mouseX >= selectX0 && mouseX <= selectX1 && mouseY >= selectY0 && mouseY <= selectY1) {
			Gui.drawRect(selectX0, selectY0, selectX1, selectY1, 1442840575);
			mouseOverFellowship = fs;
		}
		boolean isMouseOver = mouseOverFellowship == fs;
		drawFellowshipIcon(fs, x, y, 0.5f);
		String fsName = fs.getName();
		int maxLength = 110;
		if (fontRendererObj.getStringWidth(fsName) > maxLength) {
			String ellipsis = "...";
			while (fontRendererObj.getStringWidth(fsName + ellipsis) > maxLength) {
				fsName = fsName.substring(0, fsName.length() - 1);
			}
			fsName = fsName + ellipsis;
		}
		String ownerName = fs.getOwnerName();
		boolean ownerOnline = LOTRGuiFellowships.isPlayerOnline(ownerName);
		fontRendererObj.drawString(fsName, x + 15, y, 16777215);
		fontRendererObj.drawString(ownerName, x + 130, y, ownerOnline ? 16777215 : isMouseOver ? 12303291 : 7829367);
		if (isInvite) {
			int iconWidth = 8;
			int iconAcceptX = x + xSize - 18;
			int iconRejectX = x + xSize - 8;
			boolean accept = false;
			boolean reject = false;
			if (isMouseOver) {
				accept = mouseOverInviteAccept = mouseX >= iconAcceptX && mouseX <= iconAcceptX + iconWidth && mouseY >= y && mouseY <= y + iconWidth;
				reject = mouseOverInviteReject = mouseX >= iconRejectX && mouseX <= iconRejectX + iconWidth && mouseY >= y && mouseY <= y + iconWidth;
			}
			mc.getTextureManager().bindTexture(iconsTextures);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(iconAcceptX, y, 16, 16 + (accept ? 0 : iconWidth), iconWidth, iconWidth);
			this.drawTexturedModalRect(iconRejectX, y, 8, 16 + (reject ? 0 : iconWidth), iconWidth, iconWidth);
		} else {
			String memberCount = String.valueOf(fs.getMemberCount());
			String onlineMemberCount = String.valueOf(countOnlineMembers(fs)) + " | ";
			fontRendererObj.drawString(memberCount, x + xSize - fontRendererObj.getStringWidth(memberCount), y, isMouseOver ? 12303291 : 7829367);
			fontRendererObj.drawString(onlineMemberCount, x + xSize - fontRendererObj.getStringWidth(memberCount) - fontRendererObj.getStringWidth(onlineMemberCount), y, 16777215);
		}
	}

	private void drawPlayerEntry(String player, int x, int y, int titleOffset, int mouseX, int mouseY) {
		int selectX0 = x - 2;
		int selectX1 = x + xSize + 2;
		int selectY0 = y - 2;
		int selectY1 = y + fontRendererObj.FONT_HEIGHT + 2;
		if (mouseX >= selectX0 && mouseX <= selectX1 && mouseY >= selectY0 && mouseY <= selectY1) {
			Gui.drawRect(selectX0, selectY0, selectX1, selectY1, 1442840575);
			mouseOverPlayer = player;
		}
		boolean isMouseOver = mouseOverPlayer == player;
		String titleName = null;
		LOTRTitle.PlayerTitle title = viewingFellowship.getTitleFor(player);
		if (title != null) {
			titleName = title.getFormattedTitle(mc.thePlayer);
		}
		if (titleName != null) {
			fontRendererObj.drawString(titleName, x, y, 16777215);
		}
		fontRendererObj.drawString(player, x + titleOffset, y, LOTRGuiFellowships.isPlayerOnline(player) ? 16777215 : isMouseOver ? 12303291 : 7829367);
		boolean isOwner = viewingFellowship.getOwnerName().equals(player);
		boolean isAdmin = viewingFellowship.isAdmin(player);
		if (isOwner) {
			mc.getTextureManager().bindTexture(iconsTextures);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(x + titleOffset + fontRendererObj.getStringWidth(player + " "), y, 0, 0, 8, 8);
		} else if (isAdmin) {
			mc.getTextureManager().bindTexture(iconsTextures);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(x + titleOffset + fontRendererObj.getStringWidth(player + " "), y, 8, 0, 8, 8);
		}
		boolean owned = viewingFellowship.isOwned();
		boolean adminned = viewingFellowship.isAdminned();
		if (!isOwner && (owned || adminned)) {
			int iconWidth = 8;
			int iconRemoveX = x + xSize - 28;
			int iconOpDeopX = x + xSize - 18;
			int iconTransferX = x + xSize - 8;
			if (adminned) {
				iconRemoveX = x + xSize - 8;
			}
			boolean remove = false;
			boolean opDeop = false;
			boolean transfer = false;
			if (isMouseOver) {
				remove = mouseOverPlayerRemove = mouseX >= iconRemoveX && mouseX <= iconRemoveX + iconWidth && mouseY >= y && mouseY <= y + iconWidth;
				if (owned) {
					opDeop = isAdmin ? (mouseOverPlayerDeop = mouseX >= iconOpDeopX && mouseX <= iconOpDeopX + iconWidth && mouseY >= y && mouseY <= y + iconWidth) : (mouseOverPlayerOp = mouseX >= iconOpDeopX && mouseX <= iconOpDeopX + iconWidth && mouseY >= y && mouseY <= y + iconWidth);
					transfer = mouseOverPlayerTransfer = mouseX >= iconTransferX && mouseX <= iconTransferX + iconWidth && mouseY >= y && mouseY <= y + iconWidth;
				}
			}
			mc.getTextureManager().bindTexture(iconsTextures);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			this.drawTexturedModalRect(iconRemoveX, y, 8, 16 + (remove ? 0 : iconWidth), iconWidth, iconWidth);
			if (owned) {
				if (isAdmin) {
					this.drawTexturedModalRect(iconOpDeopX, y, 32, 16 + (opDeop ? 0 : iconWidth), iconWidth, iconWidth);
				} else {
					this.drawTexturedModalRect(iconOpDeopX, y, 24, 16 + (opDeop ? 0 : iconWidth), iconWidth, iconWidth);
				}
				this.drawTexturedModalRect(iconTransferX, y, 0, 16 + (transfer ? 0 : iconWidth), iconWidth, iconWidth);
			}
		}
	}

	private void drawFellowshipIcon(LOTRFellowshipClient fsClient, int x, int y, float scale) {
		ItemStack fsIcon = fsClient.getIcon();
		if (fsIcon != null) {
			GL11.glDisable(3042);
			GL11.glDisable(3008);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glDisable(2896);
			GL11.glEnable(32826);
			GL11.glEnable(2896);
			GL11.glEnable(2884);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glPushMatrix();
			GL11.glScalef(scale, scale, 1.0f);
			renderItem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), fsIcon, Math.round(x / scale), Math.round(y / scale));
			GL11.glPopMatrix();
			GL11.glDisable(2896);
		}
	}

	private void renderIconTooltip(int x, int y, String s) {
		float z = zLevel;
		int stringWidth = 200;
		List desc = fontRendererObj.listFormattedStringToWidth(s, stringWidth);
		func_146283_a(desc, x, y);
		GL11.glDisable(2896);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		zLevel = z;
	}

	public static boolean isPlayerOnline(String player) {
		EntityClientPlayerMP mcPlayer = Minecraft.getMinecraft().thePlayer;
		List list = mcPlayer.sendQueue.playerInfoList;
		for (Object obj : list) {
			GuiPlayerInfo info = (GuiPlayerInfo) obj;
			if (!info.name.equalsIgnoreCase(player)) {
				continue;
			}
			return true;
		}
		return false;
	}

	private int countOnlineMembers(LOTRFellowshipClient fs) {
		int i = 0;
		ArrayList<String> allPlayers = new ArrayList<>(fs.getAllPlayerNames());
		for (String player : allPlayers) {
			if (!LOTRGuiFellowships.isPlayerOnline(player)) {
				continue;
			}
			++i;
		}
		return i;
	}

	private List<LOTRFellowshipClient> sortFellowshipsForDisplay(List<LOTRFellowshipClient> list) {
		ArrayList<LOTRFellowshipClient> sorted = new ArrayList<>(list);
		Collections.sort(sorted, new Comparator<LOTRFellowshipClient>() {

			@Override
			public int compare(LOTRFellowshipClient fs1, LOTRFellowshipClient fs2) {
				int count2;
				int count1 = fs1.getMemberCount();
				if (count1 == (count2 = fs2.getMemberCount())) {
					return fs1.getName().toLowerCase().compareTo(fs2.getName().toLowerCase());
				}
				return -Integer.valueOf(count1).compareTo(count2);
			}
		});
		return sorted;
	}

	private List<String> sortMemberNamesForDisplay(final LOTRFellowshipClient fs) {
		ArrayList<String> members = new ArrayList<>(fs.getMemberNames());
		Collections.sort(members, new Comparator<String>() {

			@Override
			public int compare(String player1, String player2) {
				boolean online2;
				boolean admin1 = fs.isAdmin(player1);
				boolean admin2 = fs.isAdmin(player2);
				boolean online1 = LOTRGuiFellowships.isPlayerOnline(player1);
				if (online1 == (online2 = LOTRGuiFellowships.isPlayerOnline(player2))) {
					if (admin1 == admin2) {
						return player1.toLowerCase().compareTo(player2.toLowerCase());
					}
					if (admin1 && !admin2) {
						return -1;
					}
					if (!admin1 && admin2) {
						return 1;
					}
				} else {
					if (online1 && !online2) {
						return -1;
					}
					if (!online1 && online2) {
						return 1;
					}
				}
				return 0;
			}
		});
		return members;
	}

	@Override
	protected void keyTyped(char c, int i) {
		if (page == Page.CREATE && textFieldName.textboxKeyTyped(c, i)) {
			return;
		}
		if (page == Page.INVITE && textFieldPlayer.textboxKeyTyped(c, i)) {
			return;
		}
		if (page == Page.RENAME && textFieldRename.textboxKeyTyped(c, i)) {
			return;
		}
		if (page != Page.LIST) {
			if (i == 1 || i == mc.gameSettings.keyBindInventory.getKeyCode()) {
				page = page == Page.INVITE || page == Page.DISBAND || page == Page.LEAVE || page == Page.REMOVE || page == Page.OP || page == Page.DEOP || page == Page.TRANSFER || page == Page.RENAME ? Page.FELLOWSHIP : Page.LIST;
			}
		} else {
			super.keyTyped(c, i);
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		LOTRPacketFellowshipRespondInvite packet;
		super.mouseClicked(i, j, k);
		if (page == Page.LIST && mouseOverFellowship != null) {
			buttonSound();
			page = Page.FELLOWSHIP;
			viewingFellowship = mouseOverFellowship;
		}
		if (page == Page.CREATE) {
			textFieldName.mouseClicked(i, j, k);
		}
		if (page == Page.INVITE) {
			textFieldPlayer.mouseClicked(i, j, k);
		}
		if (page == Page.RENAME) {
			textFieldRename.mouseClicked(i, j, k);
		}
		if (page == Page.FELLOWSHIP && mouseOverPlayer != null && mouseOverPlayerRemove) {
			buttonSound();
			page = Page.REMOVE;
			removingPlayer = mouseOverPlayer;
		}
		if (page == Page.FELLOWSHIP && mouseOverPlayer != null && mouseOverPlayerOp) {
			buttonSound();
			page = Page.OP;
			oppingPlayer = mouseOverPlayer;
		}
		if (page == Page.FELLOWSHIP && mouseOverPlayer != null && mouseOverPlayerDeop) {
			buttonSound();
			page = Page.DEOP;
			deoppingPlayer = mouseOverPlayer;
		}
		if (page == Page.FELLOWSHIP && mouseOverPlayer != null && mouseOverPlayerTransfer) {
			buttonSound();
			page = Page.TRANSFER;
			transferringPlayer = mouseOverPlayer;
		}
		if (page == Page.INVITATIONS && mouseOverFellowship != null && mouseOverInviteAccept) {
			buttonSound();
			packet = new LOTRPacketFellowshipRespondInvite(mouseOverFellowship, true);
			LOTRPacketHandler.networkWrapper.sendToServer(packet);
			mouseOverFellowship = null;
		}
		if (page == Page.INVITATIONS && mouseOverFellowship != null && mouseOverInviteReject) {
			buttonSound();
			packet = new LOTRPacketFellowshipRespondInvite(mouseOverFellowship, false);
			LOTRPacketHandler.networkWrapper.sendToServer(packet);
			mouseOverFellowship = null;
		}
	}

	private void buttonSound() {
		buttonBack.func_146113_a(mc.getSoundHandler());
	}

	private void setupScrollBars(int i, int j) {
		if (page == Page.LIST) {
			displayedFellowshipsLeading = allFellowshipsLeading.size();
			displayedFellowshipsOther = allFellowshipsOther.size();
			scrollPaneLeading.hasScrollBar = false;
			scrollPaneOther.hasScrollBar = false;
			while (displayedFellowshipsLeading + displayedFellowshipsOther > 12) {
				if (displayedFellowshipsOther >= displayedFellowshipsLeading) {
					--displayedFellowshipsOther;
					scrollPaneOther.hasScrollBar = true;
					continue;
				}
				--displayedFellowshipsLeading;
				scrollPaneLeading.hasScrollBar = true;
			}
			scrollPaneLeading.paneX0 = guiLeft;
			scrollPaneLeading.scrollBarX0 = guiLeft + scrollBarX;
			scrollPaneLeading.paneY0 = guiTop + 10;
			scrollPaneLeading.paneY1 = scrollPaneLeading.paneY0 + fontRendererObj.FONT_HEIGHT + 10 + (fontRendererObj.FONT_HEIGHT + 5) * displayedFellowshipsLeading;
			scrollPaneLeading.mouseDragScroll(i, j);
			scrollPaneOther.paneX0 = guiLeft;
			scrollPaneOther.scrollBarX0 = guiLeft + scrollBarX;
			scrollPaneOther.paneY0 = scrollPaneLeading.paneY1 + 5;
			scrollPaneOther.paneY1 = scrollPaneOther.paneY0 + fontRendererObj.FONT_HEIGHT + 10 + (fontRendererObj.FONT_HEIGHT + 5) * displayedFellowshipsOther;
			scrollPaneOther.mouseDragScroll(i, j);
		}
		if (page == Page.FELLOWSHIP) {
			displayedMembers = viewingFellowship.getMemberNames().size();
			scrollPaneMembers.hasScrollBar = false;
			if (displayedMembers > 11) {
				displayedMembers = 11;
				scrollPaneMembers.hasScrollBar = true;
			}
			scrollPaneMembers.paneX0 = guiLeft;
			scrollPaneMembers.scrollBarX0 = guiLeft + scrollBarX;
			scrollPaneMembers.paneY0 = guiTop + 10 + fontRendererObj.FONT_HEIGHT + 5 + 16 + 10 + fontRendererObj.FONT_HEIGHT + 10;
			scrollPaneMembers.paneY1 = scrollPaneMembers.paneY0 + (fontRendererObj.FONT_HEIGHT + 5) * displayedMembers;
			scrollPaneMembers.mouseDragScroll(i, j);
		} else {
			scrollPaneMembers.hasScrollBar = false;
			scrollPaneMembers.mouseDragScroll(i, j);
		}
		if (page == Page.INVITATIONS) {
			displayedInvites = allFellowshipInvites.size();
			scrollPaneInvites.hasScrollBar = false;
			if (displayedInvites > 15) {
				displayedInvites = 15;
				scrollPaneInvites.hasScrollBar = true;
			}
			scrollPaneInvites.paneX0 = guiLeft;
			scrollPaneInvites.scrollBarX0 = guiLeft + scrollBarX;
			scrollPaneInvites.paneY0 = guiTop + 10 + fontRendererObj.FONT_HEIGHT + 10;
			scrollPaneInvites.paneY1 = scrollPaneInvites.paneY0 + (fontRendererObj.FONT_HEIGHT + 5) * displayedInvites;
			scrollPaneInvites.mouseDragScroll(i, j);
		}
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		int k = Mouse.getEventDWheel();
		if (k != 0) {
			int l;
			k = Integer.signum(k);
			if (page == Page.LIST) {
				if (scrollPaneLeading.hasScrollBar && scrollPaneLeading.mouseOver) {
					l = allFellowshipsLeading.size() - displayedFellowshipsLeading;
					scrollPaneLeading.mouseWheelScroll(k, l);
				}
				if (scrollPaneOther.hasScrollBar && scrollPaneOther.mouseOver) {
					l = allFellowshipsOther.size() - displayedFellowshipsOther;
					scrollPaneOther.mouseWheelScroll(k, l);
				}
			}
			if (page == Page.FELLOWSHIP && scrollPaneMembers.hasScrollBar && scrollPaneMembers.mouseOver) {
				l = viewingFellowship.getMemberNames().size() - displayedMembers;
				scrollPaneMembers.mouseWheelScroll(k, l);
			}
			if (page == Page.INVITATIONS && scrollPaneInvites.hasScrollBar && scrollPaneInvites.mouseOver) {
				l = allFellowshipInvites.size() - displayedInvites;
				scrollPaneInvites.mouseWheelScroll(k, l);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == buttonCreate) {
				page = Page.CREATE;
			} else if (button == buttonCreateThis) {
				String name = textFieldName.getText();
				if (checkValidFellowshipName(name) == null) {
					name = StringUtils.trim(name);
					LOTRPacketFellowshipCreate packet = new LOTRPacketFellowshipCreate(name);
					LOTRPacketHandler.networkWrapper.sendToServer(packet);
				}
				page = Page.LIST;
			} else if (button == buttonInvitePlayer) {
				page = Page.INVITE;
			} else if (button == buttonInviteThis) {
				String name = textFieldPlayer.getText();
				if (checkValidPlayerName(name) == null) {
					name = StringUtils.trim(name);
					LOTRPacketFellowshipDoPlayer packet = new LOTRPacketFellowshipDoPlayer(viewingFellowship, name, LOTRPacketFellowshipDoPlayer.PlayerFunction.INVITE);
					LOTRPacketHandler.networkWrapper.sendToServer(packet);
				}
				page = Page.FELLOWSHIP;
			} else if (button == buttonDisband) {
				page = Page.DISBAND;
			} else if (button == buttonDisbandThis) {
				LOTRPacketFellowshipDisband packet = new LOTRPacketFellowshipDisband(viewingFellowship);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.LIST;
			} else if (button == buttonLeave) {
				page = Page.LEAVE;
			} else if (button == buttonLeaveThis) {
				LOTRPacketFellowshipLeave packet = new LOTRPacketFellowshipLeave(viewingFellowship);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.LIST;
			} else if (button == buttonSetIcon) {
				LOTRPacketFellowshipSetIcon packet = new LOTRPacketFellowshipSetIcon(viewingFellowship);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
			} else if (button == buttonRemove) {
				LOTRPacketFellowshipDoPlayer packet = new LOTRPacketFellowshipDoPlayer(viewingFellowship, removingPlayer, LOTRPacketFellowshipDoPlayer.PlayerFunction.REMOVE);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.FELLOWSHIP;
			} else if (button == buttonOp) {
				LOTRPacketFellowshipDoPlayer packet = new LOTRPacketFellowshipDoPlayer(viewingFellowship, oppingPlayer, LOTRPacketFellowshipDoPlayer.PlayerFunction.OP);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.FELLOWSHIP;
			} else if (button == buttonDeop) {
				LOTRPacketFellowshipDoPlayer packet = new LOTRPacketFellowshipDoPlayer(viewingFellowship, deoppingPlayer, LOTRPacketFellowshipDoPlayer.PlayerFunction.DEOP);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.FELLOWSHIP;
			} else if (button == buttonTransfer) {
				LOTRPacketFellowshipDoPlayer packet = new LOTRPacketFellowshipDoPlayer(viewingFellowship, transferringPlayer, LOTRPacketFellowshipDoPlayer.PlayerFunction.TRANSFER);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
				page = Page.FELLOWSHIP;
			} else if (button == buttonRename) {
				page = Page.RENAME;
			} else if (button == buttonRenameThis) {
				String name = textFieldRename.getText();
				if (checkValidFellowshipName(name) == null) {
					name = StringUtils.trim(name);
					LOTRPacketFellowshipRename packet = new LOTRPacketFellowshipRename(viewingFellowship, name);
					LOTRPacketHandler.networkWrapper.sendToServer(packet);
				}
				page = Page.FELLOWSHIP;
			} else if (button == buttonBack) {
				keyTyped('E', 1);
			} else if (button == buttonInvites) {
				page = Page.INVITATIONS;
			} else if (button == buttonPVP) {
				LOTRPacketFellowshipToggle packet = new LOTRPacketFellowshipToggle(viewingFellowship, LOTRPacketFellowshipToggle.ToggleFunction.PVP);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
			} else if (button == buttonHiredFF) {
				LOTRPacketFellowshipToggle packet = new LOTRPacketFellowshipToggle(viewingFellowship, LOTRPacketFellowshipToggle.ToggleFunction.HIRED_FF);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
			} else if (button == buttonMapShow) {
				LOTRPacketFellowshipToggle packet = new LOTRPacketFellowshipToggle(viewingFellowship, LOTRPacketFellowshipToggle.ToggleFunction.MAP_SHOW);
				LOTRPacketHandler.networkWrapper.sendToServer(packet);
			} else {
				super.actionPerformed(button);
			}
		}
	}

	private String checkValidFellowshipName(String name) {
		if (!StringUtils.isWhitespace(name)) {
			if (LOTRLevelData.getData(mc.thePlayer).anyMatchingFellowshipNames(name, true)) {
				return StatCollector.translateToLocal("lotr.gui.fellowships.nameExists");
			}
			return null;
		}
		return "";
	}

	private String checkValidPlayerName(String name) {
		if (!StringUtils.isWhitespace(name)) {
			if (viewingFellowship.isPlayerIn(name)) {
				return StatCollector.translateToLocalFormatted("lotr.gui.fellowships.playerExists", name);
			}
			return null;
		}
		return "";
	}

	public LOTRFellowshipClient getMouseOverFellowship() {
		return mouseOverFellowship;
	}

	public void clearMouseOverFellowship() {
		mouseOverFellowship = null;
	}

	public enum Page {
		LIST, CREATE, FELLOWSHIP, INVITE, DISBAND, LEAVE, REMOVE, OP, DEOP, TRANSFER, RENAME, INVITATIONS;

	}

}
