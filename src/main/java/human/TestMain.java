package human;

import it.wldt.adapter.http.digital.adapter.HttpDigitalAdapter;
import it.wldt.adapter.http.digital.adapter.HttpDigitalAdapterConfiguration;
import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapter;
import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapterConfiguration;
import it.wldt.adapter.mqtt.physical.topic.MqttQosLevel;
import it.wldt.adapter.mqtt.physical.topic.incoming.DigitalTwinIncomingTopic;
import it.wldt.adapter.mqtt.physical.topic.incoming.MqttSubscribeFunction;
import it.wldt.adapter.physical.PhysicalAssetProperty;
import it.wldt.adapter.physical.event.PhysicalAssetPropertyWldtEvent;
import it.wldt.core.engine.DigitalTwin;
import it.wldt.core.engine.DigitalTwinEngine;
import it.wldt.core.event.WldtEvent;
import it.wldt.exception.*;
import it.wldt.storage.DefaultWldtStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TestMain {
    public static void main(String[] args) {

        try{

            DigitalTwin digitalTwin = new DigitalTwin("mqtt-digital-twin", new MyShadowingFunction());

            // Create an Instance of ConsoleDigital Adapter
            ConsoleDigitalAdapter consoleDigitalAdapter = new ConsoleDigitalAdapter();

            // Create an instance of MqttPhysical Adapter Configuration
            MqttPhysicalAdapterConfiguration configMqtt = MqttPhysicalAdapterConfiguration.builder("127.0.0.1", 1883) 
                    .addPhysicalAssetPropertyAndTopic("glycaemia", 0, "sensor/Glycaemia", Double::parseDouble) 
                    .addPhysicalAssetPropertyAndTopic("triglycerides", 0, "sensor/Triglycerides", Double::parseDouble)
                    .addPhysicalAssetPropertyAndTopic("creatinine", 0, "sensor/Creatinine", Double::parseDouble)
                    .addPhysicalAssetPropertyAndTopic("sodium", 0, "sensor/Sodium", Double::parseDouble)
                    .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/Beats", getSensorStateFunction("Beats")), createIncomingTopicRelatedPropertyList("Beats"), new ArrayList<>())
                    .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/Systolic", getSensorStateFunction("Systolic")), createIncomingTopicRelatedPropertyList("Systolic"), new ArrayList<>())
                    .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/Diastolic", getSensorStateFunction("Diastolic")), createIncomingTopicRelatedPropertyList("Diastolic"), new ArrayList<>())
                    .addPhysicalAssetEventAndTopic("hypertension", "text/plain", "sensor/hypertension", Function.identity())
                    .addPhysicalAssetEventAndTopic("hypotension", "text/plain", "sensor/hypotension", Function.identity())
                    .build();


            //.addPhysicalAssetActionAndTopic("switch-off","sensor.actuation","text/plain","sensor/actions/switch",MqttQosLevel.MQTT_QOS_2,true,actionBody -> "switch" + actionBody)
           

            //                .addPhysicalAssetPropertyAndTopic("intensity", 0, "sensor/intensity", Integer::parseInt)
            //                .addIncomingTopic(new DigitalTwinIncomingTopic("sensor/state", getSensorStateFunction()))
            //                .addPhysicalAssetProperty("temperature", 0)
            //                .addPhysicalAssetProperty("humidity", 0)
            //                .addPhysicalAssetEventAndTopic("overheating", "text/plain", "sensor/overheating", Function.identity())
            //                .addPhysicalAssetActionAndTopic("switch-off", "sensor.actuation", "text/plain", "sensor/overheating", actionBody -> "switch" + actionBody)
            //      .addPhysicalAssetAction("switch-off", "sensor.actuation", "text/plain");
            //      .addOutgoingTopic("switch-off", new ActionOutgoingTopic<String>("sensor/switch", actionEventBody -> "switch-"+actionEventBody));

            // Create an Instance of the MQTT Physical Adapter using the defined configuration
            MqttPhysicalAdapter mqttPhysicalAdapter = new MqttPhysicalAdapter("test-mqtt-pa", configMqtt);
            
            HttpDigitalAdapterConfiguration configHttp = new HttpDigitalAdapterConfiguration("test-http-da", "localhost", 3000);

            // Create the Digital Adapter Http with its configuration and the reference of the DT instance to describe its structure
            HttpDigitalAdapter httpDigitalAdapter = new HttpDigitalAdapter(configHttp, digitalTwin);

            // Add the HTTP Digital Adapter to the DT
            digitalTwin.addDigitalAdapter(httpDigitalAdapter);

            //Add both Digital and Physical Adapters to the DT
            digitalTwin.addDigitalAdapter(consoleDigitalAdapter);
            digitalTwin.addPhysicalAdapter(mqttPhysicalAdapter); 
            
            // Create a new WldtStorage instance using the default implementation and observing all the events
            //DefaultWldtStorage myStorage = new DefaultWldtStorage("test_storage", true);

            // Add the new Default Storage Instance to the Digital Twin Storage Manager
            //digitalTwin.getStorageManager().putStorage(myStorage);

            // Create the Digital Twin Engine
            DigitalTwinEngine digitalTwinEngine = new DigitalTwinEngine();

            // Add the Digital Twin to the Engine
            digitalTwinEngine.addDigitalTwin(digitalTwin);

            // Start all the DTs registered on the engine
            digitalTwinEngine.startAll();

            Thread.sleep(2000);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static List<PhysicalAssetProperty<?>> createIncomingTopicRelatedPropertyList(String property){
        List<PhysicalAssetProperty<?>> properties = new ArrayList<>();
        properties.add(new PhysicalAssetProperty<>(property, 0));
        return properties;
    }

    private static MqttSubscribeFunction getSensorStateFunction(String property){
        return msgPayload -> {
            List<WldtEvent<?>> events = new ArrayList<>();
            try {
                events.add(new PhysicalAssetPropertyWldtEvent<>(property, Integer.parseInt(msgPayload)));
            } catch (EventBusException e) {
                e.printStackTrace();
            }
            return events;
        };
    }
}

