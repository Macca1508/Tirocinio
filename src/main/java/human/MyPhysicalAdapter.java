package human;

import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapter;
import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapterConfiguration;
import it.wldt.adapter.mqtt.physical.exception.MqttPhysicalAdapterConfigurationException;
import it.wldt.adapter.mqtt.physical.topic.incoming.DigitalTwinIncomingTopic;
import it.wldt.adapter.mqtt.physical.topic.incoming.MqttSubscribeFunction;
import it.wldt.adapter.physical.*;
import it.wldt.adapter.physical.event.PhysicalAssetActionWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetEventWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.adapter.physical.event.PhysicalAssetRelationshipInstanceCreatedWldtEvent;
import it.wldt.core.event.WldtEvent;
import it.wldt.exception.EventBusException;
import mqttPhisicalAdapter.MessageDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;

public class MyPhysicalAdapter{

	private MqttPhysicalAdapter mqttPhysicalAdapter;

	public MyPhysicalAdapter() {
		
		try {
			MqttPhysicalAdapterConfiguration config = MqttPhysicalAdapterConfiguration.builder("127.0.0.1", 1883)
			        .addPhysicalAssetPropertyAndTopic("intensity", 0, "sensor/intensity", Integer::parseInt)
			        .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/state", getSensorStateFunction()), createIncomingTopicRelatedPropertyList(), new ArrayList<>())
			        .build();
			//.addPhysicalAssetEventAndTopic("overheating", "text/plain", "sensor/overheating", Function.identity())
	        //.addPhysicalAssetActionAndTopic("switch-off", "sensor.actuation", "text/plain", "sensor/actions/switch", actionBody -> "switch" + actionBody)
			this.mqttPhysicalAdapter = new MqttPhysicalAdapter("test-mqtt-pa", config);
			config = MqttPhysicalAdapterConfiguration.builder("127.0.0.1", 1883)
			        .addPhysicalAssetPropertyAndTopic("intensity", 0, "sensor/intensity", Integer::parseInt)
			        .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/state", getSensorStateFunction()), createIncomingTopicRelatedPropertyList(), new ArrayList<>())
			        .build();
		} catch (MqttPhysicalAdapterConfigurationException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}	
	}
	
	public MqttPhysicalAdapter getMqttPhysicalAdapter() {
		return this.mqttPhysicalAdapter;
	}
	
	private static List<PhysicalAssetProperty<?>> createIncomingTopicRelatedPropertyList(){
        List<PhysicalAssetProperty<?>> properties = new ArrayList<>();
        properties.add(new PhysicalAssetProperty<>("temperature", 0));
        properties.add(new PhysicalAssetProperty<>("humidity", 0));
        return properties;
    }

    private static MqttSubscribeFunction getSensorStateFunction(){
        return msgPayload -> {
            MessageDescriptor md = new Gson().fromJson(msgPayload, MessageDescriptor.class);
            List<WldtEvent<?>> events = new ArrayList<>();
            try {
                events.add(new PhysicalAssetPropertyWldtEvent<>("temperature", md.getTemperatureValue()));
                events.add(new PhysicalAssetPropertyWldtEvent<>("humidity", md.getHumidityValue()));
            } catch (EventBusException e) {
                e.printStackTrace();
            }
            return events;
        };
    }

	
}

