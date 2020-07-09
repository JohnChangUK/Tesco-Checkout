package com.johnc.service;

import com.johnc.model.Receipt;
import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemMetaData;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.SavingsSummary;
import com.johnc.model.summary.TotalSummary;
import com.johnc.rule.DiscountRuleEngine;
import com.johnc.utils.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckoutItemService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutItemService.class);

    private final DiscountRuleEngine engine;
    private final ReceiptService receiptService;
    private final List<TotalSummary> discountSummaries;
    private BigDecimal originalTotalPrice;
    List<CheckoutItem> items;

    public CheckoutItemService(DiscountRuleEngine engine, ReceiptService receiptService,
                               String csvFile) throws EmptyItemException {
        this.engine = engine;
        this.receiptService = receiptService;
        items = getCheckoutItems(csvFile);
        this.originalTotalPrice = getOriginalTotalPrice(items);
        this.discountSummaries = setDiscountSummaries(items);
    }

    private List<TotalSummary> setDiscountSummaries(List<CheckoutItem> items) {
        ItemSummary itemSummary = getItemSummaries(items);
        return engine.getDiscountSummaries(items, itemSummary);
    }

    public Receipt getReceipt() {
        List<List<SavingsSummary>> totalSavingSummaries = discountSummaries.stream()
                .map(TotalSummary::getSavingsSummaries)
                .collect(Collectors.toList());

        return receiptService.getReceipt(totalSavingSummaries, originalTotalPrice, items);
    }

    private ItemSummary getItemSummaries(List<CheckoutItem> items) {
        Map<BigDecimal, ItemMetaData> priceSummaryMap = new HashMap<>();
        Map<String, ItemMetaData> groupIdCountMap = new HashMap<>();
        Map<String, ItemMetaData> itemIdCountMap = new HashMap<>();
        for (CheckoutItem item : items) {
            addSummary(priceSummaryMap, item, item.getPrice());
            addSummary(groupIdCountMap, item, item.getGroupId());
            addSummary(itemIdCountMap, item, item.getItemId());
        }
        return new ItemSummary(priceSummaryMap, groupIdCountMap, itemIdCountMap);
    }

    private BigDecimal getOriginalTotalPrice(List<CheckoutItem> items) {
        return items.stream()
                .map(CheckoutItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private <T extends Comparable<T>> void addSummary(Map<T, ItemMetaData> itemMetaMap, CheckoutItem item, T id) {
        ItemMetaData itemIdSummary = itemMetaMap.get(id);
        if (itemIdSummary == null) {
            itemMetaMap.put(id, new ItemMetaData(item, 1));
        } else {
            itemMetaMap.put(id, itemIdSummary.setQuantity(itemIdSummary.getQuantity() + 1));
            if (!itemIdSummary.getItems().contains(item)) {
                itemIdSummary.addItem(item);
            }
        }
    }

    public List<CheckoutItem> getCheckoutItems(String csvFilePath) throws EmptyItemException {
        List<CheckoutItem> items = CSVReader.loadItemsFromCSVFile(csvFilePath);
        if (items == null || items.isEmpty()) {
            throw new EmptyItemException("At least one item must be provided in the checkout");
        }
        return items;
    }
}
