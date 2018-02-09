package me.mawood.packet.segment.segments;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.blocks.DoubleBlock;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.packet.segment.Segment;

import java.util.Arrays;

public class GpsSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x01};
    public GpsSegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(!(blocks[0] instanceof DoubleBlock)) throw new InvalidSegmentException("First block of gps segment must be a double");
        if(!(blocks[1] instanceof DoubleBlock)) throw new InvalidSegmentException("Second block of gps segment must be a double");
        if(!(blocks[2] instanceof DoubleBlock)) throw new InvalidSegmentException("Third block of gps segment must be a double");
    }

    public double getLatitude()
    {
        return ((DoubleBlock)blocks[0]).getData();
    }

    public double getLongitude()
    {
        return ((DoubleBlock)blocks[1]).getData();
    }

    public double getAltitude()
    {
        return ((DoubleBlock)blocks[2]).getData();
    }

    @Override
    public String toString()
    {
        return "GpsSegment{" +
                "latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", altitude=" + getAltitude() +
                '}';
    }
}
