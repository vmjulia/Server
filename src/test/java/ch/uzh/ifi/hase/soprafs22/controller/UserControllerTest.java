package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;





  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given


     User user = new User();

     user.setId(1L);
     user.setUsername("some username");

      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      Date date = simpleDateFormat.parse("2018-09-08");

      String jsonDate = "2018-09-07T22:00:00.000+00:00";


      user.setBirthday(date);
      user.setCreationDate(date);

    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id",  is(user.getId().intValue())))
            .andExpect(jsonPath("$[0].username", is(user.getUsername())))
            .andExpect(jsonPath("$[0].creation_date", is(jsonDate)))
            .andExpect(jsonPath("$[0].birthday", is(jsonDate)));
  }

    @Test
    public void postUser_asinRequirements() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("some password");
        user.setUsername("some Username");
        user.setStatus(UserStatus.ONLINE);


        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-08");
        String jsonDate = "2018-09-07T22:00:00.000+00:00";
        user.setCreationDate(date);


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("some password");
        userPostDTO.setUsername("some Username");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.creation_date", is(jsonDate)))
                .andExpect(jsonPath("$.logged_in", is(true)));

    }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setPassword("some password");
    user.setUsername("some Username");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("1");

      String pattern = "yyyy-MM-dd";
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
      Date date = simpleDateFormat.parse("2018-09-08");
      String jsonDate = "2018-09-07T22:00:00.000+00:00";
      user.setCreationDate(date);



    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("some password");
    userPostDTO.setUsername("some Username");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.creation_date", is(jsonDate)))
        .andExpect(jsonPath("$.logged_in", is(true)))
        .andExpect(jsonPath("$.token",  is(user.getToken())));
  }

    @Test
    public void loginUser_validInput_userLoggedIn() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("some password");
        user.setUsername("some Username");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-08");
        String jsonDate = "2018-09-07T22:00:00.000+00:00";
        user.setCreationDate(date);


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setPassword("some password");
        userPostDTO.setUsername("some Username");

        given(userService.loginUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.creation_date", is(jsonDate)))
                .andExpect(jsonPath("$.logged_in", is(true)))
                .andExpect(jsonPath("$.token",  is(user.getToken())));
    }


    @Test
    public void getConcreteUser() throws Exception {
        // given
        User user = new User();
        user.setId(1L);
        user.setPassword("some password");
        user.setUsername("some Username");
        user.setStatus(UserStatus.ONLINE);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-08");
        String jsonDate = "2018-09-07T22:00:00.000+00:00";
        user.setCreationDate(date);

        given(userService.getUserById(1L)).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);


        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.creation_date", is(jsonDate)))
                .andExpect(jsonPath("$.logged_in", is(true)));
    }


    @Test
    public void modifyUser_validInput_userModified() throws Exception {
        // given
        User user = new User();
        Long id = Long.valueOf(1);
        user.setId(1L);
        user.setPassword("some password");
        user.setUsername("some Username");
        user.setStatus(UserStatus.ONLINE);

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse("2018-09-08");
        String jsonDate = "2018-09-07T22:00:00.000+00:00";
        user.setCreationDate(date);

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("some Username");
        //userPutDTO.setBirthday(date);
        //userPutDTO.setLogged_in(true);
        //given(DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO)).willReturn(user);
      //Mockito.doNothing().when(userService).updateUserById(id, user);

        given(userService.getUserById(1L)).willReturn(user);
        given(userService.createUser(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO));


        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }



    /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}