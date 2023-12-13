package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.CustomerGroup;
import vn.softdreams.easypos.repository.CustomerGroupRepository;
import vn.softdreams.easypos.service.CustomerGroupService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link CustomerGroup}.
 */
@Service
@Transactional
public class CustomerGroupServiceImpl implements CustomerGroupService {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupServiceImpl.class);

    private final CustomerGroupRepository customerGroupRepository;

    public CustomerGroupServiceImpl(CustomerGroupRepository customerGroupRepository) {
        this.customerGroupRepository = customerGroupRepository;
    }

    @Override
    public CustomerGroup save(CustomerGroup customerGroup) {
        log.debug("Request to save CustomerGroup : {}", customerGroup);
        return customerGroupRepository.save(customerGroup);
    }

    @Override
    public CustomerGroup update(CustomerGroup customerGroup) {
        log.debug("Request to update CustomerGroup : {}", customerGroup);
        return customerGroupRepository.save(customerGroup);
    }

    @Override
    public Optional<CustomerGroup> partialUpdate(CustomerGroup customerGroup) {
        log.debug("Request to partially update CustomerGroup : {}", customerGroup);

        return customerGroupRepository
            .findById(customerGroup.getId())
            .map(existingCustomerGroup -> {
                if (customerGroup.getComId() != null) {
                    existingCustomerGroup.setComId(customerGroup.getComId());
                }
                if (customerGroup.getName() != null) {
                    existingCustomerGroup.setName(customerGroup.getName());
                }
                if (customerGroup.getDescription() != null) {
                    existingCustomerGroup.setDescription(customerGroup.getDescription());
                }

                return existingCustomerGroup;
            })
            .map(customerGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerGroups");
        return customerGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerGroup> findOne(Integer id) {
        log.debug("Request to get CustomerGroup : {}", id);
        return customerGroupRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete CustomerGroup : {}", id);
        customerGroupRepository.deleteById(id);
    }
}
