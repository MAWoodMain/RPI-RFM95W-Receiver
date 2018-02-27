package me.mawood.rfm95w.packet.segment.segments;

import me.mawood.rfm95w.packet.block.InvalidBlockException;

import java.util.Arrays;

public enum Segments
{
    BATTERY_SEGMENT(BatterySegment.TYPE_FLAG, BatterySegment.class),
    GPS_SEGMENT(GpsSegment.TYPE_FLAG, GpsSegment.class),
    IMU_SEGMENT(ImuSegment.TYPE_FLAG, ImuSegment.class),
    UPTIME_SEGMENT(UptimeSegment.TYPE_FLAG, UptimeSegment.class);

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
