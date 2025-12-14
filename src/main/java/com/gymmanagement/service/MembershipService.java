package com.gymmanagement.service;

import com.gymmanagement.dao.MembershipDAO;
import com.gymmanagement.model.Membership;
import com.gymmanagement.util.LoggerUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

public class MembershipService {

    private final MembershipDAO membershipDAO;
    private static final Logger LOGGER = LoggerUtil.getLogger();

    public MembershipService() {
        this.membershipDAO = new MembershipDAO();
    }

    public MembershipService(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

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

    public List<Membership> getMembershipsForMember(int memberId) {
        return membershipDAO.getMembershipsByMemberId(memberId);
    }

    public List<Membership> getAllMemberships() {
        return membershipDAO.getAllMemberships();
    }

    public BigDecimal getTotalRevenue() {
        return membershipDAO.getTotalRevenue();
    }

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
