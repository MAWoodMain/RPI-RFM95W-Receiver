package me.mawood.loraCapture.packet.block.blocks;

import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.InvalidBlockException;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BlobBlock extends Block<byte[]>
{
    public static final byte[] TYPE_FLAG = {0x00};

    public BlobBlock(byte[] data) throws InvalidBlockException
    {
        super(data);
    }

    @Override
    public byte[] getData()
    {
        return data;
    }

    @Override
    public String toString()
    {
        return "BlobBlock{" +
                "data=" + Arrays.toString(data) +
                '}';
    }

    public static BlobBlock fromData(byte[] value)
    {
        try
        {
            return new BlobBlock(value);
        } catch (InvalidBlockException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getBytes()
    {
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 2);
        buffer.put(TYPE_FLAG);
        buffer.put((byte) data.length);
        buffer.put(data);
        return buffer.array();
    }
}
