package org.globallogic.gorest.api;

import org.globallogic.gorest.core.ApiMain;
import org.globallogic.gorest.dtos.todo.ToDoRequestDTO;
import org.globallogic.gorest.dtos.todo.ToDoResponseDTO;
import org.globallogic.gorest.dtos.user.UserResponseDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ToDoAPI extends ApiMain {

    private static final String ENDPOINT = "/todos";

    public ToDoAPI(String token) {
        super(token);
    }

    public void getToDos() {
        configuration(ENDPOINT);
        logger.info("...getting todos...");
        response = request
                .get()
                .then()
                .extract()
                .response();
        logger.info("...list of todos: " + response.getBody().asString());
    }

    public List<ToDoResponseDTO> getToDos(int targetPage, int postsPerPage) {

        configuration(ENDPOINT);
        logger.info("...getting todos from page " + targetPage + " with a number of "+ postsPerPage +" entries per page...");
        response = request
                .queryParam("page", targetPage)
                .queryParam("per_page", postsPerPage)
                .get()
                .then()
                .extract()
                .response();
        logger.info("...list of todos: " + response.getBody().asString());
        return response.jsonPath().getList("", ToDoResponseDTO.class);
    }

    public ToDoResponseDTO createTodo(UserResponseDTO newUser, ToDoRequestDTO toDoRequestPayload) {
        configuration(ENDPOINT);
        logger.info("...creating new todo...");
        response = request
                .body(toDoRequestPayload)
                .queryParam("{user_id}", newUser.id())
                .post()
                .then()
                .extract()
                .response();
        logger.info("...toDo with id={} has been created...", response.jsonPath().getInt("id"));
        logger.info("...toDo content: " + response.getBody().asString());
        return response.as(ToDoResponseDTO.class);
    }

    public ToDoResponseDTO updateToDo(int id, ToDoRequestDTO updatedToDoPayload) {
        configuration(ENDPOINT);
        logger.info("...updating todo...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(updatedToDoPayload)
                .put()
                .then()
                .extract()
                .response();
        logger.info("...update todo item contains: " + response.getBody().asString());
        return response.as(ToDoResponseDTO.class);
    }

    public String deletePost(int id, ToDoRequestDTO deletePayload) {
        configuration(ENDPOINT);
        logger.info("...deleting target toDo item...");
        response = request
                .basePath(ENDPOINT + "/" + "{id}")
                .pathParams("id", String.valueOf(id))
                .body(deletePayload)
                .delete()
                .then()
                .extract()
                .response();
        logger.info("...toDo item with id={} deleted...", id);
        return response.getBody().asString();
    }

    public String getCurrentDateAndTime() {
        DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateTime.format(date);
    }
}
