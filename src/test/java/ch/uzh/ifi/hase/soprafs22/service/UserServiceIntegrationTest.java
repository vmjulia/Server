package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }



    @Test
    public void login_Success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");


        User createdUser = userService.createUser(testUser);


        User testUser2 = new User();
        testUser2.setPassword("testName");
        testUser2.setUsername("testUsername");
        User loggedinUser = userService.loginUser(testUser2);


        assertEquals(testUser.getPassword(), loggedinUser.getPassword());
        assertEquals(testUser.getUsername(), loggedinUser.getUsername());
        assertEquals(testUser.getId(), createdUser.getId());


    }

    @Test
    public void createUser_validInputs_success() {
        // given
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getCreationDate());
        //assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }


  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("testName");
    testUser.setUsername("testUsername");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the password but forget about the username
    testUser2.setPassword("testName2");
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }




    @Test
    public void login_fail_wrong_password() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername");

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser2));
    }


    @Test
    public void login_fail_wrong_username() {
        assertNull(userRepository.findByUsername("testUsername"));
        assertNull(userRepository.findByUsername("testUsername2"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        testUser.setId(1L);
        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();
        testUser2.setPassword("testName");
        testUser2.setUsername("testUsername2");

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser2));
    }


    @Test
    public void getbyId_Success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");

        User createdUser= userService.createUser(testUser);


        User foundUser = userService.getUserById(createdUser.getId());


        // check that an error is thrown
        assertEquals(testUser.getPassword(), foundUser.getPassword());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        assertEquals(testUser.getId(), foundUser.getId());
    }



    @Test
    public void updatebyId_Success() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        testUser.setId(1L);
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");



        userService.updateUserById(createdUser.getId(), testUser2);
        User res = userService.getUserById(createdUser.getId());

        // check that an error is thrown

        assertEquals(res.getUsername(), testUser2.getUsername());

    }

    @Test
    public void updatebyId_Fail_notfound() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        testUser.setId(1L);
        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUsername2");
        Long nextId = createdUser.getId()+1;

        assertThrows(ResponseStatusException.class, () -> userService.updateUserById(nextId, testUser2));

    }

    @Test
    public void updatebyId_Fail_repetitiveName() {
        assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testName");
        testUser.setUsername("testUsername");
        testUser.setId(1L);
        User testUser2 = new User();
        testUser2.setPassword("testName2");
        testUser2.setUsername("testUsername2");
        testUser2.setId(2L);
        User createdUser1 = userService.createUser(testUser);
        User createdUser2 = userService.createUser(testUser2);


        assertThrows(ResponseStatusException.class, () -> userService.updateUserById(createdUser1.getId(), testUser2));

    }



}
