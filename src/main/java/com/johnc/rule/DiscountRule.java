package com.johnc.rule;

import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.TotalSummary;

import java.util.List;

public interface DiscountRule {

    boolean isMatch(List<CheckoutItem> itemFrequency, ItemSummary itemSummary);

    TotalSummary getSummary(List<CheckoutItem> itemFrequency, ItemSummary itemSummary);
}
