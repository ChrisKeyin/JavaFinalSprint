package com.gymmanagement.model;

import java.math.BigDecimal;

/**
 * Represents a piece of merchandise sold at the gym,
 * such as clothing, drinks, or snacks.
 */
public class GymMerch {

    private int merchId;
    private String merchName;
    private String merchType;
    private BigDecimal merchPrice;
    private int quantityInStock;

    public GymMerch() {
    }

    public GymMerch(int merchId, String merchName, String merchType,
                    BigDecimal merchPrice, int quantityInStock) {
        this.merchId = merchId;
        this.merchName = merchName;
        this.merchType = merchType;
        this.merchPrice = merchPrice;
        this.quantityInStock = quantityInStock;
    }

    public int getMerchId() {
        return merchId;
    }

    public void setMerchId(int merchId) {
        this.merchId = merchId;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getMerchType() {
        return merchType;
    }

    public void setMerchType(String merchType) {
        this.merchType = merchType;
    }

    public BigDecimal getMerchPrice() {
        return merchPrice;
    }

    public void setMerchPrice(BigDecimal merchPrice) {
        this.merchPrice = merchPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "GymMerch{" +
                "merchId=" + merchId +
                ", merchName='" + merchName + '\'' +
                ", merchType='" + merchType + '\'' +
                ", merchPrice=" + merchPrice +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
