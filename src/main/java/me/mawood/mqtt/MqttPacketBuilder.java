package me.mawood.mqtt;

import me.mawood.packet.segment.Segment;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
