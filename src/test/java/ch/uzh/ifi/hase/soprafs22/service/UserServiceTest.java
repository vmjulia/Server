package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("testPassword");
    testUser.setUsername("testUsername");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }


  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getCreationDate());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsernameName_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown

      assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }



    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given

        List<User> allUsers = Collections.singletonList(testUser);
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        List<User> foundUsers = userService.getUsers();


        assertEquals(allUsers.get(0).getId(), foundUsers.get(0).getId());
        assertEquals(allUsers.get(0).getPassword(), foundUsers.get(0).getPassword());
        assertEquals(allUsers.get(0).getUsername(), foundUsers.get(0).getUsername());

    }

    @Test
    public void givenUserId_whenGetUser_notfound() throws Exception {
        // given
        Optional<User> userOptional = Optional.ofNullable(null);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(userOptional);

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
    }


    @Test
    public void changeUserById_successfully() throws Exception {
        // given

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setStatus(UserStatus.OFFLINE);
        testUser2.setUsername("testUsername2");
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-08");
        testUser2.setBirthday(date);

        Optional<User> userOptional = Optional.ofNullable(testUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(userOptional);
        userService.updateUserById(1L, testUser2);

        assertEquals(testUser.getId(), testUser2.getId());
        assertEquals(testUser.getUsername(), testUser2.getUsername());
        assertEquals(testUser.getStatus(), testUser2.getStatus());
        assertEquals(testUser.getBirthday(), testUser2.getBirthday());
    }


    @Test
    public void changeUserById_NotFound() throws Exception {
        // given

        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setStatus(UserStatus.ONLINE);
        testUser2.setUsername("testUsername2");

        Optional<User> userOptional = Optional.ofNullable(null);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(userOptional);

        assertThrows(ResponseStatusException.class, () -> userService.updateUserById(1L, testUser2));
    }

    @Test
    public void login_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser


        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        User loggedInUser = userService.loginUser(testUser);

        assertEquals(testUser.getId(), loggedInUser.getId());
        assertEquals(testUser.getPassword(), loggedInUser.getPassword());
        assertEquals(testUser.getUsername(), loggedInUser.getUsername());
        assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
    }

    @Test
    public void login_validInputs_WrongPassword() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User testUser2 = new User();
        testUser2.setId(1L);
        testUser2.setPassword("testPassword2");
        testUser2.setUsername("testUsername");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser2));
    }

    @Test
    public void login_validInputs_UserDoesNotExist() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(testUser));
    }


}
