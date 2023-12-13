package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Reservation;
import vn.softdreams.easypos.dto.reservation.ReservationCreateRequest;
import vn.softdreams.easypos.dto.reservation.ReservationResponse;
import vn.softdreams.easypos.dto.reservation.ReservationUpdateRequest;
import vn.softdreams.easypos.repository.ReservationRepository;
import vn.softdreams.easypos.service.ReservationManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ReservationManagementServiceImpl implements ReservationManagementService {

    private static final String ENTITY_NAME = "reservation";
    private final Logger log = LoggerFactory.getLogger(ReservationManagementServiceImpl.class);
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat(Constants.ZONED_TIME_FORMAT);
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.ZONED_DATE_FORMAT);
    private final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT);
    private final SimpleDateFormat orderTimeFormatter = new SimpleDateFormat(Constants.ZONED_DATE_TIME_RESERVATION_FORMAT);
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    public ReservationManagementServiceImpl(ReservationRepository reservationRepository, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO create(ReservationCreateRequest request) {
        log.info(ENTITY_NAME + "_create: REST request to create Reservation CustomerName: {}", request.getCustomerName());
        if (!Strings.isNullOrEmpty(request.getOrderDate())) {
            checkOrderDate(request.getOrderDate());
        }
        if (!Strings.isNullOrEmpty(request.getOrderTime())) {
            checkOrderTime(request.getOrderTime(), request.getOrderDate());
        }
        Reservation reservation = new Reservation();
        BeanUtils.copyProperties(request, reservation);
        reservation.setStatus(CommonConstants.RESERVATION_STATUS_DEFAULT);
        reservation.setNormalizedName(Common.normalizedName(Arrays.asList(reservation.getCustomerName())));

        reservationRepository.save(reservation);
        log.info(ENTITY_NAME + "_create: " + ResultConstants.RESERVATION_CREATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RESERVATION_CREATE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO update(ReservationUpdateRequest request) {
        log.info(ENTITY_NAME + "_create: REST request to create Reservation id: {}", request.getId());

        Optional<Reservation> reservationOptional = reservationRepository.findByIdAndComId(request.getId(), request.getComId());
        if (reservationOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.RESERVATION_NOT_EXISTS,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_NOT_EXISTS_CODE
            );
        }
        reservationOptional
            .map(existingReservation -> {
                return validateReservationUpdate(existingReservation, request);
            })
            .map(reservationRepository::save);

        log.info(ENTITY_NAME + "_update: " + ResultConstants.RESERVATION_UPDATE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RESERVATION_UPDATE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO delete(Integer id, Integer comId) {
        log.info(ENTITY_NAME + "_create: REST request to create Reservation id: {}", id);
        Integer countReservation = reservationRepository.countByIdAndComId(id, comId);

        if (countReservation != 1) {
            throw new InternalServerException(
                ExceptionConstants.RESERVATION_NOT_EXISTS,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_NOT_EXISTS_CODE
            );
        }
        reservationRepository.deleteById(id);
        log.info(ENTITY_NAME + "_delete: " + ResultConstants.RESERVATION_DELETE_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RESERVATION_DELETE_SUCCESS_VI, true);
    }

    private Reservation validateReservationUpdate(Reservation existingReservation, ReservationUpdateRequest request) {
        if (request.getCustomerPhone() != null) {
            existingReservation.setCustomerPhone(request.getCustomerPhone());
        }
        if (request.getOrderDate() != null) {
            if (!request.getOrderDate().isEmpty()) {
                checkOrderDate(request.getOrderDate());
            }
            existingReservation.setOrderDate(request.getOrderDate());
        }
        if (request.getOrderTime() != null) {
            if (!request.getOrderTime().isEmpty()) {
                checkOrderTime(request.getOrderTime(), request.getOrderDate());
            }
            existingReservation.setOrderTime(request.getOrderTime());
        }
        if (request.getStatus() != null) {
            if ((request.getArrivalTime() == null && Objects.equals(request.getStatus(), CommonConstants.RESERVATION_STATUS_CAME))) {
                existingReservation.setArrivalTime(
                    ZonedDateTime.now().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT))
                );
            }
            existingReservation.setStatus(request.getStatus());
        }
        if (request.getArrivalTime() != null) {
            if (!request.getArrivalTime().isEmpty()) {
                checkArrivalTime(request.getArrivalTime());
                existingReservation.setArrivalTime(request.getArrivalTime());
            }
        }
        if (request.getPeopleCount() != null) {
            existingReservation.setPeopleCount(request.getPeopleCount());
        }
        if (request.getNote() != null) {
            existingReservation.setNote(request.getNote());
        }
        if (request.getCustomerName() != null) {
            if (request.getCustomerName().isBlank()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.RESERVATION_CUSTOMER_NAME_NOT_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.RESERVATION_CUSTOMER_NAME_NOT_EMPTY_CODE
                );
            }
            existingReservation.setCustomerName(request.getCustomerName());
        }
        existingReservation.setNormalizedName(Common.normalizedName(Arrays.asList(existingReservation.getCustomerName())));
        return existingReservation;
    }

    private void checkOrderDate(String orderDate) {
        try {
            dateFormatter.setLenient(false);
            Date dateNow = dateFormatter.parse(dateFormatter.format(new Date()));
            if (dateFormatter.parse(orderDate).compareTo(dateNow) < 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.RESERVATION_ORDER_DATE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.RESERVATION_ORDER_DATE_INVALID_CODE
                );
            }
        } catch (ParseException e) {
            throw new BadRequestAlertException(
                ExceptionConstants.RESERVATION_ORDER_DATE_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_ORDER_DATE_INVALID_CODE
            );
        }
    }

    private void checkArrivalTime(String arrivalTime) {
        try {
            dateTimeFormatter.setLenient(false);
            if (dateTimeFormatter.parse(arrivalTime).getTime() < new Date().getTime()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.RESERVATION_ARRIVAL_TIME_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.RESERVATION_ARRIVAL_TIME_INVALID_CODE
                );
            }
        } catch (ParseException e) {
            throw new BadRequestAlertException(
                ExceptionConstants.RESERVATION_ARRIVAL_TIME_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_ARRIVAL_TIME_INVALID_CODE
            );
        }
    }

    private void checkOrderTime(String orderTime, String orderDate) {
        try {
            timeFormatter.setLenient(false);
            timeFormatter.parse(orderTime);
            if (orderDate == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.RESERVATION_ORDER_DATE_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.RESERVATION_ORDER_DATE_EMPTY_CODE
                );
            } else {
                String dateTimeString = orderDate + " " + orderTime;
                Date dateTime = orderTimeFormatter.parse(dateTimeString);
                if (dateTime.getTime() < new Date().getTime()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.RESERVATION_ORDER_TIME_INVALID_VI,
                        ENTITY_NAME,
                        ExceptionConstants.RESERVATION_ORDER_TIME_INVALID_CODE
                    );
                }
            }
        } catch (ParseException e) {
            throw new BadRequestAlertException(
                ExceptionConstants.RESERVATION_ORDER_TIME_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_ORDER_TIME_INVALID_CODE
            );
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResultDTO findAll(Pageable pageable, Integer status) {
        List<ReservationResponse> reservationResponseList = reservationRepository
            .findAll(pageable, userService.getCompanyId(), status)
            .getContent();

        log.debug(ENTITY_NAME + "_getWithPaging: " + ResultConstants.RESERVATION_GET_ALL_SUCCESS_VI);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.RESERVATION_GET_ALL_SUCCESS_VI,
            true,
            reservationResponseList,
            reservationResponseList.size()
        );
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResultDTO findOne(Integer id) {
        log.info(ENTITY_NAME + "_findOne: REST request to get Reservation Id: {}", id);
        Optional<Reservation> reservationOptional = reservationRepository.findByIdAndComId(id, userService.getCompanyId());

        if (reservationOptional.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.RESERVATION_NOT_EXISTS,
                ENTITY_NAME,
                ExceptionConstants.RESERVATION_NOT_EXISTS_CODE
            );
        }
        ReservationResponse response = new ReservationResponse();
        BeanUtils.copyProperties(reservationOptional.get(), response);
        log.info(ENTITY_NAME + "_findOne: " + ResultConstants.RESERVATION_GET_DETAIL_SUCCESS_VI);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.RESERVATION_GET_DETAIL_SUCCESS_VI, true, response);
    }
}
