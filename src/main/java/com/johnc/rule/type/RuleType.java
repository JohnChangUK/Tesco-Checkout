package com.johnc.rule.type;

public enum RuleType {
    THREE_FOR_TWO("ThreeForTwo"),
    BUY_TWO_EQUAL_SPECIAL_Price("BuyTwoEqualSpecialPrice"),
    BUY_THREE_SAME_CHEAPEST_fREE("BuyThreeSameGroupIdCheapestFree"),
    BUY_N_GET_Y_FREE("BuyNGetYFree");

    private final String rule;

    RuleType(String rule) {
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }
}
