package vn.softdreams.easypos.dto.reservation;

import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ReservationCreateRequest implements Serializable {

    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @NotBlank(message = ExceptionConstants.RESERVATION_CUSTOMER_NAME_NOT_EMPTY)
    @Size(max = 255, message = ExceptionConstants.RESERVATION_CUSTOMER_NAME_INVALID)
    private String customerName;

    @Size(max = 20, message = ExceptionConstants.RESERVATION_CUSTOMER_PHONE_INVALID)
    private String customerPhone;

    @Size(max = 10, message = ExceptionConstants.RESERVATION_ORDER_DATE_INVALID)
    private String orderDate;

    @Size(max = 10, message = ExceptionConstants.RESERVATION_ORDER_TIME_INVALID)
    private String orderTime;

    @Min(value = 1, message = ExceptionConstants.RESERVATION_PEOPLE_COUNT_INVALID)
    private Integer peopleCount;

    @Size(max = 255, message = ExceptionConstants.RESERVATION_NOTE_INVALID)
    private String note;

    public ReservationCreateRequest() {}

    public ReservationCreateRequest(
        Integer comId,
        String customerName,
        String customerPhone,
        String orderDate,
        String orderTime,
        Integer peopleCount,
        String note
    ) {
        this.comId = comId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.peopleCount = peopleCount;
        this.note = note;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getCustomerName() {
        return customerName.trim();
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
        return orderDate != null ? orderDate.trim() : null;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime != null ? orderTime.trim() : null;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
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
