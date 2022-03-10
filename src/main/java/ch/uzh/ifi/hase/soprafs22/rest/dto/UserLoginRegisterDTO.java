package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.util.Date;

public class UserLoginRegisterDTO {

    private Long id;
    private String username;
    private Date creation_date;
    private Boolean logged_in;
    private Date birthday;
    private String token;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreation_date() {
        return creation_date;
    }
    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Boolean getLogged_in() {
        return logged_in;
    }
    public void setLogged_in(UserStatus logged_in) {
        if (logged_in == UserStatus.ONLINE){
            this.logged_in = true;
        }
        else {
            this.logged_in = false;
        }
    }

    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}