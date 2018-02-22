package me.mawood.packet.segment;

import me.mawood.packet.block.Block;
import org.json.simple.JSONObject;

import java.util.Arrays;

public abstract class Segment
{
    public static final byte SEGMENT_FLAG = (byte) 0xAA;
    public static final int TYPE_FLAG_LENGTH = 2;

    protected final Block[] blocks;

    public Segment(Block[] blocks)
    {
        this.blocks = blocks;
    }

    public int getLength()
    {
        return 1 + TYPE_FLAG_LENGTH + Arrays.stream(blocks).mapToInt(Block::getLength).sum();
    }

    public abstract JSONObject toJson();

    public abstract String getJsonName();
}
