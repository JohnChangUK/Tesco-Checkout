package com.johnc.rule;

import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemMetaData;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.SavingsSummary;
import com.johnc.model.summary.TotalSummary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.johnc.rule.type.RuleType.BUY_N_GET_Y_FREE;

public class BuyNGetYFree implements DiscountRule {

    private String nItemId;
    private int nQuantity;
    private String yItemId;
    private int yQuantity;
    private List<SavingsSummary> savingsSummaries;
    private int numberOfSavings;
    private ItemMetaData nItemSummary;
    private ItemMetaData yItemSummary;

    public BuyNGetYFree(String nItemId, int nQuantity, String yItemId, int yQuantity) {
        this.nItemId = nItemId;
        this.nQuantity = nQuantity;
        this.yItemId = yItemId;
        this.yQuantity = yQuantity;
        this.savingsSummaries = new ArrayList<>();
    }

    @Override
    public boolean isMatch(List<CheckoutItem> items, ItemSummary itemSummary) {
        return itemSummary.getItemIdCountMap()
                .entrySet()
                .stream()
                .filter(entry -> nItemId.equals(entry.getKey()))
                .anyMatch(entry -> entry.getValue().getQuantity() >= nQuantity);
    }

    @Override
    public TotalSummary getSummary(List<CheckoutItem> items, ItemSummary itemSummary) {
        nItemSummary = itemSummary.getItemIdCountMap().get(nItemId);
        yItemSummary = itemSummary.getItemIdCountMap().get(yItemId);
        SavingsSummary savingsSummary = new SavingsSummary();
        if (nItemSummary != null && yItemSummary != null) {
            int nItemQuantity = nItemSummary.getQuantity();
            int yItemQuantity = yItemSummary.getQuantity();
            numberOfSavings = yItemQuantity / yQuantity;
            int totalNItemsToApply = numberOfSavings * nQuantity;
            int totalYItemsToApply = numberOfSavings * yQuantity;

            if (totalNItemsToApply > nItemQuantity) {
                numberOfSavings = nItemQuantity / nQuantity;
                totalNItemsToApply = numberOfSavings * nQuantity;
                totalYItemsToApply = numberOfSavings * yQuantity;
            }
            int yNoOfPriceSavings = numberOfSavings * yQuantity;
            if (yItemQuantity >= yQuantity) {
                addRuleToSummary(savingsSummary, totalNItemsToApply, totalYItemsToApply, yNoOfPriceSavings);
            }
        }

        return TotalSummary.builder()
                .savingsSummaries(savingsSummaries)
                .build();
    }

    private void addRuleToSummary(SavingsSummary savingsSummary, int totalNItemsToApply,
                                  int totalYItemsToApply, int yNoOfPriceSavings) {
        BigDecimal yItemPrice = yItemSummary.getItems().get(0).getPrice();
        BigDecimal savings = yItemPrice.multiply(BigDecimal.valueOf(yNoOfPriceSavings));
        addRuleToSavings(nItemSummary, totalNItemsToApply, savings, savingsSummary);
        addRuleToSavings(yItemSummary, totalYItemsToApply, savings, savingsSummary);
        savingsSummaries.add(savingsSummary);
    }

    private void addRuleToSavings(ItemMetaData itemSummary, int numberOfRules,
                                  BigDecimal savings, SavingsSummary savingsSummary) {
        savingsSummary.setRule(BUY_N_GET_Y_FREE);
        savingsSummary.setSavings(savings);
        savingsSummary.setNumberOfSavingsApplied(numberOfSavings);
        List<CheckoutItem> items = itemSummary.getItems();
        for (int i = 0; i < numberOfRules; i++) {
            CheckoutItem item = items.get(i);
            savingsSummary.addItem(item);
        }
    }
}
