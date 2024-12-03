package bc.req.wintersupplement.service;

import bc.req.wintersupplement.config.MqttConfig;
import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.UUID;

@Service
public class WinterSupplementService {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MqttClient mqttClient;

    private RulesEngineService rulesEngineService;

    private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);


    public WinterSupplementService(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }
    public void processMessage(String topic, String payload ) throws Exception {
        InputData inputData = objectMapper.readValue(payload, InputData.class);
        OutputData outputData = rulesEngineService.processInputDataForRulesEngine(inputData);

        String outputTopic = "BRE/calculateWinterSupplementOutput/" + extractTopicId(topic);
        String outputPayload = objectMapper.writeValueAsString(outputData);

        MqttMessage message = new MqttMessage(outputPayload.getBytes());
        mqttClient.publish(outputTopic, message);
    }

    private String extractTopicId(String topic) {
        return topic.split("/")[2];
    }

}
