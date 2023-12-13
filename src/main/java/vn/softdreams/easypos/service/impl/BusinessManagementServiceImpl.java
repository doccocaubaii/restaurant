package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Business;
import vn.softdreams.easypos.repository.BusinessRepository;
import vn.softdreams.easypos.service.BusinessManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Business}.
 */
@Service
@Transactional
public class BusinessManagementServiceImpl implements BusinessManagementService {

    private final Logger log = LoggerFactory.getLogger(BusinessManagementServiceImpl.class);

    private final BusinessRepository businessRepository;

    public BusinessManagementServiceImpl(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Business save(Business business) {
        log.debug("Request to save Business : {}", business);
        return businessRepository.save(business);
    }

    @Override
    public Business update(Business business) {
        log.debug("Request to update Business : {}", business);
        return businessRepository.save(business);
    }

    @Override
    public Optional<Business> partialUpdate(Business business) {
        log.debug("Request to partially update Business : {}", business);

        return businessRepository
            .findById(business.getId())
            .map(existingBusiness -> {
                if (business.getType() != null) {
                    existingBusiness.setType(business.getType());
                }
                if (business.getName() != null) {
                    existingBusiness.setName(business.getName());
                }

                return existingBusiness;
            })
            .map(businessRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Business> findAll(Pageable pageable) {
        log.debug("Request to get all Businesses");
        return businessRepository.findAll(pageable);
    }

    @Override
    public ResultDTO getWithPaging(Pageable pageable, String keyword) {
        log.debug("Request to get all Businesses");
        Page<Business> businesses = businessRepository.getAll(pageable, keyword == null ? "" : Common.normalizedName(List.of(keyword)));
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.BUSINESS_GET_ALL_SUCCESS_VI,
            true,
            businesses.getContent(),
            (int) businesses.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Business> findOne(Integer id) {
        log.debug("Request to get Business : {}", id);
        return businessRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Business : {}", id);
        businessRepository.deleteById(id);
    }
}
