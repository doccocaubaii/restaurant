package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.SupplierSupplierGroup;
import vn.softdreams.easypos.repository.SupplierSupplierGroupRepository;
import vn.softdreams.easypos.service.SupplierSupplierGroupService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SupplierSupplierGroup}.
 */
@Service
@Transactional
public class SupplierSupplierGroupServiceImpl implements SupplierSupplierGroupService {

    private final Logger log = LoggerFactory.getLogger(SupplierSupplierGroupServiceImpl.class);

    private final SupplierSupplierGroupRepository supplierSupplierGroupRepository;

    public SupplierSupplierGroupServiceImpl(SupplierSupplierGroupRepository supplierSupplierGroupRepository) {
        this.supplierSupplierGroupRepository = supplierSupplierGroupRepository;
    }

    @Override
    public SupplierSupplierGroup save(SupplierSupplierGroup supplierSupplierGroup) {
        log.debug("Request to save SupplierSupplierGroup : {}", supplierSupplierGroup);
        return supplierSupplierGroupRepository.save(supplierSupplierGroup);
    }

    @Override
    public SupplierSupplierGroup update(SupplierSupplierGroup supplierSupplierGroup) {
        log.debug("Request to update SupplierSupplierGroup : {}", supplierSupplierGroup);
        return supplierSupplierGroupRepository.save(supplierSupplierGroup);
    }

    @Override
    public Optional<SupplierSupplierGroup> partialUpdate(SupplierSupplierGroup supplierSupplierGroup) {
        log.debug("Request to partially update SupplierSupplierGroup : {}", supplierSupplierGroup);

        return supplierSupplierGroupRepository
            .findById(supplierSupplierGroup.getId())
            .map(existingSupplierSupplierGroup -> {
                if (supplierSupplierGroup.getSupplierId() != null) {
                    existingSupplierSupplierGroup.setSupplierId(supplierSupplierGroup.getSupplierId());
                }
                if (supplierSupplierGroup.getSupplierGroupId() != null) {
                    existingSupplierSupplierGroup.setSupplierGroupId(supplierSupplierGroup.getSupplierGroupId());
                }

                return existingSupplierSupplierGroup;
            })
            .map(supplierSupplierGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierSupplierGroup> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierSupplierGroups");
        return supplierSupplierGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierSupplierGroup> findOne(Integer id) {
        log.debug("Request to get SupplierSupplierGroup : {}", id);
        return supplierSupplierGroupRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete SupplierSupplierGroup : {}", id);
        supplierSupplierGroupRepository.deleteById(id);
    }
}
