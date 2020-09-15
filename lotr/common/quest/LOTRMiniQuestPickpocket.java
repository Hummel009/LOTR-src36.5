/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.Block$SoundType
 *  net.minecraft.command.IEntitySelector
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLiving
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.ai.EntitySenses
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.nbt.NBTTagList
 *  net.minecraft.nbt.NBTTagString
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.ChatComponentTranslation
 *  net.minecraft.util.IChatComponent
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.StatCollector
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package lotr.common.quest;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import lotr.common.LOTRAchievement;
import lotr.common.LOTRLevelData;
import lotr.common.LOTRMod;
import lotr.common.LOTRPlayerData;
import lotr.common.entity.npc.LOTREntityNPC;
import lotr.common.fac.LOTRAlignmentValues;
import lotr.common.fac.LOTRFaction;
import lotr.common.item.LOTRItemLeatherHat;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;

public class LOTRMiniQuestPickpocket
extends LOTRMiniQuestCollectBase {
    public LOTRFaction pickpocketFaction;
    private Set<UUID> pickpocketedEntityIDs = new HashSet<UUID>();

    public LOTRMiniQuestPickpocket(LOTRPlayerData pd) {
        super(pd);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("PickpocketFaction", this.pickpocketFaction.codeName());
        NBTTagList ids = new NBTTagList();
        for (UUID id : this.pickpocketedEntityIDs) {
            ids.appendTag((NBTBase)new NBTTagString(id.toString()));
        }
        nbt.setTag("PickpocketedIDs", (NBTBase)ids);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.pickpocketFaction = LOTRFaction.forName(nbt.getString("PickpocketFaction"));
        this.pickpocketedEntityIDs.clear();
        NBTTagList ids = nbt.getTagList("PickpocketedIDs", 8);
        for (int i = 0; i < ids.tagCount(); ++i) {
            UUID id = UUID.fromString(ids.getStringTagAt(i));
            if (id == null) continue;
            this.pickpocketedEntityIDs.add(id);
        }
    }

    @Override
    public boolean isValidQuest() {
        return super.isValidQuest() && this.pickpocketFaction != null;
    }

    @Override
    public String getQuestObjective() {
        return StatCollector.translateToLocalFormatted((String)"lotr.miniquest.pickpocket", (Object[])new Object[]{this.collectTarget, this.pickpocketFaction.factionEntityName()});
    }

    @Override
    public String getObjectiveInSpeech() {
        return this.pickpocketFaction.factionEntityName();
    }

    @Override
    public String getProgressedObjectiveInSpeech() {
        return this.collectTarget - this.amountGiven + " " + this.pickpocketFaction.factionEntityName();
    }

    @Override
    public String getQuestProgress() {
        return StatCollector.translateToLocalFormatted((String)"lotr.miniquest.pickpocket.progress", (Object[])new Object[]{this.amountGiven, this.collectTarget});
    }

    @Override
    public ItemStack getQuestIcon() {
        return LOTRMiniQuestPickpocket.createPickpocketIcon();
    }

    public static ItemStack createPickpocketIcon() {
        ItemStack hat = new ItemStack(LOTRMod.leatherHat);
        LOTRItemLeatherHat.setHatColor(hat, 0);
        LOTRItemLeatherHat.setFeatherColor(hat, 16777215);
        return hat;
    }

    @Override
    protected boolean isQuestItem(ItemStack itemstack) {
        return IPickpocketable.Helper.isPickpocketed(itemstack) && this.entityUUID.equals(IPickpocketable.Helper.getWanterID(itemstack));
    }

    @Override
    public boolean onInteractOther(final EntityPlayer entityplayer, final LOTREntityNPC npc) {
        if (entityplayer.isSneaking() && entityplayer.getHeldItem() == null && npc.getFaction() == this.pickpocketFaction && npc instanceof IPickpocketable) {
            IPickpocketable ppable = (IPickpocketable)((Object)npc);
            UUID id = npc.getPersistentID();
            if (ppable.canPickpocket() && !this.pickpocketedEntityIDs.contains(id)) {
                boolean noticed;
                boolean success;
                if (npc.getAttackTarget() != null) {
                    entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.inCombat", new Object[0]));
                    return true;
                }
                if (this.isEntityWatching((EntityLiving)npc, (EntityLivingBase)entityplayer)) {
                    entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.watched", new Object[0]));
                    return true;
                }
                Random rand = npc.getRNG();
                boolean bl = success = rand.nextInt(3) == 0;
                boolean anyoneNoticed = noticed = success ? rand.nextInt(3) == 0 : rand.nextInt(4) == 0;
                if (success) {
                    ItemStack picked = ppable.createPickpocketItem();
                    IPickpocketable.Helper.setPickpocketData(picked, npc.getNPCName(), this.entityNameFull, this.entityUUID);
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, picked);
                    entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.success", new Object[]{picked.stackSize, picked.getDisplayName(), npc.getNPCName()}));
                    npc.playSound("lotr:event.trade", 0.5f, 1.0f + (rand.nextFloat() - rand.nextFloat()) * 0.1f);
                    npc.playSound("mob.horse.leather", 0.5f, 1.0f);
                    this.spawnPickingFX("pickpocket", 1.0, (EntityLivingBase)npc);
                    this.pickpocketedEntityIDs.add(id);
                    this.updateQuest();
                    LOTRLevelData.getData(entityplayer).addAchievement(LOTRAchievement.pickpocket);
                } else {
                    entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.missed", new Object[]{npc.getNPCName()}));
                    npc.playSound(Blocks.wool.stepSound.getBreakSound(), 0.5f, ((rand.nextFloat() - rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                    this.spawnPickingFX("pickpocketFail", 0.4, (EntityLivingBase)npc);
                }
                if (noticed) {
                    entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.noticed", new Object[]{npc.getNPCName()}));
                    npc.setAttackTarget((EntityLivingBase)entityplayer, true);
                    npc.setRevengeTarget((EntityLivingBase)entityplayer);
                    this.spawnAngryFX((EntityLivingBase)npc);
                }
                if (!noticed || rand.nextFloat() < 0.5f) {
                    double civilianRange = 8.0;
                    double guardRange = 16.0;
                    double fullAlertRange = 4.0;
                    List nearbyFriends = npc.worldObj.selectEntitiesWithinAABB(LOTREntityNPC.class, npc.boundingBox.expand(16.0, 16.0, 16.0), new IEntitySelector(){

                        public boolean isEntityApplicable(Entity entity) {
                            LOTREntityNPC otherNPC = (LOTREntityNPC)entity;
                            if (otherNPC.isEntityAlive() && otherNPC.getFaction().isGoodRelation(npc.getFaction())) {
                                return otherNPC.hiredNPCInfo.getHiringPlayer() != entityplayer;
                            }
                            return false;
                        }
                    });
                    for (Object o : nearbyFriends) {
                        double maxRange;
                        boolean otherNoticed;
                        LOTREntityNPC otherNPC = (LOTREntityNPC)o;
                        if (otherNPC == npc) continue;
                        boolean civilian = otherNPC.isCivilianNPC();
                        double d = maxRange = civilian ? 8.0 : 16.0;
                        double dist = otherNPC.getDistanceToEntity((Entity)npc);
                        if (!(dist <= maxRange) || otherNPC.getAttackTarget() != null || !this.isEntityWatching((EntityLiving)otherNPC, (EntityLivingBase)entityplayer)) continue;
                        float distFactor = 1.0f - (float)((dist - 4.0) / (maxRange - 4.0));
                        float chance = 0.5f + distFactor * 0.5f;
                        if (civilian) {
                            chance *= 0.25f;
                        }
                        if (!(otherNoticed = rand.nextFloat() < chance)) continue;
                        entityplayer.addChatMessage((IChatComponent)new ChatComponentTranslation("chat.lotr.pickpocket.otherNoticed", new Object[]{otherNPC.getEntityClassName()}));
                        otherNPC.setAttackTarget((EntityLivingBase)entityplayer, true);
                        otherNPC.setRevengeTarget((EntityLivingBase)entityplayer);
                        this.spawnAngryFX((EntityLivingBase)otherNPC);
                        anyoneNoticed = true;
                    }
                }
                if (anyoneNoticed) {
                    LOTRLevelData.getData(entityplayer).addAlignment(entityplayer, LOTRAlignmentValues.PICKPOCKET_PENALTY, npc.getFaction(), (Entity)npc);
                }
                return true;
            }
        }
        return false;
    }

    private boolean isEntityWatching(EntityLiving watcher, EntityLivingBase target) {
        float fov;
        float fovCos;
        Vec3 look = watcher.getLookVec();
        Vec3 watcherEyes = Vec3.createVectorHelper((double)watcher.posX, (double)(watcher.boundingBox.minY + (double)watcher.getEyeHeight()), (double)watcher.posZ);
        Vec3 targetEyes = Vec3.createVectorHelper((double)target.posX, (double)(target.boundingBox.minY + (double)target.getEyeHeight()), (double)target.posZ);
        Vec3 disp = Vec3.createVectorHelper((double)(targetEyes.xCoord - watcherEyes.xCoord), (double)(targetEyes.yCoord - watcherEyes.yCoord), (double)(targetEyes.zCoord - watcherEyes.zCoord));
        double dot = disp.normalize().dotProduct(look.normalize());
        if (dot >= (double)(fovCos = MathHelper.cos((float)((fov = (float)Math.toRadians(130.0)) / 2.0f)))) {
            return watcher.getEntitySenses().canSee((Entity)target);
        }
        return false;
    }

    private void spawnPickingFX(String particle, double upSpeed, EntityLivingBase npc) {
        Random rand = npc.getRNG();
        int particles = 3 + rand.nextInt(8);
        for (int p = 0; p < particles; ++p) {
            double x = npc.posX;
            double y = npc.boundingBox.minY + (double)(npc.height * 0.5f);
            double z = npc.posZ;
            float w = npc.width * 0.1f;
            float ang = rand.nextFloat() * 3.1415927f * 2.0f;
            double hSpeed = MathHelper.getRandomDoubleInRange((Random)rand, (double)0.05, (double)0.08);
            double vx = (double)MathHelper.cos((float)ang) * hSpeed;
            double vz = (double)MathHelper.sin((float)ang) * hSpeed;
            double vy = MathHelper.getRandomDoubleInRange((Random)rand, (double)0.1, (double)0.25) * upSpeed;
            LOTRMod.proxy.spawnParticle(particle, x += (double)(MathHelper.cos((float)ang) * w), y, z += (double)(MathHelper.sin((float)ang) * w), vx, vy, vz);
        }
    }

    private void spawnAngryFX(EntityLivingBase npc) {
        LOTRMod.proxy.spawnParticle("angry", npc.posX, npc.boundingBox.minY + (double)(npc.height * 2.0f), npc.posZ, npc.motionX, Math.max(0.0, npc.motionY), npc.motionZ);
    }

    @Override
    public int getCoinBonus() {
        return Math.round(this.getAlignmentBonus() * 5.0f);
    }

    public static class QFPickpocket<Q extends LOTRMiniQuestPickpocket>
    extends LOTRMiniQuest.QuestFactoryBase<Q> {
        private LOTRFaction pickpocketFaction;
        private int minTarget;
        private int maxTarget;

        public QFPickpocket(String name) {
            super(name);
        }

        public QFPickpocket setPickpocketFaction(LOTRFaction f, int min, int max) {
            this.pickpocketFaction = f;
            this.minTarget = min;
            this.maxTarget = max;
            return this;
        }

        @Override
        public Class getQuestClass() {
            return LOTRMiniQuestPickpocket.class;
        }

        @Override
        public Q createQuest(LOTREntityNPC npc, Random rand) {
            LOTRMiniQuestPickpocket quest = (LOTRMiniQuestPickpocket)super.createQuest(npc, rand);
            quest.pickpocketFaction = this.pickpocketFaction;
            quest.collectTarget = MathHelper.getRandomIntegerInRange((Random)rand, (int)this.minTarget, (int)this.maxTarget);
            return (Q)quest;
        }
    }

}

