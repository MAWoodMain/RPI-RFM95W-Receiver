package me.mawood.loraCapture.packet.segment.segments;

import com.google.gson.JsonObject;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.blocks.ShortBlock;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.Segment;

import javax.persistence.*;

@Entity
@Table(name = "battery_segments")
public class BatterySegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x01};
    public BatterySegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 1) throw new InvalidSegmentException("Battery segment must contain a block");
        if(!(blocks[0] instanceof ShortBlock)) throw new InvalidSegmentException("First block should be a short block");
    }

    public BatterySegment()
    {
    }

    @Transient
    @Override
    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("batteryLevel", getBatteryLevel());
        return json;
    }

    @Transient
    @Override
    public String getJsonName()
    {
        return "BatteryLevel";
    }

    @Basic
    @Column(name = "batteryLevel")
    public short getBatteryLevel()
    {
        return ((ShortBlock) blocks[0]).getData();
    }

    public void setBatteryLevel(short batteryLevel)
    {
        //TODO implement setting block
    }



    @Transient
    @Override
    public String toString()
    {
        return "BatterySegment{" +
        "batteryLevel=" + getBatteryLevel() +
                "}";
    }
}
