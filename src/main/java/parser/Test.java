package parser;

import human.ConsoleDigitalAdapter;
import human.MyShadowingFunction;
import it.wldt.adapter.http.digital.adapter.HttpDigitalAdapter;
import it.wldt.adapter.http.digital.adapter.HttpDigitalAdapterConfiguration;
import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapter;
import it.wldt.core.engine.DigitalTwin;
import it.wldt.core.engine.DigitalTwinEngine;

public class Test {

	public static void main(String[] args) {
		 try{

	            DigitalTwin digitalTwin = new DigitalTwin("mqtt-digital-twin", new MyShadowingFunction());
	            
	            ParserCSV parser = new ParserCSV("large_dataset.csv");
	            
	            // Create an Instance of ConsoleDigital Adapter
	            ConsoleDigitalAdapter consoleDigitalAdapter = new ConsoleDigitalAdapter();

	            // Create an Instance of the MQTT Physical Adapter using the defined configuration
	            //MqttPhysicalAdapter mqttPhysicalAdapter = new MqttPhysicalAdapter("test-mqtt-pa", configMqtt);
	            
	            MqttPhysicalAdapter mqttPhysicalAdapter = new MqttPhysicalAdapter("test-mqtt-pa", parser.mqttAdapter());
	            
	            HttpDigitalAdapterConfiguration configHttp = new HttpDigitalAdapterConfiguration("test-http-da", "localhost", 3000);

	            // Create the Digital Adapter Http with its configuration and the reference of the DT instance to describe its structure
	            HttpDigitalAdapter httpDigitalAdapter = new HttpDigitalAdapter(configHttp, digitalTwin);
	            
	            digitalTwin.addPhysicalAdapter(mqttPhysicalAdapter);

	            // Add the HTTP Digital Adapter to the DT
	            digitalTwin.addDigitalAdapter(httpDigitalAdapter);

	            //Add both Digital and Physical Adapters to the DT
	            digitalTwin.addDigitalAdapter(consoleDigitalAdapter);
	            
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
	            
	            parser.updateDate();


	        }catch (Exception e){
	            e.printStackTrace();
	        }
	}

}
