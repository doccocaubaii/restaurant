package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductGroupManagerResource {

    private final Logger log = LoggerFactory.getLogger(ProductGroupManagerResource.class);

    private static final String ENTITY_NAME = "productProductGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ProductGroupManagerResource() {}
    //    @PostMapping("/product-groups/create")
    //    public ResponseEntity<ProductProductGroup> createProductProductGroup(@RequestBody ProductProductGroup productProductGroup)
    //        throws URISyntaxException {
    //        log.debug("REST request to save ProductProductGroup : {}", productProductGroup);
    //        //        if (productProductGroup.getId() != null) {
    //        //            throw new BadRequestAlertException("A new productProductGroup cannot already have an ID", ENTITY_NAME, "idexists");
    //        //        }
    //        ProductProductGroup result = productProductGroupManagerService.save(productProductGroup);
    //        return ResponseEntity
    //            .created(new URI("/api/product-product-groups/"))
    //            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, null))
    //            .body(result);
    //    }
}
