package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.ExpenditureManagementService;

@RestController
@RequestMapping("/api")
public class ExpenditureManagementResource {

    private final Logger log = LoggerFactory.getLogger(ExpenditureManagementResource.class);

    private final ExpenditureManagementService expenditureManagementService;

    private static final String ENTITY_NAME = "Expenditure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ExpenditureManagementResource(ExpenditureManagementService expenditureManagementService) {
        this.expenditureManagementService = expenditureManagementService;
    }
}
