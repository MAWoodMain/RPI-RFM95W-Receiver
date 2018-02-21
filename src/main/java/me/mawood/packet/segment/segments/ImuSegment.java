package me.mawood.packet.segment.segments;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.blocks.DoubleBlock;
import me.mawood.packet.block.blocks.FloatBlock;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.packet.segment.Segment;

public class ImuSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x02};

    public ImuSegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 3) throw new InvalidSegmentException("IMU must contain 3 blocks");
        if(!(blocks[0] instanceof FloatBlock)) throw new InvalidSegmentException("First block of imu segment must be a float");
        if(!(blocks[1] instanceof FloatBlock)) throw new InvalidSegmentException("Second block of imu segment must be a float");
        if(!(blocks[2] instanceof FloatBlock)) throw new InvalidSegmentException("Third block of imu segment must be a float");
    }

    public double getX()
    {
        return ((FloatBlock)blocks[0]).getData();
    }

    public double getY()
    {
        return ((FloatBlock)blocks[1]).getData();
    }

    public double getZ()
    {
        return ((FloatBlock)blocks[2]).getData();
    }

    @Override
    public String toString()
    {
        return "ImuSegment{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                '}';
    }
}
