package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.OwnerDevice;
import vn.softdreams.easypos.repository.OwnerDeviceRepository;
import vn.softdreams.easypos.service.OwnerDeviceService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OwnerDevice}.
 */
@Service
@Transactional
public class OwnerDeviceServiceImpl implements OwnerDeviceService {

    private final Logger log = LoggerFactory.getLogger(OwnerDeviceServiceImpl.class);

    private final OwnerDeviceRepository ownerDeviceRepository;

    public OwnerDeviceServiceImpl(OwnerDeviceRepository ownerDeviceRepository) {
        this.ownerDeviceRepository = ownerDeviceRepository;
    }

    @Override
    public OwnerDevice save(OwnerDevice ownerDevice) {
        log.debug("Request to save OwnerDevice : {}", ownerDevice);
        return ownerDeviceRepository.save(ownerDevice);
    }

    @Override
    public OwnerDevice update(OwnerDevice ownerDevice) {
        log.debug("Request to update OwnerDevice : {}", ownerDevice);
        return ownerDeviceRepository.save(ownerDevice);
    }

    @Override
    public Optional<OwnerDevice> partialUpdate(OwnerDevice ownerDevice) {
        log.debug("Request to partially update OwnerDevice : {}", ownerDevice);

        return ownerDeviceRepository
            .findById(ownerDevice.getId())
            .map(existingOwnerDevice -> {
                if (ownerDevice.getOwnerId() != null) {
                    existingOwnerDevice.setOwnerId(ownerDevice.getOwnerId());
                }
                if (ownerDevice.getName() != null) {
                    existingOwnerDevice.setName(ownerDevice.getName());
                }
                if (ownerDevice.getDeviceCode() != null) {
                    existingOwnerDevice.setDeviceCode(ownerDevice.getDeviceCode());
                }

                return existingOwnerDevice;
            })
            .map(ownerDeviceRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OwnerDevice> findAll(Pageable pageable) {
        log.debug("Request to get all OwnerDevices");
        return ownerDeviceRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerDevice> findOne(Integer id) {
        log.debug("Request to get OwnerDevice : {}", id);
        return ownerDeviceRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete OwnerDevice : {}", id);
        ownerDeviceRepository.deleteById(id);
    }
}
