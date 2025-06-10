package parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapterConfiguration;
import it.wldt.adapter.mqtt.physical.MqttPhysicalAdapterConfigurationBuilder;
import it.wldt.adapter.mqtt.physical.exception.MqttPhysicalAdapterConfigurationException;

public class ParserCSV {
	private final static Logger logger = LoggerFactory.getLogger(ParserCSV.class);

    //BROKER URL
    private static String BROKER_URI = "tcp://127.0.0.1:1883";

    //Topic used to publish generated demo data
	
	private String nomeFile;
    private List<String> allDate = new ArrayList<>();
    private List<String> dates = new ArrayList<>();
    private List<String> header = new ArrayList<>();
	
	public ParserCSV(String nome) {
		this.nomeFile = nome;
		this.leggiFile();
	}
	private void leggiFile(){
		try {
			header = Arrays.asList(Files.newBufferedReader(Paths.get(nomeFile)).readLine().split(";"));
			allDate = Files.readAllLines(Paths.get(nomeFile));
			allDate.forEach(linea -> Arrays.asList(linea.split(";")).forEach(e -> dates.add(e)));
			dates.removeAll(header);
			System.out.println(dates.size());
			//date.forEach(dati -> System.out.println("dato = "+dati));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public MqttPhysicalAdapterConfiguration mqttAdapter() {
		 try {
			 MqttPhysicalAdapterConfigurationBuilder conf = MqttPhysicalAdapterConfiguration.builder("127.0.0.1", 1883);
			 for(int i=0;i<header.size();i++) {
				 if(identifyDataType(dates.get(i)).equals("Integer")) {
					 System.out.println("Valore letto "+ dates.get(i)+ " tipo : Integer");
					 conf.addPhysicalAssetPropertyAndTopic(header.get(i), 0, "topic/"+header.get(i), Double::parseDouble);
				 }else if(identifyDataType(dates.get(i)).equals("Double")) {
					 System.out.println("Valore letto "+ dates.get(i)+ " tipo : Double");
					 conf.addPhysicalAssetPropertyAndTopic(header.get(i), 0, "topic/"+header.get(i), Integer::parseInt);
				 }else if(identifyDataType(dates.get(i)).equals("String")) {
					 System.out.println("Valore letto "+ dates.get(i)+ " tipo : String");
					 conf.addPhysicalAssetPropertyAndTopic(header.get(i), 0, "topic/"+header.get(i), String::toUpperCase);
				 }else
					 System.out.println("Valore letto "+ dates.get(i) +" da errore");
			 }
			 MqttPhysicalAdapterConfiguration configurato = conf.build();
			 return configurato;
		 } catch (MqttPhysicalAdapterConfigurationException e) {
			e.printStackTrace();
		 }
		 return null;
	}
	
	public String identifyDataType(String data){
		String input= data.strip();
		try {
	        Integer.parseInt(input);
	        return "Integer";
	    } catch (Exception e){  
        }		
		
		try {
	        Double.parseDouble(input);
	        return "Double";
	    } catch (Exception e){
        }
		
		return "String";
	}
	
	public void updateDate() {
		try{

            String mqttClientId = UUID.randomUUID().toString();

            MqttClientPersistence persistence = new MemoryPersistence();

            IMqttClient client = new MqttClient(BROKER_URI,mqttClientId, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);

            //Connect to the target broker
            logger.info("Connected ! Client Id: {}", mqttClientId);

            //Start to publish MESSAGE_COUNT messages
            for(int i = 0; i < dates.size(); i++) {
            		publishData(client,"topic/"+header.get(i%header.size()), dates.get(i).strip());
            		System.out.println(header.get(i%header.size())+" = "+ dates.get(i).strip());
            }
            //Disconnect from the broker and close connection
            client.disconnect();
            client.close();

            logger.info("Disconnected !");

        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	private void publishData(IMqttClient mqttClient, String topic, String msgString) throws MqttException {

        logger.debug("Publishing to Topic: {} Data: {}", topic, msgString);

        if (mqttClient.isConnected() && msgString != null && topic != null) {
        	
            MqttMessage msg = new MqttMessage(msgString.getBytes());
            msg.setQos(0);
            msg.setRetained(false);
            mqttClient.publish(topic,msg);
            logger.debug("Data Correctly Published !");
        }
        else{
            logger.error("Error: Topic or Msg = Null or MQTT Client is not Connected !");
        }

    }
	
	public static void main(String[] args) {
		ParserCSV a = new ParserCSV("large_dataset.csv");
		MqttPhysicalAdapterConfiguration p = a.mqttAdapter();
		a.updateDate();
	}
	
}
