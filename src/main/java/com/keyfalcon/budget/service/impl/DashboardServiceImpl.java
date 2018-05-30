package com.keyfalcon.budget.service.impl;

import com.keyfalcon.budget.Role;
import com.keyfalcon.budget.domain.UserData;
import com.keyfalcon.budget.repository.PurchaseItemRepository;
import com.keyfalcon.budget.repository.UserDataRepository;
import com.keyfalcon.budget.service.DashboardService;
import com.keyfalcon.budget.service.PurchaseItemService;
import com.keyfalcon.budget.service.UserDataService;
import com.keyfalcon.budget.service.dto.UserDataDTO;
import com.keyfalcon.budget.service.mapper.UserDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing UserData.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private final UserDataService userDataService = null;
    @Autowired
    private final PurchaseItemService purchaseItemService = null;


    public DashboardServiceImpl() {
    }


    @Override
    public String getDataCount(Long userId, Long userRole) {
        Long userCount;
        if(Role.getValue(userRole) == Role.SUPERADMIN) {
            userCount = userDataService.countAll();
        }
        else {
            userCount = userDataService.countByCreatedByAndUserType(String.valueOf(userId),String.valueOf(userRole));
        }
        return String.valueOf(userCount);
    }


}
