package com.johnc.rule;

import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemMetaData;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.SavingsSummary;
import com.johnc.model.summary.TotalSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.johnc.rule.type.RuleType.THREE_FOR_TWO;

public class ThreeForTwo implements DiscountRule {

    public static final int DIVISOR_N = 3;
    private final List<SavingsSummary> savingsSummaries;

    public ThreeForTwo() {
        this.savingsSummaries = new ArrayList<>();
    }

    @Override
    public boolean isMatch(List<CheckoutItem> items, ItemSummary itemSummary) {
        return itemSummary.getPriceSummaryMap()
                .values()
                .stream()
                .anyMatch(summary -> summary.getQuantity() >= DIVISOR_N);
    }

    @Override
    public TotalSummary getSummary(List<CheckoutItem> items, ItemSummary itemSummary) {
        for (Map.Entry<BigDecimal, ItemMetaData> entry : itemSummary.getPriceSummaryMap().entrySet()) {
            BigDecimal price = entry.getKey();
            ItemMetaData priceSummary = entry.getValue();
            int itemCount = priceSummary.getQuantity();
            if (itemCount >= DIVISOR_N) {
                int numberOfSavings = itemCount / DIVISOR_N;
                BigDecimal savings = price.multiply(BigDecimal.valueOf(numberOfSavings));
                int remainder = itemCount % DIVISOR_N;
                int numberOfRulesToApply = itemCount - remainder;
                addRuleToSummary(items, savings, priceSummary, numberOfSavings, numberOfRulesToApply);
            }
        }

        return TotalSummary.builder()
                .savingsSummaries(savingsSummaries)
                .build();
    }

    private void addRuleToSummary(List<CheckoutItem> items, BigDecimal savings,
                                  ItemMetaData priceSummary, int numberOfSavings, int numberOfRulesToApply) {
        SavingsSummary savingsSummary = new SavingsSummary();
        savingsSummary.setRule(THREE_FOR_TWO);
        savingsSummary.setSavings(savings);
        savingsSummary.setNumberOfSavingsApplied(numberOfSavings);
        int count = 0;
        for (CheckoutItem item : items) {
            if (count < numberOfRulesToApply) {
                if (priceSummary.getItems().contains(item)) {
                    savingsSummary.addItem(item);
                    count++;
                }
            }
        }
        savingsSummaries.add(savingsSummary);
    }
}
