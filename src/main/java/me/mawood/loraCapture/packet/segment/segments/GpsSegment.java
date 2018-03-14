package me.mawood.loraCapture.packet.segment.segments;

import com.google.gson.JsonObject;
import me.mawood.loraCapture.packet.block.blocks.DoubleBlock;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.segment.Segment;

import javax.persistence.*;

@Entity
@Table(name = "gps_segments")
public class GpsSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x03};
    public GpsSegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 3) throw new InvalidSegmentException("GPS must contain 3 blocks");
        if(!(blocks[0] instanceof DoubleBlock)) throw new InvalidSegmentException("First block of gps segment must be a double");
        if(!(blocks[1] instanceof DoubleBlock)) throw new InvalidSegmentException("Second block of gps segment must be a double");
        if(!(blocks[2] instanceof DoubleBlock)) throw new InvalidSegmentException("Third block of gps segment must be a double");
    }

    public GpsSegment() {
    }

    @Basic
    @Column(name="latitude")
    public double getLatitude()
    {
        return ((DoubleBlock)blocks[0]).getData();
    }

    public void setLatitude(double val)
    {
        //TODO implement setting block
    }

    @Basic
    @Column(name="longitude")
    public double getLongitude()
    {
        return ((DoubleBlock)blocks[1]).getData();
    }

    public void setLongitude(double val)
    {
        //TODO implement setting block
    }

    @Basic
    @Column(name="altitude")
    public double getAltitude()
    {
        return ((DoubleBlock)blocks[2]).getData();
    }

    public void setAltitude(double val)
    {
        //TODO implement setting block
    }

    @Transient
    @Override
    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("lat", getLatitude());
        json.addProperty("long", getLongitude());
        json.addProperty("alt", getAltitude());
        return json;
    }

    @Transient
    @Override
    public String getJsonName()
    {
        return "GPS";
    }

    @Transient
    @Override
    public String toString()
    {
        return "GpsSegment{" +
                "latitude=" + getLatitude() +
                ", longitude=" + getLongitude() +
                ", altitude=" + getAltitude() +
                '}';
    }
}
