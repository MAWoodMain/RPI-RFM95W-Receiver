package me.mawood.rfm95w.packet.block.blocks;

import me.mawood.rfm95w.packet.block.Block;
import me.mawood.rfm95w.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;

/**
 * 4 byte float block
 */
public class DoubleBlock extends Block<Double>
{
    public static final byte[] TYPE_FLAG = {0x06};
    private final double value;

    public DoubleBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        value = buffer.getDouble();
    }

    @Override
    public Double getData()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return "DoubleBlock{" +
                "value=" + value +
                '}';
    }

    public static DoubleBlock fromData(double value)
    {
        try
        {
            return new DoubleBlock(ByteBuffer.allocate(8).putDouble(value).array());
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes()
    {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(TYPE_FLAG);
        buffer.put((byte) 0x08);
        buffer.put(data);
        return buffer.array();
    }
}
