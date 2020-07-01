package com.johnc.rule;

import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.SavingsSummary;
import com.johnc.model.summary.TotalSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.johnc.rule.type.RuleType.BUY_TWO_EQUAL_SPECIAL_Price;

public class BuyTwoEqualSpecialPrice implements DiscountRule {

    public static final int N_FOR_SPECIAL_VALUE = 2;
    private String itemId;
    private BigDecimal specialPrice;
    private int frequency;
    private List<SavingsSummary> savingsSummaries;

    public BuyTwoEqualSpecialPrice(String itemId, BigDecimal specialPrice) {
        this.itemId = itemId;
        this.specialPrice = specialPrice;
        this.savingsSummaries = new ArrayList<>();
    }

    @Override
    public boolean isMatch(List<CheckoutItem> items, ItemSummary itemSummary) {
        List<String> itemIds = getItemIds(items);
        frequency = Collections.frequency(itemIds, itemId);
        return frequency >= N_FOR_SPECIAL_VALUE;
    }

    @Override
    public TotalSummary getSummary(List<CheckoutItem> items, ItemSummary itemSummary) {
        CheckoutItem specialItem = getSpecialItem(items);

        if (specialItem != null) {
            int remainder = frequency % N_FOR_SPECIAL_VALUE;
            int itemCount = frequency - remainder;
            int numberOfSavings = itemCount / N_FOR_SPECIAL_VALUE;
            addRuleToSummary(items, itemCount, numberOfSavings);
        }

        return TotalSummary.builder()
                .savingsSummaries(savingsSummaries)
                .build();
    }

    private void addRuleToSummary(List<CheckoutItem> items, int itemCount, int numberOfSavings) {
        SavingsSummary savingsSummary = new SavingsSummary();
        savingsSummary.setRule(BUY_TWO_EQUAL_SPECIAL_Price);
        savingsSummary.setNumberOfSavingsApplied(numberOfSavings);
        int count = 0;
        for (CheckoutItem item : items) {
            if (count < itemCount) {
                if (item.getItemId().equals(itemId)) {
                    BigDecimal savings = item.getPrice()
                            .multiply(BigDecimal.valueOf(itemCount))
                            .subtract(specialPrice.multiply(BigDecimal.valueOf(numberOfSavings)));
                    savingsSummary.setSavings(savings);
                    savingsSummary.addItem(item);
                    count++;
                }
            }
        }
        savingsSummaries.add(savingsSummary);
    }

    private CheckoutItem getSpecialItem(List<CheckoutItem> items) {
        return items.stream()
                .filter(item -> itemId.equals(item.getItemId()))
                .findFirst()
                .orElse(null);
    }

    private List<String> getItemIds(List<CheckoutItem> items) {
        return items.stream()
                .map(CheckoutItem::getItemId)
                .collect(Collectors.toList());
    }
}
