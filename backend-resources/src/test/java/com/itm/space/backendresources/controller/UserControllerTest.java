package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WithMockUser(roles = "MODERATOR")
public class UserControllerTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;

    @Test
    void createTest() throws Exception {
        UserRequest userRequest = createUserRequest();
        MockHttpServletRequestBuilder request = requestWithContent(post("/api/users"), userRequest);

        Mockito.doNothing().when(userService).createUser(any(UserRequest.class));
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void getUserByIdTest() throws Exception {
        UserResponse userResponse = createUserResponse();
        UUID id = UUID.randomUUID();

        Mockito.when(userService.getUserById(id)).thenReturn(userResponse);
        mvc.perform(get("/api/users/{id}", id.toString()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("email@email.com"))
                .andReturn();

    }

    @Test
    void helloTest() throws Exception {
        mvc.perform(get("/api/users/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private UserRequest createUserRequest() {
        return new UserRequest("beast", "email@email.com", "qwerty123",
                "nikolay", "fedorov");
    }

    private UserResponse createUserResponse() {
        return new UserResponse("nikolay", "fedorov", "email@email.com",
                List.of("user"), List.of("users"));
    }
}
