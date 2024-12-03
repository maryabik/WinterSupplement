package bc.req.wintersupplement.model;

import lombok.Data;

@Data
public class InputData {
    private String id;
    private int numberOfChildren;
    private String familyComposition;
    private boolean familyUnitInPayForDecember;
}
