package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.Supplier;
import vn.softdreams.easypos.repository.SupplierRepository;
import vn.softdreams.easypos.service.SupplierService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Supplier}.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {

    private final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);

    private final SupplierRepository supplierRepository;

    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier update(Supplier supplier) {
        log.debug("Request to update Supplier : {}", supplier);
        return supplierRepository.save(supplier);
    }

    @Override
    public Optional<Supplier> partialUpdate(Supplier supplier) {
        log.debug("Request to partially update Supplier : {}", supplier);

        return supplierRepository
            .findById(supplier.getId())
            .map(existingSupplier -> {
                if (supplier.getComId() != null) {
                    existingSupplier.setComId(supplier.getComId());
                }
                if (supplier.getName() != null) {
                    existingSupplier.setName(supplier.getName());
                }
                if (supplier.getCode() != null) {
                    existingSupplier.setCode(supplier.getCode());
                }
                if (supplier.getCode2() != null) {
                    existingSupplier.setCode2(supplier.getCode2());
                }
                if (supplier.getTaxCode() != null) {
                    existingSupplier.setTaxCode(supplier.getTaxCode());
                }
                if (supplier.getAddress() != null) {
                    existingSupplier.setAddress(supplier.getAddress());
                }
                if (supplier.getCity() != null) {
                    existingSupplier.setCity(supplier.getCity());
                }
                if (supplier.getDistrict() != null) {
                    existingSupplier.setDistrict(supplier.getDistrict());
                }
                if (supplier.getPhoneNumber() != null) {
                    existingSupplier.setPhoneNumber(supplier.getPhoneNumber());
                }
                if (supplier.getEmail() != null) {
                    existingSupplier.setEmail(supplier.getEmail());
                }
                if (supplier.getDescription() != null) {
                    existingSupplier.setDescription(supplier.getDescription());
                }
                if (supplier.getActive() != null) {
                    existingSupplier.setActive(supplier.getActive());
                }

                return existingSupplier;
            })
            .map(supplierRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Supplier> findAll(Pageable pageable) {
        log.debug("Request to get all Suppliers");
        return supplierRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Supplier> findOne(Integer id) {
        log.debug("Request to get Supplier : {}", id);
        return supplierRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.deleteById(id);
    }
}
