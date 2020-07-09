package com.johnc.factory;

import com.johnc.model.Rule;
import com.johnc.rule.BuyNGetYFree;
import com.johnc.rule.BuyThreeSameCheapestFree;
import com.johnc.rule.BuyTwoEqualSpecialPrice;
import com.johnc.rule.DiscountRule;
import com.johnc.rule.ThreeForTwo;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.List;

import static com.johnc.rule.type.RuleType.BUY_N_GET_Y_FREE;
import static com.johnc.rule.type.RuleType.BUY_THREE_SAME_CHEAPEST_fREE;
import static com.johnc.rule.type.RuleType.BUY_TWO_EQUAL_SPECIAL_Price;
import static com.johnc.rule.type.RuleType.THREE_FOR_TWO;

public class DiscountRuleFactory {

    public static void addDiscounts(List<DiscountRule> discountRules, String discountConfig) {
        Yaml yaml = new Yaml(new Constructor(Rule.class));
        InputStream inputStream = DiscountRuleFactory.class
                .getClassLoader()
                .getResourceAsStream(discountConfig);

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
