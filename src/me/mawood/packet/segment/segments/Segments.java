package me.mawood.packet.segment.segments;

import me.mawood.packet.block.InvalidBlockException;

import java.util.Arrays;

public enum Segments
{
    GPS_SEGMENT(GpsSegment.TYPE_FLAG, GpsSegment.class),
    IMU_SEGMENT(ImuSegment.TYPE_FLAG, ImuSegment.class),
    BATTERY_SEGMENT(BatterySegment.TYPE_FLAG, BatterySegment.class);

    final byte[] segmentId;
    final Class segmentClass;
    Segments(byte[] segmentId, Class segmentClass)
    {
        this.segmentId = segmentId;
        this.segmentClass = segmentClass;
    }
    public static Class getSegmentClass(byte[] id) throws InvalidBlockException
    {
        for(Segments s:Segments.values())
        {
            if (Arrays.equals(s.segmentId, id)) return s.segmentClass;
        }
        throw new InvalidBlockException(String.format("Unknown segment id 0x%02X%02X", id[0], id[1]));
    }
}
