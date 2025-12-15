package com.gymmanagement.service;

import com.gymmanagement.dao.GymMerchDAO;
import com.gymmanagement.model.GymMerch;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for gym merchandise operations.
 * <p>
 * Admins use this indirectly (through the app layer) to add items and
 * view the total value of stock. Members and trainers can list items.
 */
public class GymMerchService {

    private final GymMerchDAO gymMerchDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public GymMerchService() {
        this.gymMerchDAO = new GymMerchDAO();
    }

    public GymMerchService(GymMerchDAO gymMerchDAO) {
        this.gymMerchDAO = gymMerchDAO;
    }

    /**
     * Adds a new merchandise item to the catalog.
     *
     * @param name     item name
     * @param type     item type (e.g. Gear, Drink, Food)
     * @param price    price per unit
     * @param quantity number of units in stock
     * @return created {@link GymMerch} or {@code null} if creation failed
     */
    public GymMerch addMerchItem(String name, String type, BigDecimal price, int quantity) {
        GymMerch merch = new GymMerch(0, name, type, price, quantity);
        GymMerch created = gymMerchDAO.createMerch(merch);
        if (created != null) {
            LOGGER.info("Merch item added: " + name + ", quantity=" + quantity);
        } else {
            LOGGER.warning("Merch item add failed: " + name);
        }
        return created;
    }

    /**
     * Returns all merchandise items.
     *
     * @return list of merch items
     */
    public List<GymMerch> getAllMerch() {
        return gymMerchDAO.getAllMerch();
    }

    /**
     * Returns the total stock value of all merch.
     *
     * @return total stock value
     */
    public BigDecimal getTotalStockValue() {
        return gymMerchDAO.getTotalStockValue();
    }
}
