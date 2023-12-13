package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.EPPackage;
import vn.softdreams.easypos.repository.EPPackageRepository;
import vn.softdreams.easypos.service.EPPackageService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EPPackage}.
 */
@Service
@Transactional
public class EPPackageServiceImpl implements EPPackageService {

    private final Logger log = LoggerFactory.getLogger(EPPackageServiceImpl.class);

    private final EPPackageRepository ePPackageRepository;

    public EPPackageServiceImpl(EPPackageRepository ePPackageRepository) {
        this.ePPackageRepository = ePPackageRepository;
    }

    @Override
    public EPPackage save(EPPackage ePPackage) {
        log.debug("Request to save EPPackage : {}", ePPackage);
        return ePPackageRepository.save(ePPackage);
    }

    @Override
    public EPPackage update(EPPackage ePPackage) {
        log.debug("Request to update EPPackage : {}", ePPackage);
        return ePPackageRepository.save(ePPackage);
    }

    @Override
    public Optional<EPPackage> partialUpdate(EPPackage ePPackage) {
        log.debug("Request to partially update EPPackage : {}", ePPackage);

        return ePPackageRepository
            .findById(ePPackage.getId())
            .map(existingEPPackage -> {
                if (ePPackage.getPackageCode() != null) {
                    existingEPPackage.setPackageCode(ePPackage.getPackageCode());
                }
                if (ePPackage.getPackageName() != null) {
                    existingEPPackage.setPackageName(ePPackage.getPackageName());
                }
                if (ePPackage.getDescription() != null) {
                    existingEPPackage.setDescription(ePPackage.getDescription());
                }
                if (ePPackage.getLimitCompany() != null) {
                    existingEPPackage.setLimitCompany(ePPackage.getLimitCompany());
                }
                if (ePPackage.getLimitUser() != null) {
                    existingEPPackage.setLimitUser(ePPackage.getLimitUser());
                }
                if (ePPackage.getLimitVoucher() != null) {
                    existingEPPackage.setLimitVoucher(ePPackage.getLimitVoucher());
                }
                if (ePPackage.getTime() != null) {
                    existingEPPackage.setTime(ePPackage.getTime());
                }
                if (ePPackage.getType() != null) {
                    existingEPPackage.setType(ePPackage.getType());
                }
                if (ePPackage.getStatus() != null) {
                    existingEPPackage.setStatus(ePPackage.getStatus());
                }

                return existingEPPackage;
            })
            .map(ePPackageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EPPackage> findAll(Pageable pageable) {
        log.debug("Request to get all EPPackages");
        return ePPackageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EPPackage> findOne(Integer id) {
        log.debug("Request to get EPPackage : {}", id);
        return ePPackageRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete EPPackage : {}", id);
        ePPackageRepository.deleteById(id);
    }
}
