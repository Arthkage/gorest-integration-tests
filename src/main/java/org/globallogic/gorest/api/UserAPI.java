package org.globallogic.gorest.api;

import org.globallogic.gorest.core.ApiMain;
import org.globallogic.gorest.dtos.user.UserRequestDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;

import java.util.List;

public class UserAPI extends ApiMain {
    private static final String ENDPOINT = "/users";

    public UserAPI(String token) {
        super(token);
    }

    public void getUsers() {
        configuration(ENDPOINT);
        logger.info("...getting users...");
        response = request
                .get()
                .then()
                .extract()
                .response();
        logger.info("...list of users: %s" + response.getBody().asString());
    }


    public List<UserResponseDTO> getUsers(int targetPage, int usersPerPage) {
        configuration(ENDPOINT);
        logger.info("...getting users from page "+targetPage+" with a number of "+usersPerPage+" entries...");
        response = request
                .queryParam("page", targetPage)
                .queryParam("per_page", usersPerPage)
                .get()
                .then()
                .extract()
                .response();
        logger.info("...list of users: " + response.getBody().asString());
        return response.jsonPath().getList("", UserResponseDTO.class);
    }

    public UserResponseDTO createUser(UserRequestDTO requestPayload) {
        configuration(ENDPOINT);
        logger.info("...creating new user...");
        response = request
                .body(requestPayload)
                .post()
                .then()
                .extract()
                .response();
        logger.info("...user with id={} created...", response.jsonPath().getInt("id"));
        logger.info("...user content: " + response.getBody().asString() + "...");
        return response.as(UserResponseDTO.class);
    }

    public UserResponseDTO updatedUser(int id, UserRequestDTO updatedPayload) {
        configuration(ENDPOINT);
        logger.info("...updating user...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(updatedPayload)
                .put()
                .then()
                .extract()
                .response();
        logger.info("...user with id={} updated...", id);
        logger.info("...new user content: " + response.getBody().asString());
        return response.as(UserResponseDTO.class);
    }

    public String deleteUser(int id, UserRequestDTO deletePayload) {
        configuration(ENDPOINT);
        logger.info("...deleting target user...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(deletePayload)
                .delete()
                .then()
                .extract()
                .response();
        logger.info("...user with id={} deleted...", id);
        return response.getBody().asString();
    }
}