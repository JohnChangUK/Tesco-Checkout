package com.johnc.model;

import com.johnc.model.item.CheckoutItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class Receipt {

    private List<CheckoutItem> items;
    private BigDecimal totalOriginalPrice;
    private BigDecimal discountedPrice;
    private BigDecimal totalSavings;

    public BigDecimal getTotalOriginalPrice() {
        return totalOriginalPrice;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public BigDecimal getTotalSavings() {
        return totalSavings;
    }

    public Receipt setItems(List<CheckoutItem> items) {
        this.items = items;
        return this;
    }

    public Receipt setTotalOriginalPrice(BigDecimal totalOriginalPrice) {
        this.totalOriginalPrice = totalOriginalPrice;
        return this;
    }

    public Receipt setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
        return this;
    }

    public void setTotalSavings(BigDecimal totalSavings) {
        this.totalSavings = totalSavings;
    }

    @Override
    public String toString() {
        List<String> itemNames = items.stream()
                .map(CheckoutItem::toString)
                .collect(Collectors.toList());
        String items = String.join("", itemNames);
        return "Shopping receipt\n" +
                "Items purchased:\n" + items +
                "Total original price: £" + totalOriginalPrice + "\n" +
                "Discounted price: £" + discountedPrice + "\n" +
                "Total savings: £" + totalSavings;
    }
}
