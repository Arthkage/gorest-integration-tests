package org.globallogic.gorest.smoketests;

import org.apache.commons.lang.RandomStringUtils;
import org.globallogic.gorest.api.UserAPI;
import org.globallogic.gorest.core.ApiTestMain;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class UserApiSmokeTest extends ApiTestMain {

    @BeforeClass
    public void userClassSetup() {
        token = System.getProperty("token");
        userAPI = new UserAPI(token);
    }

    @BeforeMethod
    public void userTestSetup() {
        String email = String.format("oompaloompa%s@mail.com", RandomStringUtils.randomAlphanumeric(5));
        userRequestPayload = new UserRequestDTO("Oompa Loompa", email, "female", "active");
    }

    @Test
    public void testGetLatestUsers() {
        userAPI.getUsers();
        Assert.assertEquals(userAPI.getResponse().getStatusCode(), okStatusCode);
    }

    @Test
    public void testGetUsersFromTargetPage() {
        int targetPage = 1;
        int usersPerPage = 10;

        List<UserResponseDTO> users = userAPI.getUsers(targetPage, usersPerPage);
        Assert.assertEquals(userAPI.getResponse().getStatusCode(), okStatusCode);
        Assert.assertEquals(users.size(), usersPerPage);
    }

    @Test
    public void testCreateNewUserAndReturnId() {
        UserResponseDTO newUser = userAPI.createUser(userRequestPayload);
        Assert.assertEquals(userAPI.getResponse().getStatusCode(), createdStatusCode);
        Assert.assertNotNull(newUser.id());
    }

    @Test
    public void testUpdatedUserEmail() {
        UserResponseDTO newUser = userAPI.createUser(userRequestPayload);
        System.out.println(newUser);

        int userId = newUser.id();

        String newEmail = String.format("updatedLoompa%s@gmail.com", RandomStringUtils.randomAlphanumeric(5));

        UserRequestDTO updatedPayload = new UserRequestDTO("Oompa Loompa", newEmail, "female", "active");
        UserResponseDTO updatedUser = userAPI.updatedUser(userId, updatedPayload);
        System.out.println(updatedUser);

        Assert.assertEquals(userAPI.getResponse().getStatusCode(), updatedStatusCode);
        Assert.assertEquals(newEmail, updatedUser.email());
    }

    @Test
    public void testDeleteTargetUser() {

        UserResponseDTO newUser = userAPI.createUser(userRequestPayload);
        System.out.println(newUser);

        int userId = newUser.id();

        UserRequestDTO deletePayload = new UserRequestDTO(newUser.name(), newUser.email(), newUser.gender(), newUser.status());
        String deletedUser = userAPI.deleteUser(userId, deletePayload);
        System.out.println("User deleted");

        Assert.assertEquals(userAPI.getResponse().getStatusCode(), deletedStatusCode);
        Assert.assertEquals(deletedUser, "");

    }
}
