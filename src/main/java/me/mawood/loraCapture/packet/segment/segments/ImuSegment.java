package me.mawood.loraCapture.packet.segment.segments;

import com.google.gson.JsonObject;
import me.mawood.loraCapture.packet.block.Block;
import me.mawood.loraCapture.packet.block.blocks.FloatBlock;
import me.mawood.loraCapture.packet.segment.InvalidSegmentException;
import me.mawood.loraCapture.packet.segment.Segment;

import javax.persistence.*;

@Entity
@Table(name = "imu_segments")
public class ImuSegment extends Segment
{
    public static final byte[] TYPE_FLAG = {0x00, 0x02};

    public ImuSegment(Block[] blocks) throws InvalidSegmentException
    {
        super(blocks);
        if(blocks.length < 3) throw new InvalidSegmentException("IMU must contain 3 blocks");
        if(!(blocks[0] instanceof FloatBlock)) throw new InvalidSegmentException("First block of imu segment must be a float");
        if(!(blocks[1] instanceof FloatBlock)) throw new InvalidSegmentException("Second block of imu segment must be a float");
        if(!(blocks[2] instanceof FloatBlock)) throw new InvalidSegmentException("Third block of imu segment must be a float");
    }

    public ImuSegment() {
    }

    @Basic
    @Column(name="x")
    public float getX()
    {
        return ((FloatBlock)blocks[0]).getData();
    }

    public void setX(float x)
    {
        //TODO implement setting block
    }

    @Basic
    @Column(name="y")
    public float getY()
    {
        return ((FloatBlock)blocks[1]).getData();
    }

    public void setY(float y)
    {
        //TODO implement setting block
    }

    @Basic
    @Column(name="z")
    public float getZ()
    {
        return ((FloatBlock)blocks[2]).getData();
    }

    public void setZ(float z)
    {
        //TODO implement setting block
    }

    @Transient
    @Override
    public JsonObject toJson()
    {
        JsonObject json = new JsonObject();
        json.addProperty("x", getX());
        json.addProperty("y", getY());
        json.addProperty("z", getZ());
        return json;
    }

    @Transient
    @Override
    public String getJsonName()
    {
        return "IMU";
    }

    @Transient
    @Override
    public String toString()
    {
        return "ImuSegment{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", z=" + getZ() +
                '}';
    }
}
