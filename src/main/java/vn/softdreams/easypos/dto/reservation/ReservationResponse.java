package vn.softdreams.easypos.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;

public class ReservationResponse implements Serializable {

    private Integer id;
    private Integer comId;
    private String customerPhone;
    private String customerName;

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    private String orderDate;

    @JsonFormat(pattern = Constants.ZONED_TIME_FORMAT)
    private String orderTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private String arrivalTime;

    private Integer peopleCount;
    private Integer status;
    private String note;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private String createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private String updateTime;

    public ReservationResponse() {}

    public ReservationResponse(
        Integer id,
        Integer comId,
        String customerPhone,
        String customerName,
        String orderDate,
        String orderTime,
        String arrivalTime,
        Integer peopleCount,
        Integer status,
        String note,
        String createTime,
        String updateTime
    ) {
        this.id = id;
        this.comId = comId;
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.arrivalTime = arrivalTime;
        this.peopleCount = peopleCount;
        this.status = status;
        this.note = note;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getArrivalTime() {
        return arrivalTime;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
