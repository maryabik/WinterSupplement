package bc.req.wintersupplement.controller;

import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import bc.req.wintersupplement.service.RulesEngineService;
import bc.req.wintersupplement.service.WinterSupplementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WinterSupplementController {
    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private WinterSupplementService winterSupplementService;

    @Autowired
    private RulesEngineService rulesEngineService;

    private static final Logger log = LoggerFactory.getLogger(WinterSupplementController.class);

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping("/publish")
    public OutputData publishMessage(@RequestBody String inputDataJson) {
        try {
            // Deserialize input JSON
            InputData inputData = objectMapper.readValue(inputDataJson, InputData.class);

            // Process the data
            OutputData outputData = rulesEngineService.processInputDataForRulesEngine(inputData);

            String outputJson = objectMapper.writeValueAsString(outputData);
            log.info("Controller Output JSON: {}", outputJson);

            return outputData;
        } catch (Exception e) {
            log.error("Failed to process data", e);
            throw new RuntimeException("Error processing data");
        }
    }
}
