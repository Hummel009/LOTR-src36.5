package lotr.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.network.*;
import lotr.common.tileentity.LOTRTileEntityMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class LOTRGuiMobSpawner extends LOTRGuiScreenBase {
	private static ResourceLocation guiTexture = new ResourceLocation("lotr:gui/mob_spawner.png");
	private World worldObj;
	private int posX;
	private int posY;
	private int posZ;
	private LOTRTileEntityMobSpawner tileEntity;
	private int xSize = 256;
	private int ySize = 256;
	private int guiLeft;
	private int guiTop;
	private ArrayList pageControls = new ArrayList();
	private ArrayList spawnerControls = new ArrayList();
	private ArrayList mobControls = new ArrayList();
	private int page;
	private String[] pageNames = new String[] { "Spawner Properties", "Entity Properties" };
	private GuiButton buttonPage;
	private GuiButton buttonRedstone;
	private LOTRGuiSlider sliderMinSpawnDelay;
	private LOTRGuiSlider sliderMaxSpawnDelay;
	private LOTRGuiSlider sliderNearbyMobLimit;
	private LOTRGuiSlider sliderNearbyMobRange;
	private LOTRGuiSlider sliderPlayerRange;
	private LOTRGuiSlider sliderMaxSpawnCount;
	private LOTRGuiSlider sliderXZRange;
	private LOTRGuiSlider sliderYRange;
	private GuiButton buttonMobType;
	private LOTRGuiSlider sliderMaxHealth;
	private LOTRGuiSlider sliderFollowRange;
	private LOTRGuiSlider sliderMoveSpeed;
	private LOTRGuiSlider sliderAttackDamage;
	private int active;
	private int minSpawnDelay;
	private int maxSpawnDelay;
	private int nearbyMobLimit;
	private int nearbyMobCheckRange;
	private int requiredPlayerRange;
	private int maxSpawnCount;
	private int maxSpawnRange;
	private int maxSpawnRangeVertical;
	private boolean spawnsPersistentNPCs;
	private int maxHealth;
	private int navigatorRange;
	private float moveSpeed;
	private float attackDamage;
	private boolean needsUpdate;
	private Class mobClass;
	private String mobName;

	public LOTRGuiMobSpawner(World world, int i, int j, int k) {
		worldObj = world;
		posX = i;
		posY = j;
		posZ = k;
		tileEntity = (LOTRTileEntityMobSpawner) worldObj.getTileEntity(posX, posY, posZ);
		syncTileEntityDataToGui();
	}

	private void syncTileEntityDataToGui() {
		active = tileEntity.active;
		spawnsPersistentNPCs = tileEntity.spawnsPersistentNPCs;
		minSpawnDelay = tileEntity.minSpawnDelay;
		maxSpawnDelay = tileEntity.maxSpawnDelay;
		nearbyMobLimit = tileEntity.nearbyMobLimit;
		nearbyMobCheckRange = tileEntity.nearbyMobCheckRange;
		requiredPlayerRange = tileEntity.requiredPlayerRange;
		maxSpawnCount = tileEntity.maxSpawnCount;
		maxSpawnRange = tileEntity.maxSpawnRange;
		maxSpawnRangeVertical = tileEntity.maxSpawnRangeVertical;
		maxHealth = tileEntity.maxHealth;
		navigatorRange = tileEntity.navigatorRange;
		moveSpeed = tileEntity.moveSpeed;
		attackDamage = tileEntity.attackDamage;
		mobClass = LOTREntities.getClassFromString(tileEntity.getEntityClassName());
		mobName = tileEntity.getEntityClassName();
	}

	private void sendGuiDataToClientTileEntity() {
		tileEntity.active = active;
		tileEntity.spawnsPersistentNPCs = spawnsPersistentNPCs;
		tileEntity.minSpawnDelay = minSpawnDelay;
		tileEntity.maxSpawnDelay = maxSpawnDelay;
		tileEntity.nearbyMobLimit = nearbyMobLimit;
		tileEntity.nearbyMobCheckRange = nearbyMobCheckRange;
		tileEntity.requiredPlayerRange = requiredPlayerRange;
		tileEntity.maxSpawnCount = maxSpawnCount;
		tileEntity.maxSpawnRange = maxSpawnRange;
		tileEntity.maxSpawnRangeVertical = maxSpawnRangeVertical;
		tileEntity.maxHealth = maxHealth;
		tileEntity.navigatorRange = navigatorRange;
		tileEntity.moveSpeed = moveSpeed;
		tileEntity.attackDamage = attackDamage;
	}

	private void sendGuiDataToServerTileEntity() {
		LOTRPacketMobSpawner packet = new LOTRPacketMobSpawner(posX, posY, posZ, mc.thePlayer.dimension);
		packet.active = active;
		packet.spawnsPersistentNPCs = spawnsPersistentNPCs;
		packet.minSpawnDelay = minSpawnDelay;
		packet.maxSpawnDelay = maxSpawnDelay;
		packet.nearbyMobLimit = nearbyMobLimit;
		packet.nearbyMobCheckRange = nearbyMobCheckRange;
		packet.requiredPlayerRange = requiredPlayerRange;
		packet.maxSpawnCount = maxSpawnCount;
		packet.maxSpawnRange = maxSpawnRange;
		packet.maxSpawnRangeVertical = maxSpawnRangeVertical;
		packet.maxHealth = maxHealth;
		packet.navigatorRange = navigatorRange;
		packet.moveSpeed = moveSpeed;
		packet.attackDamage = attackDamage;
		LOTRPacketHandler.networkWrapper.sendToServer(packet);
		needsUpdate = false;
	}

	@Override
	public void initGui() {
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
		buttonPage = new GuiButton(1, guiLeft + xSize / 2 + 2, guiTop + 52, 110, 20, pageNames[page]);
		pageControls.add(buttonPage);
		buttonRedstone = new GuiButton(0, guiLeft + xSize / 2 - 112, guiTop + 52, 110, 20, active == 2 ? "Redstone Activated" : active == 1 ? "Active" : "Inactive");
		spawnerControls.add(buttonRedstone);
		sliderMinSpawnDelay = new LOTRGuiSlider(1, guiLeft + xSize / 2 - 112, guiTop + 76, 224, 20, "Min Spawn Delay");
		spawnerControls.add(sliderMinSpawnDelay);
		sliderMinSpawnDelay.setMinutesSecondsTime();
		sliderMinSpawnDelay.setMinMaxValues(0, 60);
		sliderMinSpawnDelay.setSliderValue(minSpawnDelay / 20);
		sliderMaxSpawnDelay = new LOTRGuiSlider(2, guiLeft + xSize / 2 - 112, guiTop + 100, 224, 20, "Max Spawn Delay");
		spawnerControls.add(sliderMaxSpawnDelay);
		sliderMaxSpawnDelay.setMinutesSecondsTime();
		sliderMaxSpawnDelay.setMinMaxValues(0, 60);
		sliderMaxSpawnDelay.setSliderValue(maxSpawnDelay / 20);
		sliderNearbyMobLimit = new LOTRGuiSlider(3, guiLeft + xSize / 2 - 112, guiTop + 124, 224, 20, "Nearby Mob Limit");
		spawnerControls.add(sliderNearbyMobLimit);
		sliderNearbyMobLimit.setMinMaxValues(0, 32);
		sliderNearbyMobLimit.setSliderValue(nearbyMobLimit);
		sliderNearbyMobRange = new LOTRGuiSlider(4, guiLeft + xSize / 2 - 112, guiTop + 148, 224, 20, "Nearby Mob Check Range");
		spawnerControls.add(sliderNearbyMobRange);
		sliderNearbyMobRange.setMinMaxValues(0, 64);
		sliderNearbyMobRange.setSliderValue(nearbyMobCheckRange);
		sliderPlayerRange = new LOTRGuiSlider(5, guiLeft + xSize / 2 - 112, guiTop + 172, 224, 20, "Required Player Range");
		spawnerControls.add(sliderPlayerRange);
		sliderPlayerRange.setMinMaxValues(1, 100);
		sliderPlayerRange.setSliderValue(requiredPlayerRange);
		sliderMaxSpawnCount = new LOTRGuiSlider(6, guiLeft + xSize / 2 - 112, guiTop + 196, 224, 20, "Max Spawn Count");
		spawnerControls.add(sliderMaxSpawnCount);
		sliderMaxSpawnCount.setMinMaxValues(1, 16);
		sliderMaxSpawnCount.setSliderValue(maxSpawnCount);
		sliderXZRange = new LOTRGuiSlider(7, guiLeft + xSize / 2 - 112, guiTop + 220, 110, 20, "Horizontal Range");
		spawnerControls.add(sliderXZRange);
		sliderXZRange.setMinMaxValues(0, 64);
		sliderXZRange.setSliderValue(maxSpawnRange);
		sliderYRange = new LOTRGuiSlider(8, guiLeft + xSize / 2 + 2, guiTop + 220, 110, 20, "Vertical Range");
		spawnerControls.add(sliderYRange);
		sliderYRange.setMinMaxValues(0, 16);
		sliderYRange.setSliderValue(maxSpawnRangeVertical);
		buttonMobType = new GuiButton(0, guiLeft + xSize / 2 - 112, guiTop + 52, 110, 20, spawnsPersistentNPCs ? "Persistent NPCs" : "Normal NPCs");
		mobControls.add(buttonMobType);
		buttonMobType.enabled = LOTREntityNPC.class.isAssignableFrom(mobClass);
		sliderMaxHealth = new LOTRGuiSlider(1, guiLeft + xSize / 2 - 112, guiTop + 76, 224, 20, "Max Health");
		mobControls.add(sliderMaxHealth);
		sliderMaxHealth.setMinMaxValues(0, 200);
		sliderMaxHealth.setSliderValue(maxHealth);
		sliderFollowRange = new LOTRGuiSlider(2, guiLeft + xSize / 2 - 112, guiTop + 100, 224, 20, "Navigator Range");
		mobControls.add(sliderFollowRange);
		sliderFollowRange.setMinMaxValues(0, 100);
		sliderFollowRange.setSliderValue(navigatorRange);
		sliderMoveSpeed = new LOTRGuiSlider(3, guiLeft + xSize / 2 - 112, guiTop + 124, 224, 20, "Movement Speed");
		mobControls.add(sliderMoveSpeed);
		sliderMoveSpeed.setFloat();
		sliderMoveSpeed.setMinMaxValues_F(0.0f, 1.0f);
		sliderMoveSpeed.setSliderValue_F(moveSpeed);
		sliderAttackDamage = new LOTRGuiSlider(4, guiLeft + xSize / 2 - 112, guiTop + 148, 224, 20, "Base Attack Damage");
		mobControls.add(sliderAttackDamage);
		sliderAttackDamage.setFloat();
		sliderAttackDamage.setMinMaxValues_F(0.0f, 20.0f);
		sliderAttackDamage.setSliderValue_F(attackDamage);
	}

	@Override
	public void setWorldAndResolution(Minecraft mc, int i, int j) {
		pageControls.clear();
		spawnerControls.clear();
		mobControls.clear();
		super.setWorldAndResolution(mc, i, j);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled && button.getClass() == GuiButton.class) {
			if (button == buttonRedstone && page == 0) {
				++active;
				if (active > 2) {
					active = 0;
				}
				switch (active) {
				case 0: {
					button.displayString = "Inactive";
					break;
				}
				case 1: {
					button.displayString = "Active";
					break;
				}
				case 2: {
					button.displayString = "Redstone Activated";
				}
				}
			}
			if (button == buttonMobType) {
				if (page == 1) {
					spawnsPersistentNPCs = !spawnsPersistentNPCs;
					button.displayString = spawnsPersistentNPCs ? "Persistent NPCs" : "Normal NPCs";
				}
				needsUpdate = true;
			}
			if (button == buttonPage) {
				page = 1 - page;
				button.displayString = pageNames[page];
			}
		}
	}

	@Override
	public void drawScreen(int i, int j, float f) {
		block4: {
			int k;
			block3: {
				drawDefaultBackground();
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
				mc.getTextureManager().bindTexture(guiTexture);
				this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
				fontRendererObj.drawString("Mob Spawner", guiLeft + 127 - fontRendererObj.getStringWidth("Mob Spawner") / 2, guiTop + 11, 4210752);
				fontRendererObj.drawString(mobName, guiLeft + 127 - fontRendererObj.getStringWidth(mobName) / 2, guiTop + 26, 4210752);
				super.drawScreen(i, j, f);
				for (k = 0; k < pageControls.size(); ++k) {
					((GuiButton) pageControls.get(k)).drawButton(mc, i, j);
				}
				if (page != 0) {
					break block3;
				}
				for (k = 0; k < spawnerControls.size(); ++k) {
					((GuiButton) spawnerControls.get(k)).drawButton(mc, i, j);
				}
				break block4;
			}
			if (page != 1) {
				break block4;
			}
			for (k = 0; k < mobControls.size(); ++k) {
				((GuiButton) mobControls.get(k)).drawButton(mc, i, j);
			}
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		if (page == 0) {
			if (sliderMinSpawnDelay.dragging) {
				minSpawnDelay = sliderMinSpawnDelay.getSliderValue() * 20;
				needsUpdate = true;
			}
			if (sliderMaxSpawnDelay.dragging) {
				maxSpawnDelay = sliderMaxSpawnDelay.getSliderValue() * 20;
				needsUpdate = true;
			}
			if (minSpawnDelay > maxSpawnDelay) {
				if (sliderMinSpawnDelay.dragging) {
					sliderMaxSpawnDelay.setSliderValue(sliderMinSpawnDelay.getSliderValue());
					maxSpawnDelay = minSpawnDelay;
					needsUpdate = true;
				} else if (sliderMaxSpawnDelay.dragging) {
					sliderMinSpawnDelay.setSliderValue(sliderMaxSpawnDelay.getSliderValue());
					minSpawnDelay = maxSpawnDelay;
					needsUpdate = true;
				}
			}
			if (sliderNearbyMobLimit.dragging) {
				nearbyMobLimit = sliderNearbyMobLimit.getSliderValue();
				needsUpdate = true;
			}
			if (sliderNearbyMobRange.dragging) {
				nearbyMobCheckRange = sliderNearbyMobRange.getSliderValue();
				needsUpdate = true;
			}
			if (sliderPlayerRange.dragging) {
				requiredPlayerRange = sliderPlayerRange.getSliderValue();
				needsUpdate = true;
			}
			if (sliderMaxSpawnCount.dragging) {
				maxSpawnCount = sliderMaxSpawnCount.getSliderValue();
				needsUpdate = true;
			}
			if (sliderXZRange.dragging) {
				maxSpawnRange = sliderXZRange.getSliderValue();
				needsUpdate = true;
			}
			if (sliderYRange.dragging) {
				maxSpawnRangeVertical = sliderYRange.getSliderValue();
				needsUpdate = true;
			}
		} else if (page == 1) {
			if (sliderMaxHealth.dragging) {
				maxHealth = sliderMaxHealth.getSliderValue();
				needsUpdate = true;
			}
			if (sliderFollowRange.dragging) {
				navigatorRange = sliderFollowRange.getSliderValue();
				needsUpdate = true;
			}
			if (sliderMoveSpeed.dragging) {
				moveSpeed = sliderMoveSpeed.getSliderValue_F();
				needsUpdate = true;
			}
			if (sliderAttackDamage.dragging) {
				attackDamage = sliderAttackDamage.getSliderValue_F();
				needsUpdate = true;
			}
		}
		if (needsUpdate) {
			sendGuiDataToClientTileEntity();
			sendGuiDataToServerTileEntity();
		}
	}

	@Override
	protected void mouseClicked(int i, int j, int k) {
		buttonList.addAll(pageControls);
		if (page == 0) {
			buttonList.addAll(spawnerControls);
		} else if (page == 1) {
			buttonList.addAll(mobControls);
		}
		super.mouseClicked(i, j, k);
		buttonList.clear();
	}
}
