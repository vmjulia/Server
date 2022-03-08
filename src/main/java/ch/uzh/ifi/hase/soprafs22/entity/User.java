package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true, unique = true)
  private Date creationDate;

  @Column(nullable = false)
  private UserStatus status;

  @Column(nullable = true)
  private Date birthday;

  @Column(nullable = true, unique = true)
  private String token;

  public String getToken() { return token;}
  public void setToken( String token)  { this.token = token;}

  public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthDate) {
        this.birthday = birthDate;
    }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date token) {
    this.creationDate = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    if (status == true){
        this.status = UserStatus.ONLINE;
    }
    else{
        this.status = UserStatus.OFFLINE;
    }
  }
}
