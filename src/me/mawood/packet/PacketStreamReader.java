package me.mawood.packet;

import me.mawood.packet.block.Block;
import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.block.blocks.Blocks;
import me.mawood.packet.segment.InvalidSegmentException;
import me.mawood.packet.segment.Segment;
import me.mawood.packet.segment.segments.Segments;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.logging.Level;

import static me.mawood.Logging.logger;

public class PacketStreamReader
{


    private ArrayList<Segment> segments;

    public static ByteArrayInputStream bytesToStream(byte[] data)
    {
        return new ByteArrayInputStream(data);
    }

    public PacketStreamReader(ByteArrayInputStream stream) throws PacketException
    {
        segments = new ArrayList<>();

        if (stream.available() < 3) throw new PacketException("Packet is too short");
        byte protocolId = (byte) stream.read();
        int versionId = (byte) stream.read();
        if(protocolId != (byte)0xDA) throw new PacketException("Unknown protocol ID " + String.format("0x%02X", protocolId));
        if(versionId < 0x01) throw new PacketException("Unknown version of protocol " + String.format("0x%02X", versionId));

        byte curr = (byte) stream.read();
        short blockLen;
        byte[] id, blockData;
        while (stream.available() > 0)
        {
            switch (curr)
            {
                case Segment.SEGMENT_FLAG:
                    id = new byte[Segment.TYPE_FLAG_LENGTH];
                    stream.read(id,0,Segment.TYPE_FLAG_LENGTH);
                    try
                    {
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
                        try
                        {
                            segments.add(interpretSegment(segmentClass, blocks.toArray(new Block[blocks.size()])));
                        } catch (InvalidSegmentException e)
                        {
                            logger.log(Level.SEVERE, e.getMessage());
                            System.exit(-1);
                        }

                    } catch (InvalidBlockException e)
                    {
                        logger.log(Level.SEVERE, e.getMessage());
                        System.exit(-1);
                    }
                    break;


                default:
                    logger.log(Level.FINER, "Unknown packet flag {0}", String.format("0x%02X", curr));
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
            Segment segment = (Segment) ctor.newInstance(new Object[] { blocks });
            return segment;
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
            Block block = (Block)ctor.newInstance(new Object[] { blockData });
            return block;
        } catch (Exception e)
        {
            throw new InvalidBlockException("Could not locate constructor or construct the block, error: " + e.getMessage());
        }
    }
}
