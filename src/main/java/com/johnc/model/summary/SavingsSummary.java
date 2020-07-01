package com.johnc.model.summary;

import com.johnc.model.item.CheckoutItem;
import com.johnc.rule.type.RuleType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class SavingsSummary {

    private RuleType rule;
    private BigDecimal savings;
    private List<CheckoutItem> items;
    private int numberOfSavingsApplied;

    public SavingsSummary() {
        items = new ArrayList<>();
    }

    public void addItem(CheckoutItem item) {
        items.add(item);
    }
}
