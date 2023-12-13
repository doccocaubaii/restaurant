package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.MoneyManagementService;

@RestController
@RequestMapping("/api")
public class MoneyManagementResource {

    private final Logger log = LoggerFactory.getLogger(MoneyManagementResource.class);

    private final MoneyManagementService moneyManagementService;

    private static final String ENTITY_NAME = "Expenditure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MoneyManagementResource(MoneyManagementService moneyManagementService) {
        this.moneyManagementService = moneyManagementService;
    }
}
