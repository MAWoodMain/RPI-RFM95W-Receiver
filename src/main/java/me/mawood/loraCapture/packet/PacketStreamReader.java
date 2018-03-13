package me.mawood.loraCapture.packet;

import me.mawood.loraCapture.packet.block.blocks.Blocks;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.segments.Segments;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.segment.Segment;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.logging.Level;

import static me.mawood.loraCapture.Logging.logger;

public class PacketStreamReader
{


    private ArrayList<Segment> segments;

    public PacketStreamReader(byte[] data) throws PacketException, InvalidBlockException, InvalidSegmentException
    {
        this(new ByteArrayInputStream(data));
    }

    public PacketStreamReader(ByteArrayInputStream stream) throws PacketException, InvalidSegmentException, InvalidBlockException
    {
        byte curr = (byte) stream.read();
        segments = new ArrayList<>();
        while(stream.available() > 0 && curr == 0x00) curr = (byte) stream.read();
        if (stream.available() < 2) throw new PacketException("Packet is too short");
        byte protocolId = curr;
        int versionId = (byte) stream.read();
        if(protocolId != (byte)0xDA) throw new PacketException("Unknown protocol ID " + String.format("0x%02X", protocolId));
        if(versionId < 0x01) throw new PacketException("Unknown version of protocol " + String.format("0x%02X", versionId));
        curr = (byte) stream.read();
        short blockLen;
        byte[] id, blockData;
        while (stream.available() > 0)
        {
            switch (curr)
            {
                case Segment.SEGMENT_FLAG:
                    id = new byte[Segment.TYPE_FLAG_LENGTH];
                    stream.read(id,0,Segment.TYPE_FLAG_LENGTH);
                    // segment class established
                    Class segmentClass = Segments.getSegmentClass(id);
                    // acquire blocks before instantiation
                    ArrayList<Block> blocks = new ArrayList<>();
                    curr = (byte) stream.read();
                    while(curr != Segment.SEGMENT_FLAG && stream.available() > 0)
                    {
                        id = new byte[Block.TYPE_FLAG_LENGTH];
                        id[0] = curr;
                        stream.read(id,1,Block.TYPE_FLAG_LENGTH-1);
                        blockLen = (short) stream.read();
                        blockData = new byte[blockLen];
                        stream.read(blockData, 0, blockLen);
                        blocks.add(interpretBlock(id,blockData));
                        curr = (byte) stream.read();
                    }
                    // blocks created, time to create segment
                    segments.add(interpretSegment(segmentClass, blocks.toArray(new Block[blocks.size()])));
                    break;


                default:
                    logger.log(Level.FINER, "Unknown LoRaPacket flag {0}", String.format("0x%02X", curr));
                    if (stream.available() > 0) curr = (byte) stream.read();
            }
        }
    }

    public Segment[] getSegments()
    {
        return segments.toArray(new Segment[segments.size()]);
    }

    private Segment interpretSegment(Class segmentClass, Block[] blocks) throws InvalidSegmentException
    {
        try
        {
            Constructor<?> ctor = segmentClass.getConstructor(Block[].class);
            return (Segment) ctor.newInstance(new Object[] { blocks });
        } catch (Exception e)
        {
            throw new InvalidSegmentException("Could not locate constructor or construct the segment, error: " + e.getCause().getMessage());
        }
    }

    private Block interpretBlock(byte[] blockId, byte[] blockData) throws InvalidBlockException
    {
        try
        {
            Class blockClass = Blocks.getBlockClass(blockId);
            Constructor<?> ctor = blockClass.getConstructor(byte[].class);
            return (Block)ctor.newInstance(new Object[] { blockData });
        } catch (Exception e)
        {
            throw new InvalidBlockException("Could not locate constructor or construct the block, error: " + e.getMessage());
        }
    }
}
