package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.CustomerCustomerGroup;
import vn.softdreams.easypos.repository.CustomerCustomerGroupRepository;
import vn.softdreams.easypos.service.CustomerCustomerGroupService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CustomerCustomerGroup}.
 */
@Service
@Transactional
public class CustomerCustomerGroupServiceImpl implements CustomerCustomerGroupService {

    private final Logger log = LoggerFactory.getLogger(CustomerCustomerGroupServiceImpl.class);

    private final CustomerCustomerGroupRepository customerCustomerGroupRepository;

    public CustomerCustomerGroupServiceImpl(CustomerCustomerGroupRepository customerCustomerGroupRepository) {
        this.customerCustomerGroupRepository = customerCustomerGroupRepository;
    }

    @Override
    public CustomerCustomerGroup save(CustomerCustomerGroup customerCustomerGroup) {
        log.debug("Request to save CustomerCustomerGroup : {}", customerCustomerGroup);
        return customerCustomerGroupRepository.save(customerCustomerGroup);
    }

    @Override
    public CustomerCustomerGroup update(CustomerCustomerGroup customerCustomerGroup) {
        log.debug("Request to update CustomerCustomerGroup : {}", customerCustomerGroup);
        return customerCustomerGroupRepository.save(customerCustomerGroup);
    }

    @Override
    public Optional<CustomerCustomerGroup> partialUpdate(CustomerCustomerGroup customerCustomerGroup) {
        log.debug("Request to partially update CustomerCustomerGroup : {}", customerCustomerGroup);

        return customerCustomerGroupRepository
            .findById(customerCustomerGroup.getId())
            .map(existingCustomerCustomerGroup -> {
                if (customerCustomerGroup.getCustomerId() != null) {
                    existingCustomerCustomerGroup.setCustomerId(customerCustomerGroup.getCustomerId());
                }
                if (customerCustomerGroup.getCustomerGroupId() != null) {
                    existingCustomerCustomerGroup.setCustomerGroupId(customerCustomerGroup.getCustomerGroupId());
                }

                return existingCustomerCustomerGroup;
            })
            .map(customerCustomerGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerCustomerGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerCustomerGroups");
        return customerCustomerGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerCustomerGroup> findOne(Integer id) {
        log.debug("Request to get CustomerCustomerGroup : {}", id);
        return customerCustomerGroupRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete CustomerCustomerGroup : {}", id);
        customerCustomerGroupRepository.deleteById(id);
    }
}
