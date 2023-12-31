package com.itm.space.backendresources.service;

import com.itm.space.backendresources.BaseIntegrationTest;
import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class UserServiceImplTest extends BaseIntegrationTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Keycloak keycloak;

    @Test
    @Order(0)
    void createUserTest() {
        UserRequest userRequest = createUserRequest();
        userService.createUser(userRequest);

        UserRepresentation userRepresentation = keycloak.realm("ITM").users().search("Beast").get(0);

        Assertions.assertEquals(userRepresentation.getUsername(), userRequest.getUsername());
        Assertions.assertEquals(userRepresentation.getEmail(), userRequest.getEmail());
        Assertions.assertEquals(userRepresentation.getFirstName(), userRequest.getFirstName());
        Assertions.assertEquals(userRepresentation.getLastName(), userRequest.getLastName());
    }

    @Test
    @Order(1)
    void getUserByIdTest() {
        UserRequest userRequest = createUserRequest();
        userService.createUser(userRequest);

        String id = keycloak.realm("ITM").users().search("Beast").get(0).getId();

        UserResponse userResponse = userService.getUserById(UUID.fromString(id));

        Assertions.assertEquals(userRequest.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(userRequest.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(userRequest.getEmail(), userResponse.getEmail());
    }

    @AfterEach
    void cleanUp() {
        UserRepresentation userRepresentation = keycloak.realm("ITM").users().search("Beast").get(0);
        keycloak.realm("ITM").users().get(userRepresentation.getId()).remove();
    }

    private UserRequest createUserRequest() {
        return new UserRequest("beast", "email@email.com", "qwerty123",
                "nikolay", "fedorov");
    }
}
