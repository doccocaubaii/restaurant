package vn.hust.easypos.service.dto;

import java.io.Serializable;
import java.util.Set;
import vn.hust.easypos.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class AdminUserDTO implements Serializable {

    boolean activated;
    Set<String> authorities;
    String email;
    String firstName;
    String langKey;
    String lastName;
    String login;
    String imageUrl;

    public AdminUserDTO() {
        // Empty constructor needed for Jackson.
    }

    public AdminUserDTO(User user) {
        this.activated = user.getStatus() == 1;
        this.authorities = user.getAuthorities();
        this.email = user.getEmail();
        this.firstName = user.getFullName();
        this.langKey = "VI";
        this.lastName = user.getFullName();
        this.login = user.getUsername();
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
