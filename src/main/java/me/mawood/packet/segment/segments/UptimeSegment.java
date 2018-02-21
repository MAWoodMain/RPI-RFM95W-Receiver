package me.mawood.packet.segment.segments;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.blocks.IntBlock;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.packet.segment.Segment;

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
    public String toString()
    {
        return "UptimeSegment{" +
                "uptime=" + getUptime() + " seconds" +
                '}';
    }
}
