package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.dto.reservation.ReservationCreateRequest;
import vn.softdreams.easypos.dto.reservation.ReservationUpdateRequest;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.ReservationManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class ReservationManagementResource {

    private final Logger log = LoggerFactory.getLogger(ReservationManagementResource.class);

    private static final String ENTITY_NAME = "reservation";
    private final Validator customValidator;
    private final ReservationManagementService reservationManagementService;
    private final UserService userService;

    public ReservationManagementResource(
        Validator customValidator,
        ReservationManagementService reservationManagementService,
        UserService userService
    ) {
        this.customValidator = customValidator;
        this.reservationManagementService = reservationManagementService;
        this.userService = userService;
    }

    @PostMapping("/client/page/reservation/create")
    @CheckAuthorize(value = AuthoritiesConstants.Reservation.ADD)
    public ResponseEntity<ResultDTO> createReservation(@RequestBody ReservationCreateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return new ResponseEntity<>(reservationManagementService.create(request), HttpStatus.OK);
    }

    @GetMapping("/client/page/reservation/get-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.Reservation.VIEW)
    public ResponseEntity<ResultDTO> getAllReservations(Pageable pageable, @RequestParam(required = false) Integer status) {
        userService.getUserWithAuthorities();
        return ResponseEntity.ok().body(reservationManagementService.findAll(pageable, status));
    }

    @PutMapping("/client/page/reservation/update")
    @CheckAuthorize(value = AuthoritiesConstants.Reservation.UPDATE)
    public ResponseEntity<ResultDTO> updateReservation(@RequestBody ReservationUpdateRequest request) {
        userService.getUserWithAuthorities(request.getComId());
        Common.validateInput(customValidator, ENTITY_NAME, request);
        return ResponseEntity.ok().body(reservationManagementService.update(request));
    }

    @GetMapping("/client/page/reservation/by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Reservation.VIEW)
    public ResponseEntity<ResultDTO> getReservation(@NotNull @PathVariable("id") Integer id) {
        userService.getUserWithAuthorities();
        return ResponseEntity.ok().body(reservationManagementService.findOne(id));
    }

    @PostMapping("/client/page/reservation/delete")
    @CheckAuthorize(value = AuthoritiesConstants.Reservation.DELETE)
    public ResponseEntity<ResultDTO> deleteReservation(@RequestParam Integer id, @RequestParam Integer comId) {
        userService.getUserWithAuthorities(comId);
        return new ResponseEntity<>(reservationManagementService.delete(id, comId), HttpStatus.OK);
    }
}
