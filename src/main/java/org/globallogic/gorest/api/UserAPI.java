package org.globallogic.gorest.api;

import org.globallogic.gorest.core.ApiMain;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;

import java.util.List;

public class UserAPI extends ApiMain {
    private static final String ENDPOINT = property.getConfig("user_endpoint");

    public UserAPI(String token) {
        super(token);
    }

    public void getUsers() {
        configuration(ENDPOINT);
        LOGGER.info("...getting users...");
        response = request
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of users: %s" + response.getBody().asString());
    }


    public List<UserResponseDTO> getUsers(int targetPage, int usersPerPage) {
        configuration(ENDPOINT);
        LOGGER.info("...getting users from page "+targetPage+" with a number of "+usersPerPage+" entries...");
        response = request
                .queryParam("page", targetPage)
                .queryParam("per_page", usersPerPage)
                .get()
                .then()
                .extract()
                .response();
        LOGGER.info("...list of users: " + response.getBody().asString());
        return response.jsonPath().getList("", UserResponseDTO.class);
    }

    public UserResponseDTO createUser(UserRequestDTO requestPayload) {
        configuration(ENDPOINT);
        LOGGER.info("...creating new user...");
        response = request
                .body(requestPayload)
                .post()
                .then()
                .extract()
                .response();
        LOGGER.info("...user with id={} created...", response.jsonPath().getInt("id"));
        LOGGER.info("...user content: " + response.getBody().asString() + "...");
        return response.as(UserResponseDTO.class);
    }

    public UserResponseDTO updatedUser(int id, UserRequestDTO updatedPayload) {
        configuration(ENDPOINT);
        LOGGER.info("...updating user...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(updatedPayload)
                .put()
                .then()
                .extract()
                .response();
        LOGGER.info("...user with id={} updated...", id);
        LOGGER.info("...new user content: " + response.getBody().asString());
        return response.as(UserResponseDTO.class);
    }

    public String deleteUser(int id, UserRequestDTO deletePayload) {
        configuration(ENDPOINT);
        LOGGER.info("...deleting target user...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(deletePayload)
                .delete()
                .then()
                .extract()
                .response();
        LOGGER.info("...user with id={} deleted...", id);
        return response.getBody().asString();
    }
}