package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserLoginRegisterDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }
    /**
     * create list of users by sending request to user service. convert each one to the getdto - this is what is returned to the cliet
     */

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

    /**
     same as above just only one user which is searched by id
     */

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserById(@PathVariable Long userId) {

        User foundUser = userService.getUserById(userId);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }


    /**
    according to specifications
     accepts PostDTO object, converts to userInput of type user and send it to the server to create it. if success gets
     User type object back, converts to GETDTO object and returns
     */

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO getUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    /**
    same as above however different return type - loginRegisterDTO, it contains token
     */

  @PostMapping("/users/register")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserLoginRegisterDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);

    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserLoginRegisterDTO(createdUser);
  }

    /**
     very similar to the above function, but calls function login from the service
     */

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserLoginRegisterDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User loggedUser = userService.loginUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserLoginRegisterDTO(loggedUser);
    }

    /**
     accepts putDTO object and sends it to convert to User object. afterwards changes are deligated to service and if success nothng is returned
     */

  @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable Long userId, @RequestBody UserPutDTO userPutDTO) {
      // convert API user to internal representation
      User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
      userService.updateUserById(userId, userInput);
    }


}
