package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Area;
import vn.softdreams.easypos.domain.AreaUnit;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitCreateRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDeleteRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitUpdateRequest;
import vn.softdreams.easypos.repository.AreaRepository;
import vn.softdreams.easypos.repository.AreaUnitRepository;
import vn.softdreams.easypos.repository.BillRepository;
import vn.softdreams.easypos.service.AreaUnitManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class AreaUnitManagementServiceImpl implements AreaUnitManagementService {

    private final Logger log = LoggerFactory.getLogger(AreaUnitManagementServiceImpl.class);
    private static final String ENTITY_NAME = "areaUnit";
    private final BillRepository billRepository;
    private final AreaRepository areaRepository;
    private final AreaUnitRepository areaUnitRepository;
    private final UserService userService;

    public AreaUnitManagementServiceImpl(
        BillRepository billRepository,
        AreaRepository areaRepository,
        AreaUnitRepository areaUnitRepository,
        UserService userService
    ) {
        this.billRepository = billRepository;
        this.areaRepository = areaRepository;
        this.areaUnitRepository = areaUnitRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO createAreaUnit(AreaUnitCreateRequest request) {
        log.info(ENTITY_NAME + "_createAreaUnit: REST request to create AreaUnit");
        Optional<Area> areaOptional = areaRepository.findByIdAndComId(request.getAreaId(), request.getComId());

        if (areaOptional.isPresent()) {
            if (request.getName().equalsIgnoreCase(CommonConstants.ERROR_NAME_ALL)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_UNIT_NAME_INVALID_01_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_UNIT_NAME_INVALID_01_CODE
                );
            }
            // check duplicate AreaUnitName in one Area
            if (areaUnitRepository.countByAreaIdAndName(request.getAreaId(), request.getName()) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_UNIT_NAME_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_UNIT_NAME_DUPLICATE_CODE
                );
            }
            AreaUnit areaUnit = new AreaUnit();
            areaUnit.setAreaId(request.getAreaId());
            areaUnit.setName(request.getName());
            areaUnit.setComId(request.getComId());
            areaUnit.setNormalizedName(Common.normalizedName(Arrays.asList(areaUnit.getName())));
            areaUnitRepository.save(areaUnit);

            log.info(ENTITY_NAME + "_createAreaUnit: " + ResultConstants.AREA_UNIT_CREATE_SUCCESS_VI);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_UNIT_CREATE_SUCCESS_VI, true);
        }
        throw new InternalServerException(ExceptionConstants.AREA_NOT_EXISTS, ENTITY_NAME, ExceptionConstants.AREA_NOT_EXISTS_CODE);
    }

    @Override
    public ResultDTO updateAreaUnit(AreaUnitUpdateRequest request) {
        log.info(ENTITY_NAME + "_updateAreaUnit: REST request to update AreaUnit id: {}", request.getId());
        Optional<AreaUnit> areaUnit = areaUnitRepository.findByIdAndComIdAndAreaId(
            request.getId(),
            request.getComId(),
            request.getAreaId()
        );
        if (areaUnit.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.AREA_UNIT_NOT_EXISTS,
                ENTITY_NAME,
                ExceptionConstants.AREA_UNIT_NOT_EXISTS_CODE
            );
        }
        // check areaUnit using
        if (
            billRepository.countByStatusAndDeliveryTypeAndAreaUnitId(
                request.getComId(),
                CommonConstants.BILL_STATUS_NOT_COMPLETE,
                CommonConstants.BILL_DELIVERY_TYPE_IN_PLACE,
                request.getId()
            ) >
            0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_UNIT_UPDATE_FAIL_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_UNIT_UPDATE_FAIL_CODE
            );
        }
        if (request.getName() != null) {
            // update areaUnitName
            if (request.getName().isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_UNIT_NAME_NOT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_UNIT_NAME_NOT_EMPTY_CODE
                );
            }
            // check name
            if (request.getName().equalsIgnoreCase(CommonConstants.ERROR_NAME_ALL)) {
                if (request.getName().isEmpty()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.AREA_UNIT_NAME_INVALID_01_VI,
                        ENTITY_NAME,
                        ExceptionConstants.AREA_UNIT_NAME_INVALID_01_CODE
                    );
                }
            }
            if (
                !Objects.equals(areaUnit.get().getName(), request.getName()) &&
                areaUnitRepository.countByAreaIdAndName(request.getAreaId(), request.getName()) > 0
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_UNIT_NAME_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_UNIT_NAME_DUPLICATE_CODE
                );
            }
            areaUnit.get().setNormalizedName(Common.normalizedName(Arrays.asList(request.getName())));
            areaUnit.get().setName(request.getName());
        }
        areaUnitRepository.save(areaUnit.get());
        log.info(ENTITY_NAME + "_updateAreaUnit: " + ResultConstants.AREA_UNIT_UPDATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_UNIT_UPDATE_SUCCESS_VI, true);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResultDTO findOneAreaUnit(Integer id) {
        log.info(ENTITY_NAME + "_findOneAreaUnit: REST request to get AreaUnit by id: {}", id);
        Optional<AreaUnit> unitOptional = areaUnitRepository.findByIdAndComId(id, userService.getCompanyId());

        if (unitOptional.isPresent()) {
            AreaUnitDetailResponse areaUnit = new AreaUnitDetailResponse();
            BeanUtils.copyProperties(unitOptional.get(), areaUnit);
            log.info(ENTITY_NAME + "_findOneAreaUnit: " + ResultConstants.AREA_UNIT_GET_DETAIL_SUCCESS_VI);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_UNIT_GET_DETAIL_SUCCESS_VI, true, areaUnit);
        }
        throw new InternalServerException(
            ExceptionConstants.AREA_UNIT_NOT_EXISTS,
            ENTITY_NAME,
            ExceptionConstants.AREA_UNIT_NOT_EXISTS_CODE
        );
    }

    @Override
    public ResultDTO deleteAreaUnit(AreaUnitDeleteRequest request) {
        log.info(ENTITY_NAME + "_deleteAreaUnit: REST request to delete AreaUnit id: {}", request.getId());
        Optional<AreaUnit> areaUnit = areaUnitRepository.findByIdAndComIdAndAreaId(
            request.getId(),
            request.getComId(),
            request.getAreaId()
        );

        if (areaUnit.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.AREA_UNIT_NOT_EXISTS,
                ENTITY_NAME,
                ExceptionConstants.AREA_UNIT_NOT_EXISTS_CODE
            );
        }
        // check areaUnit using
        if (
            billRepository.countByStatusAndDeliveryTypeAndAreaUnitId(
                request.getComId(),
                CommonConstants.BILL_STATUS_NOT_COMPLETE,
                CommonConstants.BILL_DELIVERY_TYPE_IN_PLACE,
                request.getId()
            ) >
            0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_UNIT_DELETE_FAIL_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_UNIT_DELETE_FAIL_CODE
            );
        }
        areaUnitRepository.deleteById(request.getId());
        log.info(ENTITY_NAME + "_deleteAreaUnit: " + ResultConstants.AREA_UNIT_DELETE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_UNIT_DELETE_SUCCESS_VI, true);
    }
}
