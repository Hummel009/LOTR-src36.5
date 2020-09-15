/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Supplier
 *  cpw.mods.fml.common.FMLLog
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.server.management.PlayerManager
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 *  net.minecraft.world.WorldServer
 */
package lotr.common.entity.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import lotr.common.LOTRConfig;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRPlayerData;
import lotr.common.fac.LOTRFaction;
import lotr.common.network.LOTRPacketHandler;
import lotr.common.network.LOTRPacketMiniquestOffer;
import lotr.common.network.LOTRPacketNPCIsOfferingQuest;
import lotr.common.quest.LOTRMiniQuest;
import lotr.common.quest.LOTRMiniQuestBounty;
import lotr.common.quest.LOTRMiniQuestFactory;
import lotr.common.quest.MiniQuestSelector;
import lotr.common.world.biome.LOTRBiome;
import lotr.common.world.map.LOTRWaypoint;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class LOTREntityQuestInfo {
    private LOTREntityNPC theNPC;
    private LOTRMiniQuest miniquestOffer;
    private int offerTime = 0;
    public static final int maxOfferTime = 24000;
    private int offerChance;
    private static final int offerChance_default = 20000;
    private float minAlignment;
    private Map<UUID, LOTRMiniQuest> playerSpecificOffers = new HashMap<UUID, LOTRMiniQuest>();
    private List<EntityPlayer> openOfferPlayers = new ArrayList<EntityPlayer>();
    private Map<UUID, Boolean> playerPacketCache = new HashMap<UUID, Boolean>();
    public boolean clientIsOffering;
    public int clientOfferColor;
    private List<UUID> activeQuestPlayers = new ArrayList<UUID>();
    private Predicate<EntityPlayer> bountyHelpPredicate;
    private Predicate<EntityPlayer> bountyHelpConsumer;
    private MiniQuestSelector.BountyActiveAnyFaction activeBountySelector;

    public LOTREntityQuestInfo(LOTREntityNPC npc) {
        this.theNPC = npc;
        this.offerChance = 20000;
        this.minAlignment = 0.0f;
        this.bountyHelpPredicate = new Predicate<EntityPlayer>(){

            public boolean apply(EntityPlayer player) {
                return LOTREntityQuestInfo.this.theNPC.getRNG().nextInt(3) == 0;
            }
        };
        this.bountyHelpConsumer = new Predicate<EntityPlayer>(){

            public boolean apply(EntityPlayer player) {
                return true;
            }
        };
        this.activeBountySelector = new MiniQuestSelector.BountyActiveFaction(new Supplier<LOTRFaction>(){

            public LOTRFaction get() {
                return LOTREntityQuestInfo.this.theNPC.getFaction();
            }
        });
    }

    public void setOfferChance(int i) {
        this.offerChance = i;
    }

    public void setMinAlignment(float f) {
        this.minAlignment = f;
    }

    public void setBountyHelpPredicate(Predicate<EntityPlayer> predicate) {
        this.bountyHelpPredicate = predicate;
    }

    public void setBountyHelpConsumer(Predicate<EntityPlayer> predicate) {
        this.bountyHelpConsumer = predicate;
    }

    public void setActiveBountySelector(MiniQuestSelector.BountyActiveAnyFaction sel) {
        this.activeBountySelector = sel;
    }

    private boolean canGenerateQuests() {
        if (!LOTRConfig.allowMiniquests) {
            return false;
        }
        if (this.theNPC.isChild() || this.theNPC.isDrunkard()) {
            return false;
        }
        return !this.theNPC.isTrader() && !this.theNPC.isTraderEscort && !this.theNPC.hiredNPCInfo.isActive;
    }

    public boolean canOfferQuestsTo(EntityPlayer entityplayer) {
        if (this.canGenerateQuests() && this.theNPC.isFriendly(entityplayer) && this.theNPC.getAttackTarget() == null) {
            float alignment = LOTRLevelData.getData(entityplayer).getAlignment(this.theNPC.getFaction());
            return alignment >= this.minAlignment;
        }
        return false;
    }

    private LOTRMiniQuest generateRandomMiniQuest() {
        int tries = 8;
        for (int l = 0; l < tries; ++l) {
            LOTRMiniQuest quest = this.theNPC.createMiniQuest();
            if (quest == null) continue;
            if (quest.isValidQuest()) {
                return quest;
            }
            FMLLog.severe((String)("Created an invalid LOTR miniquest " + quest.speechBankStart), (Object[])new Object[0]);
        }
        return null;
    }

    public LOTRMiniQuest getOfferFor(EntityPlayer entityplayer) {
        return this.getOfferFor(entityplayer, null);
    }

    private LOTRMiniQuest getOfferFor(EntityPlayer entityplayer, boolean[] isSpecific) {
        UUID id = entityplayer.getUniqueID();
        if (this.playerSpecificOffers.containsKey(id)) {
            if (isSpecific != null) {
                isSpecific[0] = true;
            }
            return this.playerSpecificOffers.get(id);
        }
        if (isSpecific != null) {
            isSpecific[0] = false;
        }
        return this.miniquestOffer;
    }

    public void clearMiniQuestOffer() {
        this.setMiniQuestOffer(null, 0);
    }

    public void setMiniQuestOffer(LOTRMiniQuest quest, int time) {
        this.miniquestOffer = quest;
        this.offerTime = time;
    }

    public void setPlayerSpecificOffer(EntityPlayer entityplayer, LOTRMiniQuest quest) {
        this.playerSpecificOffers.put(entityplayer.getUniqueID(), quest);
    }

    public void clearPlayerSpecificOffer(EntityPlayer entityplayer) {
        this.playerSpecificOffers.remove(entityplayer.getUniqueID());
    }

    public void addOpenOfferPlayer(EntityPlayer entityplayer) {
        this.openOfferPlayers.add(entityplayer);
    }

    public void removeOpenOfferPlayer(EntityPlayer entityplayer) {
        this.openOfferPlayers.remove((Object)entityplayer);
    }

    public boolean anyOpenOfferPlayers() {
        return !this.openOfferPlayers.isEmpty();
    }

    public void onUpdate() {
        if (!this.theNPC.worldObj.isRemote) {
            if (this.miniquestOffer == null) {
                if (this.canGenerateQuests() && this.theNPC.getRNG().nextInt(this.offerChance) == 0) {
                    this.miniquestOffer = this.generateRandomMiniQuest();
                    if (this.miniquestOffer != null) {
                        this.offerTime = 24000;
                    }
                }
            } else if (!this.miniquestOffer.isValidQuest() || !this.canGenerateQuests()) {
                this.clearMiniQuestOffer();
            } else if (!this.anyOpenOfferPlayers()) {
                if (this.offerTime > 0) {
                    --this.offerTime;
                } else {
                    this.clearMiniQuestOffer();
                }
            }
            if (!this.activeQuestPlayers.isEmpty()) {
                HashSet<UUID> removes = new HashSet<UUID>();
                for (UUID player : this.activeQuestPlayers) {
                    List<LOTRMiniQuest> playerQuests = LOTRLevelData.getData(player).getMiniQuestsForEntity(this.theNPC, true);
                    if (playerQuests.isEmpty()) {
                        removes.add(player);
                        continue;
                    }
                    for (LOTRMiniQuest quest : playerQuests) {
                        quest.updateLocation(this.theNPC);
                    }
                }
                this.activeQuestPlayers.removeAll(removes);
            }
            if (this.theNPC.ticksExisted % 10 == 0) {
                this.sendDataToAllWatchers();
            }
        }
    }

    public boolean anyActiveQuestPlayers() {
        return !this.activeQuestPlayers.isEmpty();
    }

    public void addActiveQuestPlayer(EntityPlayer entityplayer) {
        this.activeQuestPlayers.add(entityplayer.getUniqueID());
    }

    private void removeActiveQuestPlayer(EntityPlayer entityplayer) {
        this.activeQuestPlayers.remove(entityplayer.getUniqueID());
    }

    private boolean doesPlayerHaveActiveQuest(EntityPlayer entityplayer) {
        return this.activeQuestPlayers.contains(entityplayer.getUniqueID());
    }

    public boolean interact(EntityPlayer entityplayer) {
        LOTRPlayerData playerData = LOTRLevelData.getData(entityplayer);
        List<LOTRMiniQuest> thisNPCQuests = playerData.getMiniQuestsForEntity(this.theNPC, true);
        if (thisNPCQuests.isEmpty()) {
            for (LOTRMiniQuest quest : playerData.getActiveMiniQuests()) {
                if (quest.entityUUID.equals(this.theNPC.getUniqueID()) || !quest.onInteractOther(entityplayer, this.theNPC)) continue;
                return true;
            }
        }
        if (this.canOfferQuestsTo(entityplayer)) {
            List<LOTRMiniQuest> bountyQuests;
            if (!thisNPCQuests.isEmpty()) {
                LOTRMiniQuest activeQuest = thisNPCQuests.get(0);
                activeQuest.onInteract(entityplayer, this.theNPC);
                if (activeQuest.isCompleted()) {
                    this.removeActiveQuestPlayer(entityplayer);
                } else {
                    playerData.setTrackingMiniQuest(activeQuest);
                }
                return true;
            }
            LOTRMiniQuest offer = this.getOfferFor(entityplayer);
            if (offer != null && offer.isValidQuest() && offer.canPlayerAccept(entityplayer)) {
                List<LOTRMiniQuest> questsForFaction = playerData.getMiniQuestsForFaction(this.theNPC.getFaction(), true);
                if (questsForFaction.size() < LOTRMiniQuest.MAX_MINIQUESTS_PER_FACTION) {
                    this.sendMiniquestOffer(entityplayer, offer);
                    return true;
                }
                this.theNPC.sendSpeechBank(entityplayer, offer.speechBankTooMany, offer);
                return true;
            }
            LOTRMiniQuestFactory bountyHelpSpeechDir = this.theNPC.getBountyHelpSpeechDir();
            if (bountyHelpSpeechDir != null && this.bountyHelpPredicate.apply(entityplayer) && !(bountyQuests = playerData.selectMiniQuests(this.activeBountySelector)).isEmpty()) {
                LOTRWaypoint lastWP;
                LOTRMiniQuestBounty bQuest = (LOTRMiniQuestBounty)bountyQuests.get(this.theNPC.getRNG().nextInt(bountyQuests.size()));
                UUID targetID = bQuest.targetID;
                String objective = bQuest.targetName;
                LOTRPlayerData targetData = LOTRLevelData.getData(targetID);
                LOTRMiniQuestBounty.BountyHelp helpType = LOTRMiniQuestBounty.BountyHelp.getRandomHelpType(this.theNPC.getRNG());
                String location = null;
                if (helpType == LOTRMiniQuestBounty.BountyHelp.BIOME) {
                    LOTRBiome lastBiome = targetData.getLastKnownBiome();
                    if (lastBiome != null) {
                        location = lastBiome.getBiomeDisplayName();
                    }
                } else if (helpType == LOTRMiniQuestBounty.BountyHelp.WAYPOINT && (lastWP = targetData.getLastKnownWaypoint()) != null) {
                    location = lastWP.getDisplayName();
                }
                if (location != null) {
                    String speechBank = "miniquest/" + bountyHelpSpeechDir.getBaseName() + "/_bountyHelp_" + helpType.speechName;
                    this.theNPC.sendSpeechBank(entityplayer, speechBank, location, objective);
                    this.bountyHelpConsumer.apply(entityplayer);
                    return true;
                }
            }
        }
        return false;
    }

    private void sendMiniquestOffer(EntityPlayer entityplayer, LOTRMiniQuest quest) {
        NBTTagCompound nbt = new NBTTagCompound();
        quest.writeToNBT(nbt);
        LOTRPacketMiniquestOffer packet = new LOTRPacketMiniquestOffer(this.theNPC.getEntityId(), nbt);
        LOTRPacketHandler.networkWrapper.sendTo((IMessage)packet, (EntityPlayerMP)entityplayer);
        this.addOpenOfferPlayer(entityplayer);
    }

    public void receiveOfferResponse(EntityPlayer entityplayer, boolean accept) {
        this.removeOpenOfferPlayer(entityplayer);
        if (accept) {
            boolean[] container = new boolean[1];
            LOTRMiniQuest quest = this.getOfferFor(entityplayer, container);
            boolean isSpecific = container[0];
            if (quest != null && quest.isValidQuest() && this.canOfferQuestsTo(entityplayer)) {
                quest.setPlayerData(LOTRLevelData.getData(entityplayer));
                quest.start(entityplayer, this.theNPC);
                if (isSpecific) {
                    this.clearPlayerSpecificOffer(entityplayer);
                } else {
                    this.clearMiniQuestOffer();
                }
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        if (this.miniquestOffer != null) {
            NBTTagCompound questData = new NBTTagCompound();
            this.miniquestOffer.writeToNBT(questData);
            nbt.setTag("MQOffer", (NBTBase)questData);
        }
        nbt.setInteger("MQOfferTime", this.offerTime);
        NBTTagList specificTags = new NBTTagList();
        for (Map.Entry<UUID, LOTRMiniQuest> e : this.playerSpecificOffers.entrySet()) {
            UUID playerID = e.getKey();
            LOTRMiniQuest offer = e.getValue();
            NBTTagCompound offerData = new NBTTagCompound();
            offerData.setString("OfferPlayerID", playerID.toString());
            offer.writeToNBT(offerData);
            specificTags.appendTag((NBTBase)offerData);
        }
        nbt.setTag("MQSpecificOffers", (NBTBase)specificTags);
        NBTTagList activeQuestTags = new NBTTagList();
        for (UUID player : this.activeQuestPlayers) {
            String s = player.toString();
            activeQuestTags.appendTag((NBTBase)new NBTTagString(s));
        }
        nbt.setTag("ActiveQuestPlayers", (NBTBase)activeQuestTags);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        int i;
        UUID player;
        if (nbt.hasKey("MQOffer", 10)) {
            NBTTagCompound questData = nbt.getCompoundTag("MQOffer");
            this.miniquestOffer = LOTRMiniQuest.loadQuestFromNBT(questData, null);
        }
        this.offerTime = nbt.getInteger("MQOfferTime");
        this.playerSpecificOffers.clear();
        if (nbt.hasKey("MQSpecificOffers")) {
            NBTTagList specificTags = nbt.getTagList("MQSpecificOffers", 10);
            for (i = 0; i < specificTags.tagCount(); ++i) {
                NBTTagCompound offerData = specificTags.getCompoundTagAt(i);
                try {
                    UUID playerID = UUID.fromString(offerData.getString("OfferPlayerID"));
                    LOTRMiniQuest offer = LOTRMiniQuest.loadQuestFromNBT(offerData, null);
                    if (offer == null || !offer.isValidQuest()) continue;
                    this.playerSpecificOffers.put(playerID, offer);
                    continue;
                }
                catch (Exception e) {
                    FMLLog.warning((String)"Error loading NPC player-specific miniquest offer", (Object[])new Object[0]);
                    e.printStackTrace();
                }
            }
        }
        this.activeQuestPlayers.clear();
        NBTTagList activeQuestTags = nbt.getTagList("ActiveQuestPlayers", 8);
        for (i = 0; i < activeQuestTags.tagCount(); ++i) {
            String s = activeQuestTags.getStringTagAt(i);
            UUID player2 = UUID.fromString(s);
            if (player2 == null) continue;
            this.activeQuestPlayers.add(player2);
        }
        if (nbt.hasKey("NPCMiniQuestPlayer") && (player = UUID.fromString(nbt.getString("NPCMiniQuestPlayer"))) != null) {
            this.activeQuestPlayers.add(player);
        }
    }

    public void sendData(EntityPlayerMP entityplayer) {
        LOTRMiniQuest questOffer = this.getOfferFor((EntityPlayer)entityplayer);
        boolean isOffering = questOffer != null && this.canOfferQuestsTo((EntityPlayer)entityplayer);
        int color = questOffer != null ? questOffer.getQuestColor() : 0;
        boolean prevOffering = false;
        UUID uuid = entityplayer.getUniqueID();
        if (this.playerPacketCache.containsKey(uuid)) {
            prevOffering = this.playerPacketCache.get(uuid);
        }
        this.playerPacketCache.put(uuid, isOffering);
        if (isOffering != prevOffering) {
            LOTRPacketNPCIsOfferingQuest packet = new LOTRPacketNPCIsOfferingQuest(this.theNPC.getEntityId(), isOffering, color);
            LOTRPacketHandler.networkWrapper.sendTo((IMessage)packet, entityplayer);
        }
    }

    private void sendDataToAllWatchers() {
        int x = MathHelper.floor_double((double)this.theNPC.posX) >> 4;
        int z = MathHelper.floor_double((double)this.theNPC.posZ) >> 4;
        PlayerManager playermanager = ((WorldServer)this.theNPC.worldObj).getPlayerManager();
        List players = this.theNPC.worldObj.playerEntities;
        for (Object obj : players) {
            EntityPlayerMP entityplayer = (EntityPlayerMP)obj;
            if (!playermanager.isPlayerWatchingChunk(entityplayer, x, z)) continue;
            this.sendData(entityplayer);
        }
    }

    public void receiveData(LOTRPacketNPCIsOfferingQuest packet) {
        this.clientIsOffering = packet.offering;
        this.clientOfferColor = packet.offerColor;
    }

    public void onDeath() {
        if (!this.theNPC.worldObj.isRemote && !this.activeQuestPlayers.isEmpty()) {
            for (UUID player : this.activeQuestPlayers) {
                List<LOTRMiniQuest> playerQuests = LOTRLevelData.getData(player).getMiniQuestsForEntity(this.theNPC, true);
                for (LOTRMiniQuest quest : playerQuests) {
                    if (!quest.isActive()) continue;
                    quest.setEntityDead();
                }
            }
        }
    }

}

