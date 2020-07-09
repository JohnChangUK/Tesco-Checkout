package com.johnc.utils;

import com.johnc.factory.CheckoutItemFactory;
import com.johnc.factory.InvalidItemException;
import com.johnc.model.item.CheckoutItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CSVReader {

    private static final Logger log = LoggerFactory.getLogger(CSVReader.class);

    public static List<CheckoutItem> loadItemsFromCSVFile(String csvFilePath) {
        List<CheckoutItem> items = null;
        try {
            File file = new File(csvFilePath);
            InputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            items = reader.lines().skip(1)
                    .map(CSVReader::getCheckoutItem)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            reader.close();
        } catch (IOException e) {
            log.error("Error: ", e);
        }

        return items;
    }

    private static List<CheckoutItem> getCheckoutItem(String line) {
        List<CheckoutItem> items = new ArrayList<>();
        String[] csv = line.split(",");
        String itemId = csv[0];
        String groupId = csv[1];
        int quantity = Integer.parseInt(csv[2]);
        BigDecimal price = new BigDecimal(csv[3]);

        try {
            for (int i = 0; i < quantity; i++) {
                CheckoutItem checkoutItem = CheckoutItemFactory.getCheckoutItem(itemId, groupId, quantity, price);
                items.add(checkoutItem);
            }
        } catch (InvalidItemException e) {
            log.error("Error: ", e);
        }

        return items;
    }
}
