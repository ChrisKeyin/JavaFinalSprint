package com.gymmanagement.service;

import com.gymmanagement.dao.MembershipDAO;
import com.gymmanagement.model.Membership;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for membership-related operations.
 * <p>
 * Handles the logic for purchasing memberships and calculating
 * totals for members and the gym.
 */
public class MembershipService {

    private final MembershipDAO membershipDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
    }

    public MembershipService(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    /**
     * Purchases a new membership for a user.
     *
     * @param memberId    the ID of the member or trainer buying the membership
     * @param type        membership type (e.g. Monthly, Annual)
     * @param description free-form description
     * @param cost        price of the membership
     * @param durationMonths how many months the membership lasts
     * @return the created {@link Membership}, or {@code null} if creation failed
     */
    public Membership purchaseMembership(int memberId,
                                         String type,
                                         String description,
                                         BigDecimal cost,
                                         int durationMonths) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(durationMonths);

        Membership membership = new Membership(
                0,
                type,
                description,
                cost,
                memberId,
                startDate,
                endDate
        );

        Membership created = membershipDAO.createMembership(membership);
        if (created != null) {
            LOGGER.info("Membership purchased: memberId=" + memberId +
                    ", type=" + type + ", cost=" + cost);
        } else {
            LOGGER.warning("Membership purchase failed for memberId=" + memberId);
        }
        return created;
    }

    /**
     * Returns all memberships for a specific member.
     *
     * @param memberId the ID of the member
     * @return list of memberships
     */
    public List<Membership> getMembershipsForMember(int memberId) {
        return membershipDAO.getMembershipsByMemberId(memberId);
    }

    /**
     * Returns all memberships in the system.
     *
     * @return list of memberships
     */
    public List<Membership> getAllMemberships() {
        return membershipDAO.getAllMemberships();
    }

    /**
     * Returns the total revenue from all memberships.
     *
     * @return total revenue
     */
    public BigDecimal getTotalRevenue() {
        return membershipDAO.getTotalRevenue();
    }

    /**
     * Calculates the total amount a given member has spent on memberships.
     *
     * @param memberId the ID of the member
     * @return total membership expenses
     */
    public BigDecimal getTotalExpensesForMember(int memberId) {
        List<Membership> memberships = membershipDAO.getMembershipsByMemberId(memberId);
        BigDecimal total = BigDecimal.ZERO;
        for (Membership m : memberships) {
            if (m.getMembershipCost() != null) {
                total = total.add(m.getMembershipCost());
            }
        }
        return total;
    }
}
