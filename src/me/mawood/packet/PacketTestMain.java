package me.mawood.packet;

import me.mawood.packet.block.InvalidBlockException;
import me.mawood.packet.block.PacketStreamReader;

import java.util.Arrays;

public class PacketTestMain
{
    public static void main(String[] args) throws InvalidBlockException
    {
        byte[] data = new byte[]{
                (byte) 0xAA, 0x00, 0x01, // GPS segment flags
                0x06, 0x08, 0x40, 0x49, 0x77, (byte) 0xaf, 0x4a, (byte) 0xdb, (byte) 0xc6, 0x65,        // double block 1
                0x06, 0x08, (byte) 0xbf, (byte) 0xf6, 0x57, 0x49, 0x27, (byte) 0x91, 0x3e, (byte) 0x81, // double block 2
                0x06, 0x08, 0x40, 0x3e, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};                            // double block 3
        System.out.println(Arrays.toString(new PacketStreamReader(PacketStreamReader.bytesToStream(data)).getSegments())); //
    }
}
