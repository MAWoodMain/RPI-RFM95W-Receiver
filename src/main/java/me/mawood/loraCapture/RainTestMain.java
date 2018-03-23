package me.mawood.loraCapture;

import me.mawood.loraCapture.packet.PacketException;
import me.mawood.loraCapture.packet.PacketStreamReader;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.block.blocks.BlobBlock;
import me.mawood.loraCapture.packet.block.blocks.FloatBlock;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.segments.RainSegment;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;
import java.util.Base64;

public class RainTestMain
{
    public static void main(String[] args) throws InvalidBlockException, InvalidSegmentException, PacketException {
        /*PacketStreamReader psr = new PacketStreamReader(new byte[]{
                (byte)0xDA, (byte)0x01, (byte)0xAA, (byte)0x00, (byte)0x04, (byte)0x03, (byte)0x04, (byte)0x00,
                (byte)0x00, (byte)0x05, (byte)0x35, (byte)0xAA, (byte)0x00, (byte)0x01, (byte)0x02, (byte)0x02,
                (byte)0x03, (byte)0x57, (byte)0xAA, (byte)0x00, (byte)0x0F, (byte)0x00, (byte)0x13, (byte)0x7C,
                (byte)0xD6, (byte)0x4D, (byte)0x53, (byte)0xDD, (byte)0x37, (byte)0x23, (byte)0x88, (byte)0x38,
                (byte)0x4E, (byte)0x6C, (byte)0xE2, (byte)0x8D, (byte)0xC3, (byte)0xDD, (byte)0x38, (byte)0xD3,
                (byte)0x78, (byte)0x8D, (byte)0x05, (byte)0x04, (byte)0x41, (byte)0xA1, (byte)0x36, (byte)0x27});*/
        PacketStreamReader psr = new PacketStreamReader(Base64.getDecoder().decode("2gGqAAQDBAAAw++qAAECAgM3qgAPABPMecdBdBzBzAAHAAAAAAAAAACABQRBosZZ"));
        System.out.println(Arrays.toString(psr.getSegments()));
    }
}
