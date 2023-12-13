package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.SupplierGroup;
import vn.softdreams.easypos.repository.SupplierGroupRepository;
import vn.softdreams.easypos.service.SupplierGroupService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link SupplierGroup}.
 */
@Service
@Transactional
public class SupplierGroupServiceImpl implements SupplierGroupService {

    private final Logger log = LoggerFactory.getLogger(SupplierGroupServiceImpl.class);

    private final SupplierGroupRepository supplierGroupRepository;

    public SupplierGroupServiceImpl(SupplierGroupRepository supplierGroupRepository) {
        this.supplierGroupRepository = supplierGroupRepository;
    }

    @Override
    public SupplierGroup save(SupplierGroup supplierGroup) {
        log.debug("Request to save SupplierGroup : {}", supplierGroup);
        return supplierGroupRepository.save(supplierGroup);
    }

    @Override
    public SupplierGroup update(SupplierGroup supplierGroup) {
        log.debug("Request to update SupplierGroup : {}", supplierGroup);
        return supplierGroupRepository.save(supplierGroup);
    }

    @Override
    public Optional<SupplierGroup> partialUpdate(SupplierGroup supplierGroup) {
        log.debug("Request to partially update SupplierGroup : {}", supplierGroup);

        return supplierGroupRepository
            .findById(supplierGroup.getId())
            .map(existingSupplierGroup -> {
                if (supplierGroup.getComId() != null) {
                    existingSupplierGroup.setComId(supplierGroup.getComId());
                }
                if (supplierGroup.getName() != null) {
                    existingSupplierGroup.setName(supplierGroup.getName());
                }
                if (supplierGroup.getDescription() != null) {
                    existingSupplierGroup.setDescription(supplierGroup.getDescription());
                }

                return existingSupplierGroup;
            })
            .map(supplierGroupRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierGroup> findAll(Pageable pageable) {
        log.debug("Request to get all SupplierGroups");
        return supplierGroupRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SupplierGroup> findOne(Integer id) {
        log.debug("Request to get SupplierGroup : {}", id);
        return supplierGroupRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete SupplierGroup : {}", id);
        supplierGroupRepository.deleteById(id);
    }
}
