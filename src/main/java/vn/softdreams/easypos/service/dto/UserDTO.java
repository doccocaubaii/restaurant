package vn.softdreams.easypos.service.dto;

import vn.softdreams.easypos.domain.AbstractAuditingEntity;
import vn.softdreams.easypos.domain.User;

import java.io.Serializable;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class UserDTO extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String userName;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        this.userName = user.getUsername();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserDTO{" + "id='" + id + '\'' + ", userName='" + userName + '\'' + '}';
    }
}
