package vn.softdreams.easypos.service.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Area;
import vn.softdreams.easypos.domain.AreaUnit;
import vn.softdreams.easypos.domain.Bill;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.area.AreaCreateRequest;
import vn.softdreams.easypos.dto.area.AreaDeleteRequest;
import vn.softdreams.easypos.dto.area.AreaDetailResponse;
import vn.softdreams.easypos.dto.area.AreaUpdateRequest;
import vn.softdreams.easypos.dto.areaUnit.AreaUnitDetailResponse;
import vn.softdreams.easypos.dto.bill.BillUnitResponse;
import vn.softdreams.easypos.repository.AreaRepository;
import vn.softdreams.easypos.repository.AreaUnitRepository;
import vn.softdreams.easypos.repository.BillRepository;
import vn.softdreams.easypos.service.AreaManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AreaManagementServiceImpl implements AreaManagementService {

    private final Logger log = LoggerFactory.getLogger(AreaManagementServiceImpl.class);
    private static final String ENTITY_NAME = "area";
    private final AreaRepository areaRepository;
    private final AreaUnitRepository areaUnitRepository;
    private final BillRepository billRepository;
    private final UserService userService;

    private final ModelMapper mapper;

    public AreaManagementServiceImpl(
        AreaRepository areaRepository,
        AreaUnitRepository areaUnitRepository,
        BillRepository billRepository,
        UserService userService,
        ModelMapper mapper
    ) {
        this.areaRepository = areaRepository;
        this.areaUnitRepository = areaUnitRepository;
        this.billRepository = billRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllForOffline() {
        List<Area> areaResponse = areaRepository.findAllByComId(userService.getCompanyId());
        List<AreaDetailResponse> responses = Arrays.asList(mapper.map(areaResponse, AreaDetailResponse[].class));

        log.debug(ENTITY_NAME + "_getAllAreaForOffline: " + ResultConstants.AREA_GET_ALL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_GET_ALL_SUCCESS_VI, true, responses, responses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getAllWithPaging(
        Integer areaSize,
        Integer areaUnitSize,
        Integer reservationId,
        String keyword,
        String areaUnitKeyword,
        Integer areaId
    ) {
        User user = userService.getUserWithAuthorities();

        //        Lấy list area cha
        List<AreaDetailResponse> areaResponses = areaRepository.getAllWithPaging(user.getCompanyId(), areaSize, keyword, areaId);
        if (!areaResponses.isEmpty()) {
            List<Integer> areaIds;
            if (areaId != null) {
                areaIds = List.of(areaId);
            } else {
                areaIds = areaResponses.stream().map(AreaDetailResponse::getId).collect(Collectors.toList());
            }
            //          Lấy list area con
            List<AreaUnitDetailResponse> unitResponses = areaUnitRepository.getAllWithPaging(
                user.getCompanyId(),
                areaIds,
                areaUnitSize,
                areaUnitKeyword
            );
            List<Integer> unitIds = unitResponses.stream().map(AreaUnitDetailResponse::getId).collect(Collectors.toList());
            // add list AreaUnitDetailResponse to map
            Map<Integer, List<AreaUnitDetailResponse>> areaUnitMap = new LinkedHashMap<>();
            //          Lấy list bill
            List<BillUnitResponse> billUnitResponses = billRepository.getBillUnit(
                user.getCompanyId(),
                CommonConstants.BILL_DELIVERY_TYPE_IN_PLACE,
                CommonConstants.RESERVATION_STATUS_DEFAULT,
                unitIds
            );
            Map<Integer, List<BillUnitResponse>> billUnitMap = new LinkedHashMap<>();

            // add list BillUnitResponse to map
            for (BillUnitResponse billUnit : billUnitResponses) {
                List<BillUnitResponse> bills = new ArrayList<>();
                if (billUnitMap.containsKey(billUnit.getAreaUnitId())) {
                    bills = billUnitMap.get(billUnit.getAreaUnitId());
                    bills.add(billUnit);
                } else {
                    bills.add(billUnit);
                }
                billUnitMap.put(billUnit.getAreaUnitId(), bills);
            }

            for (AreaUnitDetailResponse unit : unitResponses) {
                List<AreaUnitDetailResponse> units = new ArrayList<>();
                if (billUnitMap.containsKey(unit.getId())) {
                    unit.setUsingBills(new ArrayList<>(billUnitMap.get(unit.getId())));
                } else {
                    unit.setUsingBills(new ArrayList<>());
                }
                if (areaUnitMap.containsKey(unit.getAreaId())) {
                    units = areaUnitMap.get(unit.getAreaId());
                    units.add(unit);
                } else {
                    units.add(unit);
                }
                areaUnitMap.put(unit.getAreaId(), units);
            }

            for (AreaDetailResponse area : areaResponses) {
                if (areaUnitMap.containsKey(area.getId())) {
                    area.setUnits(new ArrayList<>(areaUnitMap.get(area.getId())));
                } else {
                    area.setUnits(new ArrayList<>());
                }
            }
        }
        log.debug(ENTITY_NAME + "_getAllAreaWithPaging: " + ResultConstants.AREA_GET_ALL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_GET_ALL_SUCCESS_VI, true, areaResponses, areaResponses.size());
    }

    @Override
    public ResultDTO createArea(AreaCreateRequest request) {
        log.info(ENTITY_NAME + "_createArea: REST request to create Area name: {}", request.getName());
        if (request.getName().equalsIgnoreCase(CommonConstants.ERROR_NAME_ALL)) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_NAME_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_NAME_INVALID_CODE
            );
        }
        // check duplicate AreaName in one Company
        if (areaRepository.countByComIdAndName(request.getComId(), request.getName()) == 0) {
            Area area = new Area();
            area.setName(request.getName());
            area.setComId(request.getComId());
            area.setNormalizedName(Common.normalizedName(Arrays.asList(area.getName())));
            areaRepository.save(area);

            log.info(ENTITY_NAME + "_create: " + ResultConstants.AREA_CREATE_SUCCESS_VI);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_CREATE_SUCCESS_VI, true);
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_NAME_DUPLICATE_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_NAME_DUPLICATE_CODE
            );
        }
    }

    @Override
    public ResultDTO updateArea(AreaUpdateRequest request) {
        log.info(ENTITY_NAME + "_updateArea: REST request to update Area id: {}", request.getId());
        Optional<Area> getAreaOptional = areaRepository.findByIdAndComId(request.getId(), userService.getCompanyId());
        if (getAreaOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.AREA_NOT_EXISTS, ENTITY_NAME, ExceptionConstants.AREA_NOT_EXISTS_CODE);
        }
        if (request.getName() != null) {
            Area area = getAreaOptional.get();
            if (request.getName().isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_NAME_NOT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_NAME_NOT_EMPTY_CODE
                );
            }
            if (request.getName().equalsIgnoreCase(CommonConstants.ERROR_NAME_ALL)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_NAME_INVALID_CODE,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_NAME_INVALID_VI
                );
            }
            // check duplicate AreaName in one Company
            if (
                !Objects.equals(getAreaOptional.get().getName(), request.getName()) &&
                areaRepository.countByComIdAndName(request.getComId(), request.getName()) > 0
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.AREA_NAME_DUPLICATE_VI,
                    ENTITY_NAME,
                    ExceptionConstants.AREA_NAME_DUPLICATE_CODE
                );
            }
            area.setName(request.getName());
            area.setNormalizedName(Common.normalizedName(Arrays.asList(area.getName())));
            areaRepository.save(area);

            List<AreaUnit> areaUnits = areaUnitRepository.findByAreaId(area.getId());
            List<Integer> ids = new ArrayList<>();
            for (AreaUnit areaUnit : areaUnits) {
                ids.add(areaUnit.getId());
            }
            List<Bill> bills = billRepository.findByComIdAndAreaUnitIdIn(userService.getCompanyId(), ids);
            for (Bill bill : bills) {
                bill.setAreaName(request.getName());
            }
            billRepository.saveAll(bills);
        }
        log.info(ENTITY_NAME + "_update: " + ResultConstants.AREA_UPDATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_UPDATE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO deleteArea(AreaDeleteRequest request) {
        log.info(ENTITY_NAME + "_deleteArea: REST request to delete Area id: {}", request.getId());
        Optional<Area> areaOptional = areaRepository.findByIdAndComId(request.getId(), request.getComId());

        if (areaOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.AREA_NOT_EXISTS, ENTITY_NAME, ExceptionConstants.AREA_NOT_EXISTS_CODE);
        }
        // check: List Area in Company
        Integer areaSize = areaRepository.countByComId(request.getComId());

        if (areaSize == 1) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_DELETE_FAIL_01_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_DELETE_FAIL_01_CODE
            );
        }

        //check areaUnits exists & using in this area
        if (
            billRepository.countByStatusAndDeliveryTypeAndAreaId(
                request.getComId(),
                CommonConstants.BILL_STATUS_NOT_COMPLETE,
                CommonConstants.BILL_DELIVERY_TYPE_IN_PLACE,
                request.getId()
            ) >
            0
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.AREA_DELETE_FAIL_02_VI,
                ENTITY_NAME,
                ExceptionConstants.AREA_DELETE_FAIL_02_CODE
            );
        } else {
            areaRepository.deleteById(request.getId());
        }
        log.info(ENTITY_NAME + "_delete: " + ResultConstants.AREA_DELETE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_DELETE_SUCCESS_VI, true);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findOneArea(Integer id) {
        log.info(ENTITY_NAME + "_findOneArea: REST request to search AreaId: {}", id);
        User user = userService.getUserWithAuthorities();
        Area areaResponse = areaRepository.findByIdAndComId(id, user.getCompanyId()).get();
        if (areaResponse != null) {
            log.info(ENTITY_NAME + "_fineOne: " + ResultConstants.AREA_GET_DETAIL_SUCCESS_VI);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.AREA_GET_DETAIL_SUCCESS_VI, true, areaResponse);
        }
        throw new InternalServerException(ExceptionConstants.AREA_NOT_EXISTS, ENTITY_NAME, ExceptionConstants.AREA_NOT_EXISTS_CODE);
    }
}
