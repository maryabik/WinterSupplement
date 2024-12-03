package bc.req.wintersupplement;

import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;

public class MqttListenerTest {
    @Autowired
    private MqttListener mqttListener;

    private MqttClient mockMqttClient;

    @BeforeEach
    void setUp() {
        mockMqttClient = Mockito.mock(MqttClient.class);
        mqttListener.setMqttClient(mockMqttClient); // Assume setter is added for testing
    }

    @Test
    void testOnMessageReceived() throws Exception {
        InputData inputData = new InputData();
        inputData.setId("testTopic");
        inputData.setNumberOfChildren(1);
        inputData.setFamilyComposition("single");
        inputData.setFamilyUnitInPayForDecember(true);

        String inputJson = gson.toJson(inputData);
        MqttMessage message = new MqttMessage(inputJson.getBytes());

        mqttListener.onMessageReceived("BRE/calculateWinterSupplementInput/testTopic", message);

        OutputData expectedOutput = new OutputData("testTopic", true, 100.0, 50.0, 150.0);
        String expectedJson = gson.toJson(expectedOutput);

        verify(mockMqttClient, times(1)).publish(eq("BRE/calculateWinterSupplementOutput/testTopic"),
                argThat(arg -> new String(arg.getPayload()).equals(expectedJson)));
    }
}
