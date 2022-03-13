package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

    /** find the user by id and if found return it, otheriwise throw error */
  public User getUserById(Long id ){

       Optional<User> user =  userRepository.findById(id);

        if (user.isPresent()){
            User foundUser = user.get();
            return foundUser;
        }

       else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("user with %s was not found", id));
        }
  }


    /** find the user by id and update it depending on what is provided: username/bd/status.
     * when updating username make sure that such username does not exist already */
  public void updateUserById(Long id, User userNew){


        Optional<User> user =  userRepository.findById(id);

        if (user.isPresent()){
            User foundUser = user.get();

            if (userNew.getUsername() != null){
                checkIfAnotherUserExists(userNew, foundUser);
                foundUser.setUsername(userNew.getUsername());}

            if (userNew.getBirthday() != null){
            foundUser.setBirthday(userNew.getBirthday());}


            if (userNew.getStatus() != null && userNew.getStatus() == UserStatus.OFFLINE){
                foundUser.setStatus(UserStatus.OFFLINE);}
        }

        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("user was not found"));
        }
    }


    /** function to login the user veryfying that credentials and correct ptherwise throws error
     * it also changes status to onlin eif success */

public User loginUser(User newUser){
    User userByUsername = userRepository.findByUsername(newUser.getUsername());
    if (userByUsername == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("user with was not found" ));
    }
    else {
        if (!userByUsername.getPassword().equals(newUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    String.format("password is wrong" ));
        }
        else {
            userByUsername.setStatus(UserStatus.ONLINE);
            return (userByUsername);
        }
    }

}

    /** fore registering user, saves todaxs day, verifiesthat there is no conflict in username and if success saves to repository */
  public User createUser(User newUser) {
      newUser.setToken(UUID.randomUUID().toString());

      Date localDate = Calendar.getInstance().getTime();
    newUser.setCreationDate(localDate);


    //checkIfEmpty(newUser);
    checkIfUserExists(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
      newUser.setStatus(UserStatus.ONLINE);
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;

  }

  /**
   * This is helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    String baseErrorMessage = "add User failed because username already exists";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
          String.format(baseErrorMessage));
    }
  }

    /**
     * This is helper method that will check the uniqueness criteria of the
     * username and the name
     * defined in the User entity. makes sure that the user with whom we compare is not the same that we are considering now. The method will do nothing if the input is unique
     * and throw an error otherwise.
     *
     * @param userToBeCreated, existingUser
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */

    private void checkIfAnotherUserExists(User userToBeCreated, User existingUser) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        String baseErrorMessage = "change User failed because username already exists";
        if (userByUsername != null && userByUsername != existingUser) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage));
        }
    }

/**
 used initially but then showed to be unnecerssary
    private void checkIfEmpty(User userToBeCreated) {

        String baseErrorMessage = "The %s provided is null. Therefore, the user could not be created!";
        if (userToBeCreated.getUsername().length() == 0  ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "username"));
        }
        else if (userToBeCreated.getPassword().length() == 0  ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format(baseErrorMessage, "password"));
        }
    }
 */
}
