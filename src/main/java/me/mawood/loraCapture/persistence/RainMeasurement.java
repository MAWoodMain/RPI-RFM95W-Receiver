package me.mawood.loraCapture.persistence;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "rain")
public class RainMeasurement implements Persistable
{
    private int rainId;

    private short measurement;
    private Instant timestamp;
    private float temperature;
    private short batteryLevel;

    public RainMeasurement(short measurement, Instant timestamp, float temperature, short batteryLevel) {
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.batteryLevel = batteryLevel;
    }

    public RainMeasurement() {
    }


    @Id
    @Column(name = "rainId")
    @GeneratedValue
    public int getRainId()
    {
        return rainId;
    }

    public void setRainId(int rainId) {
        this.rainId = rainId;
    }

    @Basic
    @Column(name = "measurement")
    public short getMeasurement() {
        return measurement;
    }

    public void getMeasurement(short measurement) {
        this.measurement = measurement;
    }

    @Transient
    public Instant getTimestamp() {
        return timestamp;
    }

    @Basic
    @Column(name = "timestamp", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDateTime()
    {
        return Date.from(timestamp);
    }
    public void setDateTime(Date dt)
    {
    }

    @Basic
    @Column(name = "temperature")
    public float getTemperature() {
        return temperature;
    }
    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    @Basic
    @Column(name = "batteryLevel")
    public short getBatteryLevel() {
        return batteryLevel;
    }
    public void getBatteryLevel(short batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public String toString() {
        return "RainMeasurement{" +
                "measurement=" + measurement +
                ", timestamp=" + timestamp.toString() +
                ", temperature=" + temperature +
                ", batteryLevel=" + batteryLevel +
                '}';
    }

    @Override
    public void prepare() {

    }
}
