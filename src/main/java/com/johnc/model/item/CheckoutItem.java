package com.johnc.model.item;

import java.math.BigDecimal;

public interface CheckoutItem {

    String getItemId();

    String getGroupId();

    BigDecimal getPrice();
}
