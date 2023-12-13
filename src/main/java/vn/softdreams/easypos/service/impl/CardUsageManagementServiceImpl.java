package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.LoyaltyCardUsage;
import vn.softdreams.easypos.repository.LoyaltyCardUsageRepository;
import vn.softdreams.easypos.service.CardUsageManagementService;

/**
 * Service Implementation for managing {@link LoyaltyCardUsage}.
 */
@Service
@Transactional
public class CardUsageManagementServiceImpl implements CardUsageManagementService {

    private final Logger log = LoggerFactory.getLogger(CardUsageManagementServiceImpl.class);

    private final LoyaltyCardUsageRepository loyaltyCardUsageRepository;

    public CardUsageManagementServiceImpl(LoyaltyCardUsageRepository loyaltyCardUsageRepository) {
        this.loyaltyCardUsageRepository = loyaltyCardUsageRepository;
    }
}
