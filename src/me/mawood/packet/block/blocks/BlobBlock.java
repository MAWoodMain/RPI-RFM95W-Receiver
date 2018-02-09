package me.mawood.packet.block.blocks;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;

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
}
