package bc.req.wintersupplement.config;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "WinterSupplementEngine";
    private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);

    @Bean
    public MqttClient mqttClient() throws Exception {
        MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());

        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);

            options.setCleanSession(true);
//            options.setUserName(MQTT_USER);
//            options.setPassword(MQTT_PASSWORD.toCharArray());

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("Message received on topic " + topic + ": " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("Delivery complete: " + token.isComplete());
                }
            });

            client.connect(options);
            log.info("Successfully connected to MQTT broker: {}", BROKER_URL);

            return client;
        } catch (MqttException e){
            log.error("Failed to create MQTT client: {}", e.getMessage(), e);
            throw e;
        }
    }
}
