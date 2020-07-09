package com.johnc.factory;

import com.johnc.model.item.Apple;
import com.johnc.model.item.Banana;
import com.johnc.model.item.Carrot;
import com.johnc.model.item.CheckoutItem;
import com.johnc.model.item.Steak;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckoutItemFactory {

    public static final String APPLE_ID = "apple-1";
    public static final String BANANA_ID = "banana-1";
    public static final String CARROT_ID = "carrot-1";
    public static final String STEAK_ID = "steak-1";

    public static CheckoutItem getCheckoutItem(
            String itemId, String groupId, int quantity, BigDecimal price) throws InvalidItemException {

        switch (itemId) {
            case APPLE_ID:
                return new Apple(UUID.randomUUID(), itemId, groupId, quantity, price);
            case BANANA_ID:
                return new Banana(UUID.randomUUID(), itemId, groupId, quantity, price);
            case CARROT_ID:
                return new Carrot(UUID.randomUUID(), itemId, groupId, quantity, price);
            case STEAK_ID:
                return new Steak(UUID.randomUUID(), itemId, groupId, quantity, price);
        }

        throw new InvalidItemException("Item with item-id: " + itemId + " does not exist," +
                " please provide a valid item-id");
    }
}
