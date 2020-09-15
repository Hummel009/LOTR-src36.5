/*
 * Decompiled with CFR 0.148.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  cpw.mods.fml.common.network.simpleimpl.IMessageHandler
 *  cpw.mods.fml.common.network.simpleimpl.MessageContext
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.util.StatCollector
 */
package lotr.common.network;

import java.util.Map;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import lotr.common.LOTRMod;
import lotr.common.fac.LOTRAlignmentBonusMap;
import lotr.common.fac.LOTRAlignmentValues;
import lotr.common.fac.LOTRFaction;
import net.minecraft.util.StatCollector;

public class LOTRPacketAlignmentBonus
implements IMessage {
    private LOTRFaction mainFaction;
    private float prevMainAlignment;
    private LOTRAlignmentBonusMap factionBonusMap = new LOTRAlignmentBonusMap();
    private float conquestBonus;
    private double posX;
    private double posY;
    private double posZ;
    private String name;
    private boolean needsTranslation;
    private boolean isKill;
    private boolean isHiredKill;

    public LOTRPacketAlignmentBonus() {
    }

    public LOTRPacketAlignmentBonus(LOTRFaction f, float pre, LOTRAlignmentBonusMap fMap, float conqBonus, double x, double y, double z, LOTRAlignmentValues.AlignmentBonus source) {
        this.mainFaction = f;
        this.prevMainAlignment = pre;
        this.factionBonusMap = fMap;
        this.conquestBonus = conqBonus;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.name = source.name;
        this.needsTranslation = source.needsTranslation;
        this.isKill = source.isKill;
        this.isHiredKill = source.killByHiredUnit;
    }

    public void toBytes(ByteBuf data) {
        data.writeByte(this.mainFaction.ordinal());
        data.writeFloat(this.prevMainAlignment);
        if (!this.factionBonusMap.isEmpty()) {
            for (Map.Entry e : this.factionBonusMap.entrySet()) {
                LOTRFaction faction = (LOTRFaction)((Object)e.getKey());
                float bonus = ((Float)e.getValue()).floatValue();
                data.writeByte(faction.ordinal());
                data.writeFloat(bonus);
            }
            data.writeByte(-1);
        } else {
            data.writeByte(-1);
        }
        data.writeFloat(this.conquestBonus);
        data.writeDouble(this.posX);
        data.writeDouble(this.posY);
        data.writeDouble(this.posZ);
        byte[] nameData = this.name.getBytes(Charsets.UTF_8);
        data.writeShort(nameData.length);
        data.writeBytes(nameData);
        data.writeBoolean(this.needsTranslation);
        data.writeBoolean(this.isKill);
        data.writeBoolean(this.isHiredKill);
    }

    public void fromBytes(ByteBuf data) {
        this.mainFaction = LOTRFaction.forID(data.readByte());
        this.prevMainAlignment = data.readFloat();
        byte factionID = 0;
        while ((factionID = data.readByte()) >= 0) {
            LOTRFaction faction = LOTRFaction.forID(factionID);
            float bonus = data.readFloat();
            this.factionBonusMap.put(faction, Float.valueOf(bonus));
        }
        this.conquestBonus = data.readFloat();
        this.posX = data.readDouble();
        this.posY = data.readDouble();
        this.posZ = data.readDouble();
        short length = data.readShort();
        this.name = data.readBytes((int)length).toString(Charsets.UTF_8);
        this.needsTranslation = data.readBoolean();
        this.isKill = data.readBoolean();
        this.isHiredKill = data.readBoolean();
    }

    public static class Handler
    implements IMessageHandler<LOTRPacketAlignmentBonus, IMessage> {
        public IMessage onMessage(LOTRPacketAlignmentBonus packet, MessageContext context) {
            String name = packet.name;
            if (packet.needsTranslation) {
                name = StatCollector.translateToLocal((String)name);
            }
            LOTRMod.proxy.spawnAlignmentBonus(packet.mainFaction, packet.prevMainAlignment, packet.factionBonusMap, name, packet.isKill, packet.isHiredKill, packet.conquestBonus, packet.posX, packet.posY, packet.posZ);
            return null;
        }
    }

}

