package me.mawood.packet.segment.segments;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.blocks.ShortBlock;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.packet.segment.Segment;

public class BatterySegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x02};
    public BatterySegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 1) throw new InvalidSegmentException("Battery segment must contain a block");
        if(!(blocks[0] instanceof ShortBlock)) throw new InvalidSegmentException("First block should be a short block");
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
