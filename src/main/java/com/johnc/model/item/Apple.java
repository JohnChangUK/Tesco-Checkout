package com.johnc.model.item;

import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode
public class Apple implements CheckoutItem {

    private UUID uuid;
    private String itemId;
    private String groupId;
    private int quantity;
    private BigDecimal price;

    public Apple(UUID uuid, String itemId, String groupId, int quantity, BigDecimal price) {
        this.uuid = uuid;
        this.itemId = itemId;
        this.groupId = groupId;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Apple: £" + price + "\n";
    }
}
