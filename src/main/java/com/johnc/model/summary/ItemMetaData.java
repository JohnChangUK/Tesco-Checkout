package com.johnc.model.summary;

import com.johnc.model.item.CheckoutItem;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemMetaData {

    private int quantity;
    private String discount;
    private final List<CheckoutItem> items = new ArrayList<>();

    public ItemMetaData(CheckoutItem checkoutItem, int quantity) {
        this.items.add(checkoutItem);
        this.quantity = quantity;
    }

    public void addItem(CheckoutItem item) {
        this.items.add(item);
    }

    public ItemMetaData setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }
}
