package bc.req.wintersupplement.model;

import lombok.Data;

@Data
public class OutputData {
    private String id;
    private boolean isEligible;
    private float baseAmount;
    private float childrenAmount;
    private float supplementAmount;
}
