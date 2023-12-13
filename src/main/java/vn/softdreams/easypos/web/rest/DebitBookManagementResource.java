package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.DebitBookManagementService;

@RestController
@RequestMapping("/api")
public class DebitBookManagementResource {

    private final Logger log = LoggerFactory.getLogger(DebitBookManagementResource.class);

    private final DebitBookManagementService debitBookManagementService;

    private static final String ENTITY_NAME = "Customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public DebitBookManagementResource(DebitBookManagementService debitBookManagementService) {
        this.debitBookManagementService = debitBookManagementService;
    }
}
