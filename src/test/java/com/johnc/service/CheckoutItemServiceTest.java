package com.johnc.service;

import com.johnc.model.Receipt;
import com.johnc.rule.DiscountRuleEngine;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

public class CheckoutItemServiceTest {

    private DiscountRuleEngine discountRuleEngine;
    private ReceiptService receiptService;
    private static final String EMPTY_CSV_FILE = "src/test/resources/empty_item.csv";
    private static final String TEST_CSV_FILE = "src/test/resources/test_checkout_items.csv";
    private static final String TEST_CSV_FILE_2 = "src/test/resources/test_checkout_items_2.csv";
    private static final String TEST_CSV_FILE_3 = "src/test/resources/test_checkout_items_3.csv";
    private static final String TEST_RULE_DISCOUNT_CONFIG = "test-rule-config.yml";
    private static final String TEST_RULE_DISCOUNT_CONFIG_2 = "test-rule-config-2.yml";
    private static final String TEST_RULE_DISCOUNT_CONFIG_3 = "test-rule-config-3.yml";

    @Before
    public void init() {
        discountRuleEngine = new DiscountRuleEngine(TEST_RULE_DISCOUNT_CONFIG);
        receiptService = new ReceiptService();
    }

    /**
     * The test configuration 'test_checkout_items.csv' file has the items:
     * 3 Apples, 2.00 each
     * 4 Bananas, 1.00 each
     * 3 Carrots, 1.50 each
     * 4 Steaks, 2.50 each
     * The BEST discount rules to be applied from the 'test-rule-config.yml' are
     * BuyNGetYFree: Buy 3 bananas and get 3 apples free (6.00 discount)
     * BuyThreeSameGroupIdCheapestFree: Buy 3 group-id 'vegetable-1' and get cheapest free; 3 carrots were bought,
     * therefore the price of 1 carrot is free (1.50 discount)
     * ThreeForTwo: Buy 3 steaks for the price of 2, (2.50 discount)
     * Total savings: 10.00
     * Total original price: 24.50
     * New discounted price: 14.50
     */
    @Test
    public void testTheCorrectPricesAreListedOnReceiptTestCsvFile() throws EmptyItemException {
        CheckoutItemService checkoutItemService = new CheckoutItemService(discountRuleEngine, receiptService, TEST_CSV_FILE);
        Receipt receipt = checkoutItemService.getReceipt();
        assertEquals(BigDecimal.valueOf(24.50).setScale(2, RoundingMode.HALF_UP), receipt.getTotalOriginalPrice());
        assertEquals(BigDecimal.valueOf(10.00).setScale(2, RoundingMode.HALF_UP), receipt.getTotalSavings());
        assertEquals(BigDecimal.valueOf(14.50).setScale(2, RoundingMode.HALF_UP), receipt.getDiscountedPrice());
    }

    /**
     * The 2nd configuration 'test_checkout_items_2.csv' file has the items:
     * 4 Apples, 2.00 each
     * 7 Bananas, 1.00 each
     * 5 Carrots, 1.50 each
     * 3 Steaks, 2.50 each
     * The BEST discount rules to be applied from the 2nd Rule config 'test-rule-config-2.yml' are
     * BuyNGetYFree: Buy 3 steaks and get 2 apples free (4.00 discount)
     * ThreeForTwo: Buy 3 carrots for the price of 2, (1.50 discount)
     * ThreeForTwo: Buy 6 bananas for the price of 4, (2.00 discount)
     * Total savings: 7.50
     * Total original price: 24.50
     * New discounted price: 14.50
     */
    @Test
    public void testTheCorrectPricesAreListedOnReceiptTestCsvFile2() throws EmptyItemException {
        discountRuleEngine = new DiscountRuleEngine(TEST_RULE_DISCOUNT_CONFIG_2);
        CheckoutItemService checkoutItemService = new CheckoutItemService(discountRuleEngine, receiptService, TEST_CSV_FILE_2);
        Receipt receipt = checkoutItemService.getReceipt();
        assertEquals(BigDecimal.valueOf(30.00).setScale(2, RoundingMode.HALF_UP), receipt.getTotalOriginalPrice());
        assertEquals(BigDecimal.valueOf(7.50).setScale(2, RoundingMode.HALF_UP), receipt.getTotalSavings());
        assertEquals(BigDecimal.valueOf(22.50).setScale(2, RoundingMode.HALF_UP), receipt.getDiscountedPrice());
    }

    /**
     * The 3rd configuration 'test_checkout_items_3.csv' file has the items:
     * 4 Apples, 2.00 each
     * 3 Bananas, 1.00 each
     * 4 Carrots, 1.50 each
     * 3 Steaks, 2.50 each
     * The BEST discount rules to be applied from the 3rd Rule config 'test-rule-config-3.yml' are
     * BuyTwoEqualSpecialPrice: Buy 2 equal item-id 'apple-1' apples for a special price of 1.50.
     * Each apple is 2.00, so 4x apple is ORIGINALLY 8.00, but with the discount it will cost 2.00 (6.00 discount)
     * BuyNGetYFree: Buy 3 carrots and get 2 steaks free (5.00 discount)
     * ThreeForTwo: Buy 3 bananas for the price of 2, (1.00 discount)
     * Total savings: 12.00
     * Total original price: 24.50
     * New discounted price: 12.50
     */
    @Test
    public void testTheCorrectPricesAreListedOnReceiptTestCsvFile3() throws EmptyItemException {
        discountRuleEngine = new DiscountRuleEngine(TEST_RULE_DISCOUNT_CONFIG_3);
        CheckoutItemService checkoutItemService = new CheckoutItemService(discountRuleEngine, receiptService, TEST_CSV_FILE_3);
        Receipt receipt = checkoutItemService.getReceipt();
        assertEquals(BigDecimal.valueOf(24.50).setScale(2, RoundingMode.HALF_UP), receipt.getTotalOriginalPrice());
        assertEquals(BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_UP), receipt.getTotalSavings());
        assertEquals(BigDecimal.valueOf(12.50).setScale(2, RoundingMode.HALF_UP), receipt.getDiscountedPrice());
    }

    @Test(expected = EmptyItemException.class)
    public void emptyItemExceptionThrownWhenEmptyItemCSVFileConfigGiven() throws EmptyItemException {
        CheckoutItemService checkoutItemService = new CheckoutItemService(discountRuleEngine, receiptService, EMPTY_CSV_FILE);
    }
}
