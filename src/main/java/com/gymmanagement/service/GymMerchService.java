package com.gymmanagement.service;

import com.gymmanagement.dao.GymMerchDAO;
import com.gymmanagement.model.GymMerch;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

public class GymMerchService {

    private final GymMerchDAO gymMerchDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public GymMerchService() {
        this.gymMerchDAO = new GymMerchDAO();
    }

    public GymMerchService(GymMerchDAO gymMerchDAO) {
        this.gymMerchDAO = gymMerchDAO;
    }

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

    public List<GymMerch> getAllMerch() {
        return gymMerchDAO.getAllMerch();
    }

    public BigDecimal getTotalStockValue() {
        return gymMerchDAO.getTotalStockValue();
    }
}
