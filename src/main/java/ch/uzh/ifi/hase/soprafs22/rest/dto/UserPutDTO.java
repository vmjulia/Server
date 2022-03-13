package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import java.util.Date;
/**
 class to store info to send oput requests, so contasins those fields which are supposed to change
 */
public class UserPutDTO {

    private Long id;
    private String username;
    private Date creation_date;
    private Boolean logged_in;
    private Date birthday;

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
    /**
     tbis is for the mapping between this and user status from the class status
     */
    public UserStatus getLogged_in() {

        if (logged_in!= null && logged_in == true) {
            return (UserStatus.ONLINE);
        }
        else if (logged_in!= null){
            return (UserStatus.OFFLINE);
        }
        else {return null;}
    }


    public void setLogged_in(boolean logged_in) {
            this.logged_in = logged_in;
    }



    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }








}
