package vn.softdreams.easypos.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ReservationUpdateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.RESERVATION_ID_NOT_NULL)
    private Integer id;

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @Size(max = 255, message = ExceptionConstants.RESERVATION_CUSTOMER_NAME_INVALID)
    private String customerName;

    @Size(max = 20, message = ExceptionConstants.RESERVATION_CUSTOMER_PHONE_INVALID)
    private String customerPhone;

    @Size(max = 10, message = ExceptionConstants.RESERVATION_ORDER_DATE_INVALID)
    private String orderDate;

    @Size(max = 10, message = ExceptionConstants.RESERVATION_ORDER_TIME_INVALID)
    private String orderTime;

    @Min(value = CommonConstants.RESERVATION_STATUS_DEFAULT, message = ExceptionConstants.RESERVATION_STATUS_INVALID)
    @Max(value = CommonConstants.RESERVATION_STATUS_CANCELED, message = ExceptionConstants.RESERVATION_STATUS_INVALID)
    private Integer status;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private String arrivalTime;

    @Min(value = 1, message = ExceptionConstants.RESERVATION_PEOPLE_COUNT_INVALID)
    private Integer peopleCount;

    @Size(max = 255, message = ExceptionConstants.RESERVATION_NOTE_INVALID)
    private String note;

    public ReservationUpdateRequest() {}

    public ReservationUpdateRequest(
        Integer id,
        Integer comId,
        String customerName,
        String customerPhone,
        String orderDate,
        String orderTime,
        Integer status,
        String arrivalTime,
        Integer peopleCount,
        String note
    ) {
        this.id = id;
        this.comId = comId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.status = status;
        this.arrivalTime = arrivalTime;
        this.peopleCount = peopleCount;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCustomerName() {
        return customerName != null ? customerName.trim() : null;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getArrivalTime() {
        return arrivalTime != null ? arrivalTime.trim() : null;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
