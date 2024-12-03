package bc.req.wintersupplement;

import bc.req.wintersupplement.model.InputData;
import bc.req.wintersupplement.model.OutputData;
import bc.req.wintersupplement.service.RulesEngineService;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RulesEngineServiceTest {

    private final RulesEngineService rulesEngineService = new RulesEngineService();

    @Test
    void testEligibilitySingleWithChildren() {
        InputData input = new InputData();
        input.setId("test123");
        input.setNumberOfChildren(2);
        input.setFamilyComposition("single");
        input.setFamilyUnitInPayForDecember(true);

        OutputData output = rulesEngineService.processInputDataForRulesEngine(input);

        assertTrue(output.isEligible());
        assertEquals(100.0, output.getBaseAmount());
        assertEquals(100.0, output.getChildrenAmount());
        assertEquals(200.0, output.getSupplementAmount());
    }

    @Test
    void testEligibilityCoupleWithoutChildren() {
        InputData input = new InputData();
        input.setId("test456");
        input.setNumberOfChildren(0);
        input.setFamilyComposition("couple");
        input.setFamilyUnitInPayForDecember(true);

        OutputData output = rulesEngineService.process(input);

        assertTrue(output.isEligible());
        assertEquals(150.0, output.getBaseAmount());
        assertEquals(0.0, output.getChildrenAmount());
        assertEquals(150.0, output.getSupplementAmount());
    }

    @Test
    void testIneligibility() {
        InputData input = new InputData();
        input.setId("test789");
        input.setNumberOfChildren(3);
        input.setFamilyComposition("single");
        input.setFamilyUnitInPayForDecember(false);

        OutputData output = rulesEngineService.process(input);

        assertFalse(output.isEligible());
        assertEquals(0.0, output.getBaseAmount());
        assertEquals(0.0, output.getChildrenAmount());
        assertEquals(0.0, output.getSupplementAmount());
    }

    @Test
    void testInvalidFamilyComposition() {
        InputData input = new InputData();
        input.setId("test000");
        input.setNumberOfChildren(1);
        input.setFamilyComposition("unknown");
        input.setFamilyUnitInPayForDecember(true);

        OutputData output = rulesEngineService.process(input);

        assertTrue(output.isEligible());
        assertEquals(0.0, output.getBaseAmount());
        assertEquals(50.0, output.getChildrenAmount());
        assertEquals(50.0, output.getSupplementAmount());
    }

}
