package me.mawood.loraCapture.packet.segment.segments;

import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.block.blocks.BlobBlock;
import me.mawood.loraCapture.packet.block.blocks.FloatBlock;
import me.mawood.loraCapture.packet.block.blocks.ShortBlock;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.Segment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class RainSegment  extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x0f};

    public RainSegment(Block[] blocks) throws InvalidSegmentException {
        super(blocks);
        if(blocks.length < 2) throw new InvalidSegmentException("Rain segment must have 2 blocks");
        if(!(blocks[0] instanceof BlobBlock)) throw new InvalidSegmentException("Rain segment must contain a bob block");
        if(!(blocks[1] instanceof FloatBlock)) throw new InvalidSegmentException("Rain segment must contain a float block");
    }

    public short[] getRainMeasurements() throws InvalidBlockException {
        ByteBuffer data = ByteBuffer.allocate(20);
        byte[] raw = ((BlobBlock)blocks[0]).getData();
        if(raw.length < 19) throw new InvalidBlockException("blob does not have enough data");
        data.put((byte)0x00);
        data.put(raw[18]);
        for(int i = 16; i >= 0; i-=2)
        {
            data.put(raw[i]);
            data.put(raw[i+1]);
        }

        String binary = "";
        for(byte b:data.array()) binary += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');

        binary = binary.substring(10);
        String[] readingsStrings = binary.split("(?<=\\G.{10})");

        short[] readings = new short[readingsStrings.length];
        for(int i = 0; i < readingsStrings.length; i++)
        {
            readings[i] = Short.parseShort(readingsStrings[i], 2);
        }
        /*
        final int baseMask = 0x000003ff;
        int mask, value, reg = 0, c = 0;;
        for(int i = 0; i < 15; i++)
        {
            value = 0;
            // lower
            mask = baseMask;
            mask <<= c;
            value |= (registers[reg] & (mask & 0xffff)) << 8;
            // upper
            if(c > 5)
            {
                reg++;
                mask &= 0xffff0000;
                mask >>= 16;
                value |= (registers[reg] & (mask & 0xffff));
            }
            readings[i] = (short) value;

            c += 10;
            c %= 16;
        }*/

        return readings;
    }

    public float getTemperature()
    {
        return ((FloatBlock)blocks[1]).getData();
    }

    @Override
    public String toString() {
        try {
            return "RainSegment{" +
                    "rainMeasurements=" + Arrays.toString(getRainMeasurements()) +
                    ", temperature=" + getTemperature() +
                    '}';
        } catch (InvalidBlockException e) {
            e.printStackTrace();
        }
        return "";
    }
}
