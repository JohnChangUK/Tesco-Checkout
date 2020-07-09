package com.johnc.factory;

import org.junit.Test;

import java.math.BigDecimal;

import static com.johnc.factory.CheckoutItemFactory.APPLE_ID;

public class CheckoutItemFactoryTest {

    private static final String INVALID_ITEM_ID = "INVALID ITEM ID";

    @Test(expected = InvalidItemException.class)
    public void exceptionThrownWhenInvalidItemIdGiven() throws InvalidItemException {
        CheckoutItemFactory.getCheckoutItem(INVALID_ITEM_ID, APPLE_ID, 3, BigDecimal.valueOf(2));
    }

}
