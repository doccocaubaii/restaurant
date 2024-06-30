package vn.hust.restaurant.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@DynamicUpdate
@Table(name = "otp")
public class Otp extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "type")
    private Integer type;


    @Column(name = "status")
    private Integer status;

    public Otp(String code, Integer userId, Integer type) {
        this.code = code;
        this.userId = userId;
        this.type = type;
        this.status = 1;
    }

    public Otp() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
