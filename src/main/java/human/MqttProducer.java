package human;

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

import com.google.gson.Gson;

import mqttPhisicalAdapter.EngineSensor;
import mqttPhisicalAdapter.JsonProducer;
import mqttPhisicalAdapter.MessageDescriptor;

public class MqttProducer {
	private final static Logger logger = LoggerFactory.getLogger(JsonProducer.class);

    //BROKER URL
    private static String BROKER_URI = "tcp://127.0.0.1:1883";

    //Message Limit generated and sent by the producer
    private static final int MESSAGE_COUNT = 100;

    private static final long SLEEP_TIME_MS = 1500;

    //Topic used to publish generated demo data
    private static final String TOPIC1 = "sensor/Beats";
    private static final String TOPIC2 = "sensor/Systolic";
    private static final String TOPIC3 = "sensor/Diastolic";
    private static final String TOPIC4 = "sensor/Glycaemia";
    private static final String TOPIC5 = "sensor/Triglycerides";
    private static final String TOPIC6 = "sensor/Creatinine";
    private static final String TOPIC7 = "sensor/Sodium";
    

    public static void main(String[] args) {

        logger.info("JsonProducer started ...");

        try{

            //Generate a random MQTT client ID using the UUID class
            String mqttClientId = UUID.randomUUID().toString();

            //Represents a persistent data store, used to store outbound and inbound messages while they
            //are in flight, enabling delivery to the QoS specified. In that case use a memory persistence.
            //When the application stops all the temporary data will be deleted.
            MqttClientPersistence persistence = new MemoryPersistence();

            //The persistence is not passed to the constructor the default file persistence is used.
            //In case of a file-based storage the same MQTT client UUID should be used
            IMqttClient client = new MqttClient(BROKER_URI,mqttClientId, persistence);

            //Define MQTT Connection Options such as reconnection, persistent/clean session and connection timeout
            //Authentication option can be added -> See AuthProducer example
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            client.connect(options);

            //Connect to the target broker
            logger.info("Connected ! Client Id: {}", mqttClientId);

            //Create an instance of an Engine Temperature Sensor
            SmartWatch MySmartWatch = new SmartWatch();
            Sensor MySensor = new Sensor();

            //Start to publish MESSAGE_COUNT messages
            for(int i = 0; i < MESSAGE_COUNT; i++) {

                //Get updated temperature value and build the associated Json Message
                //through the internal method buildJsonMessage
            	String payloadBeats         = "" + MySmartWatch.getBeatsMinute();
            	String payloadSystolic      = "" + MySmartWatch.getSystolicPressure();
            	String payloadDiastolic     = "" + MySmartWatch.getDiastolicPressure();
            	String payloadGlycaemia     = "" + MySensor.getGlycaemia();
            	String payloadTriglycerides = "" + MySensor.getTriglycerides();
            	String payloadCreatinine    = "" + MySensor.getCreatinine();
            	String payloadSodium        = "" + MySensor.getSodium();
            	//Internal Method to publish MQTT data using the created MQTT Client
            	if(payloadBeats != null) {
            		publishData(client, TOPIC1, payloadBeats);
            	}else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadSystolic != null) {
            		publishData(client, TOPIC2, payloadSystolic);	
            	}else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadDiastolic != null)
            		publishData(client, TOPIC3, payloadDiastolic);    		
            	else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadGlycaemia != null)
            		publishData(client, TOPIC4, payloadGlycaemia);    		
            	else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadTriglycerides != null)
            		publishData(client, TOPIC5, payloadTriglycerides);    		
            	else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadCreatinine != null)
            		publishData(client, TOPIC6, payloadCreatinine);    		
            	else
            		logger.error("Skipping message send due to NULL Payload !");
            	
            	if(payloadSodium != null)
            		publishData(client, TOPIC7, payloadSodium);    		
            	else
            		logger.error("Skipping message send due to NULL Payload !");

            	Thread.sleep(SLEEP_TIME_MS);
            }

            //Disconnect from the broker and close connection
            client.disconnect();
            client.close();

            logger.info("Disconnected !");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static String buildJsonMessage(int temperatureSensorValue, double humiditySensorValue) {

    	try {

    		Gson gson = new Gson();

        	MessageDescriptor messageDescriptor = new MessageDescriptor(System.currentTimeMillis()
                    , "ENGINE_SENSOR",
                    temperatureSensorValue, humiditySensorValue);

        	return gson.toJson(messageDescriptor);

    	}catch(Exception e) {
    		logger.error("Error creating json payload ! Message: {}", e.getLocalizedMessage());
    		return null;
    	}
    }

    /**
     * Send a target String Payload to the specified MQTT topic
     *
     * @param mqttClient
     * @param topic
     * @param msgString
     * @throws MqttException
     */
    public static void publishData(IMqttClient mqttClient, String topic, String msgString) throws MqttException {

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
}
