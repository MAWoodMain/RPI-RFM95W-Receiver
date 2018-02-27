package me.mawood.rfm95w.mqtt;

import me.mawood.rfm95w.packet.segment.Segment;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;

public class MqttPacketBuilder
{
    private final Segment[] segments;

    public MqttPacketBuilder(Segment... segments)
    {

        this.segments = segments;
    }

    public MqttMessage getMessage()
    {
        JSONObject value = new JSONObject();
        for (Segment s:segments) value.put(s.getJsonName(),s.toJson());
        JSONObject payload = new JSONObject();
        payload.put("value", value);
        MqttMessage message = new MqttMessage(payload.toJSONString().getBytes());
        message.setQos(2);
        message.setRetained(false);
        return message;
    }
}
