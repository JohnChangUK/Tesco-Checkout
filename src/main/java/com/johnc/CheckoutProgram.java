package com.johnc;

import com.johnc.exception.EmptyItemException;
import com.johnc.model.Receipt;
import com.johnc.rule.DiscountRuleEngine;
import com.johnc.service.CheckoutItemService;
import com.johnc.service.ReceiptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckoutProgram implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CheckoutProgram.class);

    private final ReceiptService receiptService;
    private final DiscountRuleEngine ruleEngine;
    private final CheckoutItemService checkoutItemService;
    private Receipt receipt;

    public CheckoutProgram(String csvFile, String discountConfig) throws EmptyItemException {
        this.receiptService = new ReceiptService();
        this.ruleEngine = new DiscountRuleEngine(discountConfig);
        this.checkoutItemService = new CheckoutItemService(ruleEngine, receiptService, csvFile);
    }

    public static void main(String[] args) {
        try {
            new Thread(new CheckoutProgram(args[0], args[1])).start();
        } catch (EmptyItemException e) {
            log.error("Error: ", e);
        }
    }

    @Override
    public void run() {
        receipt = checkoutItemService.getReceipt();
        System.out.println(receipt);
    }

    public Receipt getReceipt() {
        return receipt;
    }
}
