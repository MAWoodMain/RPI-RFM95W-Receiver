package me.mawood.loraCapture.persistence;

import java.time.Instant;

public class RainMeasurement
{
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

    @Override
    public String toString() {
        return "RainMeasurement{" +
                "measurement=" + measurement +
                ", timestamp=" + timestamp.toString() +
                ", temperature=" + temperature +
                ", batteryLevel=" + batteryLevel +
                '}';
    }
}
