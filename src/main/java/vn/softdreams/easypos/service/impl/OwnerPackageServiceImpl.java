package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.OwnerPackage;
import vn.softdreams.easypos.repository.OwnerPackageRepository;
import vn.softdreams.easypos.service.OwnerPackageService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OwnerPackage}.
 */
@Service
@Transactional
public class OwnerPackageServiceImpl implements OwnerPackageService {

    private final Logger log = LoggerFactory.getLogger(OwnerPackageServiceImpl.class);

    private final OwnerPackageRepository ownerPackageRepository;

    public OwnerPackageServiceImpl(OwnerPackageRepository ownerPackageRepository) {
        this.ownerPackageRepository = ownerPackageRepository;
    }

    @Override
    public OwnerPackage save(OwnerPackage ownerPackage) {
        log.debug("Request to save OwnerPackage : {}", ownerPackage);
        return ownerPackageRepository.save(ownerPackage);
    }

    @Override
    public OwnerPackage update(OwnerPackage ownerPackage) {
        log.debug("Request to update OwnerPackage : {}", ownerPackage);
        return ownerPackageRepository.save(ownerPackage);
    }

    @Override
    public Optional<OwnerPackage> partialUpdate(OwnerPackage ownerPackage) {
        log.debug("Request to partially update OwnerPackage : {}", ownerPackage);

        return ownerPackageRepository
            .findById(ownerPackage.getId())
            .map(existingOwnerPackage -> {
                if (ownerPackage.getOwnedId() != null) {
                    existingOwnerPackage.setOwnedId(ownerPackage.getOwnedId());
                }
                if (ownerPackage.getPackageId() != null) {
                    existingOwnerPackage.setPackageId(ownerPackage.getPackageId());
                }
                if (ownerPackage.getStatus() != null) {
                    existingOwnerPackage.setStatus(ownerPackage.getStatus());
                }
                if (ownerPackage.getStartDate() != null) {
                    existingOwnerPackage.setStartDate(ownerPackage.getStartDate());
                }
                if (ownerPackage.getEndDate() != null) {
                    existingOwnerPackage.setEndDate(ownerPackage.getEndDate());
                }
                if (ownerPackage.getPackCount() != null) {
                    existingOwnerPackage.setPackCount(ownerPackage.getPackCount());
                }
                if (ownerPackage.getVoucherUsing() != null) {
                    existingOwnerPackage.setVoucherUsing(ownerPackage.getVoucherUsing());
                }

                return existingOwnerPackage;
            })
            .map(ownerPackageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OwnerPackage> findAll(Pageable pageable) {
        log.debug("Request to get all OwnerPackages");
        return ownerPackageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerPackage> findOne(Integer id) {
        log.debug("Request to get OwnerPackage : {}", id);
        return ownerPackageRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete OwnerPackage : {}", id);
        ownerPackageRepository.deleteById(id);
    }
}
