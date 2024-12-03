package bc.req.wintersupplement.service;

import bc.req.wintersupplement.config.MqttConfig;
import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RulesEngineService {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final Logger log = LoggerFactory.getLogger(MqttConfig.class);


    public String generateMqttTopicId() {
        return UUID.randomUUID().toString();
    }

    public OutputData processInputDataForRulesEngine(InputData inputData) {
        OutputData outputData = new OutputData();
        outputData.setId(inputData.getId());
        outputData.setEligible(inputData.isFamilyUnitInPayForDecember());

        if (!inputData.isFamilyUnitInPayForDecember()) {
            outputData.setBaseAmount(0);
            outputData.setChildrenAmount(0);
            outputData.setSupplementAmount(0);
            return outputData;
        }

        float baseAmount = 0;
        switch (inputData.getFamilyComposition()) {
            case "single":
                baseAmount = 60;
                break;
            case "couple":
                baseAmount = 120;
                break;
        }

        float childrenAmount = inputData.getNumberOfChildren() * 20;
        float supplementAmount = baseAmount + childrenAmount;

        outputData.setBaseAmount(baseAmount);
        outputData.setChildrenAmount(childrenAmount);
        outputData.setSupplementAmount(supplementAmount);

        try {
            String outputJson = objectMapper.writeValueAsString(outputData);
            log.info("Processed Output JSON: {}", outputJson);
        } catch (Exception e) {
            log.error("Failed to log output JSON", e);
        }

        return outputData;
    }


}
