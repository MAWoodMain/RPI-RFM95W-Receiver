package me.mawood.packet.segment.segments;

import me.mawood.packet.block.Block;
import me.mawood.packet.segment.Segment;

import java.util.Arrays;

public class GpsSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x01};
    public GpsSegment(Block[] blocks)
    {
        super(blocks);
    }

    @Override
    public String toString()
    {
        return "GpsSegment{" +
                "blocks=" + Arrays.toString(blocks) +
                '}';
    }
}
