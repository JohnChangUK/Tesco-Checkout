package com.johnc.rule;

import com.johnc.factory.DiscountRuleFactory;
import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.TotalSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountRuleEngine {

    private final List<DiscountRule> discountRules;

    public DiscountRuleEngine(String discountConfig) {
        this.discountRules = new ArrayList<>();
        DiscountRuleFactory.addDiscounts(discountRules, discountConfig);
    }

    public List<TotalSummary> getDiscountSummaries(List<CheckoutItem> items, ItemSummary itemSummary) {
        return discountRules.stream()
                .filter(rule -> rule.isMatch(items, itemSummary))
                .map(rule -> rule.getSummary(items, itemSummary))
                .collect(Collectors.toList());
    }
}
