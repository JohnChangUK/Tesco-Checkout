package com.johnc.service;

import com.johnc.model.Receipt;
import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.SavingsSummary;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToIntFunction;

public class ReceiptService {

    private Receipt receipt;
    private BigDecimal totalSavings;
    private Set<CheckoutItem> discountedItems;

    public ReceiptService() {
        this.totalSavings = BigDecimal.ZERO;
        discountedItems = new HashSet<>();
        receipt = new Receipt();
    }

    public Receipt getReceipt(List<List<SavingsSummary>> totalSavingSummaries, BigDecimal originalTotalPrice,
                              List<CheckoutItem> items) {
        Optional<SavingsSummary> highestSavings = getSavings(totalSavingSummaries);
        highestSavings.ifPresent(savingsSummary -> {
            totalSavings = totalSavings.add(savingsSummary.getSavings());
            discountedItems.addAll(savingsSummary.getItems());
            totalSavingSummaries.forEach(savingsSummaries -> savingsSummaries.remove(savingsSummary));
        });

        while (!totalSavingSummaries.stream().allMatch(List::isEmpty)) {
            addSavingsToTotal(totalSavingSummaries);
        }

        receipt.setItems(items)
                .setTotalOriginalPrice(originalTotalPrice)
                .setDiscountedPrice(originalTotalPrice.subtract(totalSavings))
                .setTotalSavings(totalSavings);

        return receipt;
    }

    private void addSavingsToTotal(List<List<SavingsSummary>> totalSavingSummaries) {
        Optional<SavingsSummary> alternateHighestSavings = getSavings(totalSavingSummaries);
        alternateHighestSavings.ifPresent(savingsSummary -> {
            for (CheckoutItem item : savingsSummary.getItems()) {
                if (discountedItems.contains(item)) {
                    totalSavingSummaries.forEach(summaries -> summaries.remove(savingsSummary));
                    return;
                }
                discountedItems.addAll(savingsSummary.getItems());
                totalSavings = totalSavings.add(savingsSummary.getSavings());
                totalSavingSummaries.forEach(summaries -> summaries.remove(savingsSummary));
            }
        });
    }

    private Optional<SavingsSummary> getSavings(List<List<SavingsSummary>> totalSavingSummaries) {
        return totalSavingSummaries.stream()
                .flatMap(Collection::stream)
                .max(Comparator.comparingInt(getSavingsPricePerDiscountApplied));
    }

    ToIntFunction<? super SavingsSummary> getSavingsPricePerDiscountApplied =
            summary -> summary.getSavings().intValue() / summary.getNumberOfSavingsApplied();
}
