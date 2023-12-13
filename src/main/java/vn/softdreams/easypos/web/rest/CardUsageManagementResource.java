package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.CardUsageManagementService;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.LoyaltyCardUsage}.
 */
@RestController
@RequestMapping("/api")
public class CardUsageManagementResource {

    private final Logger log = LoggerFactory.getLogger(CardUsageManagementResource.class);

    private static final String ENTITY_NAME = "loyaltyCardUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardUsageManagementService loyaltyCardUsageService;

    public CardUsageManagementResource(CardUsageManagementService loyaltyCardUsageService) {
        this.loyaltyCardUsageService = loyaltyCardUsageService;
    }
}
