package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.ownerDevice.OwnerDeviceResult;
import vn.softdreams.easypos.repository.OwnerDeviceRepository;
import vn.softdreams.easypos.service.OwnerDeviceManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

@Service
@Transactional
public class OwnerDeviceManagementServiceImpl implements OwnerDeviceManagementService {

    private final Logger log = LoggerFactory.getLogger(ConfigManagementServiceImpl.class);
    private final String ENTITY_NAME = "owner-device";
    private final OwnerDeviceRepository ownerDeviceRepository;

    public OwnerDeviceManagementServiceImpl(OwnerDeviceRepository ownerDeviceRepository) {
        this.ownerDeviceRepository = ownerDeviceRepository;
    }

    @Override
    public ResultDTO getWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate) {
        Page<OwnerDeviceResult> resultPage = ownerDeviceRepository.getWithPaging(pageable, ownerId, keyword, fromDate, toDate);
        log.debug(ENTITY_NAME + "_getWithPaging: " + ResultConstants.OWNER_DEVICE_GET_ALL_SUCCESS_VI);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.OWNER_DEVICE_GET_ALL_SUCCESS_VI,
            true,
            resultPage.getContent(),
            (int) resultPage.getTotalElements()
        );
    }
}
