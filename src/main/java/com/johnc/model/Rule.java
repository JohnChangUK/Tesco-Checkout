package com.johnc.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    private String rule;
    private String description;
    private BigDecimal specialPrice;
    private String itemId;
    private String nItemId;
    private int nQuantity;
    private String yItemId;
    private int yQuantity;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(BigDecimal specialPrice) {
        this.specialPrice = specialPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getnItemId() {
        return nItemId;
    }

    public void setnItemId(String nItemId) {
        this.nItemId = nItemId;
    }

    public int getnQuantity() {
        return nQuantity;
    }

    public void setnQuantity(int nQuantity) {
        this.nQuantity = nQuantity;
    }

    public String getyItemId() {
        return yItemId;
    }

    public void setyItemId(String yItemId) {
        this.yItemId = yItemId;
    }

    public int getyQuantity() {
        return yQuantity;
    }

    public void setyQuantity(int yQuantity) {
        this.yQuantity = yQuantity;
    }
}
