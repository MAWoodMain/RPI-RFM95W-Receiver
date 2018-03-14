package me.mawood.loraCapture.packet;

import me.mawood.loraCapture.packet.block.InvalidBlockException;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.persistence.loRaPacket.LoRaPacket;


public class PacketDecoder
{


    public static DecodedPacket decode(LoRaPacket loraPacket) throws InvalidSegmentException, PacketException, InvalidBlockException
    {

        return new DecodedPacket(loraPacket);

    }
}
