package com.johnc.rule;

import com.johnc.model.Rule;
import com.johnc.model.item.CheckoutItem;
import com.johnc.model.summary.ItemSummary;
import com.johnc.model.summary.TotalSummary;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.johnc.rule.type.RuleType.BUY_N_GET_Y_FREE;
import static com.johnc.rule.type.RuleType.BUY_THREE_SAME_CHEAPEST_fREE;
import static com.johnc.rule.type.RuleType.BUY_TWO_EQUAL_SPECIAL_Price;
import static com.johnc.rule.type.RuleType.THREE_FOR_TWO;

public class DiscountRuleEngine {

    private final List<DiscountRule> discountRules;

    public DiscountRuleEngine(String discountConfig) {
        this.discountRules = new ArrayList<>();
        loadDiscountConfig(discountConfig);
    }

    public List<TotalSummary> getDiscountSummaries(List<CheckoutItem> items, ItemSummary itemSummary) {
        return discountRules.stream()
                .filter(rule -> rule.isMatch(items, itemSummary))
                .map(rule -> rule.getSummary(items, itemSummary))
                .collect(Collectors.toList());
    }

    public void loadDiscountConfig(String discountConfigFile) {
        Yaml yaml = new Yaml(new Constructor(Rule.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream(discountConfigFile);

        yaml.loadAll(inputStream).forEach(obj -> {
            Rule rule = (Rule) obj;
            if (THREE_FOR_TWO.getRule().equals(rule.getRule())) {
                discountRules.add(new ThreeForTwo());
            } else if (BUY_TWO_EQUAL_SPECIAL_Price.getRule().equals(rule.getRule())) {
                discountRules.add(new BuyTwoEqualSpecialPrice(rule.getItemId(), rule.getSpecialPrice()));
            } else if (BUY_THREE_SAME_CHEAPEST_fREE.getRule().equals(rule.getRule())) {
                discountRules.add(new BuyThreeSameCheapestFree());
            } else if (BUY_N_GET_Y_FREE.getRule().equals(rule.getRule())) {
                discountRules.add(new BuyNGetYFree(rule.getnItemId(), rule.getnQuantity(),
                        rule.getyItemId(), rule.getyQuantity()));
            }
        });
    }
}
