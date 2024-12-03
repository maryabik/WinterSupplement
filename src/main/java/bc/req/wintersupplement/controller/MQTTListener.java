package bc.req.wintersupplement.controller;

import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import bc.req.wintersupplement.service.RulesEngineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.gson.Gson;

public class MQTTListener {

    private static final String INPUT_TOPIC = "BRE/calculateWinterSupplementInput/";
    private static final String OUTPUT_TOPIC = "BRE/calculateWinterSupplementOutput/";

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private RulesEngineService rulesEngineService;

    ObjectMapper objectMapper = new ObjectMapper();

    public void MqttListener() throws Exception {
        mqttClient.subscribe(INPUT_TOPIC + "#", this::onMessageReceived);
    }

    private void onMessageReceived(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            InputData inputData = objectMapper.readValue(payload, InputData.class);

            OutputData outputData = rulesEngineService.processInputDataForRulesEngine(inputData);
            String outputPayload = objectMapper.writeValueAsString(outputData);

            String outputTopic = OUTPUT_TOPIC + inputData.getId();
            mqttClient.publish(outputTopic, new MqttMessage(outputPayload.getBytes()));
            System.out.println("Published: " + outputPayload + " to " + outputTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
