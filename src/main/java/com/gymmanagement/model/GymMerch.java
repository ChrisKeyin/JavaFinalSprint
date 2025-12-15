package com.gymmanagement.model;

import java.math.BigDecimal;

/**
 * Represents a merchandise item available for purchase at the gym.
 * Stores information about merch products including name, type, price, and stock quantity.
 */
public class GymMerch {

    // Merchandise properties
    private int merchId;
    private String merchName;
    private String merchType;
    private BigDecimal merchPrice;
    private int quantityInStock;

    /**
     * Default constructor for GymMerch.
     * Creates an empty merchandise instance.
     */
    public GymMerch() {
    }

    /**
     * Constructor for GymMerch with all properties.
     * 
     * @param merchId the unique identifier for the merchandise item
     * @param merchName the name of the merchandise
     * @param merchType the category/type of merchandise (e.g., Gear, Drink, Food)
     * @param merchPrice the price of the merchandise item
     * @param quantityInStock the current quantity available in inventory
     */
    public GymMerch(int merchId, String merchName, String merchType,
                    BigDecimal merchPrice, int quantityInStock) {
        this.merchId = merchId;
        this.merchName = merchName;
        this.merchType = merchType;
        this.merchPrice = merchPrice;
        this.quantityInStock = quantityInStock;
    }

    // Getters and Setters

    /**
     * Gets the unique merchandise ID.
     */
    public int getMerchId() {
        return merchId;
    }

    /**
     * Sets the merchandise ID.
     */
    public void setMerchId(int merchId) {
        this.merchId = merchId;
    }

    /**
     * Gets the name of the merchandise.
     */
    public String getMerchName() {
        return merchName;
    }

    /**
     * Sets the name of the merchandise.
     */
    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    /**
     * Gets the type/category of merchandise.
     */
    public String getMerchType() {
        return merchType;
    }

    /**
     * Sets the type/category of merchandise.
     */
    public void setMerchType(String merchType) {
        this.merchType = merchType;
    }

    /**
     * Gets the price of the merchandise.
     */
    public BigDecimal getMerchPrice() {
        return merchPrice;
    }

    /**
     * Sets the price of the merchandise.
     */
    public void setMerchPrice(BigDecimal merchPrice) {
        this.merchPrice = merchPrice;
    }

    /**
     * Gets the quantity of this merchandise currently in stock.
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }

    /**
     * Sets the quantity of this merchandise in stock.
     */
    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    /**
     * Returns a string representation of the GymMerch object.
     * Includes all merchandise details in a readable format.
     */
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
