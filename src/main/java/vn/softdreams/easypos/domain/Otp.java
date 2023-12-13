package vn.softdreams.easypos.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicUpdate;
import vn.softdreams.easypos.config.Constants;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Otp.
 */
@Entity
@Table(name = "otp")
@DynamicUpdate
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Otp extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "otp")
    private String otp;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    @Column(name = "expired_time")
    private ZonedDateTime expiredTime;

    @Column(name = "status")
    private Integer status;

    @Column(name = "type")
    private Integer type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Otp username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOtp() {
        return this.otp;
    }

    public Otp otp(String otp) {
        this.setOtp(otp);
        return this;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public ZonedDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(ZonedDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Otp)) {
            return false;
        }
        return id != null && id.equals(((Otp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Otp{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", otp=" + getOtp() +
            "}";
    }
}
