package ch.uzh.ifi.hase.soprafs22.rest.dto;


import java.util.Date;

public class UserPutDTO {

    private String username;
    private Date birthDate;


    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date creationDate) {
        this.birthDate = birthDate;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }


}