package com.johnc.rule;

import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemMetaData;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.SavingsSummary;
import com.johnc.model.summary.TotalSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.johnc.rule.type.RuleType.BUY_THREE_SAME_CHEAPEST_fREE;

public class BuyThreeSameCheapestFree implements DiscountRule {

    public static final int BUY_N_CHEAPEST_FREE_VALUE = 3;
    private List<SavingsSummary> savingsSummaries;

    public BuyThreeSameCheapestFree() {
        this.savingsSummaries = new ArrayList<>();
    }

    @Override
    public boolean isMatch(List<CheckoutItem> items, ItemSummary itemSummary) {
        return itemSummary.getGroupIdCountMap()
                .values()
                .stream()
                .anyMatch(summary -> summary.getQuantity() >= BUY_N_CHEAPEST_FREE_VALUE);
    }

    @Override
    public TotalSummary getSummary(List<CheckoutItem> items, ItemSummary itemSummary) {
        for (Map.Entry<String, ItemMetaData> entry : itemSummary.getGroupIdCountMap().entrySet()) {
            ItemMetaData groupIdSummary = entry.getValue();
            int groupIdCount = groupIdSummary.getQuantity();

            if (groupIdCount >= BUY_N_CHEAPEST_FREE_VALUE) {
                List<CheckoutItem> groupIdItems = groupIdSummary.getItems();
                groupIdItems.sort(Comparator.comparingInt(item -> item.getPrice().intValue()));
                int numberOfSavings = groupIdCount / BUY_N_CHEAPEST_FREE_VALUE;
                BigDecimal savings = BigDecimal.ZERO;
                for (int i = 0; i < numberOfSavings; i++) {
                    CheckoutItem cheapestItem = groupIdItems.get(i);
                    savings = savings.add(cheapestItem.getPrice());
                }
                addRuleToSummary(groupIdCount, groupIdItems, numberOfSavings, savings);
            }
        }

        return TotalSummary.builder()
                .savingsSummaries(savingsSummaries)
                .build();
    }

    private void addRuleToSummary(int groupIdCount, List<CheckoutItem> groupIdItems,
                                  int numberOfSavings, BigDecimal savings) {
        SavingsSummary savingsSummary = new SavingsSummary();
        savingsSummary.setRule(BUY_THREE_SAME_CHEAPEST_fREE);
        savingsSummary.setSavings(savings);
        savingsSummary.setNumberOfSavingsApplied(numberOfSavings);
        int numberOfRulesToApply = groupIdCount - (groupIdCount % BUY_N_CHEAPEST_FREE_VALUE);
        for (int i = 0; i < numberOfRulesToApply; i++) {
            CheckoutItem item = groupIdItems.get(i);
            savingsSummary.addItem(item);
        }
        savingsSummaries.add(savingsSummary);
    }
}
