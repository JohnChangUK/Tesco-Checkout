package com.johnc.model.summary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ItemSummary {

    private Map<BigDecimal, ItemMetaData> priceSummaryMap;
    private Map<String, ItemMetaData> groupIdCountMap;
    private Map<String, ItemMetaData> itemIdCountMap;
}
