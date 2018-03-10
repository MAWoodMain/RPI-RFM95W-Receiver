package me.mawood.rfm95w.packet.segment.segments;

import com.google.gson.JsonObject;
import me.mawood.rfm95w.packet.block.Block;
import me.mawood.rfm95w.packet.block.blocks.IntBlock;
import me.mawood.rfm95w.packet.segment.InvalidSegmentException;
import me.mawood.rfm95w.packet.segment.Segment;

public class UptimeSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x04};
    public UptimeSegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 1) throw new InvalidSegmentException("Uptime segment must contain a block");
        if(!(blocks[0] instanceof IntBlock)) throw new InvalidSegmentException("First block should be a int block");
    }

    public int getUptime()
    {
        return ((IntBlock) blocks[0]).getData(); //Instant.ofEpochMilli(((IntBlock) blocks[0]).getData());
    }

    @Override
    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("uptime", getUptime());
        return json;
    }

    @Override
    public String getJsonName()
    {
        return "Uptime";
    }

    @Override
    public String toString()
    {
        return "UptimeSegment{" +
                "uptime=" + getUptime() + " seconds" +
                '}';
    }
}
