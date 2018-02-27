package me.mawood.rfm95w.packet.segment.segments;

import me.mawood.rfm95w.packet.block.Block;
import me.mawood.rfm95w.packet.block.blocks.ShortBlock;
import me.mawood.rfm95w.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.packet.segment.Segment;
import org.json.simple.JSONObject;

public class BatterySegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x01};
    public BatterySegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 1) throw new InvalidSegmentException("Battery segment must contain a block");
        if(!(blocks[0] instanceof ShortBlock)) throw new InvalidSegmentException("First block should be a short block");
    }

    @Override
    public JSONObject toJson()
    {
        JSONObject json = new JSONObject();
        json.put("batteryLevel", getBatteryLevel());
        return json;
    }

    @Override
    public String getJsonName()
    {
        return "BatteryLevel";
    }

    public short getBatteryLevel()
    {
        return ((ShortBlock) blocks[0]).getData();
    }

    @Override
    public String toString()
    {
        return "BatterySegment{" +
        "batteryLevel=" + getBatteryLevel() +
                "}";
    }
}
